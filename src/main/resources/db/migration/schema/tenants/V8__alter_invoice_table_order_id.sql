alter table tbl_purchase_invoice_main add column order_id integer;
alter table tbl_purchase_return_invoice_main add column order_id integer;
alter table tbl_memo_purchase_invoice_main add column order_id integer;
alter table tbl_memo_purchase_return_invoice_main add column order_id integer;
alter table tbl_sales_invoice_main add column order_id integer;
alter table tbl_sales_return_invoice_main add column order_id integer;
alter table tbl_memo_sales_invoice_main add column order_id integer;
alter table tbl_memo_sales_return_invoice_main add column order_id integer;