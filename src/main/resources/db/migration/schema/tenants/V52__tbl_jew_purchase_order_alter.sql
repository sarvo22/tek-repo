
alter table tbl_jew_purchase_order_main add column party_type varchar(30);
alter table tbl_jew_purchase_order_main add column party_id integer;
alter table tbl_jew_purchase_order_main add column order_category varchar(30);
alter table tbl_jew_purchase_order_main add column customer_id integer;

alter table tbl_jew_purchase_order_det add column order_type varchar(30);
alter table tbl_jew_purchase_order_det add column metal_wt  numeric(20,4);
alter table tbl_jew_purchase_order_det add column prod_remarks text;
alter table tbl_jew_purchase_order_det add column jew_id integer;

create SEQUENCE tbl_jew_purchase_order_component_seq;
create table if not exists tbl_jew_purchase_order_component
(
	component_id integer not null default nextval('tbl_jew_purchase_order_component_seq'),
	purchase_order_det_id 	integer,
	purchase_order_id 		integer,
	lot_id			integer,
	commodity_id 	integer not null,
	shape_id		integer,
	color_id		integer,
	cut_id 			integer,
	setting_type varchar(50),
	sieve_size   varchar(50),
	mm_size   	 varchar(50),
	qty1		numeric(20,2) default 0 not null,
	qty2		numeric(20,2) default 0 not null,
	is_center_stone integer default 0 not null,
	setting_type_id integer,
	total_wt numeric(20,4) default 0 not null,
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_by 		integer NOT NULL,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_by 	integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(component_id),
	constraint tbl_jew_purchase_order_component_fk1 FOREIGN key(purchase_order_det_id) references tbl_jew_purchase_order_det(purchase_order_det_id),
	constraint tbl_jew_purchase_order_component_fk2 FOREIGN key(purchase_order_id) references tbl_jew_purchase_order_main(purchase_order_id)
);

create SEQUENCE tbl_jew_pod_labour_seq;
create table if not exists tbl_jew_pod_labour
(
	labour_id integer not null default nextval('tbl_jew_pod_labour_seq'),
	purchase_order_det_id integer not null,
	purchase_order_id integer not null,
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
	PRIMARY KEY(labour_id),
	constraint tbl_jew_pod_labour_fk1 FOREIGN key(purchase_order_det_id) references tbl_jew_purchase_order_det(purchase_order_det_id),
	constraint tbl_jew_pod_labour_fk2 FOREIGN key(purchase_order_id) references tbl_jew_purchase_order_main(purchase_order_id),
	constraint tbl_jew_pod_labour_fk3 FOREIGN key(process_id) references tbl_process(process_id)
);

create SEQUENCE tbl_jew_pod_finding_seq;
create table if not exists tbl_jew_pod_finding
(
	finding_id integer not null default nextval('tbl_jew_pod_finding_seq'),
	purchase_order_det_id integer not null,
	purchase_order_id integer not null,
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
	PRIMARY KEY(finding_id),
	constraint tbl_jew_pod_finding_fk1 FOREIGN key(purchase_order_det_id) references tbl_jew_purchase_order_det(purchase_order_det_id),
	constraint tbl_jew_pod_finding_fk2 FOREIGN key(purchase_order_id) references tbl_jew_purchase_order_main(purchase_order_id)
);
create SEQUENCE tbl_jew_pod_mould_part_seq;
create table if not exists tbl_jew_pod_mould_part
(
	mould_part_id integer not null default nextval('tbl_jew_pod_mould_part_seq'),
	purchase_order_det_id integer not null,
	purchase_order_id integer not null,
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
	PRIMARY KEY(mould_part_id),
	constraint tbl_jew_pod_mould_part_fk1 FOREIGN key(purchase_order_det_id) references tbl_jew_purchase_order_det(purchase_order_det_id),
	constraint tbl_jew_pod_mould_part_fk2 FOREIGN key(purchase_order_id) references tbl_jew_purchase_order_main(purchase_order_id)
);

create SEQUENCE tbl_jew_pod_wax_seq;
create table if not exists tbl_jew_pod_wax
(
	wax_id integer not null default nextval('tbl_jew_pod_wax_seq'),
	purchase_order_det_id integer not null,
	purchase_order_id integer not null,
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
	PRIMARY KEY(wax_id),
	constraint tbl_jew_pod_wax_fk1 FOREIGN key(purchase_order_det_id) references tbl_jew_purchase_order_det(purchase_order_det_id),
	constraint tbl_jew_pod_wax_fk2 FOREIGN key(purchase_order_id) references tbl_jew_purchase_order_main(purchase_order_id)
);