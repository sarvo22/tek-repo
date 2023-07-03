create sequence public.tbl_main_menu_seq;
create table if not exists public.tbl_main_menu
(
    main_menu_id integer not null default nextval('tbl_main_menu_seq'),
    key varchar(50),
    label varchar(50),
    icon  varchar(50),
    link  varchar(50),
    collapsed boolean,
    isTitle   boolean,
    badge varchar(50),
    parentKey integer,
    showInInner boolean,
    remarks			text,
    system_remarks	text,
    sort_seq		integer default 0 not null,
    is_locked		integer default 0 not null,
    created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
    modified_dt		TIMESTAMP,
    is_deleted		integer default 0 not null,
    PRIMARY KEY(main_menu_id)
);

CREATE SEQUENCE public.tbl_menu_seq;
create table if not exists public.tbl_menu
(
	menu_id		integer  NOT NULL DEFAULT nextval('tbl_menu_seq'),

	main_menu_id integer,
	key varchar(50),
    label varchar(50),
    icon  varchar(50),
    link  varchar(50),
    add  boolean,
    addLink varchar(50),
    parentKey varchar(50),

    remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(menu_id)
);

CREATE SEQUENCE public.tbl_privilege_seq;
create table if not exists public.tbl_privilege
(
	privilege_id		integer  NOT NULL DEFAULT nextval('tbl_privilege_seq'),
	privilege_name	VARCHAR ( 50 ) UNIQUE NOT NULL,
	privilege_group	VARCHAR ( 50 ) NOT NULL,
	menu_id integer,
	fullaccess boolean,
    view boolean,
    add boolean,
    edit boolean,
    delete boolean,
    print boolean,
    share boolean,
    email boolean,
    detailview boolean,
    detailadd boolean,
    detaildelete boolean,
    mainlock     boolean,
    mainunlock   boolean,
    detaillock   boolean,
    detailunlock boolean,
    remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(privilege_id)
);


CREATE SEQUENCE public.tbl_country_seq;
create table if not exists public.tbl_country
(
	country_id		integer  NOT NULL DEFAULT nextval('tbl_country_seq'),
	country_code	VARCHAR ( 50 ) UNIQUE NOT NULL,
	country_name	VARCHAR ( 50 ) NOT NULL,
	mobile_code		VARCHAR ( 50 ) UNIQUE NOT NULL,
	date_format 		VARCHAR(50),
	ui_date_format      varchar(50)
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(country_id)
);

insert into tbl_country(country_code,country_name,mobile_code,ui_date_format) values ('IN','India','+91','d/m/Y');
insert into tbl_country(country_code,country_name,mobile_code,ui_date_format) values ('CN','China','+86','Y/m/d');
insert into tbl_country(country_code,country_name,mobile_code,ui_date_format) values ('HK','Hongkong','+852','d/m/Y');


create table if not exists public.tbl_state
(
	state_code		VARCHAR ( 50 ) UNIQUE NOT NULL,
	state_name		VARCHAR ( 50 ) NOT NULL,
	country_id		integer,
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(state_code)
);

-- Default states for India Country
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('AD','Andhra Pradesh',1,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('AR','Arunachal Pradesh',2,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('AS','Assam',3,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('BR','Bihar',4,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('CG','Chattisgarh',5,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('DL','Delhi',6,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('GA','Goa',7,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('GJ','Gujarat',8,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('HR','Haryana',9,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('HP','Himachal Pradesh',10,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('JK','Jammu and Kashmir',11,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('JH','Jharkhand',12,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('KA','Karnataka',13,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('KL','Kerala',14,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('LD','Lakshadweep Islands',15,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('MP','Madhya Pradesh',16,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('MH','Maharashtra',17,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('MN','Manipur',18,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('ML','Meghalaya',19,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('MZ','Mizoram',20,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('NL','Nagaland',21,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('OD','Odisha',22,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('PY','Pondicherry',23,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('PB','Punjab',24,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('RJ','Rajasthan',25,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('SK','Sikkim',26,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('TN','Tamil Nadu',27,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('TS','Telangana',28,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('TR','Tripura',29,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('UP','Uttar Pradesh',30,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('UK','Uttarakhand',31,(select country_id from public.tbl_country where country_code = 'IN'));
insert into public.tbl_state(state_code,state_name,sort_seq,country_id) values('WB','West Bengal',32,(select country_id from public.tbl_country where country_code = 'IN'));


create SEQUENCE public.tbl_global_parameter_seq;
create table if not exists public.tbl_global_parameter
(
	parameter_id   integer not null default nextval('tbl_global_parameter_seq'),
	parameter_code	varchar(100),
	parameter_name	varchar(100),
	parameter_group	varchar(100),	
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(parameter_id)
);

-- Insert default parameter values
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('STONE','Diamond','COMMODITY_GROUP_TYPE',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('GEMSTONE','Color Stone','COMMODITY_GROUP_TYPE',2);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('METAL','Metal','COMMODITY_GROUP_TYPE',3);

insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('Trading','Trading','BUSINESS_TYPE',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('Manufacturing','Manufacturing','BUSINESS_TYPE',2);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('April-March','April-March','FISCAL_YEAR',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('January-December','January-December','FISCAL_YEAR',2);

insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('CT','CTs','STONE_UOM',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('PCS','PCS','STONE_UOM',2);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('GM','GM','METAL_UOM',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('PCS','PCS','JEW_UOM',1);	

insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('NORMAL','General','ACCOUNT_CATEGORY',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('MAIN_CASH','Cash','ACCOUNT_CATEGORY',2);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('PETTY_CASH','Petty Cash','ACCOUNT_CATEGORY',3);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('BANK','Bank','ACCOUNT_CATEGORY',4);

insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('RM_PUR','RM Purchase','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('RM_SAL','RM Sales','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('RM_MEMO_PUR','RM Memo Purchase','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('RM_MEMO_SAL','RM Memo Sales','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('BIN_TRANSFER','Bin Transfer','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('JEW_PUR','Jewellery Purchase','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('JEW_SAL','Jewellery Sales','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('JEW_MEMO_PUR','Jewellery Memo Purchase','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('JEW_MEMO_SAL','Jewellery Memo Sales','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('JEW_BIN_TRANSFER','Jewellery Bin Transfer','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('FACTORY_INVOICE','Factory Invoice','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('MIXING','Mixing','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('PURCHASE_ORDER','Purchase Order','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('SALES_ORDER','Sales Order','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('PURCHASE_SETTLEMENT','Purchase Settlement','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('SALES_SETTLEMENT','Sales Settlement','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('CASH_PAYMENT','Cash Payment','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('CASH_RECEIPT','Cash Received','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('PETTY_CASH_PAYMENT','Petty Cash Payment','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('PETTY_CASH_RECEIPT','Petty Cash Received','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('BANK_PAYMENT','Bank Payment','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('BANK_RECEIPT','Bank Receipt','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('DEBIT_NOTE','Debit Note','VOUCHER_TYPE_GROUP',1);
insert into public.tbl_global_parameter(parameter_code,parameter_name,parameter_group,sort_seq) values('CREDIT_NOTE','Credit Note','VOUCHER_TYPE_GROUP',1);

CREATE SEQUENCE public.tbl_signup_seq;
create table if not exists public.tbl_signup
(
	signup_id		integer  NOT NULL DEFAULT nextval('tbl_signup_seq'),
	first_name		VARCHAR ( 100 ) NOT NULL,
	last_name		VARCHAR ( 100 ) NOT NULL,
	email			VARCHAR ( 100 ) UNIQUE NOT NULL,
	username		VARCHAR ( 100 ) UNIQUE NOT NULL,
	password		TEXT,
	country_id		integer	NOT NULL,
	company_name	VARCHAR (200),
	mobile_no		TEXT,
	job_title		VARCHAR(50),
	employee_count	VARCHAR(50),
	token			TEXT,
	is_token_verified	integer,
	token_verified_date	TIMESTAMP,
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_dt		TIMESTAMP DEFAULT current_timestamp not null,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(signup_id),
	CONSTRAINT tbl_signup_fk1 FOREIGN KEY(country_id) REFERENCES tbl_country(country_id)
)
;

CREATE SEQUENCE public.tbl_user_seq;
create table if not exists public.tbl_user
(
	user_id			integer  NOT NULL DEFAULT nextval('tbl_user_seq'),
	username		VARCHAR ( 50 ) UNIQUE NOT NULL,
	password 		text not null,
	email 			varchar(100),
	user_uid		varchar(100),
	total_login_count	integer default 0 not null,
	is_active		integer default 0 not null,
	image_path      varchar(100),
	remarks			text,
	system_remarks	TEXT,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_dt		TIMESTAMP DEFAULT current_timestamp not null,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(user_id)
)
;


CREATE SEQUENCE public.tbl_company_seq;
create table if not exists public.tbl_company (

	company_id 			integer  NOT NULL DEFAULT nextval('tbl_company_seq'),
	company_name 		VARCHAR ( 100 ) NOT NULL,
	display_name 		VARCHAR ( 100 ),
	company_uid 		varchar(20),
	business_type 		VARCHAR ( 100 ),
	company_type 		VARCHAR ( 100 ),
	default_currency 	VARCHAR ( 100 ),
	country_id 			integer,
	state 				VARCHAR ( 100 ),
	city 				VARCHAR ( 100 ),
	street 				VARCHAR ( 100 ) ,	
	area 				VARCHAR ( 100 ) ,
	pin_code 				VARCHAR (100) ,
	prime_contact_name 	VARCHAR ( 100 ) ,
	email 				VARCHAR ( 100 ) ,
	fax_no				varchar(100),
	phone_number 		VARCHAR ( 100 ) ,
	mobile_no 			VARCHAR ( 100 ) ,
	website 			VARCHAR ( 100 ) ,
	reg_type 			VARCHAR ( 100 ) ,
	reg_number 			VARCHAR ( 100 ) ,
	tax_type 			VARCHAR ( 100 ) ,
	tax_number 			VARCHAR ( 100 ) ,
	status 				VARCHAR ( 100 ) ,
	date_format 		VARCHAR ( 100 ) ,
	date_time_divider 	varchar(50),
	ui_date_format      varchar(50),
	time_zone 			VARCHAR ( 100 ) ,
	fiscal_year			varchar(100),
	payment_address		text,
	log_url				text,
	remarks 			text,
	system_remarks		TEXT,
	created_by 			integer NOT NULL,
	created_dt 			TIMESTAMP DEFAULT current_timestamp not null,
    modified_by 		integer,
	modified_dt 		TIMESTAMP, 
	is_deleted 			integer default 0 not null,
	PRIMARY KEY(company_id),
	CONSTRAINT tbl_company_fk1 FOREIGN KEY(country_id) REFERENCES tbl_country(country_id)
);

CREATE SEQUENCE public.tbl_user_company_map_seq;
create table if not exists public.tbl_user_company_map (

	map_id 				integer  NOT NULL DEFAULT nextval('tbl_user_company_map_seq'),
	user_id 			integer  NOT NULL,
	company_id 			integer  NOT NULL,
	is_active			integer default 1 not null,
	is_default          integer default 1 not null,
	remarks 			text,
	system_remarks		TEXT,
	created_by 			integer NOT NULL,
	created_dt 			TIMESTAMP DEFAULT current_timestamp not null,
    modified_by 		integer ,
	modified_dt 		TIMESTAMP, 
	is_deleted 			integer default 0 not null,
	PRIMARY KEY(map_id),
	CONSTRAINT tbl_company_fk1 FOREIGN KEY(user_id) REFERENCES tbl_user(user_id),
	CONSTRAINT tbl_company_fk2 FOREIGN KEY(company_id) REFERENCES tbl_company(company_id)
);


create table if not exists public.tbl_currency
(
	currency_code	varchar(50)  UNIQUE not null,
	currency_name 	varchar(100) UNIQUE not null,
	symbol  		varchar(50) not null,
    formatter   	varchar(50) not null,
    decimal_places  integer 	not null,
	remarks			text,
	system_remarks	text,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(currency_code)
);

insert into public.tbl_currency(currency_code,currency_name,symbol,formatter,decimal_places,sort_seq,is_locked,is_deleted) values('USD','US Dollar','$','10,00,000.00',2,1,0,0);
insert into public.tbl_currency(currency_code,currency_name,symbol,formatter,decimal_places,sort_seq,is_locked,is_deleted) values('INR','Indian Rupees','₹','10,00,000.00',2,2,0,0);
insert into public.tbl_currency(currency_code,currency_name,symbol,formatter,decimal_places,sort_seq,is_locked,is_deleted) values('RMB','Chinese Yuan','元','10,00,000.00',2,3,0,0);
insert into public.tbl_currency(currency_code,currency_name,symbol,formatter,decimal_places,sort_seq,is_locked,is_deleted) values('HKD','Hong Kong Dollar','HK$','10,00,000.00',2,4,0,0);
insert into public.tbl_currency(currency_code,currency_name,symbol,formatter,decimal_places,sort_seq,is_locked,is_deleted) values('THB','Thai Baht','฿','10,00,000.00',2,5,0,0);


CREATE SEQUENCE public.tbl_roles_seq;
create table if not exists public.tbl_roles(
   	role_id 		integer  NOT NULL DEFAULT nextval('tbl_roles_seq'),
   	role_name 		VARCHAR (200) not null,
   	company_id      integer,
   	remarks 		text,
	system_remarks	text,
    created_by 	integer NOT NULL,
	created_dt		TIMESTAMP DEFAULT current_timestamp not null,
    modified_by 	integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(role_id)
);


create sequence public.tbl_role_privilege_seq;
create table if not exists public.tbl_role_privilege
(
    map_id          integer not null default nextval('public.tbl_role_privilege_seq'),
    role_id         integer,
    privilege_id	integer  NOT NULL,
    privilege_name	VARCHAR (50),
    privilege_group	VARCHAR (50),
    menu_id         integer,
    fullaccess      boolean,
    view            boolean,
    add             boolean,
    edit            boolean,
    delete          boolean,
    print           boolean,
    share           boolean,
    email           boolean,
    detailview      boolean,
    detailadd       boolean,
    detaildelete    boolean,

    mainlock     boolean,
    mainunlock   boolean,
    detaillock   boolean,
    detailunlock boolean,

    remarks 		text,
    system_remarks	text,
    created_by 	integer NOT NULL,
    created_dt		TIMESTAMP DEFAULT current_timestamp not null,
    modified_by 	integer,
    modified_dt		TIMESTAMP,
    is_deleted		integer default 0 not null,
    PRIMARY KEY (map_id)
);

CREATE SEQUENCE public.tbl_user_roles_seq;
create table if not exists public.tbl_user_roles (
	role_map_id		integer NOT NULL DEFAULT nextval('tbl_user_roles_seq'),
  	user_id 		integer NOT NULL,
  	role_id 		integer NOT NULL,
	company_id		integer NOT NULL,
   	remarks 		text,
	system_remarks	text,
    created_by 	integer NOT NULL,
	created_dt		TIMESTAMP DEFAULT current_timestamp not null,
    modified_by 	integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
  	PRIMARY KEY (user_id, role_id),
  	CONSTRAINT tbl_user_roles_fk1 FOREIGN KEY(user_id) REFERENCES tbl_user(user_id),
  	CONSTRAINT tbl_user_roles_fk2 FOREIGN KEY(role_id) REFERENCES tbl_roles(role_id)
);

CREATE SEQUENCE public.tbl_user_tenant_seq;
create table if not exists public.tbl_user_tenant
(
	id			    integer  NOT NULL DEFAULT nextval('tbl_user_tenant_seq'),
	user_uid  	    varchar(100) NOT NULL,
	tenant_uid 	    varchar(100) NOT NULL,
	user_id 	    integer not null,
	company_id      integer not null,
	remarks 			text,
	system_remarks		TEXT,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_by      integer,
	created_dt		TIMESTAMP DEFAULT current_timestamp not null,
	modified_by     integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(id)
);

create sequence public.tbl_api_seq;
create table if not exists public.tbl_api
(
	api_id			integer  NOT NULL DEFAULT nextval('tbl_api_seq'),
	api_name  		varchar(100) NOT NULL,
	description 	text,
	action_code		varchar(50),
	api_endpoint	varchar(100),
	remarks 		text,
	system_remarks	TEXT,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_dt		TIMESTAMP DEFAULT current_timestamp not null,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(api_id)
);

insert into public.tbl_api(api_name,description,action_code,sort_seq,is_locked,is_deleted) values('Metal Rate','Metal Rate service','METAL_RATE',1,0,0);
insert into public.tbl_api(api_name,description,action_code,sort_seq,is_locked,is_deleted) values('Rapaport Price','Daily Diamond Rapaport Price service','RAPAPORT_RATE',2,0,0);
insert into public.tbl_api(api_name,description,action_code,sort_seq,is_locked,is_deleted) values('SMS Service','SMS service','SMS_SERVICE',3,0,0);


create sequence public.tbl_api_user_config_seq;
create table if not exists public.tbl_api_user_config
(
	api_config_id	integer  NOT NULL DEFAULT nextval('tbl_api_user_config_seq'),
	api_id 			integer not null,
	api_user_name	varchar(100) not null,
	api_key			varchar(500) not null,
	api_sid			varchar(500) not null,
	cronexpression  varchar(50) not null,
	confirmation	varchar(30) not null,
	tenant_uid 		varchar(100) not null,
	company_id 		integer not null,
	is_enabled 		integer default 0 not null,
	remarks 			text,
	system_remarks		TEXT,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_by      integer,
	created_dt		TIMESTAMP DEFAULT current_timestamp not null,
	modified_by     integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(api_config_id)
);
----Incomplete stage need to focus
create sequence public.tbl_user_storage_plan_seq;
create table if not exists public.tbl_user_storage_plan
(
	user_storage_plan_id integer not null default nextval('tbl_user_storage_plan_seq'),
	company_id 		integer not null,
	is_enabled 		integer default 0 not null,
	remarks 			text,
	system_remarks		TEXT,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_by      integer,
	created_dt		TIMESTAMP DEFAULT current_timestamp not null,
	modified_by     integer,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(user_storage_plan_id)	
);

create sequence public.tbl_reports_seq;
create table if not exists public.tbl_reports
(
    report_id       integer not null default nextval('tbl_reports_seq'),
    report_name     varchar(100),
    display_name    varchar(100),
    report_group    varchar(100),
    report_category varchar(50),
	is_enabled 		integer default 0 not null,
	remarks 		text,
	system_remarks	text,
	group_sort_seq		integer default 0 not null,
	sort_seq		integer default 0 not null,
	is_locked		integer default 0 not null,
	created_dt		TIMESTAMP DEFAULT current_timestamp not null,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(report_id)
);


create sequence public.tbl_inquery_seq;
create table if not exists public.tbl_inquery
(
    inquery_id      integer not null default nextval('tbl_inquery_seq'),
    first_name      varchar(100),
    last_name       varchar(100),
    phone_number    varchar(100),
    email_id        varchar(50),
    message         text,
	created_dt		TIMESTAMP DEFAULT current_timestamp not null,
	modified_dt		TIMESTAMP,
	is_deleted		integer default 0 not null,
	PRIMARY KEY(inquery_id)
);

create table if not exists public.tbl_apps
(
    app_id integer not null,
    app_name    varchar(100) not null,
    app_desc    text,
    logo        text,
    sort_seq    integer default 0 not null,
    is_active  integer default 1 not null,
    is_locked  integer default 0 not null,
    created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
    is_deleted integer default 0 not null,
    primary key(app_id)
);

insert into public.tbl_apps(app_id,app_name,app_desc,logo,sort_seq,is_active,is_locked,is_deleted) values(5001,'Gem Spark','Gemstone Product','gemstone.png',1,1,0,0);
insert into public.tbl_apps(app_id,app_name,app_desc,logo,sort_seq,is_active,is_locked,is_deleted) values(5002,'Jewellery Trade','Jewellery Trading','diamond.png',2,1,0,0);
insert into public.tbl_apps(app_id,app_name,app_desc,logo,sort_seq,is_active,is_locked,is_deleted) values(5003,'Balance Books','Books of Accounts','book.png',3,1,0,0);
insert into public.tbl_apps(app_id,app_name,app_desc,logo,sort_seq,is_active,is_locked,is_deleted) values(5004,'Smart Factory','Smart Factory','smart-factory.png',4,1,0,0);

create table if not exists public.tbl_subscription
(
    subscription_id integer not null,
    subscription_name    varchar(100) not null,
    subscription_desc    text,
    app_id      integer null null,
    sort_seq    integer default 0 not null,
    is_active  integer default 1 not null,
    is_locked  integer default 0 not null,
    created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
    is_deleted integer default 0 not null,
    primary key(subscription_id)
);

insert into public.tbl_subscription(subscription_id,subscription_name,subscription_desc,app_id,sort_seq,is_active,is_locked,is_deleted) values(6001,'Gem Spark Basic','Gem Spark Basic',5001,1,1,0,0);
insert into public.tbl_subscription(subscription_id,subscription_name,subscription_desc,app_id,sort_seq,is_active,is_locked,is_deleted) values(6002,'Gem Spark Premium','Gem Spark Premium',5001,2,1,0,0);
insert into public.tbl_subscription(subscription_id,subscription_name,subscription_desc,app_id,sort_seq,is_active,is_locked,is_deleted) values(6003,'Jewellery Trade Basic','Jewellery Trade Basic',5002,3,1,0,0);
insert into public.tbl_subscription(subscription_id,subscription_name,subscription_desc,app_id,sort_seq,is_active,is_locked,is_deleted) values(6004,'Jewellery Trade Premium','Jewellery Trade Premium',5002,4,1,0,0);
insert into public.tbl_subscription(subscription_id,subscription_name,subscription_desc,app_id,sort_seq,is_active,is_locked,is_deleted) values(6005,'Balance Books Basic','Balance Books Basic',5003,5,1,0,0);
insert into public.tbl_subscription(subscription_id,subscription_name,subscription_desc,app_id,sort_seq,is_active,is_locked,is_deleted) values(6006,'Balance Books Premium','Balance Books Premium',5003,6,1,0,0);
insert into public.tbl_subscription(subscription_id,subscription_name,subscription_desc,app_id,sort_seq,is_active,is_locked,is_deleted) values(6007,'Smart Factory Basic','Smart Factory Basic',5004,7,1,0,0);
insert into public.tbl_subscription(subscription_id,subscription_name,subscription_desc,app_id,sort_seq,is_active,is_locked,is_deleted) values(6008,'Smart Factory Premium','Smart Factory Premium',5004,8,1,0,0);

create sequence public.tbl_company_subscription_map_seq;
create table if not exists public.tbl_company_subscription_map
(
    map_id          integer not null default nextval('tbl_company_subscription_map_seq'),
    company_id	    integer  NOT NULL,
    subscription_id integer not null,
    remarks 		text,
    system_remarks	text,
    created_dt		TIMESTAMP DEFAULT current_timestamp not null,
    modified_dt		TIMESTAMP,
    is_deleted		integer default 0 not null,
    PRIMARY KEY (map_id)
);


create sequence public.tbl_subscription_privilege_map_seq;
create table if not exists public.tbl_subscription_privilege_map
(
    map_id          integer not null default nextval('tbl_subscription_privilege_map_seq'),
    subscription_id integer not null,
    privilege_id	integer  NOT NULL,
    privilege_name	VARCHAR (50),
    privilege_group	VARCHAR (50),
    menu_id         integer,
    fullaccess      boolean,
    view            boolean,
    add             boolean,
    edit            boolean,
    delete          boolean,
    print           boolean,
    share           boolean,
    email           boolean,
    detailview      boolean,
    detailadd       boolean,
    detaildelete    boolean,
    mainlock        boolean,
    mainunlock      boolean,
    detaillock      boolean,
    detailunlock    boolean,
    remarks 		text,
    system_remarks	text,
    created_dt		TIMESTAMP DEFAULT current_timestamp not null,
    modified_dt		TIMESTAMP,
    is_deleted		integer default 0 not null,
    PRIMARY KEY (map_id)
);