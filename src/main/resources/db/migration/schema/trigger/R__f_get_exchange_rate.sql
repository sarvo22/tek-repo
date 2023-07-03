CREATE OR REPLACE FUNCTION f_get_exchange_rate(
	i_currency character varying,
	i_date character varying,
	i_dateformat character varying)
    RETURNS numeric
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
declare

    v_exchange_rate 	numeric(20,4);
    
begin

	select 
	  t.exchange_rate
	INTO
		v_exchange_rate
	from 
		tbl_exchange_rate t 
    where 	
		is_deleted = 0
		and currency = i_currency
		and (exchange_rate_dt) <= to_date(i_date, i_dateformat)
		order by exchange_rate_dt desc limit 1
	;
    return v_exchange_rate;
end;
$BODY$;