-- Debit Credit Note changes
alter table tbl_debit_credit_note_det alter column account_id set not null;
update tbl_debit_credit_note_det set tax_pct = 0 where tax_pct is null;
ALTER TABLE tbl_debit_credit_note_det ALTER COLUMN tax_pct SET NOT NULL;
ALTER TABLE tbl_debit_credit_note_det ALTER COLUMN tax_pct SET  default 0;
ALTER TABLE tbl_debit_credit_note_det RENAME COLUMN amount TO gross_amount;
ALTER TABLE tbl_debit_credit_note_det ALTER COLUMN gross_amount SET NOT NULL;
ALTER TABLE tbl_debit_credit_note_det ALTER COLUMN gross_amount SET  default 0;
alter table tbl_debit_credit_note_det add column net_amount numeric(20,4) default 0  not null;

-- JV Table alters
update tbl_jv_det set tax_pct = 0 where tax_pct is null;
update tbl_jv_det set debit_amount = 0 where debit_amount is null;
update tbl_jv_det set credit_amount = 0 where credit_amount is null;
update tbl_jv_det set debit_tax_amount = 0 where debit_tax_amount is null;
update tbl_jv_det set credit_tax_amount = 0 where credit_tax_amount is null;
alter table tbl_jv_det alter column account_id set not null;
ALTER TABLE tbl_jv_det ALTER COLUMN tax_pct SET NOT NULL;
ALTER TABLE tbl_jv_det ALTER COLUMN tax_pct SET  default 0;
ALTER TABLE tbl_jv_det ALTER COLUMN debit_amount SET NOT NULL;
ALTER TABLE tbl_jv_det ALTER COLUMN debit_amount SET  default 0;
ALTER TABLE tbl_jv_det ALTER COLUMN credit_amount SET NOT NULL;
ALTER TABLE tbl_jv_det ALTER COLUMN credit_amount SET  default 0;
ALTER TABLE tbl_jv_det ALTER COLUMN debit_tax_amount SET NOT NULL;
ALTER TABLE tbl_jv_det ALTER COLUMN debit_tax_amount SET  default 0;
ALTER TABLE tbl_jv_det ALTER COLUMN credit_tax_amount SET NOT NULL;
ALTER TABLE tbl_jv_det ALTER COLUMN credit_tax_amount SET  default 0;
-- JV Breakup Table alters
alter table tbl_jv_breakup alter column cost_center_id set not null;
alter table tbl_jv_breakup alter column cost_category_id set not null;
update tbl_jv_breakup set tax_pct = 0 where tax_pct is null;
update tbl_jv_breakup set debit_amount = 0 where debit_amount is null;
update tbl_jv_breakup set credit_amount = 0 where credit_amount is null;
update tbl_jv_breakup set debit_tax_amount = 0 where debit_tax_amount is null;
update tbl_jv_breakup set credit_tax_amount = 0 where credit_tax_amount is null;
ALTER TABLE tbl_jv_breakup ALTER COLUMN tax_pct SET NOT NULL;
ALTER TABLE tbl_jv_breakup ALTER COLUMN tax_pct SET  default 0;
ALTER TABLE tbl_jv_breakup ALTER COLUMN debit_amount SET NOT NULL;
ALTER TABLE tbl_jv_breakup ALTER COLUMN debit_amount SET  default 0;
ALTER TABLE tbl_jv_breakup ALTER COLUMN credit_amount SET NOT NULL;
ALTER TABLE tbl_jv_breakup ALTER COLUMN credit_amount SET  default 0;
ALTER TABLE tbl_jv_breakup ALTER COLUMN debit_tax_amount SET NOT NULL;
ALTER TABLE tbl_jv_breakup ALTER COLUMN debit_tax_amount SET  default 0;
ALTER TABLE tbl_jv_breakup ALTER COLUMN credit_tax_amount SET NOT NULL;
ALTER TABLE tbl_jv_breakup ALTER COLUMN credit_tax_amount SET  default 0;

create SEQUENCE tbl_debit_credit_note_breakup_seq;
create table if not exists tbl_debit_credit_note_breakup
(
	breakup_id       integer not null default nextval('tbl_debit_credit_note_breakup_seq'),
	inv_type		 varchar(30) not null,
	inv_det_id		 integer not null,
	cost_center_id   integer not null,
	cost_category_id integer not null,
	description 	 varchar(100),
	tax_pct         numeric(20,4) default 0 not null,
	gross_amount    numeric(20,4) default 0 not null,
	tax_amount      numeric(20,4) default 0 not null,
	net_amount      numeric(20,4) default 0 not null,
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_by 		integer NOT NULL,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_by 	integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(breakup_id)
);

-- Breakup table constraint to respective detail table
ALTER TABLE tbl_jv_breakup ADD CONSTRAINT tbl_jv_breakup_fk1 FOREIGN KEY (jv_det_id) REFERENCES tbl_jv_det(jv_det_id);
ALTER TABLE tbl_bank_pay_rec_breakup ADD CONSTRAINT tbl_bank_pay_rec_breakup_fk1 FOREIGN KEY (inv_det_id) REFERENCES tbl_bank_pay_rec_det(inv_det_id);
ALTER TABLE tbl_cash_pay_rec_breakup ADD CONSTRAINT tbl_cash_pay_rec_breakup_fk1 FOREIGN KEY (inv_det_id) REFERENCES tbl_cash_pay_rec_det(inv_det_id);
ALTER TABLE tbl_debit_credit_note_breakup ADD CONSTRAINT tbl_debit_credit_note_breakup_fk1 FOREIGN KEY (inv_det_id) REFERENCES tbl_debit_credit_note_det(inv_det_id);