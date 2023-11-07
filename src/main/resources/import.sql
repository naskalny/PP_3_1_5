#Пользователь с ролью user (user, user)
INSERT INTO users (name, surname, age, username, password) VALUES ('oleg', 'tinkov', 43, 'user', '$2a$10$FsEjSjfYi1CgaOVg1IkqPei1T.x9H4uzpwQXajaTuIPuNfBfEIah.');

#Пользователь с ролью admin (admin, admin)
INSERT INTO users (name, surname, age, username, password) VALUES ('og', 'buda', 23, 'admin', '$2a$10$cJ9KtncsPQbeCbDpyH9kqObFBG2PhG5kr.M17dEHlxsv7kcb9SLrq');

INSERT INTO role (name) VALUES ('ROLE_USER');
INSERT INTO role (name) VALUES ('ROLE_ADMIN');

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2);