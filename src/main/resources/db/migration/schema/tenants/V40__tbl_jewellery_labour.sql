create SEQUENCE tbl_jewellery_labour_seq;
create table if not exists tbl_jewellery_labour
(
	labour_id integer not null default nextval('tbl_jewellery_labour_seq'),
	inv_det_id  integer not null,
	inv_id      integer not null,
	jew_id      integer not null,
	process_id  integer not null,
    description text,
    qty     numeric(20,4) default 0 not null,
    rate    numeric(20,4) default 0 not null,
    amount  numeric(20,4) default 0 not null,
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
	constraint tbl_jewellery_labour_fk1 FOREIGN key(inv_det_id) references tbl_goods_inward_det(inv_det_id),
	constraint tbl_jewellery_labour_fk2 FOREIGN key(inv_id) references tbl_goods_inward_main(inv_id)
);