INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES('selene', '12345', 1, 'Selene', 'Vasquez', 'svasquez@gmail.com');
INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES('joan', '12345', 2, 'Joan', 'Vasquez', 'jvasquez@gmail.com');

INSERT INTO `roles` (nombre) VALUES ('ROLE_USER');
INSERT INTO `roles` (nombre) VALUES ('ROLE_ADMIN');


INSERT INTO `usuarios_roles` (usuario_id, role_id) values (1,1);
INSERT INTO `usuarios_roles` (usuario_id, role_id) values (2,2);
INSERT INTO `usuarios_roles` (usuario_id, role_id) values (2,1);