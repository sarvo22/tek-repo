ALTER TABLE tbl_company_currency ALTER COLUMN company_id SET NOT NULL;
ALTER TABLE tbl_company_currency ALTER COLUMN currency_code SET NOT NULL;
ALTER TABLE tbl_company_currency ADD CONSTRAINT tbl_company_currency_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_company_currency ADD CONSTRAINT tbl_company_currency_fk2 FOREIGN KEY (currency_code) REFERENCES public.tbl_currency(currency_code);

ALTER TABLE tbl_department ADD CONSTRAINT tbl_department_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_employee ADD CONSTRAINT tbl_employee_fk2 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);

ALTER TABLE tbl_payment_type ALTER COLUMN due_months SET NOT NULL;
ALTER TABLE tbl_payment_type ALTER COLUMN due_days SET NOT NULL;
ALTER TABLE tbl_payment_type ADD CONSTRAINT tbl_payment_type_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);

ALTER TABLE tbl_supplier ADD CONSTRAINT tbl_supplier_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_supplier ADD CONSTRAINT tbl_supplier_fk2 FOREIGN KEY (payment_type_id) REFERENCES tbl_payment_type(payment_type_id);
ALTER TABLE tbl_supplier ADD CONSTRAINT tbl_supplier_fk3 FOREIGN KEY (head_buyer_id) REFERENCES tbl_employee(employee_id);
ALTER TABLE tbl_supplier ADD CONSTRAINT tbl_supplier_fk4 FOREIGN KEY (buyer_id) REFERENCES tbl_employee(employee_id);

ALTER TABLE tbl_supplier_address ADD CONSTRAINT tbl_supplier_address_fk1 FOREIGN KEY (supplier_id) REFERENCES tbl_supplier(supplier_id);
ALTER TABLE tbl_supplier_contact ADD CONSTRAINT tbl_supplier_contact_fk2 FOREIGN KEY (supplier_id) REFERENCES tbl_supplier(supplier_id);
ALTER TABLE tbl_supplier_document ADD CONSTRAINT tbl_supplier_document_fk3 FOREIGN KEY (supplier_id) REFERENCES tbl_supplier(supplier_id);
ALTER TABLE tbl_supplier_limit ADD CONSTRAINT tbl_supplier_limit_fk4 FOREIGN KEY (supplier_id) REFERENCES tbl_supplier(supplier_id);

ALTER TABLE tbl_factory ADD CONSTRAINT tbl_factory_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_factory_address ADD CONSTRAINT tbl_factory_address_fk2 FOREIGN KEY (factory_id) REFERENCES tbl_factory(factory_id);
ALTER TABLE tbl_factory_contact ADD CONSTRAINT tbl_factory_contact_fk3 FOREIGN KEY (factory_id) REFERENCES tbl_factory(factory_id);
ALTER TABLE tbl_factory_document ADD CONSTRAINT tbl_factory_document_fk4 FOREIGN KEY (factory_id) REFERENCES tbl_factory(factory_id);
ALTER TABLE tbl_factory_limit ADD CONSTRAINT tbl_factory_limit_fk5 FOREIGN KEY (factory_id) REFERENCES tbl_factory(factory_id);

update tbl_exchange_rate set exchange_rate = 0 where exchange_rate is null;
ALTER TABLE tbl_exchange_rate ALTER COLUMN currency SET NOT NULL;
ALTER TABLE tbl_exchange_rate ALTER COLUMN exchange_rate SET NOT NULL;
ALTER TABLE tbl_exchange_rate ADD CONSTRAINT tbl_exchange_rate_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);

update tbl_metal_rate set exchange_rate = 0 where exchange_rate is null;
update tbl_metal_rate set input_rate = 0 where input_rate is null;
update tbl_metal_rate set uom = 'OZ' where uom is null;

ALTER TABLE tbl_metal_rate ALTER COLUMN commodity_id SET NOT NULL;
ALTER TABLE tbl_metal_rate ALTER COLUMN uom SET NOT NULL;
ALTER TABLE tbl_metal_rate ALTER COLUMN currency SET NOT NULL;
ALTER TABLE tbl_metal_rate ALTER COLUMN exchange_rate SET NOT NULL;
ALTER TABLE tbl_metal_rate ALTER COLUMN input_rate SET NOT NULL;

ALTER TABLE tbl_metal_rate ADD CONSTRAINT tbl_metal_rate_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_metal_rate ADD CONSTRAINT tbl_metal_rate_fk2 FOREIGN KEY (commodity_id) REFERENCES tbl_commodity(commodity_id);

ALTER TABLE tbl_commodity_group ADD CONSTRAINT tbl_commodity_group_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_commodity ADD CONSTRAINT tbl_commodity_company_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_cut ADD CONSTRAINT tbl_cut_company_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_color ADD CONSTRAINT tbl_color_company_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_shape ADD CONSTRAINT tbl_shape_company_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_clarity ADD CONSTRAINT tbl_clarity_company_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_karatage ADD CONSTRAINT tbl_karatage_company_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_bin ADD CONSTRAINT tbl_bin_company_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_product ADD CONSTRAINT tbl_product_company_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);