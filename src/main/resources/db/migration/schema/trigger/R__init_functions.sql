CREATE OR REPLACE FUNCTION f_get_rm_pp_wac(
	lotid integer,
	date character varying,
	dateformat character varying,
	currency character varying)
    RETURNS numeric
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
declare

    v_exchange_rate 	numeric(20,4);
    o_pp_rate_loc       numeric(20,4);
    o_pp_rate_intl      numeric(20,4);
    v_INV_QTY1            numeric(20,4);
    v_PP_RATE_INTL        numeric(20,4);
    v_PP_RATE_LOC         numeric(20,4);
    v_CUR_QTY1            numeric(20,4);
    v_PREV_TRANS_QTY      numeric(20,4);
    v_PREV_TRANS_PP_intl  numeric(20,4);
    v_PREV_TRANS_PP_loc   numeric(20,4);
    v_jcp                 numeric(20,4);
    v_cur_code            varchar(20);
    v_counter             integer;
    v_date_format 			varchar(20);

	cur_pp_rate record;

begin

    v_counter := 1;

	for cur_pp_rate in
	(
			with v_trans as (
				--Prepare the S.No Order by Invoice Dt, Created Dt
				select
					row_number() over (partition by t1.product_id order by t1.product_id,t1.inv_dt,t1.created_dt) sr_no,
					t1.inv_id,
					t1.inv_type,
					t1.inv_no,
					t1.inv_dt,
					t1.created_dt,
					t1.in_out_flag,
					t1.product_id,
					t2.product_no,
					t1.qty1 inv_qty1,
					t1.qty2 inv_qty2,
					case
						when t1.in_out_flag = 1 and t1.currency_code = 'USD' then t1.inv_rate
						when t1.in_out_flag = 1 and t1.exchange_rate > 0 then t1.inv_rate / t1.exchange_rate
						when t1.in_out_flag = 1 and t1.exchange_rate <= 0 then 0
						else null
					end pp_rate_intl,
					case
						when t1.in_out_flag = 1 and t1.currency_code = 'USD' then t1.inv_rate * t1.exchange_rate
						when t1.in_out_flag = 1 then t1.inv_rate
						else null
					end pp_rate_loc,
					t1.in_out_flag * t1.qty1 inv_out_qty1
				from tbl_product_stock_trans t1
				inner join tbl_product t2 on t1.product_id = t2.product_id
				where t1.is_deleted = 0
					and t1.product_id = lotId
					and t1.inv_dt <= to_date(date, dateFormat)
					and t1.inv_type in ('RPRI','RSI','MIX','RPI')
				order by t1.inv_dt,t1.created_dt
			),
			v_running_trans as (
				--As per Sno preprare running pp rate(take previous calculated pp rate in case PP rate not available for this transaction
				select
					t2.*,
					sum(inv_out_qty1) over (partition by product_id order by product_id,sr_no) cur_qty1,
					coalesce((array_remove(array_agg(pp_rate_intl) over (order by product_id,sr_no), null))[cardinality(array_remove(array_agg(pp_rate_intl) over (order by product_id,sr_no), null))], 0) run_pp_rate_intl,
					coalesce((array_remove(array_agg(pp_rate_loc) over (order by product_id,sr_no), null))[cardinality(array_remove(array_agg(pp_rate_loc) over (order by product_id,sr_no), null))], 0) run_pp_rate_loc
				from
					v_trans t2
			),
			v_pre_trans as(
				select
					--Prepare Running current balance stock
					--carry forward previous trans PP Rate and Qty for PP calculation
					--In case of RM Assortment and stock out then considet Qty in negative (only place out stock changes the Rate)
					vt.inv_qty1,
					vt.PP_RATE_INTL,
					vt.PP_RATE_LOC,
					vt.CUR_QTY1,
					(case when inv_type = 'OPN' then 0 else  cur_qty1 - inv_out_qty1 end) prev_trans_qty,
					coalesce(lag(vt.run_pp_rate_intl, 1) over (order by product_id,sr_no),PP_RATE_INTL,0) prev_trans_PP_intl,
					coalesce(lag(vt.run_pp_rate_loc, 1) over (order by product_id,sr_no),PP_RATE_LOC,0) prev_trans_PP_loc
				from
					v_running_trans vt
			)
			select * from v_pre_trans pre where (pre.PP_RATE_LOC is not null or pre.PP_RATE_INTL is not null)
	)
	loop

		v_INV_QTY1  := cur_pp_rate.inv_qty1;
		v_PP_RATE_INTL := cur_pp_rate.pp_rate_intl;
		v_PP_RATE_LOC := cur_pp_rate.pp_rate_loc;
		v_CUR_QTY1 := cur_pp_rate.cur_qty1;
		v_PREV_TRANS_QTY := cur_pp_rate.prev_trans_qty;
		v_PREV_TRANS_PP_intl := cur_pp_rate.prev_trans_pp_intl;
		v_PREV_TRANS_PP_loc	:= cur_pp_rate.prev_trans_pp_loc;

	 	if v_counter > 1 then
			   v_PREV_TRANS_PP_loc := coalesce(o_pp_rate_loc,0);
               v_PREV_TRANS_PP_intl := coalesce(o_pp_rate_intl,0);
            end if;

           if v_CUR_QTY1 < 0 then v_CUR_QTY1 := 0; end if;
           if v_prev_trans_qty < 0 then v_prev_trans_qty := 0; end if;

           --IF Running stock is less than or equal to zero the should not calculate Weighted average need to take previous calculated rate
           if v_CUR_QTY1 > 0 then
                o_pp_rate_loc := ((v_prev_trans_qty * v_PREV_TRANS_PP_loc) +
								  (v_INV_QTY1 * v_PP_RATE_LOC)) / nullif((v_PREV_TRANS_QTY + v_INV_QTY1), 0);
                o_pp_rate_intl := ((v_prev_trans_qty * v_PREV_TRANS_PP_intl) + (v_INV_QTY1 * v_PP_RATE_INTL)) / nullif((v_PREV_TRANS_QTY + v_INV_QTY1), 0);
           end if;

           if v_CUR_QTY1 <= 0 then
		    	o_pp_rate_loc := coalesce(v_PREV_TRANS_PP_loc, 0);
                o_pp_rate_intl := coalesce(v_PREV_TRANS_PP_intl, 0);
		   end if;

           v_counter := v_counter + 1;

	end loop;

    return(case when upper(coalesce(currency,'USD')) = 'USD' then o_pp_rate_intl else o_pp_rate_loc end);

end;
$BODY$;