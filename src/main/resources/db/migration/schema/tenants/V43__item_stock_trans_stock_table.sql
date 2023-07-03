create SEQUENCE tbl_item_stock_trans_seq;
create table if not exists tbl_item_stock_trans
(
	trans_id 		integer not null default nextval('tbl_item_stock_trans_seq'),
	company_id 		integer not null,
	item_id 		integer not null,
	bin_id 			integer not null,
	inv_dt 			date not null,
	in_out_flag 	integer not null,
	qty 			numeric(20,4),
 	adj_qty 		numeric(20,4),
	party_type      varchar(50),
	party_id     	integer,
	currency_code	varchar(50),
	exchange_rate  numeric(20,4) default 0 not null,
	inv_rate		numeric(20,4),
	cost_price		numeric(20,4),
	inv_type  		varchar(50),
	inv_det_id  	integer,
	inv_id 			integer,
	inv_no          varchar(30),
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_by 		integer NOT NULL,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_by 	integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(trans_id)
);
ALTER TABLE tbl_item_stock_trans ADD CONSTRAINT trans_company_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_item_stock_trans ADD CONSTRAINT trans_item_fk1 FOREIGN KEY (item_id) REFERENCES tbl_item(item_id);
ALTER TABLE tbl_item_stock_trans ADD CONSTRAINT trans_bin_fk1 FOREIGN KEY (bin_id) REFERENCES tbl_bin(bin_id);

-- Stock table
create SEQUENCE tbl_item_stock_seq;
create table if not exists tbl_item_stock
(
	stock_id integer not null default nextval('tbl_item_stock_seq'),
	company_id integer NOT NULL,
	item_id integer NOT NULL,
	bin_id integer NOT NULL,
	in_qty numeric(20,4),
	out_qty numeric(20,4),
 	adj_in_qty numeric(20,4),
	adj_out_qty numeric(20,4),
	balance_qty numeric(20,4),
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_by 		integer NOT NULL,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_by 	integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(stock_id)
);
ALTER TABLE tbl_item_stock ADD CONSTRAINT stock_company_fk1 FOREIGN KEY (company_id) REFERENCES public.tbl_company(company_id);
ALTER TABLE tbl_item_stock ADD CONSTRAINT stock_item_fk1 FOREIGN KEY (item_id) REFERENCES tbl_item(item_id);
ALTER TABLE tbl_item_stock ADD CONSTRAINT stock_bin_fk1 FOREIGN KEY (bin_id) REFERENCES tbl_bin(bin_id);