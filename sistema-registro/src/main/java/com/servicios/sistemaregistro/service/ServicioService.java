package com.servicios.sistemaregistro.service;

import com.servicios.sistemaregistro.dto.ServicioDTO;
import com.servicios.sistemaregistro.exception.ValidacionException;
import com.servicios.sistemaregistro.model.Servicio;
import com.servicios.sistemaregistro.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ServicioService {
    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public Servicio registrarServicio(ServicioDTO dto) {
        //Validamos que el codigo del servicio no sea vacio
        if(dto.getCodigo()==null || dto.getCodigo().isBlank()) {
            throw new ValidacionException("El codigo de servicio es obligatorio.");
        }

        //Limpiamos la llegada del codigo del servicio
        String codigoLimpio = dto.getCodigo()
                .toLowerCase()
                .replace(" ", "")
                .trim();
        //Validamos que distrito no sea vacio
        if(dto.getDistrito() == null || dto.getDistrito().isBlank()) {
            throw new ValidacionException("El distrito es obligatorio.");
        }
        //Validamos que el tipo de servicio no sea vacio
        if(dto.getTipoServicio() == null || dto.getTipoServicio().isBlank()) {
            throw new ValidacionException("El tipo de servicio es obligatorio.");
        }
        //Validamos que peaje sea true o false
        if(dto.getPeaje() == null) {
            throw new ValidacionException("Debe indicar si hay peaje o no");
        }
        //Validamos que monto peaje no sea vacio y sea estrictamente mayor a 0
        if(dto.getPeaje() && (dto.getMontoPeaje() == null ||
                dto.getMontoPeaje().compareTo(BigDecimal.ZERO)<=0)) {
            throw new ValidacionException("El monto del peaje debe ser mayor a 0.");
        }
        //Validamos que monto servicio no sea vacio y sea estrictamente mayor a 0
        if(dto.getMontoServicio() == null ||
                dto.getMontoServicio().compareTo(BigDecimal.ZERO)<=0) {
            throw new ValidacionException("El monto del servicio debe ser mayor a 0");
        }
        //Creamos nuestro maximo y minimo de registro de fechas
        LocalDate hoy = LocalDate.now();
        LocalDate minPermitido = hoy.minusWeeks(1);
        //Validamos que el registro de la fecha no este vacia
        if(dto.getFechaServicio() == null) {
            throw new ValidacionException("La fecha del servicio es obligatoria.");
        }
        //Validamos que el registro de los servicios sea como maximo hoy
        if(dto.getFechaServicio().isAfter(hoy)) {
            throw new ValidacionException("la fecha no puede ser futura");
        }
        //Validamos que el registro de los servicios sea como minimo una semana anterior
        if(dto.getFechaServicio().isBefore(minPermitido)) {
            throw new ValidacionException("La fecha no puede ser mayor a 1 semana atras");
        }
        //Creamos el objeto servicio, se guardara en la base de datos
        //Guardamos los campos
        Servicio servicio = new Servicio();
        servicio.setCodigoServicio(codigoLimpio);
        servicio.setDistrito(dto.getDistrito());
        servicio.setTipoServicio(dto.getTipoServicio());
        servicio.setPeaje(dto.getPeaje());
        servicio.setMontoServicio(dto.getMontoServicio());
        servicio.setFechaServicio(dto.getFechaServicio());
        //Logica del peaje, guardamos el monto del peaje si peaje es true
        if(dto.getPeaje()) {
            servicio.setMontoPeaje(dto.getMontoPeaje());
        } else {
            servicio.setMontoPeaje(BigDecimal.ZERO);
        }
        //Guardamos el objeto y nos devuelve el objeto con su ID generado
        return servicioRepository.save(servicio);
    }
}
