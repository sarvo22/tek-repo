CREATE OR REPLACE FUNCTION fn_item_purchase_invoice_det() RETURNS trigger AS $$
declare
	v_count int;
	inv_main tbl_item_purchase_invoice_main%rowtype;
	v_operation_flag integer:= 0;
    v_old_qty numeric(20,4) := 0;
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
	 	tbl_item_purchase_invoice_main
	where
		inv_id = NEW.inv_id
    ;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

    if v_operation_flag > 1 then
		v_old_qty := OLD.inv_qty;
		v_old_input_amt := OLD.input_amt;
	end if;

	select
		count(1)
	into
		v_count
	from
		tbl_item_stock_trans tr
	where
		tr.inv_det_id = NEW.inv_det_id
		and tr.inv_type = inv_main.inv_type
	;

	if v_count = 0 then
		 insert into tbl_item_stock_trans
		 (
				company_id,
				item_id,
				bin_id,
				inv_dt,
				in_out_flag,
				qty,
				adj_qty,
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
            NEW.item_id,
            NEW.bin_id,
            im.inv_dt,
            1	in_out_flag,
            NEW.inv_qty	qty,
            0	adj_qty,
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
            tbl_item_purchase_invoice_main im
        where
            im.inv_id = new.inv_id
     ;
	else

		update tbl_item_stock_trans
		set
			inv_dt = inv_main.inv_dt,
			party_id = inv_main.supplier_id,
			currency_code = inv_main.currency,
			qty = NEW.inv_qty,
			inv_rate = NEW.input_rate,
			cost_price = NEW.cost_price,
			modified_by = NEW.modified_by,
			modified_dt = current_timestamp,
			is_deleted = NEW.is_deleted
		where
			inv_det_id = NEW.inv_det_id
			and inv_type = inv_main.inv_type
		;
	end if;

	update tbl_item_purchase_invoice_main
    	set
    		total_invoice_qty = (total_invoice_qty) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty end ) - (v_old_qty)),
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

DROP TRIGGER IF EXISTS trg_item_purchase_invoice_det ON tbl_item_purchase_invoice_det;

CREATE trigger trg_item_purchase_invoice_det
  AFTER INSERT or update or delete
  ON tbl_item_purchase_invoice_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_item_purchase_invoice_det();

-- Purchase Return
CREATE OR REPLACE FUNCTION fn_item_purchase_return_invoice_det() RETURNS trigger AS
$$
declare
	v_count int;
	inv_main tbl_item_purchase_return_invoice_main%rowtype;
    v_operation_flag integer:= 0;
	v_old_qty numeric(20,4) := 0;
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
	 	tbl_item_purchase_return_invoice_main
	where
		inv_id = NEW.inv_id
	;

	if inv_main.is_deleted = 1 then
		raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
	end if;

	if v_operation_flag > 1 then
		v_old_qty := OLD.inv_qty;
		v_old_input_amt := OLD.input_amt;
	end if;

	select
		count(1)
	into
		v_count
	from
		tbl_item_stock_trans tr
	where
		tr.inv_det_id = NEW.inv_det_id
		and tr.inv_type = inv_main.inv_type
	;

	if v_count = 0 then
		 insert into tbl_item_stock_trans
		 (
				company_id,
				item_id,
				bin_id,
				inv_dt,
				in_out_flag,
				qty,
				adj_qty,
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
            NEW.item_id,
            NEW.bin_id,
            im.inv_dt,
            -1	in_out_flag,
            NEW.inv_qty	qty,
            0	adj_qty,
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
				tbl_item_purchase_return_invoice_main im
			where
				im.inv_id = new.inv_id
		 ;
	else

		update tbl_item_stock_trans
		set
			inv_dt = inv_main.inv_dt,
			party_id = inv_main.supplier_id,
			currency_code = inv_main.currency,
			qty = NEW.inv_qty,
			inv_rate = NEW.input_rate,
			cost_price = NEW.cost_price,
			modified_by = NEW.modified_by,
			modified_dt = current_timestamp,
			is_deleted = NEW.is_deleted
		where
			inv_det_id = NEW.inv_det_id
			and inv_type = inv_main.inv_type
		;
	end if;

		update tbl_item_purchase_return_invoice_main
    	set
    		total_invoice_qty = (total_invoice_qty) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty end ) - (v_old_qty)),
    		total_invoice_gross_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)),
    		total_invoice_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)) +
            							   (party_payable_amount)
    	where
    		inv_id = NEW.inv_id
    		and inv_type = inv_main.inv_type
    	;

    	update
    		tbl_item_purchase_invoice_det
    	set
    		ret_qty = (ret_qty) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty end ) - (v_old_qty)),
    		ret_input_rate = NEW.input_rate,
    		ret_input_amt = (ret_input_amt) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt))
    	where
    		inv_det_id = NEW.ref_inv_det_id
    	;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS trg_item_purchase_return_invoice_det ON tbl_item_purchase_return_invoice_det;

CREATE trigger trg_item_purchase_return_invoice_det
  AFTER INSERT or update or delete
  ON tbl_item_purchase_return_invoice_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_item_purchase_return_invoice_det();

CREATE OR REPLACE FUNCTION fn_item_sales_invoice_det() RETURNS trigger AS
$$
declare
  v_count int;
  inv_main tbl_item_sales_invoice_main%rowtype;
  v_operation_flag integer:= 0;
  v_old_qty numeric(20,4) := 0;
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


  select
    *
  into
    inv_main
  from
    tbl_item_sales_invoice_main
  where
    inv_id = NEW.inv_id
  ;

  if inv_main.is_deleted = 1 then
    raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
  end if;

  if v_operation_flag > 1 then
    v_old_qty := OLD.inv_qty;
    v_old_input_amt := OLD.input_amt;
  end if;


  select
    count(1)
  into
    v_count
  from
    tbl_item_stock_trans tr
  where
    tr.inv_det_id = NEW.inv_det_id
    and tr.inv_type = inv_main.inv_type
  ;

  if v_count = 0 then
     insert into tbl_item_stock_trans
     (

        company_id,
        item_id,
        bin_id,
        inv_dt,
        in_out_flag,
        qty,
        adj_qty,
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
        NEW.item_id,
        NEW.bin_id,
        im.inv_dt,
        -1  in_out_flag,
        NEW.inv_qty qty,
        0 dj_qty,
        'CUSTOMER' party_type,
        im.customer_id  party_id,
        im.currency currency_code,
        NEW.input_rate inv_rate,
        NEW.cost_price,
        im.inv_type,
        NEW.inv_det_id,
        im.inv_id,
        null  remarks,
        0 sort_seq,
        0 is_locked,
        NEW.created_by,
        current_timestamp created_dt,
        NEW.is_deleted,
        im.exchange_rate,
        im.inv_no
      from
        tbl_item_sales_invoice_main im
      where
        im.inv_id = new.inv_id
     ;
  else

    update tbl_item_stock_trans
    set
      inv_dt = inv_main.inv_dt,
      party_id = inv_main.customer_id,
      currency_code = inv_main.currency,
      qty = NEW.inv_qty,
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

      update tbl_item_sales_invoice_main
      set
        total_invoice_qty = (total_invoice_qty) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty end ) - (v_old_qty)),
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


DROP TRIGGER IF EXISTS trg_item_sales_invoice_det ON tbl_item_sales_invoice_det;

CREATE trigger trg_item_sales_invoice_det
  AFTER INSERT or update or delete
  ON tbl_item_sales_invoice_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_item_sales_invoice_det();

CREATE OR REPLACE FUNCTION fn_item_sales_return_invoice_det() RETURNS trigger AS
$$
declare
  v_count int;
  inv_main  tbl_item_sales_return_invoice_main%rowtype;
  v_operation_flag integer:= 0;
  v_old_qty numeric(20,4) := 0;
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

  select
    *
  into
    inv_main
  from
    tbl_item_sales_return_invoice_main
  where
    inv_id = NEW.inv_id
  ;

  if inv_main.is_deleted = 1 then
    raise exception 'Invoice Main is Deleted, Unable to execute Stock Impact !';
  end if;

  if v_operation_flag > 1 then
    v_old_qty := OLD.inv_qty;
    v_old_input_amt := OLD.input_amt;
  end if;

  select
    count(1)
  into
    v_count
  from
    tbl_item_stock_trans tr
  where
    tr.inv_det_id = NEW.inv_det_id
    and tr.inv_type = inv_main.inv_type
  ;

  if v_count = 0 then
     insert into tbl_item_stock_trans
     (

        company_id,
        item_id,
        bin_id,
        inv_dt,
        in_out_flag,
        qty,
        adj_qty,
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
        NEW.item_id,
        NEW.bin_id,
        im.inv_dt,
        -1  in_out_flag,
        NEW.inv_qty qty,
        0 dj_qty,
        'CUSTOMER' party_type,
        im.customer_id  party_id,
        im.currency currency_code,
        NEW.input_rate inv_rate,
        NEW.cost_price,
        im.inv_type,
        NEW.inv_det_id,
        im.inv_id,
        null  remarks,
        0 sort_seq,
        0 is_locked,
        NEW.created_by,
        current_timestamp created_dt,
        NEW.is_deleted,
        im.exchange_rate,
        im.inv_no
      from
        tbl_item_sales_return_invoice_main im
      where
        im.inv_id = new.inv_id
     ;
  else

    update tbl_item_stock_trans
    set
      inv_dt = inv_main.inv_dt,
      party_id = inv_main.customer_id,
      currency_code = inv_main.currency,
      qty = NEW.inv_qty,
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

       update tbl_item_sales_return_invoice_main
      set
        total_invoice_qty = (total_invoice_qty) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty end ) - (v_old_qty)),
        total_invoice_gross_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)),
            total_invoice_amount = (total_invoice_gross_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt)) +
                             (party_payable_amount)
      where
        inv_id = NEW.inv_id
        and inv_type = inv_main.inv_type
      ;

      update
        tbl_item_sales_invoice_det
      set
        ret_qty = (ret_qty) + ((case when NEW.is_deleted = 1 then 0 else NEW.inv_qty end ) - (v_old_qty)),
        ret_input_rate = NEW.input_rate,
        ret_input_amt = (ret_input_amt) + ((case when NEW.is_deleted = 1 then 0 else NEW.input_amt end ) - (v_old_input_amt))
      where
        inv_det_id = NEW.ref_inv_det_id
      ;


    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS trg_item_sales_return_invoice_det ON tbl_item_sales_return_invoice_det;

CREATE trigger trg_item_sales_return_invoice_det
  AFTER INSERT or update or delete
  ON tbl_item_sales_return_invoice_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_item_sales_return_invoice_det();

CREATE OR REPLACE FUNCTION fn_item_stock_trans()
  RETURNS trigger AS
$$
declare
	v_operation_flag integer:= 0;
	v_count integer := 0;
	v_rec_stock tbl_item_stock%rowtype;
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

		v_rec_stock.in_qty := (case when OLD.in_out_flag = 1 then OLD.qty else 0 end);
		v_rec_stock.out_qty := (case when OLD.in_out_flag = -1 then OLD.qty else 0 end);

		update
			tbl_item_stock
		set
			in_qty = in_qty - v_rec_stock.in_qty,
			out_qty = out_qty - v_rec_stock.out_qty,
			balance_qty = balance_qty - (v_rec_stock.in_qty - v_rec_stock.out_qty),
			modified_by = OLD.modified_by,
			modified_dt = current_timestamp
		where
			is_deleted = 0
			and item_id = OLD.item_id
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
		tbl_item_stock stk
	where
		stk.bin_id = NEW.bin_id
		and stk.item_id = NEW.item_id
		and stk.company_id = NEW.company_id
		and stk.is_deleted = 0
	;

	v_rec_stock.in_qty := (case when NEW.in_out_flag = 1 then NEW.qty else 0 end);
	v_rec_stock.out_qty := (case when NEW.in_out_flag = -1 then NEW.qty else 0 end);


	if v_count = 0 then
		insert into tbl_item_stock
		(
			company_id,
			item_id,
			bin_id,
			in_qty,
			out_qty,
			adj_in_qty,
			adj_out_qty,
			balance_qty,
			sort_seq,
			is_locked,
			created_by,
			created_dt,
			is_deleted
		)
		values
		(
			NEW.company_id,
			NEW.item_id,
			NEW.bin_id,
			v_rec_stock.in_qty,
			v_rec_stock.out_qty,
			0,
			0,
			(v_rec_stock.in_qty - v_rec_stock.out_qty),
			0,
			0,
			NEW.created_by,
			current_timestamp,
			0
		);
	else
	 	update
			tbl_item_stock
    	set
			in_qty = (case when is_deleted = 1 then 0 else in_qty end) + v_rec_stock.in_qty,
			out_qty = (case when is_deleted = 1 then 0 else out_qty end) + v_rec_stock.out_qty,
			balance_qty = (case when is_deleted = 1 then 0 else balance_qty end) + (v_rec_stock.in_qty - v_rec_stock.out_qty),
			modified_by = new.modified_by,
			modified_dt = current_timestamp,
			is_deleted = 0
		where
			bin_id = NEW.bin_id
			and item_id = NEW.item_id
			and company_id = NEW.company_id
		;
	end if;


    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_item_stock_trans ON tbl_item_stock_trans;

CREATE  TRIGGER trg_item_stock_trans
  AFTER INSERT or update or delete
  ON tbl_item_stock_trans
  FOR EACH ROW
  EXECUTE PROCEDURE fn_item_stock_trans();
end ;

--------Invoice Other Charges Trigger to update Invoice Main
CREATE OR REPLACE FUNCTION fn_item_purchase_invoice_chgs() RETURNS trigger AS $$
declare
	v_count int;
	inv_main tbl_item_purchase_invoice_main%rowtype;
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
	 	tbl_item_purchase_invoice_main
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

	update tbl_item_purchase_invoice_main
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

DROP TRIGGER IF EXISTS trg_item_purchase_invoice_chgs ON tbl_item_purchase_invoice_chgs;

CREATE trigger trg_item_purchase_invoice_chgs
  AFTER INSERT or update or delete
  ON tbl_item_purchase_invoice_chgs
  FOR EACH ROW
  EXECUTE PROCEDURE fn_item_purchase_invoice_chgs();

CREATE OR REPLACE FUNCTION fn_item_purchase_return_invoice_chgs() RETURNS trigger AS $$
declare
  v_count int;
  inv_main tbl_item_purchase_return_invoice_main%rowtype;
  v_operation_flag integer:= 0;
  v_old_input_amt numeric(20,4) := 0;
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
    tbl_item_purchase_return_invoice_main
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

  update tbl_item_purchase_return_invoice_main
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

DROP TRIGGER IF EXISTS trg_item_purchase_return_invoice_chgs ON tbl_item_purchase_return_invoice_chgs;

CREATE trigger trg_item_purchase_return_invoice_chgs
  AFTER INSERT or update or delete
  ON tbl_item_purchase_return_invoice_chgs
  FOR EACH ROW
  EXECUTE PROCEDURE fn_item_purchase_return_invoice_chgs();

CREATE OR REPLACE FUNCTION fn_item_sales_invoice_chgs() RETURNS trigger AS $$
declare
  v_count int;
  inv_main tbl_item_sales_invoice_main%rowtype;
  v_operation_flag integer:= 0;
  v_old_input_amt numeric(20,4) := 0;
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
    tbl_item_sales_invoice_main
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

  update tbl_item_sales_invoice_main
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

DROP TRIGGER IF EXISTS trg_item_sales_invoice_chgs ON tbl_item_sales_invoice_chgs;

CREATE trigger trg_item_sales_invoice_chgs
  AFTER INSERT or update or delete
  ON tbl_item_sales_invoice_chgs
  FOR EACH ROW
  EXECUTE PROCEDURE fn_item_sales_invoice_chgs();

CREATE OR REPLACE FUNCTION fn_item_sales_return_invoice_chgs() RETURNS trigger AS $$
declare
  v_count int;
  inv_main tbl_item_sales_return_invoice_main%rowtype;
  v_operation_flag integer:= 0;
  v_old_input_amt numeric(20,4) := 0;
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
    tbl_item_sales_return_invoice_main
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

  update tbl_item_sales_return_invoice_main
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

DROP TRIGGER IF EXISTS trg_item_sales_return_invoice_chgs ON tbl_item_sales_return_invoice_chgs;

CREATE trigger trg_item_sales_return_invoice_chgs
  AFTER INSERT or update or delete
  ON tbl_item_sales_return_invoice_chgs
  FOR EACH ROW
  EXECUTE PROCEDURE fn_item_sales_return_invoice_chgs();