alter table tbl_item_sales_invoice_det add column ref_inv_type 	varchar(30);
alter table tbl_item_sales_invoice_det  add column ref_inv_id  	integer;
alter table tbl_item_sales_invoice_det add column ref_inv_det_id  integer;

alter table tbl_item_sales_return_invoice_det add column ref_inv_type 	varchar(30);
alter table tbl_item_sales_return_invoice_det  add column ref_inv_id  	integer;
alter table tbl_item_sales_return_invoice_det add column ref_inv_det_id  integer;

