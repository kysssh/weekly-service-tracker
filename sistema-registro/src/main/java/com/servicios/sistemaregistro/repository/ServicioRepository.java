package com.servicios.sistemaregistro.repository;

import com.servicios.sistemaregistro.model.Servicio;
import com.servicios.sistemaregistro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Define las opearciones de acceso a datos para la entidad Servicio
 * y permite comunicarse con la base de datos PostgreSQL
 * Extendemos ServicioRepository con JpaRepository para obtener 4 operaciones
 */

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    /**
     * Busca todos los servicios pertenecientes a un usuario específico
     * Recibe el objeto Usuario completo porque la entidad Servicio
     * tiene una relación @ManyToOne con Usuario
     */
    List<Servicio> findByUsuario (Usuario usuario);

    /**
     * Elimina todos los registros cuyo created_at sea anterior a la fecha dada
     * Se usa para la limpieza automática de registros con más de 30 días
     * Usa LocalDateTime por su mejor integración con JPA y PostgreSQL
     */
    void deleteByCreatedAtBefore (LocalDateTime tiempo);
}
