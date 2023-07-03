create table if not exists tbl_origin
(
    origin     varchar(50) not null,
    remarks			text,
    system_remarks	text,
    sort_seq		integer default 0 not null,
    is_locked		integer default 0 not null,
    created_dt		TIMESTAMP DEFAULT current_timestamp  NOT NULL,
    is_deleted		integer default 0 not null,
    PRIMARY KEY(origin)
);

insert into tbl_origin (origin,sort_seq,is_deleted) values('China',1,0);
insert into tbl_origin (origin,sort_seq,is_deleted) values('India',2,0);
insert into tbl_origin (origin,sort_seq,is_deleted) values('USA',3,0);
insert into tbl_origin (origin,sort_seq,is_deleted) values('Belgium',4,0);