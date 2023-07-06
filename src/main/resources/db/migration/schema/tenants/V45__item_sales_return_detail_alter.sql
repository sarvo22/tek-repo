ALTER TABLE tbl_item_sales_invoice_det RENAME COLUMN product_id TO item_id;
ALTER TABLE tbl_item_sales_invoice_det RENAME COLUMN prod_description TO item_description;

ALTER TABLE tbl_item_sales_return_invoice_det RENAME COLUMN product_id TO item_id;
ALTER TABLE tbl_item_sales_return_invoice_det RENAME COLUMN prod_description TO item_description;