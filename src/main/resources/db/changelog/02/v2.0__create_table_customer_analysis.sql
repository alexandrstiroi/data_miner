CREATE TABLE if not exists public.t_customer_analysis(
    id serial NOT NULL,
    customer_id varchar(20),
    customer_result text,
    create_at timestamp default current_timestamp);