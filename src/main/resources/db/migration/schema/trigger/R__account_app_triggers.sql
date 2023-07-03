-- Accounts module triggers
CREATE OR REPLACE FUNCTION fn_bank_payment_receipt_det() RETURNS trigger AS
$$
declare
	v_count int;
	inv_main 				tbl_bank_pay_rec_main%rowtype;
    v_operation_flag 		integer:= 0;
	v_old_net_amount 	numeric(20,4) := 0;
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
		v_old_net_amount := OLD.net_amount;
	end if;


	select
		*
	into
	 	inv_main
	from
	 	tbl_bank_pay_rec_main
	where
		inv_id = NEW.inv_id
	;

	if inv_main.is_deleted = 1 then
		raise exception 'Main is Deleted, Unable to execute Total Calculation !';
	end if;

	update tbl_bank_pay_rec_main
	set
		total_debit_amount = (total_debit_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.net_amount end ) - (v_old_net_amount)),
		total_credit_amount = (total_credit_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.net_amount end ) - (v_old_net_amount))
	where
		inv_id = NEW.inv_id
	;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_bank_payment_receipt_det ON tbl_bank_pay_rec_det;

CREATE trigger trg_bank_payment_receipt_det
  AFTER INSERT or update or delete
  ON tbl_bank_pay_rec_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_bank_payment_receipt_det();

-- Cash Payment and Receipt
CREATE OR REPLACE FUNCTION fn_cash_payment_receipt_det() RETURNS trigger AS
$$
declare
	v_count int;
	inv_main 				tbl_cash_pay_rec_main%rowtype;
    v_operation_flag 		integer:= 0;
	v_old_net_amount 	numeric(20,4) := 0;
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
		v_old_net_amount := OLD.net_amount;
	end if;


	select
		*
	into
	 	inv_main
	from
	 	tbl_cash_pay_rec_main
	where
		inv_id = NEW.inv_id
	;

	if inv_main.is_deleted = 1 then
		raise exception 'Main is Deleted, Unable to execute Total Calculation !';
	end if;

	update tbl_cash_pay_rec_main
	set
		total_debit_amount = (total_debit_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.net_amount end ) - (v_old_net_amount)),
		total_credit_amount = (total_credit_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.net_amount end ) - (v_old_net_amount))
	where
		inv_id = NEW.inv_id
	;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_cash_payment_receipt_det ON tbl_cash_pay_rec_det;

CREATE trigger trg_cash_payment_receipt_det
  AFTER INSERT or update or delete
  ON tbl_cash_pay_rec_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_cash_payment_receipt_det();

-- Debit and Credit Note
CREATE OR REPLACE FUNCTION fn_debit_credit_note_det() RETURNS trigger AS
$$
declare
  v_count int;
  inv_main        tbl_debit_credit_note_main%rowtype;
    v_operation_flag    integer:= 0;
  v_old_net_amount  numeric(20,4) := 0;
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
    v_old_net_amount := OLD.net_amount;
  end if;


  select
    *
  into
    inv_main
  from
    tbl_debit_credit_note_main
  where
    inv_id = NEW.inv_id
  ;

  if inv_main.is_deleted = 1 then
    raise exception 'Main is Deleted, Unable to execute Total Calculation !';
  end if;

  update tbl_debit_credit_note_main
  set
    total_debit_amount = (total_debit_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.net_amount end ) - (v_old_net_amount)),
    total_credit_amount = (total_credit_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.net_amount end ) - (v_old_net_amount))
  where
    inv_id = NEW.inv_id
  ;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_debit_credit_note_det ON tbl_debit_credit_note_det;

CREATE trigger trg_debit_credit_note_det
  AFTER INSERT or update or delete
  ON tbl_debit_credit_note_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_debit_credit_note_det();

-- JV
CREATE OR REPLACE FUNCTION fn_jv_det() RETURNS trigger AS
$$
declare
  v_count int;
  inv_main        tbl_jv_main%rowtype;
  v_operation_flag    integer:= 0;
  v_old_debit_amount  numeric(20,4) := 0;
  v_old_credit_amount  numeric(20,4) := 0;
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
    v_old_debit_amount := OLD.debit_amount;
    v_old_credit_amount := OLD.credit_amount;
  end if;


  select
    *
  into
    inv_main
  from
    tbl_jv_main
  where
    jv_main_id = NEW.jv_det_id
  ;

  if inv_main.is_deleted = 1 then
    raise exception 'Main is Deleted, Unable to execute Total Calculation !';
  end if;

  update tbl_jv_main
  set
    total_debit_amount = (total_debit_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.debit_amount end ) - (v_old_debit_amount)),
    total_credit_amount = (total_credit_amount) + ((case when NEW.is_deleted = 1 then 0 else NEW.credit_amount end ) - (v_old_credit_amount))
  where
    jv_main_id = NEW.jv_main_id
  ;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_jv_det ON tbl_jv_det;

CREATE trigger trg_jv_det
  AFTER INSERT or update or delete
  ON tbl_jv_det
  FOR EACH ROW
  EXECUTE PROCEDURE fn_jv_det();