ALTER TABLE topicos
ADD COLUMN autor_id BIGINT NOT NULL;

ALTER TABLE topicos
ADD CONSTRAINT fk_topicos_autores_id
FOREIGN KEY (autor_id) REFERENCES autores(id);

ALTER TABLE topicos
ADD COLUMN curso_id BIGINT NOT NULL;

ALTER TABLE topicos
ADD CONSTRAINT fk_topicos_cursos_id
FOREIGN KEY (curso_id) REFERENCES cursos(id);