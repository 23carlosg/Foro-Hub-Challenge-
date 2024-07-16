package com.desafio.forohub.domain.topico;

import com.desafio.forohub.domain.autor.DatosAutor;
import com.desafio.forohub.domain.curso.DatosCurso;

import java.time.LocalDateTime;

public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fecha,
        DatosAutor autor,
        DatosCurso curso
) {}
