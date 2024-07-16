package com.desafio.forohub.domain.topico;

import com.desafio.forohub.domain.autor.DatosAutor;
import com.desafio.forohub.domain.curso.DatosCurso;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroTopico(
        @NotBlank
        String titulo,
        @NotBlank
        String mensaje,
        @NotNull
        @Valid
        DatosAutor autor,
        @NotNull
        @Valid
        DatosCurso curso
) {
}
