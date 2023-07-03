alter table tbl_jew_sales_return_invoice_det drop column uom2;
ALTER TABLE tbl_jew_sales_return_invoice_det RENAME COLUMN uom1 TO uom;
ALTER TABLE tbl_jew_sales_return_invoice_det ALTER COLUMN uom SET NOT NULL;