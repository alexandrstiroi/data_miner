CREATE TABLE if not exists public.task_info (
	id serial4 NOT NULL,
	task_name varchar(100),
    task_value varchar(100),
    task_status varchar(100),
    create_stamp timestamp,
    modify_stamp timestamp
);