alter table tbl_country add default_currency varchar(30);
update tbl_country set default_currency = 'INR' where country_code = 'IN';
update tbl_country set default_currency = 'HKD' where country_code = 'HK';
update tbl_country set default_currency = 'CNY' where country_code = 'CN';
alter table tbl_country alter column default_currency set not null;