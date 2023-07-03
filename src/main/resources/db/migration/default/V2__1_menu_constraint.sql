-- Main Menu
--ALTER TABLE tbl_main_menu DROP CONSTRAINT tbl_main_menu_uk1;
ALTER TABLE tbl_main_menu ADD CONSTRAINT tbl_main_menu_uk1   UNIQUE(key);
-- Menu
ALTER TABLE tbl_menu ALTER COLUMN menu_id SET NOT NULL;
ALTER TABLE tbl_menu ALTER COLUMN key SET NOT NULL;
ALTER TABLE tbl_menu ALTER COLUMN label SET NOT NULL;

--ALTER TABLE tbl_menu DROP CONSTRAINT tbl_menu_uk1;
--ALTER TABLE tbl_menu DROP CONSTRAINT tbl_menu_fk1;
ALTER TABLE tbl_menu ADD CONSTRAINT tbl_menu_uk1   UNIQUE(key);
ALTER TABLE tbl_menu ADD CONSTRAINT tbl_menu_fk1 FOREIGN KEY (main_menu_id) REFERENCES tbl_main_menu(main_menu_id);
--Privilege
ALTER TABLE tbl_privilege ALTER COLUMN menu_id SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN fullaccess  SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN view  SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN add  SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN edit  SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN delete  SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN print  SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN share  SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN email  SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN detailview  SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN detailadd  SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN detaildelete  SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN mainlock      SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN mainunlock    SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN detaillock    SET NOT NULL;
ALTER TABLE tbl_privilege ALTER COLUMN detailunlock  SET NOT NULL;
-- Default values
ALTER TABLE tbl_privilege ALTER COLUMN fullaccess SET DEFAULT false;
ALTER TABLE tbl_privilege ALTER COLUMN view  SET DEFAULT false;
ALTER TABLE tbl_privilege ALTER COLUMN add  SET DEFAULT false;
ALTER TABLE tbl_privilege ALTER COLUMN edit  SET DEFAULT false;
ALTER TABLE tbl_privilege ALTER COLUMN delete  SET DEFAULT false;
ALTER TABLE tbl_privilege ALTER COLUMN print  SET DEFAULT false;
ALTER TABLE tbl_privilege ALTER COLUMN share  SET DEFAULT false;
ALTER TABLE tbl_privilege ALTER COLUMN email  SET DEFAULT false;
ALTER TABLE tbl_privilege ALTER COLUMN detailview  SET DEFAULT false;
ALTER TABLE tbl_privilege ALTER COLUMN detailadd  SET DEFAULT false;
ALTER TABLE tbl_privilege ALTER COLUMN detaildelete  SET DEFAULT false;
ALTER TABLE tbl_privilege ALTER COLUMN mainlock      SET DEFAULT false;
ALTER TABLE tbl_privilege ALTER COLUMN mainunlock    SET DEFAULT false;
ALTER TABLE tbl_privilege ALTER COLUMN detaillock    SET DEFAULT false;
ALTER TABLE tbl_privilege ALTER COLUMN detailunlock  SET DEFAULT false;
ALTER TABLE tbl_privilege ADD CONSTRAINT tbl_privilege_fk1 FOREIGN KEY (menu_id) REFERENCES tbl_menu(menu_id);

--Role Privilege
ALTER TABLE tbl_role_privilege ADD CONSTRAINT tbl_role_privilege_fk1 FOREIGN KEY (role_id) REFERENCES tbl_roles(role_id);
ALTER TABLE tbl_role_privilege ADD CONSTRAINT tbl_role_privilege_fk2 FOREIGN KEY (privilege_id) REFERENCES tbl_privilege(privilege_id);
--subscription
ALTER TABLE tbl_subscription ADD CONSTRAINT tbl_subscription_fk1 FOREIGN KEY (app_id) REFERENCES tbl_apps(app_id);
ALTER TABLE tbl_subscription_privilege_map ADD CONSTRAINT tbl_subscription_privilege_map_fk1 FOREIGN KEY (subscription_id) REFERENCES tbl_subscription(subscription_id);
ALTER TABLE tbl_subscription_privilege_map ADD CONSTRAINT tbl_subscription_privilege_map_fk2 FOREIGN KEY (privilege_id) REFERENCES tbl_privilege(privilege_id);
-- Country Master
ALTER TABLE tbl_country ALTER COLUMN date_format SET DEFAULT 'dd/MM/yyyy';
ALTER TABLE tbl_country ALTER COLUMN ui_date_format SET DEFAULT 'd/m/Y';
-- update default values for null columns
update tbl_country set date_format = 'dd/MM/yyyy' where date_format is null;
update tbl_country set ui_date_format = 'd/m/Y' where ui_date_format is null;
ALTER TABLE tbl_country ALTER COLUMN date_format SET NOT NULL;
ALTER TABLE tbl_country ALTER COLUMN ui_date_format SET NOT NULL;
-- state
ALTER TABLE tbl_state ALTER COLUMN country_id SET NOT NULL;
ALTER TABLE tbl_state ADD CONSTRAINT tbl_state_fk1 FOREIGN KEY (country_id) REFERENCES tbl_country(country_id);
-- Signup
ALTER TABLE tbl_signup ALTER COLUMN company_name SET NOT NULL;
ALTER TABLE tbl_signup ADD CONSTRAINT tbl_signup_fk1 FOREIGN KEY (country_id) REFERENCES tbl_country(country_id);
-- Users
-- update default values for null columns
update tbl_user set email = 'unknown@tekfilo.com' where email is null;
update tbl_user set user_uid = 'WRONGUID' where user_uid is null;
ALTER TABLE tbl_user ALTER COLUMN email SET NOT NULL;
ALTER TABLE tbl_user ALTER COLUMN user_uid SET NOT NULL;
-- Company Master
update tbl_company set company_uid ='WRONGUID' where company_uid is null;
update tbl_company set default_currency ='USD' where default_currency is null;
update tbl_company set status ='ACTIVE' where status is null;
update tbl_company set date_format = 'dd/MM/yyyy' where date_format is null;
update tbl_company set ui_date_format = 'd/m/Y' where ui_date_format is null;

ALTER TABLE tbl_company ALTER COLUMN company_uid SET NOT NULL;
ALTER TABLE tbl_company ALTER COLUMN default_currency SET NOT NULL;
ALTER TABLE tbl_company ALTER COLUMN default_currency SET DEFAULT 'USD';
ALTER TABLE tbl_company ALTER COLUMN country_id SET  NOT NULL;
ALTER TABLE tbl_company ALTER COLUMN status SET DEFAULT 'ACTIVE';
ALTER TABLE tbl_company ALTER COLUMN status SET NOT NULL;
ALTER TABLE tbl_country ALTER COLUMN date_format SET DEFAULT 'dd/MM/yyyy';
ALTER TABLE tbl_country ALTER COLUMN date_format SET NOT NULL;
ALTER TABLE tbl_country ALTER COLUMN ui_date_format SET DEFAULT 'd/m/Y';
ALTER TABLE tbl_country ALTER COLUMN ui_date_format SET NOT NULL;
ALTER TABLE tbl_company ADD CONSTRAINT tbl_company_fk1 FOREIGN KEY (country_id) REFERENCES tbl_country(country_id);
--Role
ALTER TABLE tbl_roles ALTER COLUMN company_id SET NOT NULL;
ALTER TABLE tbl_roles ADD CONSTRAINT tbl_roles_fk1 FOREIGN KEY (company_id) REFERENCES tbl_company(company_id);
-- Role Privilege Map
ALTER TABLE tbl_role_privilege ALTER COLUMN role_id SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN menu_id SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN fullaccess  SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN view  SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN add  SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN edit  SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN delete  SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN print  SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN share  SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN email  SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN detailview  SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN detailadd  SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN detaildelete  SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN mainlock      SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN mainunlock    SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN detaillock    SET NOT NULL;
ALTER TABLE tbl_role_privilege ALTER COLUMN detailunlock  SET NOT NULL;
-- Default values
ALTER TABLE tbl_role_privilege ALTER COLUMN fullaccess SET DEFAULT false;
ALTER TABLE tbl_role_privilege ALTER COLUMN view  SET DEFAULT false;
ALTER TABLE tbl_role_privilege ALTER COLUMN add  SET DEFAULT false;
ALTER TABLE tbl_role_privilege ALTER COLUMN edit  SET DEFAULT false;
ALTER TABLE tbl_role_privilege ALTER COLUMN delete  SET DEFAULT false;
ALTER TABLE tbl_role_privilege ALTER COLUMN print  SET DEFAULT false;
ALTER TABLE tbl_role_privilege ALTER COLUMN share  SET DEFAULT false;
ALTER TABLE tbl_role_privilege ALTER COLUMN email  SET DEFAULT false;
ALTER TABLE tbl_role_privilege ALTER COLUMN detailview  SET DEFAULT false;
ALTER TABLE tbl_role_privilege ALTER COLUMN detailadd  SET DEFAULT false;
ALTER TABLE tbl_role_privilege ALTER COLUMN detaildelete  SET DEFAULT false;
ALTER TABLE tbl_role_privilege ALTER COLUMN mainlock      SET DEFAULT false;
ALTER TABLE tbl_role_privilege ALTER COLUMN mainunlock    SET DEFAULT false;
ALTER TABLE tbl_role_privilege ALTER COLUMN detaillock    SET DEFAULT false;
ALTER TABLE tbl_role_privilege ALTER COLUMN detailunlock  SET DEFAULT false;
ALTER TABLE tbl_role_privilege ADD CONSTRAINT tbl_privilege_fk1 FOREIGN KEY (menu_id) REFERENCES tbl_menu(menu_id);
-- tbl_user_tenant
ALTER TABLE tbl_user_tenant ADD CONSTRAINT tbl_user_tenant_fk1 FOREIGN KEY (user_id) REFERENCES tbl_user(user_id);
ALTER TABLE tbl_user_tenant ADD CONSTRAINT tbl_user_tenant_fk2 FOREIGN KEY (company_id) REFERENCES tbl_company(company_id);
--User Storage Plans
ALTER TABLE tbl_user_storage_plan ADD CONSTRAINT tbl_user_storage_plan_fk1 FOREIGN KEY (company_id) REFERENCES tbl_company(company_id);
-- Reports
ALTER TABLE tbl_reports ALTER COLUMN report_name  SET NOT NULL;
ALTER TABLE tbl_reports ALTER COLUMN report_group  SET NOT NULL;