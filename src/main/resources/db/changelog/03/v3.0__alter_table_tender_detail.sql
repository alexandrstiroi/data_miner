alter table public.tender_detail
add column status varchar(255);

alter table public.tender_detail
add column status_details varchar(255);

alter table public.tender_detail
add column period text;

alter table public.tender_detail
add column auction_period timestamp;

alter table public.tender_detail
add column documents text;