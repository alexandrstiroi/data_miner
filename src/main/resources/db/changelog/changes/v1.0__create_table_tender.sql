create table if not exists tender(
	id serial,
    country varchar(200),
    site varchar(200),
    name varchar(500),
    url varchar(500),
    customer_name varchar(500),
    value varchar(500),
    date varchar(500));