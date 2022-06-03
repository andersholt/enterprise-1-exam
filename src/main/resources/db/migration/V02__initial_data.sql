insert into users
values (nextval('users_user_id_seq'), 'admin@admin.com', '$2a$10$rFLKNyc8mprXFAGBQAKcU.bddEpRpXTM1YRmWsrW1X4fzZ6kOrOR2',
        now(), true);
insert into users
values (nextval('users_user_id_seq'), 'user@user.com', '$2a$10$rFLKNyc8mprXFAGBQAKcU.bddEpRpXTM1YRmWsrW1X4fzZ6kOrOR2',
        now(), true);
insert into users
values (nextval('users_user_id_seq'), 'workuser@workuser.com',
        '$2a$10$rFLKNyc8mprXFAGBQAKcU.bddEpRpXTM1YRmWsrW1X4fzZ6kOrOR2', now(), true);

insert into users
values (nextval('users_user_id_seq'), 'test@delete.com',
        '$2a$10$rFLKNyc8mprXFAGBQAKcU.bddEpRpXTM1YRmWsrW1X4fzZ6kOrOR2', now(), true);

insert into animals
values (nextval('animals_animal_id_seq'), 'Pebbles', 'cat', 'norwegian forest', now(), true, 6, 73);
insert into animals
values (nextval('animals_animal_id_seq'), 'Hobbes', 'cat', 'persian', now(), true, 6, 65);
insert into animals
values (nextval('animals_animal_id_seq'), 'Jason', 'cat', 'american', now(), true, 40, 100);


insert into authorities
values (nextval('authorities_authority_id_seq'), 'USER');

insert into authorities
values (nextval('authorities_authority_id_seq'), 'ADMIN');

insert into authorities
values (nextval('authorities_authority_id_seq'), 'WORKUSER');

-- ADMIN with access to all
insert into users_authorities
values (1, 1);

insert into users_authorities
values (2, 1);

insert into users_authorities
values (3, 1);

-- USER with USER access
insert into users_authorities
values (1, 2);


-- WORKUSER with USER and WORKUSER access
insert into users_authorities
values (1, 3);

insert into users_authorities
values (3, 3);



