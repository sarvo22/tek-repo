alter table tbl_roles add subscription_id integer;
-- for live update
update tbl_roles set subscription_id = 6006 where company_id = 50 and role_id = 50;
update tbl_roles set subscription_id = 6002 where company_id = 60 and role_id = 69;
update tbl_roles set subscription_id = 6002 where company_id = 60 and role_id = 71;
update tbl_roles set subscription_id = 6006 where company_id = 60 and role_id = 74;
update tbl_roles set subscription_id = 6004 where company_id = 3 and role_id = 1;
update tbl_roles set subscription_id = 6002 where company_id = 3 and role_id = 70;
update tbl_roles set subscription_id = 6006 where company_id = 3 and role_id = 73;
--alter table tbl_roles alter column subscription_id set not null;