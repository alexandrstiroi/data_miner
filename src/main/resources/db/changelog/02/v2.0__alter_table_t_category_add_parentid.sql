ALTER TABLE t_category
ADD COLUMN IF NOT EXISTS parent_id int;

delete from t_category;

alter sequence t_category_id_seq restart with 9455;