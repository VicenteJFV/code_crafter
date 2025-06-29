package com.perfulandia.service.Controller;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.service.Auth.controller.AuthController;
import com.perfulandia.service.inventory.controller.ProductoController;
import com.perfulandia.service.dto.RecursoDTO;
import com.perfulandia.service.user.Controller.UsuarioController;

import io.swagger.v3.oas.annotations.Operation;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;

@RestController
@RequestMapping("/api")
public class ApiRootController {

        @GetMapping
        @Operation(summary = "Root API", description = "Muestra los endpoints disponibles con HATEOAS")
        public ResponseEntity<CollectionModel<EntityModel<RecursoDTO>>> root() {
                List<EntityModel<RecursoDTO>> enlaces = List.of(
                                EntityModel.of(new RecursoDTO("Usuarios"),
                                                linkTo(methodOn(UsuarioController.class).obtenerUsuarios())
                                                                .withRel("usuarios")),
                                EntityModel.of(new RecursoDTO("Productos"),
                                                linkTo(methodOn(ProductoController.class).listarProductos())
                                                                .withRel("productos")),
                                EntityModel.of(new RecursoDTO("Login"),
                                                linkTo(methodOn(AuthController.class).login(null)).withRel("login")),
                                EntityModel.of(new RecursoDTO("Registro"),
                                                linkTo(methodOn(AuthController.class).registrarUsuario(null))
                                                                .withRel("register")));

                CollectionModel<EntityModel<RecursoDTO>> modelo = CollectionModel.of(enlaces,
                                linkTo(methodOn(ApiRootController.class).root()).withSelfRel());

                return ResponseEntity.ok(modelo);
        }

}
