create table autores(

    id bigint not null auto_increment,
    nombre varchar(100) not null,
    email varchar(300) not null unique,
    contrasena datetime not null,

    primary key(id)
);