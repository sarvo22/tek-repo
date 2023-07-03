create SEQUENCE tbl_default_invoice_chgs_seq;
create table if not exists tbl_default_invoice_chgs
(
	default_charge_id   integer not null default nextval('tbl_default_invoice_chgs_seq'),
	inv_type            varchar(30) not null,
	charge_name      	varchar(100) not null,
	plus_minus_flag  	integer not null,
	input_pct_amt_type 	varchar(50) not null,
	input_pct_amt_value numeric(20,4) default 0 not null,
	input_amt 			numeric(20,4) default 0 not null,
	is_party_payable    integer default 0 not null,
	company_id          integer not null,
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_by 		integer NOT NULL,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_by 	integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(default_charge_id)
);