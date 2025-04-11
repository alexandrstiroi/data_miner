CREATE TABLE if not exists public.tender_detail (
	id serial4 NOT NULL,
	name varchar(500) NULL,
	unique_id varchar(100) NULL,
	urls varchar(500) NULL,
	category varchar(500) NULL,
	category_name varchar(500) NULL,
	amount numeric(15, 2) NULL,
	currency varchar(500) NULL,
	lots text NULL
);