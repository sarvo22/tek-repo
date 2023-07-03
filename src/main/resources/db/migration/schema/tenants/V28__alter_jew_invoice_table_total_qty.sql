alter table tbl_jew_purchase_invoice_main add column total_invoice_qty  numeric(20,4) default 0 not null;
alter table tbl_jew_purchase_return_invoice_main add column total_invoice_qty  numeric(20,4) default 0 not null;
alter table tbl_jew_memo_purchase_invoice_main add column total_invoice_qty  numeric(20,4) default 0 not null;
alter table tbl_jew_memo_purchase_return_invoice_main add column total_invoice_qty  numeric(20,4) default 0 not null;
alter table tbl_jew_sales_invoice_main add column total_invoice_qty  numeric(20,4) default 0 not null;
alter table tbl_jew_sales_return_invoice_main add column total_invoice_qty  numeric(20,4) default 0 not null;
alter table tbl_jew_memo_sales_invoice_main add column total_invoice_qty  numeric(20,4) default 0 not null;
alter table tbl_jew_memo_sales_return_invoice_main add column total_invoice_qty  numeric(20,4) default 0 not null;
