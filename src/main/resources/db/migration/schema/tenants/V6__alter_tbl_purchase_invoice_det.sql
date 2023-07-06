update tbl_purchase_invoice_det set discount_type = 'PCT' where discount_type is null;
update tbl_purchase_invoice_det set discount_value = 0 where discount_value is null;

ALTER TABLE tbl_purchase_invoice_det ALTER COLUMN discount_type SET NOT NULL;
ALTER TABLE tbl_purchase_invoice_det ALTER COLUMN discount_value SET NOT NULL;

ALTER TABLE tbl_purchase_invoice_det add COLUMN ocp_rate numeric(20,4) default 0 not null;
ALTER TABLE tbl_purchase_invoice_det add COLUMN commission_type   varchar(50) default 'PCT' not null;
ALTER TABLE tbl_purchase_invoice_det add COLUMN commission_value    numeric(20,4) default 0 not null;