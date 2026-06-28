package com.servicios.sistemaregistro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** La clase es un archivo de configuracion que contiene metodos para crear objetos,
 * es la fabrica*/
@Configuration
public class SecurityConfig {

    /** Ejecuta el metodo, agarra el valor que devuelva, y guardalo para cuando se necesite*/
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
