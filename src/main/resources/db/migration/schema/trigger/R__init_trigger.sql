CREATE OR REPLACE FUNCTION fn_purchase_invoice_det() RETURNS trigger AS $$
declare
	v_count int;
	inv_main tbl_purchase_invoice_main%rowtype;
	v_operation_flag integer:= 0;
	v_old_qty1 numeric(20,4) := 0;
	v_old_qty2	numeric(20,4) := 0;
	v_old_input_amt	numeric(20,4) := 0;
BEGIN

	if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select 
		* 
	into
	 	inv_main 
	from
	 	tbl_purchase_invoice_main 
	where 
		inv_id = NEW.inv_id
    ;
	
	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	if v_operation_flag > 1 then
		v_old_qty1 := OLD.inv_qty1;
		v_old_qty2 := OLD.inv_qty2;
		v_old_input_amt := OLD.input_amt;
	end if;

	select
		count(1)
	into
		v_count
	from
		tbl_product_stock_trans tr
	where
		tr.inv_det_id = NEW.inv_det_id
		and tr.inv_type = inv_main.inv_type
	;
	
	if v_count = 0 then	 
		 insert into tbl_product_stock_trans
		 (
		 
				company_id,
				product_id,
				bin_id,
				inv_dt,
				in_out_flag,
				qty1,
				qty2,
				adj_qty1,
				adj_qty2,	
				party_type,
				party_id,
				currency_code,	
				inv_rate,
				cost_price,	
				inv_type,
				inv_det_id,
				inv_id,
				remarks,
				sort_seq,
				is_locked,
				created_by,
				created_dt,
				is_deleted,
				exchange_rate,
				inv_no
		 )
		 select
		 		im.company_id,
				NEW.product_id,
				rid.bin_id bin_id,
				im.inv_dt,
				-1	in_out_flag,
				NEW.inv_qty1	qty1,
				NEW.inv_qty2	qty2,
				0	dj_qty1,
				0	dj_qty2,
			 	'SUPPLIER' party_type,
				im.supplier_id	party_id,
				im.currency	currency_code,
				NEW.input_rate inv_rate,
				NEW.cost_price,
				im.inv_type,
				NEW.inv_det_id,
				im.inv_id,
				'Confirm invoice stock reduce from memo purchase'	remarks,
				0	sort_seq,
				0	is_locked,
				NEW.created_by,
				current_timestamp created_dt,
				NEW.is_deleted,
				im.exchange_rate,
				im.inv_no
			from
				tbl_purchase_invoice_main im
				inner join tbl_memo_purchase_invoice_main rim on rim.inv_id = NEW.ref_inv_id
				    and rim.inv_type = NEW.ref_inv_type
				    and im.company_id = rim.company_id
				    and rim.is_deleted = 0
				inner join tbl_memo_purchase_invoice_det rid on rid.inv_det_id = NEW.ref_inv_det_id
				    and rim.inv_id = rid.inv_id
				    and rid.is_deleted = 0
			where
				im.inv_id = new.inv_id
				and NEW.ref_inv_type is not null
         union all
		 select
		 		im.company_id,
				NEW.product_id,
				NEW.bin_id,
				im.inv_dt,
				1	in_out_flag,
				NEW.inv_qty1	qty1,
				NEW.inv_qty2	qty2,
				0	dj_qty1,
				0	dj_qty2,	
			 	'SUPPLIER' party_type,
				im.supplier_id	party_id,
				im.currency	currency_code,	
				NEW.input_rate inv_rate,
				NEW.cost_price,	
				im.inv_type,
				NEW.inv_det_id,
				im.inv_id,
				null	remarks,
				0	sort_seq,
				0	is_locked,
				NEW.created_by,
				current_timestamp created_dt,
				NEW.is_deleted,
				im.exchange_rate,
				im.inv_no
			from
				tbl_purchase_invoice_main im
			where
				im.inv_id = new.inv_id
		 ;
	else
		
		update tbl_product_stock_trans
		set
			inv_dt = inv_main.inv_dt,
			party_id = inv_main.supplier_id,
			currency_code = inv_main.currency,
			qty1 = NEW.inv_qty1,
			qty2 = NEW.inv_qty2,
			inv_rate = NEW.input_rate,
			cost_price = NEW.cost_price,
			modified_by = NEW.modified_by,
			modified_dt = current_timestamp,
			is_deleted = NEW.is_deleted,
			exchange_rate = inv_main.exchange_rate
		where
			inv_det_id = NEW.inv_det_id
			and inv_type = inv_main.inv_type
		;
	end if;

	update tbl_purchase_invoice_main
	set
		total_invoice_qty1 = (total_invoice_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty1 end ) - (v_old_qty1)),
		total_invoice_qty2 = (total_invoice_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty2 end ) - (v_old_qty2)),
		total_invoice_gross_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)),
        total_invoice_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)) +
							   (party_payable_amount)
	where
		inv_id = NEW.inv_id
		and inv_type = inv_main.inv_type
	;

    -- For Confirm Purchase need to update the reference Memo purchase Invoice qty
    if NEW.ref_inv_type = 'RAPI' then
       update
           tbl_memo_purchase_invoice_det
       set
           conf_qty1 = (conf_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty1 end ) - (v_old_qty1)),
           conf_qty2 = (conf_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty2 end ) - (v_old_qty2)),
           conf_input_rate = case when NEW.conf_qty1 > 0 then NEW.input_rate else 0 end ,
           conf_input_amt = case when NEW.conf_qty1 > 0 then ((conf_input_amt) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt))) else 0 end
       where
           inv_det_id = NEW.ref_inv_det_id
       ;
    end if;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_purchase_invoice_det ON tbl_purchase_invoice_det;

CREATE trigger trg_purchase_invoice_det
  AFTER INSERT or update or delete
  ON tbl_purchase_invoice_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_purchase_invoice_det();

CREATE OR REPLACE FUNCTION fn_purchase_return_invoice_det() RETURNS trigger AS
$$
declare
	v_count int;
	inv_main tbl_purchase_return_invoice_main%rowtype;
  v_operation_flag integer:= 0;
	v_old_qty1 numeric(20,4) := 0;
	v_old_qty2	numeric(20,4) := 0;
	v_old_input_amt	numeric(20,4) := 0;
BEGIN

    if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_purchase_return_invoice_main
	where
		inv_id = NEW.inv_id
	;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	if v_operation_flag > 1 then
		v_old_qty1 := OLD.inv_qty1;
		v_old_qty2 := OLD.inv_qty2;
		v_old_input_amt := OLD.input_amt;
	end if;

	select
		count(1)
	into
		v_count
	from
		tbl_product_stock_trans tr
	where
		tr.inv_det_id = NEW.inv_det_id
		and tr.inv_type = inv_main.inv_type
	;

	if v_count = 0 then
		 insert into tbl_product_stock_trans
		 (

				company_id,
				product_id,
				bin_id,
				inv_dt,
				in_out_flag,
				qty1,
				qty2,
				adj_qty1,
				adj_qty2,
				party_type,
				party_id,
				currency_code,
				inv_rate,
				cost_price,
				inv_type,
				inv_det_id,
				inv_id,
				remarks,
				sort_seq,
				is_locked,
				created_by,
				created_dt,
				is_deleted,
				exchange_rate,
				inv_no
		 )
		 select
		 		im.company_id,
				NEW.product_id,
				NEW.bin_id,
				im.inv_dt,
				-1	in_out_flag,
				NEW.inv_qty1	qty1,
				NEW.inv_qty2	qty2,
				0	dj_qty1,
				0	dj_qty2,
			 	'SUPPLIER' 			party_type,
				im.supplier_id	party_id,
				im.currency	currency_code,
				NEW.input_rate inv_rate,
				NEW.cost_price,
				im.inv_type,
				NEW.inv_det_id,
				im.inv_id,
				null	remarks,
				0	sort_seq,
				0	is_locked,
				NEW.created_by,
				current_timestamp created_dt,
				NEW.is_deleted,
				im.exchange_rate,
				im.inv_no
			from
				tbl_purchase_return_invoice_main im
			where
				im.inv_id = new.inv_id
		 ;
	else

		update tbl_product_stock_trans
		set
			inv_dt = inv_main.inv_dt,
			party_id = inv_main.supplier_id,
			currency_code = inv_main.currency,
			qty1 = NEW.inv_qty1,
			qty2 = NEW.inv_qty2,
			inv_rate = NEW.input_rate,
			cost_price = NEW.cost_price,
			modified_by = NEW.modified_by,
			modified_dt = current_timestamp,
			is_deleted = NEW.is_deleted,
			exchange_rate = inv_main.exchange_rate
		where
			inv_det_id = NEW.inv_det_id
			and inv_type = inv_main.inv_type
		;
	end if;

		update tbl_purchase_return_invoice_main
    	set
    		total_invoice_qty1 = (total_invoice_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty1 end ) - (v_old_qty1)),
    		total_invoice_qty2 = (total_invoice_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty2 end ) - (v_old_qty2)),
    		total_invoice_gross_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)),
    		total_invoice_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)) +
            							   (party_payable_amount)
    	where
    		inv_id = NEW.inv_id
    		and inv_type = inv_main.inv_type
    	;

    	update
    		tbl_purchase_invoice_det
    	set
    		ret_qty1 = (ret_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty1 end ) - (v_old_qty1)),
    		ret_qty2 = (ret_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty2 end ) - (v_old_qty2)),
    		ret_input_rate = NEW.input_rate,
    		ret_input_amt = (ret_input_amt) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt))
    	where
    		inv_det_id = NEW.ref_inv_det_id
    	;


    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS trg_purchase_return_invoice_det ON tbl_purchase_return_invoice_det;

CREATE trigger trg_purchase_return_invoice_det
  AFTER INSERT or update or delete
  ON tbl_purchase_return_invoice_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_purchase_return_invoice_det();

CREATE OR REPLACE FUNCTION fn_sales_invoice_det() RETURNS trigger AS
$$
declare
	v_count int;
	inv_main tbl_sales_invoice_main%rowtype;
    v_operation_flag integer:= 0;
	v_old_qty1 numeric(20,4) := 0;
	v_old_qty2	numeric(20,4) := 0;
	v_old_input_amt	numeric(20,4) := 0;
BEGIN

    if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_sales_invoice_main
	where
		inv_id = NEW.inv_id
	;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	if v_operation_flag > 1 then
		v_old_qty1 := OLD.inv_qty1;
		v_old_qty2 := OLD.inv_qty2;
		v_old_input_amt := OLD.input_amt;
	end if;

	select
		count(1)
	into
		v_count
	from
		tbl_product_stock_trans tr
	where
		tr.inv_det_id = NEW.inv_det_id
		and tr.inv_type = inv_main.inv_type
	;

	if v_count = 0 then
		 insert into tbl_product_stock_trans
		 (

				company_id,
				product_id,
				bin_id,
				inv_dt,
				in_out_flag,
				qty1,
				qty2,
				adj_qty1,
				adj_qty2,
				party_type,
				party_id,
				currency_code,
				inv_rate,
				cost_price,
				inv_type,
				inv_det_id,
				inv_id,
				remarks,
				sort_seq,
				is_locked,
				created_by,
				created_dt,
				is_deleted,
				exchange_rate,
				inv_no
		 )
		 select
		 		im.company_id,
				NEW.product_id,
				NEW.bin_id,
				im.inv_dt,
				-1	in_out_flag,
				NEW.inv_qty1	qty1,
				NEW.inv_qty2	qty2,
				0	dj_qty1,
				0	dj_qty2,
			 	'CUSTOMER' party_type,
				im.customer_id	party_id,
				im.currency	currency_code,
				NEW.input_rate inv_rate,
				NEW.cost_price,
				im.inv_type,
				NEW.inv_det_id,
				im.inv_id,
				null	remarks,
				2	sort_seq,
				0	is_locked,
				NEW.created_by,
				current_timestamp created_dt,
				NEW.is_deleted,
				im.exchange_rate,
				im.inv_no
			from
				tbl_sales_invoice_main im
			where
				im.inv_id = new.inv_id
		 ;
	else

		update tbl_product_stock_trans
		set
			inv_dt = inv_main.inv_dt,
			party_id = inv_main.customer_id,
			currency_code = inv_main.currency,
			qty1 = NEW.inv_qty1,
			qty2 = NEW.inv_qty2,
			inv_rate = NEW.input_rate,
			cost_price = NEW.cost_price,
			modified_by = NEW.modified_by,
			modified_dt = current_timestamp,
			is_deleted = NEW.is_deleted,
			exchange_rate = inv_main.exchange_rate
		where
			inv_det_id = NEW.inv_det_id
			and inv_type = inv_main.inv_type
		;

	end if;
	    update tbl_sales_invoice_main
    	set
    		total_invoice_qty1 = (total_invoice_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty1 end ) - (v_old_qty1)),
    		total_invoice_qty2 = (total_invoice_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty2 end ) - (v_old_qty2)),
    		total_invoice_gross_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)),
            total_invoice_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)) +
							   (party_payable_amount)
    	where
    		inv_id = NEW.inv_id
    		and inv_type = inv_main.inv_type
    	;
    	-- For Confirm Sales need to update the reference Memo Sales Invoice qty
        if NEW.ref_inv_type = 'RASI' then
           update
               tbl_memo_sales_invoice_det
           set
               conf_qty1 = (conf_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty1 end ) - (v_old_qty1)),
               conf_qty2 = (conf_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty2 end ) - (v_old_qty2)),
               conf_input_rate = case when NEW.conf_qty1 > 0 then NEW.input_rate else 0 end ,
               conf_input_amt = case when NEW.conf_qty1 > 0 then ((conf_input_amt) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt))) else 0 end
           where
               inv_det_id = NEW.ref_inv_det_id
           ;
        end if;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS trg_sales_invoice_det ON tbl_sales_invoice_det;

CREATE trigger trg_sales_invoice_det
  AFTER INSERT or update or delete
  ON tbl_sales_invoice_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_sales_invoice_det();

CREATE OR REPLACE FUNCTION fn_sales_return_invoice_det() RETURNS trigger AS
$$
declare
	v_count int;
	inv_main tbl_sales_return_invoice_main%rowtype;
  	v_operation_flag integer:= 0;
	v_old_qty1 numeric(20,4) := 0;
	v_old_qty2	numeric(20,4) := 0;
	v_old_input_amt	numeric(20,4) := 0;
BEGIN

    if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_sales_return_invoice_main
	where
		inv_id = NEW.inv_id
	;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	if v_operation_flag > 1 then
		v_old_qty1 := OLD.inv_qty1;
		v_old_qty2 := OLD.inv_qty2;
		v_old_input_amt := OLD.input_amt;
	end if;

	select
		count(1)
	into
		v_count
	from
		tbl_product_stock_trans tr
	where
		tr.inv_det_id = NEW.inv_det_id
		and tr.inv_type = inv_main.inv_type
	;

	if v_count = 0 then
		 insert into tbl_product_stock_trans
		 (

				company_id,
				product_id,
				bin_id,
				inv_dt,
				in_out_flag,
				qty1,
				qty2,
				adj_qty1,
				adj_qty2,
				party_type,
				party_id,
				currency_code,
				inv_rate,
				cost_price,
				inv_type,
				inv_det_id,
				inv_id,
				remarks,
				sort_seq,
				is_locked,
				created_by,
				created_dt,
				is_deleted,
				exchange_rate,
				inv_no
		 )
		 select
		 		im.company_id,
				NEW.product_id,
				NEW.bin_id,
				im.inv_dt,
				1	in_out_flag,
				NEW.inv_qty1	qty1,
				NEW.inv_qty2	qty2,
				0	dj_qty1,
				0	dj_qty2,
			 	'CUSTOMER'      party_type,
				im.customer_id	party_id,
				im.currency	currency_code,
				NEW.input_rate inv_rate,
				NEW.cost_price,
				im.inv_type,
				NEW.inv_det_id,
				im.inv_id,
				null	remarks,
				0	sort_seq,
				0	is_locked,
				NEW.created_by,
				current_timestamp created_dt,
				NEW.is_deleted,
				im.exchange_rate,
				im.inv_no
			from
				tbl_sales_return_invoice_main im
			where
				im.inv_id = new.inv_id
		 ;
	else

		update tbl_product_stock_trans
		set
			inv_dt = inv_main.inv_dt,
			party_id = inv_main.customer_id,
			currency_code = inv_main.currency,
			qty1 = NEW.inv_qty1,
			qty2 = NEW.inv_qty2,
			inv_rate = NEW.input_rate,
			cost_price = NEW.cost_price,
			modified_by = NEW.modified_by,
			modified_dt = current_timestamp,
			is_deleted = NEW.is_deleted,
			exchange_rate = inv_main.exchange_rate
		where
			inv_det_id = NEW.inv_det_id
			and inv_type = inv_main.inv_type
		;
	end if;

		update tbl_sales_return_invoice_main
    	set
    		total_invoice_qty1 = (total_invoice_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty1 end ) - (v_old_qty1)),
    		total_invoice_qty2 = (total_invoice_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty2 end ) - (v_old_qty2)),
    		total_invoice_gross_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)),
            total_invoice_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)) +
            							   (party_payable_amount)
    	where
    		inv_id = NEW.inv_id
    		and inv_type = inv_main.inv_type
    	;

    	update
    		tbl_sales_invoice_det
    	set
    		ret_qty1 = (ret_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty1 end ) - (v_old_qty1)),
    		ret_qty2 = (ret_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty2 end ) - (v_old_qty2)),
    		ret_input_rate = NEW.input_rate,
    		ret_input_amt = (ret_input_amt) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt))
    	where
    		inv_det_id = NEW.ref_inv_det_id
    	;


    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS trg_sales_return_invoice_det ON tbl_sales_return_invoice_det;

CREATE trigger trg_sales_return_invoice_det
  AFTER INSERT or update or delete
  ON tbl_sales_return_invoice_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_sales_return_invoice_det();

CREATE OR REPLACE FUNCTION fn_memo_purchase_invoice_det()
  RETURNS trigger AS
$$
declare

	v_count 	int;
	inv_main 	tbl_memo_purchase_invoice_main%rowtype;
    v_operation_flag integer:= 0;
	v_old_qty1 numeric(20,4) := 0;
	v_old_qty2	numeric(20,4) := 0;
	v_old_input_amt	numeric(20,4) := 0;
BEGIN

    if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_memo_purchase_invoice_main
	where
		inv_id = NEW.inv_id
	;

	if v_operation_flag > 1 then
		v_old_qty1 := OLD.inv_qty1;
		v_old_qty2 := OLD.inv_qty2;
		v_old_input_amt := OLD.input_amt;
	end if;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	select
		count(1)
	into
		v_count
	from
		tbl_product_stock_trans tr
	where
		tr.inv_det_id = NEW.inv_det_id
		and tr.inv_type = inv_main.inv_type
	;

	if v_count = 0 then
		 insert into tbl_product_stock_trans
		 (

				company_id,
				product_id,
				bin_id,
				inv_dt,
				in_out_flag,
				qty1,
				qty2,
				adj_qty1,
				adj_qty2,
				party_type,
				party_id,
				currency_code,
				inv_rate,
				cost_price,
				inv_type,
				inv_det_id,
				inv_id,
				remarks,
				sort_seq,
				is_locked,
				created_by,
				created_dt,
				is_deleted,
				exchange_rate,
				inv_no
		 )
		 select
		 		im.company_id,
				NEW.product_id,
				NEW.bin_id,
				im.inv_dt,
				1	in_out_flag,
				NEW.inv_qty1	qty1,
				NEW.inv_qty2	qty2,
				0	dj_qty1,
				0	dj_qty2,
			 	'SUPPLIER' party_type,
				im.supplier_id	party_id,
				im.currency	currency_code,
				NEW.input_rate inv_rate,
				NEW.cost_price,
				im.inv_type,
				NEW.inv_det_id,
				im.inv_id,
				null	remarks,
				0	sort_seq,
				0	is_locked,
				NEW.created_by,
				current_timestamp created_dt,
				NEW.is_deleted,
				im.exchange_rate,
				im.inv_no
			from
				tbl_memo_purchase_invoice_main im
			where
				im.inv_id = new.inv_id
		 ;
	else

		update tbl_product_stock_trans
		set
			inv_dt = inv_main.inv_dt,
			party_id = inv_main.supplier_id,
			currency_code = inv_main.currency,
			qty1 = NEW.inv_qty1,
			qty2 = NEW.inv_qty2,
			inv_rate = NEW.input_rate,
			cost_price = NEW.cost_price,
			modified_by = NEW.modified_by,
			modified_dt = current_timestamp,
			is_deleted = NEW.is_deleted,
			exchange_rate = inv_main.exchange_rate
		where
			inv_det_id = NEW.inv_det_id
			and inv_type = inv_main.inv_type
		;

	end if;

        update tbl_memo_purchase_invoice_main
    	set
    		total_invoice_qty1 = (total_invoice_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty1 end ) - (v_old_qty1)),
    		total_invoice_qty2 = (total_invoice_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty2 end ) - (v_old_qty2)),
    		total_invoice_gross_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)),
            total_invoice_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)) +
							   (party_payable_amount)
    	where
    		inv_id = NEW.inv_id
    		and inv_type = inv_main.inv_type
    	;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_memo_purchase_invoice_det ON tbl_memo_purchase_invoice_det;

CREATE trigger trg_memo_purchase_invoice_det
  AFTER INSERT or update or delete
  ON tbl_memo_purchase_invoice_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_memo_purchase_invoice_det();

CREATE OR REPLACE FUNCTION fn_memo_purchase_return_invoice_det()
  RETURNS trigger AS
$$
declare

	v_count 	int;
	inv_main 	tbl_memo_purchase_return_invoice_main%rowtype;
    v_operation_flag integer:= 0;
	v_old_qty1 numeric(20,4) := 0;
	v_old_qty2	numeric(20,4) := 0;
	v_old_input_amt	numeric(20,4) := 0;
BEGIN

    if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_memo_purchase_return_invoice_main
	where
		inv_id = NEW.inv_id
	;

	if v_operation_flag > 1 then
		v_old_qty1 := OLD.inv_qty1;
		v_old_qty2 := OLD.inv_qty2;
		v_old_input_amt := OLD.input_amt;
	end if;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	select
		count(1)
	into
		v_count
	from
		tbl_product_stock_trans tr
	where
		tr.inv_det_id = NEW.inv_det_id
		and tr.inv_type = inv_main.inv_type
	;

	if v_count = 0 then
		 insert into tbl_product_stock_trans
		 (

				company_id,
				product_id,
				bin_id,
				inv_dt,
				in_out_flag,
				qty1,
				qty2,
				adj_qty1,
				adj_qty2,
				party_type,
				party_id,
				currency_code,
				inv_rate,
				cost_price,
				inv_type,
				inv_det_id,
				inv_id,
				remarks,
				sort_seq,
				is_locked,
				created_by,
				created_dt,
				is_deleted,
				exchange_rate,
				inv_no
		 )
		 select
		 		im.company_id,
				NEW.product_id,
				NEW.bin_id,
				im.inv_dt,
				-1	in_out_flag,
				NEW.inv_qty1	qty1,
				NEW.inv_qty2	qty2,
				0	dj_qty1,
				0	dj_qty2,
			 	'SUPPLIER' party_type,
				im.supplier_id	party_id,
				im.currency	currency_code,
				NEW.input_rate inv_rate,
				NEW.cost_price,
				im.inv_type,
				NEW.inv_det_id,
				im.inv_id,
				null	remarks,
				0	sort_seq,
				0	is_locked,
				NEW.created_by,
				current_timestamp created_dt,
				NEW.is_deleted,
				im.exchange_rate,
				im.inv_no
			from
				tbl_memo_purchase_return_invoice_main im
			where
				im.inv_id = new.inv_id
		 ;
	else

		update tbl_product_stock_trans
		set
			inv_dt = inv_main.inv_dt,
			party_id = inv_main.supplier_id,
			currency_code = inv_main.currency,
			qty1 = NEW.inv_qty1,
			qty2 = NEW.inv_qty2,
			inv_rate = NEW.input_rate,
			cost_price = NEW.cost_price,
			modified_by = NEW.modified_by,
			modified_dt = current_timestamp,
			is_deleted = NEW.is_deleted,
			exchange_rate = inv_main.exchange_rate
		where
			inv_det_id = NEW.inv_det_id
			and inv_type = inv_main.inv_type
		;

	end if;

        update tbl_memo_purchase_return_invoice_main
    	set
    		total_invoice_qty1 = (total_invoice_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty1 end ) - (v_old_qty1)),
    		total_invoice_qty2 = (total_invoice_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty2 end ) - (v_old_qty2)),
    		total_invoice_gross_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)),
            total_invoice_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)) +
            							   (party_payable_amount)
    	where
    		inv_id = NEW.inv_id
    		and inv_type = inv_main.inv_type
    	;

    	update
    		tbl_memo_purchase_invoice_det
    	set
    		ret_qty1 = (ret_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty1 end ) - (v_old_qty1)),
    		ret_qty2 = (ret_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty2 end ) - (v_old_qty2)),
    		ret_input_rate = NEW.input_rate,
    		ret_input_amt = (ret_input_amt) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt))
    	where
    		inv_det_id = NEW.ref_inv_det_id
    	;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS trg_memo_purchase_return_invoice_det ON tbl_memo_purchase_return_invoice_det;

CREATE trigger trg_memo_purchase_return_invoice_det
  AFTER INSERT or update or delete
  ON tbl_memo_purchase_return_invoice_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_memo_purchase_return_invoice_det();

CREATE OR REPLACE FUNCTION fn_memo_sales_invoice_det() RETURNS trigger AS
$$
declare

	v_count int;
	inv_main tbl_memo_sales_invoice_main%rowtype;
    v_operation_flag integer:= 0;
	v_old_qty1 numeric(20,4) := 0;
	v_old_qty2	numeric(20,4) := 0;
	v_old_input_amt	numeric(20,4) := 0;

	v_memo_sales_bin integer;
BEGIN

    if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_memo_sales_invoice_main
	where
		inv_id = NEW.inv_id
	;

	if v_operation_flag > 1 then
		v_old_qty1 := OLD.inv_qty1;
		v_old_qty2 := OLD.inv_qty2;
		v_old_input_amt := OLD.input_amt;
	end if;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	select max(bin_id) into v_memo_sales_bin from tbl_bin where is_deleted = 0 and bin_type = 'RM_MEMO_SALES' and is_default = 1;

	if v_memo_sales_bin is null then
	    raise exception 'Memo Sales Bin not configured, Unable to execute Stock Impact';
	end if;

	select
		count(1)
	into
		v_count
	from
		tbl_product_stock_trans tr
	where
		tr.inv_det_id = NEW.inv_det_id
		and tr.inv_type = inv_main.inv_type
	;

	if v_count = 0 then
		 insert into tbl_product_stock_trans
		 (

				company_id,
				product_id,
				bin_id,
				inv_dt,
				in_out_flag,
				qty1,
				qty2,
				adj_qty1,
				adj_qty2,
				party_type,
				party_id,
				currency_code,
				inv_rate,
				cost_price,
				inv_type,
				inv_det_id,
				inv_id,
				remarks,
				sort_seq,
				is_locked,
				created_by,
				created_dt,
				is_deleted,
				exchange_rate,
				inv_no
		 )
		 select
		 		im.company_id,
				NEW.product_id,
				NEW.bin_id,
				im.inv_dt,
				-1	in_out_flag,
				NEW.inv_qty1	qty1,
				NEW.inv_qty2	qty2,
				0	dj_qty1,
				0	dj_qty2,
			 	'CUSTOMER' party_type,
				im.customer_id	party_id,
				im.currency	currency_code,
				NEW.input_rate inv_rate,
				NEW.cost_price,
				im.inv_type,
				NEW.inv_det_id,
				im.inv_id,
				null	remarks,
				1	sort_seq,
				0	is_locked,
				NEW.created_by,
				current_timestamp created_dt,
				NEW.is_deleted,
				im.exchange_rate,
				im.inv_no
			from
				tbl_memo_sales_invoice_main im
			where
				im.inv_id = new.inv_id
            union all
            select
                im.company_id,
                NEW.product_id,
                v_memo_sales_bin bin_id,
                im.inv_dt,
                1	in_out_flag,
                NEW.inv_qty1	qty1,
                NEW.inv_qty2	qty2,
                0	dj_qty1,
                0	dj_qty2,
                'CUSTOMER' party_type,
                im.customer_id	party_id,
                im.currency	currency_code,
                NEW.input_rate inv_rate,
                NEW.cost_price,
                im.inv_type,
                NEW.inv_det_id,
                im.inv_id,
                null	remarks,
                2	sort_seq,
                0	is_locked,
                NEW.created_by,
                current_timestamp created_dt,
                NEW.is_deleted,
                im.exchange_rate,
                im.inv_no
            from
                tbl_memo_sales_invoice_main im
            where
                im.inv_id = new.inv_id
		 ;
	else

		update tbl_product_stock_trans
		set
			inv_dt = inv_main.inv_dt,
			party_id = inv_main.customer_id,
			currency_code = inv_main.currency,
			qty1 = NEW.inv_qty1,
			qty2 = NEW.inv_qty2,
			inv_rate = NEW.input_rate,
			cost_price = NEW.cost_price,
			modified_by = NEW.modified_by,
			modified_dt = current_timestamp,
			is_deleted = NEW.is_deleted,
			exchange_rate = inv_main.exchange_rate
		where
			inv_det_id = NEW.inv_det_id
			and inv_type = inv_main.inv_type
		;
	end if;

	update tbl_memo_sales_invoice_main
    set
        total_invoice_qty1 = (total_invoice_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty1 end ) - (v_old_qty1)),
        total_invoice_qty2 = (total_invoice_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty2 end ) - (v_old_qty2)),
        total_invoice_gross_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)),
        total_invoice_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)) +
        							   (party_payable_amount)
    where
        inv_id = NEW.inv_id
        and inv_type = inv_main.inv_type
    ;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS trg_memo_sales_invoice_det ON tbl_memo_sales_invoice_det;

CREATE trigger trg_memo_sales_invoice_det
  AFTER INSERT or update or delete
  ON tbl_memo_sales_invoice_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_memo_sales_invoice_det();

CREATE OR REPLACE FUNCTION fn_memo_sales_return_invoice_det() RETURNS trigger AS
$$
declare

	v_count int;
	inv_main tbl_memo_sales_return_invoice_main%rowtype;
    v_operation_flag integer:= 0;
	v_old_qty1 numeric(20,4) := 0;
	v_old_qty2	numeric(20,4) := 0;
	v_old_input_amt	numeric(20,4) := 0;
	v_memo_sales_bin integer;
BEGIN

    if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_memo_sales_return_invoice_main
	where
		inv_id = NEW.inv_id
	;

	if v_operation_flag > 1 then
		v_old_qty1 := OLD.inv_qty1;
		v_old_qty2 := OLD.inv_qty2;
		v_old_input_amt := OLD.input_amt;
	end if;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	select max(bin_id) into v_memo_sales_bin from tbl_bin where is_deleted = 0 and bin_type = 'RM_MEMO_SALES' and is_default = 1;

    if v_memo_sales_bin is null then
        raise exception 'Memo Sales Bin not configured, Unable to execute Stock Impact';
    end if;

	select
		count(1)
	into
		v_count
	from
		tbl_product_stock_trans tr
	where
		tr.inv_det_id = NEW.inv_det_id
		and tr.inv_type = inv_main.inv_type
	;

	if v_count = 0 then
		 insert into tbl_product_stock_trans
		 (

				company_id,
				product_id,
				bin_id,
				inv_dt,
				in_out_flag,
				qty1,
				qty2,
				adj_qty1,
				adj_qty2,
				party_type,
				party_id,
				currency_code,
				inv_rate,
				cost_price,
				inv_type,
				inv_det_id,
				inv_id,
				remarks,
				sort_seq,
				is_locked,
				created_by,
				created_dt,
				is_deleted,
				exchange_rate,
				inv_no
		 )
		 select
		 		im.company_id,
				NEW.product_id,
				v_memo_sales_bin bin_id,
				im.inv_dt,
				-1	in_out_flag,
				NEW.inv_qty1	qty1,
				NEW.inv_qty2	qty2,
				0	dj_qty1,
				0	dj_qty2,
			 	'CUSTOMER' party_type,
				im.customer_id	party_id,
				im.currency	currency_code,
				NEW.input_rate inv_rate,
				NEW.cost_price,
				im.inv_type,
				NEW.inv_det_id,
				im.inv_id,
				null	remarks,
				1	sort_seq,
				0	is_locked,
				NEW.created_by,
				current_timestamp created_dt,
				NEW.is_deleted,
				im.exchange_rate,
				im.inv_no
			from
				tbl_memo_sales_return_invoice_main im
			where
				im.inv_id = new.inv_id
            union all
            select
                im.company_id,
                NEW.product_id,
                NEW.bin_id,
                im.inv_dt,
                1	in_out_flag,
                NEW.inv_qty1	qty1,
                NEW.inv_qty2	qty2,
                0	dj_qty1,
                0	dj_qty2,
                'CUSTOMER' party_type,
                im.customer_id	party_id,
                im.currency	currency_code,
                NEW.input_rate inv_rate,
                NEW.cost_price,
                im.inv_type,
                NEW.inv_det_id,
                im.inv_id,
                null	remarks,
                1	sort_seq,
                0	is_locked,
                NEW.created_by,
                current_timestamp created_dt,
                NEW.is_deleted,
                im.exchange_rate,
                im.inv_no
            from
                tbl_memo_sales_return_invoice_main im
            where
                im.inv_id = new.inv_id
		 ;
	else

		update tbl_product_stock_trans
		set
			inv_dt = inv_main.inv_dt,
			party_id = inv_main.customer_id,
			currency_code = inv_main.currency,
			qty1 = NEW.inv_qty1,
			qty2 = NEW.inv_qty2,
			inv_rate = NEW.input_rate,
			cost_price = NEW.cost_price,
			modified_by = NEW.modified_by,
			modified_dt = current_timestamp,
			is_deleted = NEW.is_deleted,
			exchange_rate = inv_main.exchange_rate
		where
			inv_det_id = NEW.inv_det_id
			and inv_type = inv_main.inv_type
		;
	end if;

	update tbl_memo_sales_return_invoice_main
    set
        total_invoice_qty1 = (total_invoice_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty1 end ) - (v_old_qty1)),
        total_invoice_qty2 = (total_invoice_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty2 end ) - (v_old_qty2)),
        total_invoice_gross_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)),
        total_invoice_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)) +
							   (party_payable_amount)
    where
        inv_id = NEW.inv_id
        and inv_type = inv_main.inv_type
    ;

    update
        tbl_memo_sales_invoice_det
    set
        ret_qty1 = (ret_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty1 end ) - (v_old_qty1)),
        ret_qty2 = (ret_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty2 end ) - (v_old_qty2)),
        ret_input_rate = NEW.input_rate,
        ret_input_amt = (ret_input_amt) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt))
    where
        inv_det_id = NEW.ref_inv_det_id
    ;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS trg_memo_sales_return_invoice_det ON tbl_memo_sales_return_invoice_det;

CREATE trigger trg_memo_sales_return_invoice_det
  AFTER INSERT or update or delete
  ON tbl_memo_sales_return_invoice_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_memo_sales_return_invoice_det();

CREATE OR REPLACE FUNCTION fn_bin_transfer_det() RETURNS trigger AS
$$
declare

	v_count int;
	inv_main tbl_bin_transfer_main%rowtype;
    v_operation_flag integer:= 0;
	v_old_qty1 numeric(20,4) := 0;
	v_old_qty2	numeric(20,4) := 0;
    v_old_input_amt	numeric(20,4) := 0;
BEGIN

    if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	if v_operation_flag > 1 then
		v_old_qty1 := OLD.transfer_qty1;
		v_old_qty2 := OLD.transfer_qty2;
		v_old_input_amt := 0;
	end if;


	select
		*
	into
	 	inv_main
	from
	 	tbl_bin_transfer_main
	where
		transfer_id = NEW.transfer_id
	;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	select
		count(1)
	into
		v_count
	from
		tbl_product_stock_trans tr
	where
		tr.inv_det_id = NEW.transfer_det_id
		and tr.inv_type = inv_main.transfer_type
	;

	if v_count = 0 then
		 insert into tbl_product_stock_trans
		 (
				company_id,
				product_id,
				bin_id,
				inv_dt,
				in_out_flag,
				qty1,
				qty2,
				adj_qty1,
				adj_qty2,
				party_type,
				party_id,
				currency_code,
				inv_rate,
				cost_price,
				inv_type,
				inv_det_id,
				inv_id,
				remarks,
				sort_seq,
				is_locked,
				created_by,
				created_dt,
				is_deleted,
				exchange_rate,
				inv_no
		 )
		 select
		 		im.company_id,
				NEW.product_id,
				NEW.source_bin_id 	bin_id,
				im.transfer_dt		inv_dt,
				-1	in_out_flag,
				NEW.transfer_qty1	qty1,
				NEW.transfer_qty2	qty2,
				0	dj_qty1,
				0	dj_qty2,
			 	'NA' party_type,
				-1	 party_id,
				im.currency_code	currency_code,
				NEW.cost_price 		inv_rate,
				NEW.cost_price,
				im.transfer_type 	inv_type,
				NEW.transfer_det_id	inv_det_id,
				im.transfer_id		inv_id,
				null	remarks,
				1       sort_seq,
				0	is_locked,
				NEW.created_by,
				current_timestamp created_dt,
				NEW.is_deleted,
				im.exchange_rate,
				im.transfer_no
			from
				tbl_bin_transfer_main im
			where
				im.transfer_id = new.transfer_id
			union all
			select
		 		im.company_id,
				NEW.product_id,
				NEW.dest_bin_id 	bin_id,
				im.transfer_dt		inv_dt,
				1	in_out_flag,
				NEW.transfer_qty1	qty1,
				NEW.transfer_qty2	qty2,
				0	dj_qty1,
				0	dj_qty2,
			 	'NA' party_type,
				-1	 party_id,
				im.currency_code	currency_code,
				NEW.cost_price 		inv_rate,
				NEW.cost_price,
				im.transfer_type 	inv_type,
				NEW.transfer_det_id	inv_det_id,
				im.transfer_id		inv_id,
				null	remarks,
				2   sort_seq,
				0	is_locked,
				NEW.created_by,
				current_timestamp created_dt,
				NEW.is_deleted,
				im.exchange_rate,
				im.transfer_no
			from
				tbl_bin_transfer_main im
			where
				im.transfer_id = new.transfer_id
		 ;
	else

		update tbl_product_stock_trans
		set
			inv_dt = inv_main.transfer_dt,
			currency_code = inv_main.currency_code,
			qty1 = NEW.transfer_qty1,
			qty2 = NEW.transfer_qty2,
			inv_rate = NEW.cost_price,
			cost_price = NEW.cost_price,
			modified_by = NEW.modified_by,
			modified_dt = current_timestamp,
			is_deleted = NEW.is_deleted,
			exchange_rate = inv_main.exchange_rate
		where
			inv_det_id = NEW.transfer_det_id
			and inv_type = inv_main.transfer_type
		;

	end if;

	update tbl_bin_transfer_main
	set
		total_qty1 = (total_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.transfer_qty1 end ) - (v_old_qty1)),
		total_qty2 = (total_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.transfer_qty2 end ) - (v_old_qty2))
	where
		transfer_id = NEW.transfer_id
		and transfer_type = inv_main.transfer_type
	;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_bin_transfer_det ON tbl_bin_transfer_det;

CREATE trigger trg_bin_transfer_det
  AFTER INSERT or update or delete
  ON tbl_bin_transfer_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_bin_transfer_det();

CREATE OR REPLACE FUNCTION fn_mixing_det() RETURNS trigger AS
$$
declare
	v_count int;
	inv_main tbl_mixing_main%rowtype;
	v_operation_flag    integer := 0;
	v_old_qty1 numeric(20,4) := 0;
	v_old_qty2	numeric(20,4) := 0;
	v_old_input_amt	numeric(20,4) := 0;
BEGIN

	if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

    if v_operation_flag = 2 then
        raise exception 'Update option not available enabled, Unable to execute Stock Impact !';
    end if;

	if v_operation_flag > 1 then
		v_old_qty1 := OLD.mixing_qty1;
		v_old_qty2 := OLD.mixing_qty2;
		v_old_input_amt := OLD.destination_cost_price;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_mixing_main
	where
		mixing_id = NEW.mixing_id
	;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	select
		count(1)
	into
		v_count
	from
		tbl_product_stock_trans tr
	where
		tr.inv_det_id = NEW.mixing_det_id
		and tr.inv_type = inv_main.mixing_type
	;

	if v_count = 0 then
		 insert into tbl_product_stock_trans
		 (

				company_id,
				product_id,
				bin_id,
				inv_dt,
				in_out_flag,
				qty1,
				qty2,
				adj_qty1,
				adj_qty2,
				party_type,
				party_id,
				currency_code,
				inv_rate,
				cost_price,
				inv_type,
				inv_det_id,
				inv_id,
				remarks,
				sort_seq,
				is_locked,
				created_by,
				created_dt,
				is_deleted,
				exchange_rate,
				inv_no
		 )
		 select
		 		im.company_id,
				NEW.source_product_id	product_id,
				NEW.source_bin_id 		bin_id,
				im.mixing_dt			inv_dt,
				-1	in_out_flag,
				NEW.mixing_qty1		qty1,
				NEW.mixing_qty2		qty2,
				0	dj_qty1,
				0	dj_qty2,
			 	'NA' party_type,
				-1	 party_id,
				im.currency_code	currency_code,
				NEW.source_cost_price 		inv_rate,
				NEW.source_cost_price,
				im.mixing_type		inv_type,
				NEW.mixing_det_id	inv_det_id,
				im.mixing_id		inv_id,
				null	remarks,
				0	sort_seq,
				0	is_locked,
				NEW.created_by,
				current_timestamp created_dt,
				NEW.is_deleted,
				im.exchange_rate,
				im.mixing_no
			from
				tbl_mixing_main im
			where
				im.mixing_id = new.mixing_id
			union all
			select
		 		im.company_id,
				NEW.dest_product_id	product_id,
				NEW.dest_bin_id 	bin_id,
				im.mixing_dt		inv_dt,
				1	in_out_flag,
				NEW.mixing_qty1	qty1,
				NEW.mixing_qty2	qty2,
				0	dj_qty1,
				0	dj_qty2,
			 	'NA' party_type,
				-1	 party_id,
				im.currency_code	currency_code,
				NEW.destination_cost_price 		inv_rate,
				NEW.destination_cost_price,
				im.mixing_type 	inv_type,
				NEW.mixing_det_id	inv_det_id,
				im.mixing_id		inv_id,
				null				remarks,
				0	sort_seq,
				0	is_locked,
				NEW.created_by,
				current_timestamp created_dt,
				NEW.is_deleted,
				im.exchange_rate,
				im.mixing_no
			from
				tbl_mixing_main im
			where
				im.mixing_id = new.mixing_id
		 ;
	end if;

	update tbl_mixing_main
	set
		total_mix_qty1 = (total_mix_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.mixing_qty1 end ) - (v_old_qty1)),
		total_mix_qty2 = (total_mix_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.mixing_qty2 end ) - (v_old_qty2)),
		total_mix_amount = (total_mix_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.destination_cost_price end ) - (v_old_input_amt))
	where
		mixing_id = NEW.mixing_id
	;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS trg_mixing_det ON tbl_mixing_det;

CREATE trigger trg_mixing_det
  AFTER INSERT or update or delete
  ON tbl_mixing_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_mixing_det();

CREATE OR REPLACE FUNCTION fn_product_stock_trans()
  RETURNS trigger AS
$$
declare
	v_operation_flag integer:= 0;
	v_count integer := 0;
	v_rec_stock tbl_product_stock%rowtype;
BEGIN

	if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	if v_operation_flag > 1 then

		v_rec_stock.in_qty1 := (case when OLD.in_out_flag = 1 then OLD.qty1 else 0 end);
		v_rec_stock.in_qty2 := (case when OLD.in_out_flag = 1 then OLD.qty2 else 0 end);
		v_rec_stock.out_qty1 := (case when OLD.in_out_flag = -1 then OLD.qty1 else 0 end);
		v_rec_stock.out_qty2 := (case when OLD.in_out_flag = -1 then OLD.qty2 else 0 end);

		update
			tbl_product_stock
		set
			in_qty1 = in_qty1 - v_rec_stock.in_qty1,
			in_qty2 = in_qty2 - v_rec_stock.in_qty2,
			out_qty1 = out_qty1 - v_rec_stock.out_qty1,
			out_qty2 = out_qty2 - v_rec_stock.out_qty2,
			balance_qty1 = balance_qty1 - (v_rec_stock.in_qty1 - v_rec_stock.out_qty1),
			balance_qty2 = balance_qty2 - (v_rec_stock.in_qty2 - v_rec_stock.out_qty2),
			modified_by = OLD.modified_by,
			modified_dt = current_timestamp
		where
			is_deleted = 0
			and product_id = OLD.product_id
			and bin_id = OLD.bin_id
			and company_id = OLD.company_id
		;
	end if;

	if v_operation_flag = 3 then
        return null;
    end if;


	select
		count(1)
	into
		v_count
	from
		tbl_product_stock stk
	where
		stk.bin_id = NEW.bin_id
		and stk.product_id = NEW.product_id
		and stk.company_id = NEW.company_id
		and stk.is_deleted = 0
	;

	v_rec_stock.in_qty1 := (case when NEW.in_out_flag = 1 then NEW.qty1 else 0 end);
	v_rec_stock.in_qty2 := (case when NEW.in_out_flag = 1 then NEW.qty2 else 0 end);
	v_rec_stock.out_qty1 := (case when NEW.in_out_flag = -1 then NEW.qty1 else 0 end);
	v_rec_stock.out_qty2 := (case when NEW.in_out_flag = -1 then NEW.qty2 else 0 end);


	if v_count = 0 then
		insert into tbl_product_stock
		(
			company_id,
			product_id,
			bin_id,
			in_qty1,
			in_qty2,
			out_qty1,
			out_qty2,
			adj_in_qty1,
			adj_in_qty2,
			adj_out_qty1,
			adj_out_qty2,
			balance_qty1,
			balance_qty2,
			sort_seq,
			is_locked,
			created_by,
			created_dt,
			is_deleted
		)
		values
		(
			NEW.company_id,
			NEW.product_id,
			NEW.bin_id,
			v_rec_stock.in_qty1,
			v_rec_stock.in_qty2,
			v_rec_stock.out_qty1,
			v_rec_stock.out_qty2,
			0,
			0,
			0,
			0,
			(v_rec_stock.in_qty1 - v_rec_stock.out_qty1),
			(v_rec_stock.in_qty2 - v_rec_stock.out_qty2),
			0,
			0,
			NEW.created_by,
			current_timestamp,
			0
		);
	else
	 	update
			tbl_product_stock
    	set
			in_qty1 = (case when is_deleted = 1 then 0 else in_qty1 end) + v_rec_stock.in_qty1,
			in_qty2 = (case when is_deleted = 1 then 0 else in_qty2 end) + v_rec_stock.in_qty2,
			out_qty1 = (case when is_deleted = 1 then 0 else out_qty1 end) + v_rec_stock.out_qty1,
			out_qty2 = (case when is_deleted = 1 then 0 else out_qty2 end) + v_rec_stock.out_qty2,
			balance_qty1 = (case when is_deleted = 1 then 0 else balance_qty1 end) + (v_rec_stock.in_qty1 - v_rec_stock.out_qty1),
			balance_qty2 = (case when is_deleted = 1 then 0 else balance_qty2 end) + (v_rec_stock.in_qty2 - v_rec_stock.out_qty2),
			modified_by = new.modified_by,
			modified_dt = current_timestamp,
			is_deleted = 0
		where
			bin_id = NEW.bin_id
			and product_id = NEW.product_id
			and company_id = NEW.company_id
		;
	end if;


    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_product_stock_trans ON tbl_product_stock_trans;

CREATE  TRIGGER trg_product_stock_trans
  AFTER INSERT or update or delete
  ON tbl_product_stock_trans
  FOR EACH ROW
  EXECUTE PROCEDURE fn_product_stock_trans();
end ;

-- Payment Receipt and Paid Triggers

CREATE OR REPLACE FUNCTION fn_payment_received_det() RETURNS trigger AS $$
declare
	v_count int;
	inv_main 		tbl_payment_received_main%rowtype;
	v_operation_flag 	integer:= 0;
	v_old_input_amt		numeric(20,4) := 0;
	v_payment_status 	varchar(30);
	v_total_invoiced_amount	numeric(20, 4) := 0;
	v_total_received_amount numeric(20, 4) := 0;
	v_now_received_amount	numeric(20, 4) := 0;
	v_old_received_amount	numeric(20, 4) := 0;
BEGIN

	if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_payment_received_main
	where
		payment_id = NEW.payment_id
    ;

	if inv_main.is_deleted = 1 then
		raise exception 'Payment Main is Deleted, Unable to execute !';
	end if;

	if v_operation_flag > 1 then
		v_old_input_amt := OLD.received_amount;
	end if;

	v_now_received_amount := ((case when NEW.is_deleted = 1 then 0 else NEW.received_amount end ) - (v_old_input_amt));

	update tbl_party_account_register
	set
		settled_amount = (settled_amount) + v_now_received_amount,
		modified_by = NEW.modified_by,
		modified_dt = current_timestamp
	where
		inv_type_id = NEW.inv_type || '-' || NEW.inv_id
	;


	if NEW.inv_type = 'RSI' then

		select
			coalesce(max(v.total_invoice_amount),0),
			coalesce(max(v.total_received_amount),0)
		into
			v_total_invoiced_amount,
			v_old_received_amount
		from
			tbl_sales_invoice_main v
		where
			v.inv_type = NEW.inv_type
			and v.inv_id = NEW.inv_id
		;

		select
			case
				when v_old_received_amount + v_now_received_amount = 0 then 'UNPAID'
				when v_total_invoiced_amount - (v_old_received_amount + v_now_received_amount) = 0 then 'PAID'
				else 'PARTPAID'
			end
		into
			v_payment_status
		;


		update tbl_sales_invoice_main
		set
			payment_status = v_payment_status,
			total_received_amount = (v_old_received_amount) + v_now_received_amount
		where
			inv_id = NEW.inv_id
			and inv_type = NEW.inv_type
		;

	end if;

	update tbl_party_account_register
	set
		status = v_payment_status
	where
		inv_type_id = NEW.inv_type || '-' || NEW.inv_id
	;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_payment_received_det ON tbl_payment_received_det;

CREATE trigger trg_payment_received_det
  AFTER INSERT or update or delete
  ON tbl_payment_received_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_payment_received_det();

CREATE OR REPLACE FUNCTION fn_payment_paid_det() RETURNS trigger AS $$
declare
	v_count int;
	inv_main 		tbl_payment_paid_main%rowtype;
	v_operation_flag 	integer:= 0;
	v_old_input_amt		numeric(20,4) := 0;
	v_payment_status 	varchar(30);
	v_total_invoiced_amount	numeric(20, 4) := 0;
	v_total_paid_amount numeric(20, 4) := 0;
	v_now_paid_amount	numeric(20, 4) := 0;
	v_old_paid_amount	numeric(20, 4) := 0;
BEGIN

	if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_payment_paid_main
	where
		payment_id = NEW.payment_id
    ;

	if inv_main.is_deleted = 1 then
		raise exception 'Payment Main is Deleted, Unable to execute !';
	end if;

	if v_operation_flag > 1 then
		v_old_input_amt := OLD.paid_amount;
	end if;

	v_now_paid_amount := ((case when NEW.is_deleted = 1 then 0 else NEW.paid_amount end ) - (v_old_input_amt));

	update tbl_party_account_register
	set
		settled_amount = (settled_amount) + v_now_paid_amount,
		modified_by = NEW.modified_by,
		modified_dt = current_timestamp
	where
		inv_type_id = NEW.inv_type || '-' || NEW.inv_id
	;


	if NEW.inv_type = 'RPI' then
		select
			coalesce(max(v.total_invoice_amount),0),
			coalesce(max(v.total_paid_amount),0)
		into
			v_total_invoiced_amount,
			v_old_paid_amount
		from
			tbl_purchase_invoice_main v
		where
			v.inv_type = NEW.inv_type
			and v.inv_id = NEW.inv_id
		;
		select
			case
				when v_old_paid_amount + v_now_paid_amount = 0 then 'UNPAID'
				when v_total_invoiced_amount - (v_old_paid_amount + v_now_paid_amount) = 0 then 'PAID'
				else 'PARTPAID'
			end
		into
			v_payment_status
		;
		update tbl_purchase_invoice_main
		set
			payment_status = v_payment_status,
			total_paid_amount = (v_old_paid_amount) + v_now_paid_amount
		where
			inv_id = NEW.inv_id
			and inv_type = NEW.inv_type
		;
	end if;

		if NEW.inv_type = 'JPI' then
    		select
    			coalesce(max(v.total_invoice_amount),0),
    			coalesce(max(v.total_paid_amount),0)
    		into
    			v_total_invoiced_amount,
    			v_old_paid_amount
    		from
    			tbl_jew_purchase_invoice_main v
    		where
    			v.inv_type = NEW.inv_type
    			and v.inv_id = NEW.inv_id
    		;
    		select
    			case
    				when v_old_paid_amount + v_now_paid_amount = 0 then 'UNPAID'
    				when v_total_invoiced_amount - (v_old_paid_amount + v_now_paid_amount) = 0 then 'PAID'
    				else 'PARTPAID'
    			end
    		into
    			v_payment_status
    		;
    		update tbl_jew_purchase_invoice_main
    		set
    			payment_status = v_payment_status,
    			total_paid_amount = (v_old_paid_amount) + v_now_paid_amount
    		where
    			inv_id = NEW.inv_id
    			and inv_type = NEW.inv_type
    		;
    	end if;

	update tbl_party_account_register
	set
		status = v_payment_status
	where
		inv_type_id = NEW.inv_type || '-' || NEW.inv_id
	;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_payment_paid_det ON tbl_payment_paid_det;

CREATE trigger trg_payment_paid_det
  AFTER INSERT or update or delete
  ON tbl_payment_paid_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_payment_paid_det();

CREATE OR REPLACE FUNCTION fn_purchase_order_det() RETURNS trigger AS
$$
declare
	v_count int;
	v_operation_flag integer:= 0;
	v_old_qty1 numeric(20,4) := 0;
	v_old_qty2	numeric(20,4) := 0;
	v_old_input_amt	numeric(20,4) := 0;
BEGIN

    if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	if v_operation_flag > 1 then
		v_old_qty1 := OLD.qty1;
		v_old_qty2 := OLD.qty2;
		v_old_input_amt := OLD.pp_amt;
	end if;

		update tbl_purchase_order_main
    	set
    		total_qty1 = (total_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.qty1 end ) - (v_old_qty1)),
    		total_qty2 = (total_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.qty2 end ) - (v_old_qty2)),
    		total_amount = (total_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.pp_amt end ) - (v_old_input_amt))
    	where
    		purchase_order_id = NEW.purchase_order_id
    	;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS trg_purchase_order_det ON tbl_purchase_order_det;

CREATE trigger trg_purchase_order_det
  AFTER INSERT or update or delete
  ON tbl_purchase_order_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_purchase_order_det();

CREATE OR REPLACE FUNCTION fn_sales_order_det() RETURNS trigger AS
$$
declare
  v_count int;
  v_operation_flag integer:= 0;
  v_old_qty1 numeric(20,4) := 0;
  v_old_qty2  numeric(20,4) := 0;
  v_old_input_amt numeric(20,4) := 0;
BEGIN

  if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
    v_operation_flag := 1;
  elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
  else
    return null;
  end if;

  if v_operation_flag > 1 then
    v_old_qty1 := OLD.qty1;
    v_old_qty2 := OLD.qty2;
    v_old_input_amt := OLD.sp_amt;
  end if;

    update tbl_sales_order_main
      set
        total_qty1 = (total_qty1) + ((case when NEW.is_deleted = 1 then 0 else NEW.qty1 end ) - (v_old_qty1)),
        total_qty2 = (total_qty2) + ((case when NEW.is_deleted = 1 then 0 else NEW.qty2 end ) - (v_old_qty2)),
        total_amount = (total_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.sp_amt end ) - (v_old_input_amt))
      where
        sales_order_id = NEW.sales_order_id
      ;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS trg_sales_order_det ON tbl_sales_order_det;

CREATE trigger trg_sales_order_det
  AFTER INSERT or update or delete
  ON tbl_sales_order_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_sales_order_det();

--------Invoice Other Charges Trigger to update Invoice Main
CREATE OR REPLACE FUNCTION fn_purchase_invoice_chgs() RETURNS trigger AS $$
declare
	v_count int;
	inv_main tbl_purchase_invoice_main%rowtype;
	v_operation_flag integer:= 0;
	v_old_input_amt	numeric(20,4) := 0;

	v_new_party_payable_amount numeric(20,4) := 0;
	v_new_charges_plus_amount numeric(20,4) := 0;
	v_new_charges_minus_amount numeric(20,4) := 0;

	v_old_party_payable_amount numeric(20,4) := 0;
	v_old_charges_plus_amount numeric(20,4) := 0;
	v_old_charges_minus_amount numeric(20,4) := 0;

BEGIN

	if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_purchase_invoice_main
	where
		inv_id = NEW.inv_id
    ;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	if v_operation_flag > 1 then
		v_old_input_amt := OLD.input_amt;
		v_old_party_payable_amount := case when OLD.is_supplier_payable = 1 then OLD.plus_minus_flag * OLD.input_amt else 0 end;
		v_old_charges_plus_amount := case when OLD.plus_minus_flag = 1 then OLD.input_amt else 0 end;
		v_old_charges_minus_amount := case when OLD.plus_minus_flag = -1 then OLD.input_amt else 0 end;
	end if;


	v_new_party_payable_amount := case when NEW.is_supplier_payable = 1 then NEW.plus_minus_flag * NEW.input_amt else 0 end;
	v_new_charges_plus_amount := case when NEW.plus_minus_flag = 1 then NEW.input_amt else 0 end;
	v_new_charges_minus_amount := case when NEW.plus_minus_flag = -1 then NEW.input_amt else 0 end;

	update tbl_purchase_invoice_main
	set
		party_payable_amount= (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount)),
		charges_plus_amount= (charges_plus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_plus_amount end ) - (v_old_charges_plus_amount)),
		charges_minus_amount= (charges_minus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_minus_amount end ) - (v_old_charges_minus_amount)),
		total_invoice_amount = (total_invoice_gross_amount) + (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount))
	where
		inv_id = NEW.inv_id
		and inv_type = inv_main.inv_type
	;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_purchase_invoice_chgs ON tbl_purchase_invoice_chgs;

CREATE trigger trg_purchase_invoice_chgs
  AFTER INSERT or update or delete
  ON tbl_purchase_invoice_chgs
  FOR EACH ROW
  EXECUTE PROCEDURE fn_purchase_invoice_chgs();

CREATE OR REPLACE FUNCTION fn_purchase_return_invoice_chgs() RETURNS trigger AS $$
declare
	v_count int;
	inv_main tbl_purchase_return_invoice_main%rowtype;
	v_operation_flag integer:= 0;
	v_old_input_amt	numeric(20,4) := 0;

	v_new_party_payable_amount numeric(20,4) := 0;
	v_new_charges_plus_amount numeric(20,4) := 0;
	v_new_charges_minus_amount numeric(20,4) := 0;

	v_old_party_payable_amount numeric(20,4) := 0;
	v_old_charges_plus_amount numeric(20,4) := 0;
	v_old_charges_minus_amount numeric(20,4) := 0;

BEGIN

	if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_purchase_return_invoice_main
	where
		inv_id = NEW.inv_id
    ;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	if v_operation_flag > 1 then
		v_old_input_amt := OLD.input_amt;
		v_old_party_payable_amount := case when OLD.is_supplier_payable = 1 then OLD.plus_minus_flag * OLD.input_amt else 0 end;
		v_old_charges_plus_amount := case when OLD.plus_minus_flag = 1 then OLD.input_amt else 0 end;
		v_old_charges_minus_amount := case when OLD.plus_minus_flag = -1 then OLD.input_amt else 0 end;
	end if;


	v_new_party_payable_amount := case when NEW.is_supplier_payable = 1 then NEW.plus_minus_flag * NEW.input_amt else 0 end;
	v_new_charges_plus_amount := case when NEW.plus_minus_flag = 1 then NEW.input_amt else 0 end;
	v_new_charges_minus_amount := case when NEW.plus_minus_flag = -1 then NEW.input_amt else 0 end;

	update tbl_purchase_return_invoice_main
	set
		party_payable_amount= (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount)),
		charges_plus_amount= (charges_plus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_plus_amount end ) - (v_old_charges_plus_amount)),
		charges_minus_amount= (charges_minus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_minus_amount end ) - (v_old_charges_minus_amount)),
		total_invoice_amount = (total_invoice_gross_amount) + (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount))
	where
		inv_id = NEW.inv_id
		and inv_type = inv_main.inv_type
	;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_purchase_return_invoice_chgs ON tbl_purchase_return_invoice_chgs;

CREATE trigger trg_purchase_return_invoice_chgs
  AFTER INSERT or update or delete
  ON tbl_purchase_return_invoice_chgs
  FOR EACH ROW
  EXECUTE PROCEDURE fn_purchase_return_invoice_chgs();

CREATE OR REPLACE FUNCTION fn_memo_purchase_invoice_chgs() RETURNS trigger AS $$
declare
	v_count int;
	inv_main tbl_memo_purchase_invoice_main%rowtype;
	v_operation_flag integer:= 0;
	v_old_input_amt	numeric(20,4) := 0;

	v_new_party_payable_amount numeric(20,4) := 0;
	v_new_charges_plus_amount numeric(20,4) := 0;
	v_new_charges_minus_amount numeric(20,4) := 0;

	v_old_party_payable_amount numeric(20,4) := 0;
	v_old_charges_plus_amount numeric(20,4) := 0;
	v_old_charges_minus_amount numeric(20,4) := 0;

BEGIN

	if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_memo_purchase_invoice_main
	where
		inv_id = NEW.inv_id
    ;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	if v_operation_flag > 1 then
		v_old_input_amt := OLD.input_amt;
		v_old_party_payable_amount := case when OLD.is_supplier_payable = 1 then OLD.plus_minus_flag * OLD.input_amt else 0 end;
		v_old_charges_plus_amount := case when OLD.plus_minus_flag = 1 then OLD.input_amt else 0 end;
		v_old_charges_minus_amount := case when OLD.plus_minus_flag = -1 then OLD.input_amt else 0 end;
	end if;


	v_new_party_payable_amount := case when NEW.is_supplier_payable = 1 then NEW.plus_minus_flag * NEW.input_amt else 0 end;
	v_new_charges_plus_amount := case when NEW.plus_minus_flag = 1 then NEW.input_amt else 0 end;
	v_new_charges_minus_amount := case when NEW.plus_minus_flag = -1 then NEW.input_amt else 0 end;

	update tbl_memo_purchase_invoice_main
	set
		party_payable_amount= (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount)),
		charges_plus_amount= (charges_plus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_plus_amount end ) - (v_old_charges_plus_amount)),
		charges_minus_amount= (charges_minus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_minus_amount end ) - (v_old_charges_minus_amount)),
		total_invoice_amount = (total_invoice_gross_amount) + (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount))
	where
		inv_id = NEW.inv_id
		and inv_type = inv_main.inv_type
	;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_memo_purchase_invoice_chgs ON tbl_memo_purchase_invoice_chgs;

CREATE trigger trg_memo_purchase_invoice_chgs
  AFTER INSERT or update or delete
  ON tbl_memo_purchase_invoice_chgs
  FOR EACH ROW
  EXECUTE PROCEDURE fn_memo_purchase_invoice_chgs();

CREATE OR REPLACE FUNCTION fn_memo_purchase_return_invoice_chgs() RETURNS trigger AS $$
declare
	v_count int;
	inv_main tbl_memo_purchase_return_invoice_main%rowtype;
	v_operation_flag integer:= 0;
	v_old_input_amt	numeric(20,4) := 0;

	v_new_party_payable_amount numeric(20,4) := 0;
	v_new_charges_plus_amount numeric(20,4) := 0;
	v_new_charges_minus_amount numeric(20,4) := 0;

	v_old_party_payable_amount numeric(20,4) := 0;
	v_old_charges_plus_amount numeric(20,4) := 0;
	v_old_charges_minus_amount numeric(20,4) := 0;

BEGIN

	if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_memo_purchase_return_invoice_main
	where
		inv_id = NEW.inv_id
    ;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	if v_operation_flag > 1 then
		v_old_input_amt := OLD.input_amt;
		v_old_party_payable_amount := case when OLD.is_supplier_payable = 1 then OLD.plus_minus_flag * OLD.input_amt else 0 end;
		v_old_charges_plus_amount := case when OLD.plus_minus_flag = 1 then OLD.input_amt else 0 end;
		v_old_charges_minus_amount := case when OLD.plus_minus_flag = -1 then OLD.input_amt else 0 end;
	end if;


	v_new_party_payable_amount := case when NEW.is_supplier_payable = 1 then NEW.plus_minus_flag * NEW.input_amt else 0 end;
	v_new_charges_plus_amount := case when NEW.plus_minus_flag = 1 then NEW.input_amt else 0 end;
	v_new_charges_minus_amount := case when NEW.plus_minus_flag = -1 then NEW.input_amt else 0 end;

	update tbl_memo_purchase_return_invoice_main
	set
		party_payable_amount= (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount)),
		charges_plus_amount= (charges_plus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_plus_amount end ) - (v_old_charges_plus_amount)),
		charges_minus_amount= (charges_minus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_minus_amount end ) - (v_old_charges_minus_amount)),
		total_invoice_amount = (total_invoice_gross_amount) + (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount))
	where
		inv_id = NEW.inv_id
		and inv_type = inv_main.inv_type
	;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_memo_purchase_return_invoice_chgs ON tbl_memo_purchase_return_invoice_chgs;

CREATE trigger trg_memo_purchase_return_invoice_chgs
  AFTER INSERT or update or delete
  ON tbl_memo_purchase_return_invoice_chgs
  FOR EACH ROW
  EXECUTE PROCEDURE fn_memo_purchase_return_invoice_chgs();

CREATE OR REPLACE FUNCTION fn_sales_invoice_chgs() RETURNS trigger AS $$
declare
	v_count int;
	inv_main tbl_sales_invoice_main%rowtype;
	v_operation_flag integer:= 0;
	v_old_input_amt	numeric(20,4) := 0;

	v_new_party_payable_amount numeric(20,4) := 0;
	v_new_charges_plus_amount numeric(20,4) := 0;
	v_new_charges_minus_amount numeric(20,4) := 0;

	v_old_party_payable_amount numeric(20,4) := 0;
	v_old_charges_plus_amount numeric(20,4) := 0;
	v_old_charges_minus_amount numeric(20,4) := 0;

BEGIN

	if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_sales_invoice_main
	where
		inv_id = NEW.inv_id
    ;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	if v_operation_flag > 1 then
		v_old_input_amt := OLD.input_amt;
		v_old_party_payable_amount := case when OLD.is_customer_payable = 1 then OLD.plus_minus_flag * OLD.input_amt else 0 end;
		v_old_charges_plus_amount := case when OLD.plus_minus_flag = 1 then OLD.input_amt else 0 end;
		v_old_charges_minus_amount := case when OLD.plus_minus_flag = -1 then OLD.input_amt else 0 end;
	end if;


	v_new_party_payable_amount := case when NEW.is_customer_payable = 1 then NEW.plus_minus_flag * NEW.input_amt else 0 end;
	v_new_charges_plus_amount := case when NEW.plus_minus_flag = 1 then NEW.input_amt else 0 end;
	v_new_charges_minus_amount := case when NEW.plus_minus_flag = -1 then NEW.input_amt else 0 end;

	update tbl_sales_invoice_main
	set
		party_payable_amount= (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount)),
		charges_plus_amount= (charges_plus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_plus_amount end ) - (v_old_charges_plus_amount)),
		charges_minus_amount= (charges_minus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_minus_amount end ) - (v_old_charges_minus_amount)),
		total_invoice_amount = (total_invoice_gross_amount) + (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount))
	where
		inv_id = NEW.inv_id
		and inv_type = inv_main.inv_type
	;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_sales_invoice_chgs ON tbl_sales_invoice_chgs;

CREATE trigger trg_sales_invoice_chgs
  AFTER INSERT or update or delete
  ON tbl_sales_invoice_chgs
  FOR EACH ROW
  EXECUTE PROCEDURE fn_sales_invoice_chgs();

CREATE OR REPLACE FUNCTION fn_sales_return_invoice_chgs() RETURNS trigger AS $$
declare
	v_count int;
	inv_main tbl_sales_return_invoice_main%rowtype;
	v_operation_flag integer:= 0;
	v_old_input_amt	numeric(20,4) := 0;

	v_new_party_payable_amount numeric(20,4) := 0;
	v_new_charges_plus_amount numeric(20,4) := 0;
	v_new_charges_minus_amount numeric(20,4) := 0;

	v_old_party_payable_amount numeric(20,4) := 0;
	v_old_charges_plus_amount numeric(20,4) := 0;
	v_old_charges_minus_amount numeric(20,4) := 0;

BEGIN

	if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_sales_return_invoice_main
	where
		inv_id = NEW.inv_id
    ;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	if v_operation_flag > 1 then
		v_old_input_amt := OLD.input_amt;
		v_old_party_payable_amount := case when OLD.is_customer_payable = 1 then OLD.plus_minus_flag * OLD.input_amt else 0 end;
		v_old_charges_plus_amount := case when OLD.plus_minus_flag = 1 then OLD.input_amt else 0 end;
		v_old_charges_minus_amount := case when OLD.plus_minus_flag = -1 then OLD.input_amt else 0 end;
	end if;


	v_new_party_payable_amount := case when NEW.is_customer_payable = 1 then NEW.plus_minus_flag * NEW.input_amt else 0 end;
	v_new_charges_plus_amount := case when NEW.plus_minus_flag = 1 then NEW.input_amt else 0 end;
	v_new_charges_minus_amount := case when NEW.plus_minus_flag = -1 then NEW.input_amt else 0 end;

	update tbl_sales_return_invoice_main
	set
		party_payable_amount= (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount)),
		charges_plus_amount= (charges_plus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_plus_amount end ) - (v_old_charges_plus_amount)),
		charges_minus_amount= (charges_minus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_minus_amount end ) - (v_old_charges_minus_amount)),
		total_invoice_amount = (total_invoice_gross_amount) + (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount))
	where
		inv_id = NEW.inv_id
		and inv_type = inv_main.inv_type
	;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_sales_return_invoice_chgs ON tbl_sales_return_invoice_chgs;

CREATE trigger trg_sales_return_invoice_chgs
  AFTER INSERT or update or delete
  ON tbl_sales_return_invoice_chgs
  FOR EACH ROW
  EXECUTE PROCEDURE fn_sales_return_invoice_chgs();

CREATE OR REPLACE FUNCTION fn_memo_sales_invoice_chgs() RETURNS trigger AS $$
declare
	v_count int;
	inv_main tbl_memo_sales_invoice_main%rowtype;
	v_operation_flag integer:= 0;
	v_old_input_amt	numeric(20,4) := 0;

	v_new_party_payable_amount numeric(20,4) := 0;
	v_new_charges_plus_amount numeric(20,4) := 0;
	v_new_charges_minus_amount numeric(20,4) := 0;

	v_old_party_payable_amount numeric(20,4) := 0;
	v_old_charges_plus_amount numeric(20,4) := 0;
	v_old_charges_minus_amount numeric(20,4) := 0;

BEGIN

	if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_memo_sales_invoice_main
	where
		inv_id = NEW.inv_id
    ;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	if v_operation_flag > 1 then
		v_old_input_amt := OLD.input_amt;
		v_old_party_payable_amount := case when OLD.is_customer_payable = 1 then OLD.plus_minus_flag * OLD.input_amt else 0 end;
		v_old_charges_plus_amount := case when OLD.plus_minus_flag = 1 then OLD.input_amt else 0 end;
		v_old_charges_minus_amount := case when OLD.plus_minus_flag = -1 then OLD.input_amt else 0 end;
	end if;


	v_new_party_payable_amount := case when NEW.is_customer_payable = 1 then NEW.plus_minus_flag * NEW.input_amt else 0 end;
	v_new_charges_plus_amount := case when NEW.plus_minus_flag = 1 then NEW.input_amt else 0 end;
	v_new_charges_minus_amount := case when NEW.plus_minus_flag = -1 then NEW.input_amt else 0 end;

	update tbl_memo_sales_invoice_main
	set
		party_payable_amount= (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount)),
		charges_plus_amount= (charges_plus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_plus_amount end ) - (v_old_charges_plus_amount)),
		charges_minus_amount= (charges_minus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_minus_amount end ) - (v_old_charges_minus_amount)),
		total_invoice_amount = (total_invoice_gross_amount) + (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount))
	where
		inv_id = NEW.inv_id
		and inv_type = inv_main.inv_type
	;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_memo_sales_invoice_chgs ON tbl_memo_sales_invoice_chgs;

CREATE trigger trg_memo_sales_invoice_chgs
  AFTER INSERT or update or delete
  ON tbl_memo_sales_invoice_chgs
  FOR EACH ROW
  EXECUTE PROCEDURE fn_memo_sales_invoice_chgs();

CREATE OR REPLACE FUNCTION fn_memo_sales_return_invoice_chgs() RETURNS trigger AS $$
declare
	v_count int;
	inv_main tbl_memo_sales_return_invoice_main%rowtype;
	v_operation_flag integer:= 0;
	v_old_input_amt	numeric(20,4) := 0;

	v_new_party_payable_amount numeric(20,4) := 0;
	v_new_charges_plus_amount numeric(20,4) := 0;
	v_new_charges_minus_amount numeric(20,4) := 0;

	v_old_party_payable_amount numeric(20,4) := 0;
	v_old_charges_plus_amount numeric(20,4) := 0;
	v_old_charges_minus_amount numeric(20,4) := 0;

BEGIN

	if TG_OP = 'INSERT'  or (TG_OP = 'UPDATE'  and OLD.is_deleted = 1 and NEW.is_deleted  = 0) then
		v_operation_flag := 1;
	elsif TG_OP = 'DELETE' or (TG_OP = 'UPDATE' and OLD.is_deleted = 0 and NEW.is_deleted = 1) then
        v_operation_flag := 3;
    elsif TG_OP = 'UPDATE' then
        v_operation_flag := 2;
	else
		return null;
	end if;

	select
		*
	into
	 	inv_main
	from
	 	tbl_memo_sales_return_invoice_main
	where
		inv_id = NEW.inv_id
    ;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	if v_operation_flag > 1 then
		v_old_input_amt := OLD.input_amt;
		v_old_party_payable_amount := case when OLD.is_customer_payable = 1 then OLD.plus_minus_flag * OLD.input_amt else 0 end;
		v_old_charges_plus_amount := case when OLD.plus_minus_flag = 1 then OLD.input_amt else 0 end;
		v_old_charges_minus_amount := case when OLD.plus_minus_flag = -1 then OLD.input_amt else 0 end;
	end if;


	v_new_party_payable_amount := case when NEW.is_customer_payable = 1 then NEW.plus_minus_flag * NEW.input_amt else 0 end;
	v_new_charges_plus_amount := case when NEW.plus_minus_flag = 1 then NEW.input_amt else 0 end;
	v_new_charges_minus_amount := case when NEW.plus_minus_flag = -1 then NEW.input_amt else 0 end;

	update tbl_memo_sales_return_invoice_main
	set
		party_payable_amount= (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount)),
		charges_plus_amount= (charges_plus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_plus_amount end ) - (v_old_charges_plus_amount)),
		charges_minus_amount= (charges_minus_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_charges_minus_amount end ) - (v_old_charges_minus_amount)),
		total_invoice_amount = (total_invoice_gross_amount) + (party_payable_amount) + ((case when NEW.is_deleted = 1 then 0 else v_new_party_payable_amount end ) - (v_old_party_payable_amount))
	where
		inv_id = NEW.inv_id
		and inv_type = inv_main.inv_type
	;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_memo_sales_return_invoice_chgs ON tbl_memo_sales_return_invoice_chgs;

CREATE trigger trg_memo_sales_return_invoice_chgs
  AFTER INSERT or update or delete
  ON tbl_memo_sales_return_invoice_chgs
  FOR EACH ROW
  EXECUTE PROCEDURE fn_memo_sales_return_invoice_chgs();

--- Views for the tenants

DROP VIEW IF EXISTS v_party;

create or replace view v_party as
select
	'CUSTOMER' || tcus.customer_id party_type_id,
	'CUSTOMER' party_type,
	tcus.customer_id 	party_id,
	tcus.customer_name 	party_name
from
tbl_customer tcus
union all
select
	'SUPPLIER' || tsup.supplier_id party_type_id,
	'SUPPLIER' party_type,
	tsup.supplier_id 	party_id,
	tsup.supplier_name 	party_name
from
    tbl_supplier tsup
;