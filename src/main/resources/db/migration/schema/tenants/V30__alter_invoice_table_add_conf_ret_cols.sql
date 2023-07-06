alter table tbl_jew_purchase_invoice_det add column	conf_qty numeric(20,4) default 0 not null;
alter table tbl_jew_purchase_invoice_det add column	ret_qty	numeric(20,4) default 0 not null;
alter table tbl_jew_purchase_invoice_det add column	conf_input_rate	numeric(20,4) default 0 not null;
alter table tbl_jew_purchase_invoice_det add column	conf_input_amt	numeric(20,4) default 0 not null;
alter table tbl_jew_purchase_invoice_det add column	ret_input_rate	numeric(20,4) default 0 not null;
alter table tbl_jew_purchase_invoice_det add column	ret_input_amt	numeric(20,4) default 0 not null;

alter table tbl_jew_purchase_return_invoice_det add column	conf_qty numeric(20,4) default 0 not null;
alter table tbl_jew_purchase_return_invoice_det add column	ret_qty	numeric(20,4) default 0 not null;
alter table tbl_jew_purchase_return_invoice_det add column	conf_input_rate	numeric(20,4) default 0 not null;
alter table tbl_jew_purchase_return_invoice_det add column	conf_input_amt	numeric(20,4) default 0 not null;
alter table tbl_jew_purchase_return_invoice_det add column	ret_input_rate	numeric(20,4) default 0 not null;
alter table tbl_jew_purchase_return_invoice_det add column	ret_input_amt	numeric(20,4) default 0 not null;

alter table tbl_jew_memo_purchase_invoice_det add column	conf_qty numeric(20,4) default 0 not null;
alter table tbl_jew_memo_purchase_invoice_det add column	ret_qty	numeric(20,4) default 0 not null;
alter table tbl_jew_memo_purchase_invoice_det add column	conf_input_rate	numeric(20,4) default 0 not null;
alter table tbl_jew_memo_purchase_invoice_det add column	conf_input_amt	numeric(20,4) default 0 not null;
alter table tbl_jew_memo_purchase_invoice_det add column	ret_input_rate	numeric(20,4) default 0 not null;
alter table tbl_jew_memo_purchase_invoice_det add column	ret_input_amt	numeric(20,4) default 0 not null;

alter table tbl_jew_memo_purchase_return_invoice_det add column	conf_qty numeric(20,4) default 0 not null;
alter table tbl_jew_memo_purchase_return_invoice_det add column	ret_qty	numeric(20,4) default 0 not null;
alter table tbl_jew_memo_purchase_return_invoice_det add column	conf_input_rate	numeric(20,4) default 0 not null;
alter table tbl_jew_memo_purchase_return_invoice_det add column	conf_input_amt	numeric(20,4) default 0 not null;
alter table tbl_jew_memo_purchase_return_invoice_det add column	ret_input_rate	numeric(20,4) default 0 not null;
alter table tbl_jew_memo_purchase_return_invoice_det add column	ret_input_amt	numeric(20,4) default 0 not null;


alter table tbl_jew_sales_invoice_det add column	conf_qty numeric(20,4) default 0 not null;
alter table tbl_jew_sales_invoice_det add column	ret_qty	numeric(20,4) default 0 not null;
alter table tbl_jew_sales_invoice_det add column	conf_input_rate	numeric(20,4) default 0 not null;
alter table tbl_jew_sales_invoice_det add column	conf_input_amt	numeric(20,4) default 0 not null;
alter table tbl_jew_sales_invoice_det add column	ret_input_rate	numeric(20,4) default 0 not null;
alter table tbl_jew_sales_invoice_det add column	ret_input_amt	numeric(20,4) default 0 not null;

alter table tbl_jew_sales_return_invoice_det add column	conf_qty numeric(20,4) default 0 not null;
alter table tbl_jew_sales_return_invoice_det add column	ret_qty	numeric(20,4) default 0 not null;
alter table tbl_jew_sales_return_invoice_det add column	conf_input_rate	numeric(20,4) default 0 not null;
alter table tbl_jew_sales_return_invoice_det add column	conf_input_amt	numeric(20,4) default 0 not null;
alter table tbl_jew_sales_return_invoice_det add column	ret_input_rate	numeric(20,4) default 0 not null;
alter table tbl_jew_sales_return_invoice_det add column	ret_input_amt	numeric(20,4) default 0 not null;

-- Reference Columns
alter table tbl_jew_purchase_invoice_det add column	ref_inv_type varchar(30);
alter table tbl_jew_purchase_invoice_det add column	ref_inv_id integer;
alter table tbl_jew_purchase_invoice_det add column	ref_inv_det_id integer;

alter table tbl_jew_purchase_return_invoice_det add column	ref_inv_type varchar(30);
alter table tbl_jew_purchase_return_invoice_det add column	ref_inv_id integer;
alter table tbl_jew_purchase_return_invoice_det add column	ref_inv_det_id integer;

alter table tbl_jew_memo_purchase_return_invoice_det add column	ref_inv_type varchar(30);
alter table tbl_jew_memo_purchase_return_invoice_det add column	ref_inv_id integer;
alter table tbl_jew_memo_purchase_return_invoice_det add column	ref_inv_det_id integer;

alter table tbl_jew_sales_invoice_det add column	ref_inv_type varchar(30);
alter table tbl_jew_sales_invoice_det add column	ref_inv_id integer;
alter table tbl_jew_sales_invoice_det add column	ref_inv_det_id integer;

alter table tbl_jew_sales_return_invoice_det add column	ref_inv_det_id integer;

alter table tbl_jew_memo_sales_invoice_det add column	ref_inv_type varchar(30);
alter table tbl_jew_memo_sales_invoice_det add column	ref_inv_id integer;
alter table tbl_jew_memo_sales_invoice_det add column	ref_inv_det_id integer;

alter table tbl_jew_memo_sales_return_invoice_det add column	ref_inv_det_id integer;


alter table tbl_jew_sales_return_invoice_det drop column inv_qty2;
ALTER TABLE tbl_jew_sales_return_invoice_det RENAME COLUMN inv_qty1 TO inv_qty;
ALTER TABLE tbl_jew_sales_return_invoice_det ALTER COLUMN inv_qty SET NOT NULL;
ALTER TABLE tbl_jew_sales_return_invoice_det ALTER COLUMN inv_qty SET  default 0;
