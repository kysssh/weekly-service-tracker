package com.servicios.sistemaregistro.repository;

import com.servicios.sistemaregistro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio de Usuario
 * Define las operaciones de acceso a datos para la entidad Usuario
 * y permite la comunicación con la base de datos PostgreSQL
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Al extender JpaRepository heredamos las operaciones CRUD principales:
    // Create (save), Read (findById, findAll), Update (save), Delete (delete)


    /**
     * Busca un usuario por su nombre de usuario
     * Retorna Optional porque el usuario puede no existir en la base de datos
     * Sin Optional, recibiríamos null y un uso descuidado causaría un NullPointerException
     * (Se crashearia la aplicacion)
     */
    Optional<Usuario> findByNombreUsuario (String nombre);
}
