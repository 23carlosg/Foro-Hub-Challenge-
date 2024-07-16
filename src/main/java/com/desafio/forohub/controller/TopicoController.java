package com.desafio.forohub.controller;

import com.desafio.forohub.domain.autor.Autor;
import com.desafio.forohub.domain.autor.AutorRepository;
import com.desafio.forohub.domain.autor.DatosAutor;
import com.desafio.forohub.domain.curso.Curso;
import com.desafio.forohub.domain.curso.CursoRepository;
import com.desafio.forohub.domain.curso.DatosCurso;
import com.desafio.forohub.domain.topico.DatosRegistroTopico;
import com.desafio.forohub.domain.topico.DatosRespuestaTopico;
import com.desafio.forohub.domain.topico.Topico;
import com.desafio.forohub.domain.topico.TopicoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datos) {
        if (datos.titulo() == null || datos.mensaje() == null || datos.autor() == null || datos.curso() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            return ResponseEntity.badRequest().build();
        }

        Autor autor = autorRepository.findByEmail(datos.autor().email())
                .orElseGet(() -> {
                    Autor nuevoAutor = new Autor(datos.autor());
                    nuevoAutor.setContrasena(passwordEncoder.encode(datos.autor().contrasena()));
                    return autorRepository.save(nuevoAutor);
                });

        Curso curso = cursoRepository.findByNombreAndCategoria(datos.curso().nombre(), datos.curso().categoria())
                .orElseGet(() -> cursoRepository.save(new Curso(datos.curso())));

        Topico topico = new Topico(datos, autor, curso);
        topicoRepository.save(topico);

        return ResponseEntity.ok(new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFecha(),
                new DatosAutor(autor.getNombre(), autor.getEmail(), null),
                new DatosCurso(curso.getNombre(), curso.getCategoria())
        ));
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaTopico>> listarTopicos(@PageableDefault(size = 10) Pageable paginacion) {
        Page<Topico> topicos = topicoRepository.findAll(paginacion);

        Page<DatosRespuestaTopico> respuesta = topicos.map(topico -> new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFecha(),
                new DatosAutor(topico.getAutor().getNombre(), topico.getAutor().getEmail(), null),
                new DatosCurso(topico.getCurso().getNombre(), topico.getCurso().getCategoria())
        ));

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> detalleTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.findById(id).orElse(null);
        if (topico == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFecha(),
                new DatosAutor(topico.getAutor().getNombre(), topico.getAutor().getEmail(), null),
                new DatosCurso(topico.getCurso().getNombre(), topico.getCurso().getCategoria())
        ));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(@PathVariable Long id, @RequestBody @Valid DatosRegistroTopico datos) {
        Topico topico = topicoRepository.findById(id).orElse(null);
        if (topico == null) {
            return ResponseEntity.notFound().build();
        }

        if (datos.titulo() == null || datos.mensaje() == null || datos.autor() == null || datos.curso() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (topicoRepository.existsByTituloAndMensajeAndIdNot(datos.titulo(), datos.mensaje(), id)) {
            return ResponseEntity.badRequest().build();
        }

        topico.setTitulo(datos.titulo());
        topico.setMensaje(datos.mensaje());

        Autor autor = autorRepository.findByEmail(datos.autor().email())
                .orElseGet(() -> {
                    Autor nuevoAutor = new Autor(datos.autor());
                    nuevoAutor.setContrasena(passwordEncoder.encode(datos.autor().contrasena()));
                    return autorRepository.save(nuevoAutor);
                });
        topico.setAutor(autor);

        Curso curso = cursoRepository.findByNombreAndCategoria(datos.curso().nombre(), datos.curso().categoria())
                .orElseGet(() -> cursoRepository.save(new Curso(datos.curso())));
        topico.setCurso(curso);

        topicoRepository.save(topico);

        return ResponseEntity.ok(new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFecha(),
                new DatosAutor(autor.getNombre(), autor.getEmail(), null),
                new DatosCurso(curso.getNombre(), curso.getCategoria())
        ));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarTopico(@PathVariable Long id) {
        if (!topicoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.ok().body("El topico fue borrado satisfactoriamente");
    }
}
