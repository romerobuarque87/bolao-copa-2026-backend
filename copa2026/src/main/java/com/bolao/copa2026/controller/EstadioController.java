package com.bolao.copa2026.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolao.copa2026.model.Estadio;
import com.bolao.copa2026.service.EstadioService;

@RestController
@RequestMapping("/estadios")
@PreAuthorize("hasRole('ADMIN')")
public class EstadioController {

    private final EstadioService estadioService;

    public EstadioController(EstadioService estadioService) {
        this.estadioService = estadioService;
    }

    @PostMapping
    public ResponseEntity<Estadio> criar(@RequestBody Estadio estadio) {

        Estadio estadioCriado = estadioService.criar(estadio);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(estadioCriado);
    }

    @GetMapping
    public ResponseEntity<List<Estadio>> listarTodos() {
        return ResponseEntity.ok(estadioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estadio> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(estadioService.buscarPorId(id));
    }
}