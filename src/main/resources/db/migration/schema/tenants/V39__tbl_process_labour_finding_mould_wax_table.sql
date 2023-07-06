create SEQUENCE tbl_process_seq;
create table if not exists tbl_process
(
	process_id integer not null default nextval('tbl_process_seq'),
	process_name varchar(50) not null,
	company_id integer not null,
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_by 		integer NOT NULL,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_by 	integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(process_id)
);

create SEQUENCE tbl_sku_labour_seq;
create table if not exists tbl_sku_labour
(
	sku_labour_id integer not null default nextval('tbl_sku_labour_seq'),
	sku_id integer not null,
	process_id integer not null,
    description text,
    qty numeric(20,4) default 0 not null,
    rate numeric(20,4) default 0 not null,
    amount numeric(20,4) default 0 not null,
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_by 		integer NOT NULL,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_by 	integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(sku_labour_id),
	constraint tbl_sku_labour_fk1 FOREIGN key(sku_id) references tbl_sku(sku_id),
	constraint tbl_sku_labour_fk2 FOREIGN key(process_id) references tbl_process(process_id)
);

create SEQUENCE tbl_sku_finding_seq;
create table if not exists tbl_sku_finding
(
	sku_finding_id integer not null default nextval('tbl_sku_finding_seq'),
	sku_id          integer not null,
	finding_name    varchar(50) not null,
    description     text,
    qty             numeric(20,4) default 0 not null,
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_by 		integer NOT NULL,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_by 	integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(sku_finding_id),
	constraint tbl_sku_labour_fk1 FOREIGN key(sku_id) references tbl_sku(sku_id)
);
create SEQUENCE tbl_sku_mould_part_seq;
create table if not exists tbl_sku_mould_part
(
	sku_mould_part_id integer not null default nextval('tbl_sku_mould_part_seq'),
	sku_id      integer not null,
	part_name   varchar(50) not null,
	part_no     varchar(50),
    description text,
    pieces          numeric(20,4) default 0 not null,
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_by 		integer NOT NULL,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_by 	integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(sku_mould_part_id),
	constraint tbl_sku_labour_fk1 FOREIGN key(sku_id) references tbl_sku(sku_id)
);

create SEQUENCE tbl_sku_wax_seq;
create table if not exists tbl_sku_wax
(
	sku_wax_id integer not null default nextval('tbl_sku_wax_seq'),
	sku_id      integer not null,
	product_size varchar(50) not null,
	karatage_id  integer,
    description  text,
    wax_wt          numeric(20,4) default 0 not null,
    metal_wt        numeric(20,4) default 0 not null,
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_by 		integer NOT NULL,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_by 	integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(sku_wax_id),
	constraint tbl_sku_labour_fk1 FOREIGN key(sku_id) references tbl_sku(sku_id)
);