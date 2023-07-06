--create SEQUENCE tbl_mfg_unit_seq;
--create table if not exists tbl_mfg_unit
--(
--    mfg_unit_id    integer not null default nextval('tbl_mfg_unit_seq'),
--    mfg_unit_name     varchar(50) not null,
--    mfg_unit_belongs_to   varchar(50) not null,
--    company_id      integer not null,
--    remarks			text,
--    system_remarks	text,
--    sort_seq		integer default 0 not null,
--    is_locked		integer default 0 not null,
--    created_by 		integer NOT NULL,
--    created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
--    modified_by 	integer,
--    modified_dt		TIMESTAMP,
--    is_deleted		integer default 0 not null,
--    PRIMARY KEY(mfg_unit_id),
--    CONSTRAINT tbl_mfg_unit_fk1 FOREIGN KEY(company_id) REFERENCES public.tbl_company(company_id)
--);
--
--create SEQUENCE tbl_mfg_unit_department_map_seq;
--create table if not exists tbl_mfg_unit_department_map
--(
--    map_id    integer not null default nextval('tbl_mfg_unit_department_map_seq'),
--    mfg_unit_id     integer not null,
--    department_id   integer not null,
--    remarks			text,
--    system_remarks	text,
--    sort_seq		integer default 0 not null,
--    is_locked		integer default 0 not null,
--    created_by 		integer NOT NULL,
--    created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
--    modified_by 	integer,
--    modified_dt		TIMESTAMP,
--    is_deleted		integer default 0 not null,
--    PRIMARY KEY(map_id),
--    CONSTRAINT tbl_mfg_unit_department_map_fk1 FOREIGN KEY(mfg_unit_id) REFERENCES tbl_mfg_unit(mfg_unit_id),
--    CONSTRAINT tbl_mfg_unit_department_map_fk2 FOREIGN KEY(department_id) REFERENCES tbl_mfg_unit_department(department_id)
--);
--
--create SEQUENCE tbl_mfg_process_seq;
--create table if not exists tbl_mfg_process
--(
--    process_id    integer not null default nextval('tbl_mfg_process_seq'),
--    process_name     varchar(50) not null,
--    company_id      integer not null,
--    remarks			text,
--    system_remarks	text,
--    sort_seq		integer default 0 not null,
--    is_locked		integer default 0 not null,
--    created_by 		integer NOT NULL,
--    created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
--    modified_by 	integer,
--    modified_dt		TIMESTAMP,
--    is_deleted		integer default 0 not null,
--    PRIMARY KEY(process_id),
--    CONSTRAINT tbl_mfg_process_fk1 FOREIGN KEY(company_id) REFERENCES public.tbl_company(company_id)
--);
--
--
--create SEQUENCE tbl_mfg_department_process_map_seq;
--create table if not exists tbl_mfg_department_process_map
--(
--    map_id    integer not null default nextval('tbl_mfg_department_process_map_seq'),
--    department_id   integer not null,
--    process_id   integer not null,
--    remarks			text,
--    system_remarks	text,
--    sort_seq		integer default 0 not null,
--    is_locked		integer default 0 not null,
--    created_by 		integer NOT NULL,
--    created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
--    modified_by 	integer,
--    modified_dt		TIMESTAMP,
--    is_deleted		integer default 0 not null,
--    PRIMARY KEY(map_id),
--    CONSTRAINT tbl_mfg_department_process_map_fk1 FOREIGN KEY(department_id) REFERENCES tbl_mfg_department(department_id),
--    CONSTRAINT tbl_mfg_department_process_map_fk2 FOREIGN KEY(process_id) REFERENCES tbl_mfg_process(process_id)
--);
--
--create SEQUENCE tbl_mfg_employee_process_map_seq;
--create table if not exists tbl_mfg_employee_process_map
--(
--    map_id    integer not null default nextval('tbl_mfg_employee_process_map_seq'),
--    employee_id   integer not null,
--    process_id   integer not null,
--    remarks			text,
--    system_remarks	text,
--    sort_seq		integer default 0 not null,
--    is_locked		integer default 0 not null,
--    created_by 		integer NOT NULL,
--    created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
--    modified_by 	integer,
--    modified_dt		TIMESTAMP,
--    is_deleted		integer default 0 not null,
--    PRIMARY KEY(map_id),
--    CONSTRAINT tbl_mfg_employee_process_map_fk1 FOREIGN KEY(employee_id) REFERENCES tbl_employee(employee_id),
--    CONSTRAINT tbl_mfg_employee_process_map_fk2 FOREIGN KEY(process_id) REFERENCES tbl_mfg_process(process_id)
--);