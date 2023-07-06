ALTER TABLE tbl_product_stock_trans ALTER COLUMN company_id SET NOT NULL;
ALTER TABLE tbl_product_stock_trans ALTER COLUMN product_id SET NOT NULL;
ALTER TABLE tbl_product_stock_trans ALTER COLUMN bin_id SET NOT NULL;
ALTER TABLE tbl_product_stock_trans ALTER COLUMN inv_dt SET NOT NULL;
ALTER TABLE tbl_product_stock_trans ALTER COLUMN in_out_flag SET NOT NULL;
ALTER TABLE tbl_product_stock_trans add COLUMN exchange_rate  numeric(20,4) default 0 not null;
ALTER TABLE tbl_product_stock_trans ADD CONSTRAINT trans_company_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_product_stock_trans ADD CONSTRAINT trans_product_fk1 FOREIGN KEY (product_id) REFERENCES tbl_product(product_id);
ALTER TABLE tbl_product_stock_trans ADD CONSTRAINT trans_bin_fk1 FOREIGN KEY (bin_id) REFERENCES tbl_bin(bin_id);
---Product Stock changes
ALTER TABLE tbl_product_stock ALTER COLUMN company_id SET NOT NULL;
ALTER TABLE tbl_product_stock ALTER COLUMN product_id SET NOT NULL;
ALTER TABLE tbl_product_stock ALTER COLUMN bin_id SET NOT NULL;
ALTER TABLE tbl_product_stock ADD CONSTRAINT stock_company_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_product_stock ADD CONSTRAINT stock_product_fk1 FOREIGN KEY (product_id) REFERENCES tbl_product(product_id);
ALTER TABLE tbl_product_stock ADD CONSTRAINT stock_bin_fk1 FOREIGN KEY (bin_id) REFERENCES tbl_bin(bin_id);