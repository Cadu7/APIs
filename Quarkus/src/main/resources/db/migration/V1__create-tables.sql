CREATE TABLE IF NOT EXISTS user_table(
    id uuid NOT NULL PRIMARY KEY,
    cpf varchar(255) NOT NULL,
    name varchar(255) NOT NULL,
    user_name varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    role varchar(255) NOT NULL
);
INSERT INTO user_table values ('bc1e4eba-3057-11ed-a261-0242ac120002','00000000000','admin','admin_user','$2a$10$0i89PXn1ZIolL6WUnrOQcOzrt6nwP8Cw/mNm8.cSYY2FsBvOjcXYm','ADMIN');

