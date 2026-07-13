package com.servicios.sistemaregistro.scheduler;

import com.servicios.sistemaregistro.repository.ServicioRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// @Component: le decimos a Spring que gestione esta clase como bean,
// igual que hicimos con JwtAuthenticationFilter — no es lógica de negocio central,
// es una tarea de mantenimiento del sistema.
@Component
public class ServicioLimpiezaScheduler {

    private final ServicioRepository servicioRepository;

    // Inyección por constructor, mismo patrón que ya usas en todos tus Services.
    public ServicioLimpiezaScheduler(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    // @Scheduled: le dice a Spring "ejecuta este método automáticamente,
    // según la programación que le indique".
    // cron = "0 0 3 * * *" significa: segundo 0, minuto 0, hora 3 → todos los días a las 3:00 AM.
    // Elegimos una hora de madrugada porque es cuando menos usuarios activos hay,
    // minimizando el impacto de esta operación de borrado masivo.
    @Scheduled(cron = "0 0 3 * * *")
    public void eliminarServiciosAntiguos() {
        // Calculamos la fecha límite: todo lo creado ANTES de hace 30 días se borra.
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(30);

        // Usamos el método que ya tenías declarado en el repository.
        servicioRepository.deleteByCreatedAtBefore(fechaLimite);
    }
}