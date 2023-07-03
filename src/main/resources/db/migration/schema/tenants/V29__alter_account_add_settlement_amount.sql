alter table tbl_jv_main add settled_amount numeric(20,4) default 0 not null;
alter table tbl_bank_pay_rec_main add settled_amount numeric(20,4) default 0 not null;
alter table tbl_cash_pay_rec_main add settled_amount numeric(20,4) default 0 not null;
alter table tbl_debit_credit_note_main add settled_amount numeric(20,4) default 0 not null;