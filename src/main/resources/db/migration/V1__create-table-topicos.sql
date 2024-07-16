create table topicos(

    id bigint not null auto_increment,
    titulo varchar(100) not null,
    mensaje varchar(300) not null unique,
    fecha datetime not null,
    status tinyint default 1,

    primary key(id)
);