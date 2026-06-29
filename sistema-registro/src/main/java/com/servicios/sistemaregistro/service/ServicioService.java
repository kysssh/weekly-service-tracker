package com.servicios.sistemaregistro.service;

import com.servicios.sistemaregistro.dto.ResumenDTO;
import com.servicios.sistemaregistro.dto.ServicioDTO;
import com.servicios.sistemaregistro.exception.ValidacionException;
import com.servicios.sistemaregistro.model.Servicio;
import com.servicios.sistemaregistro.model.Usuario;
import com.servicios.sistemaregistro.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    //Esperamos que recba el usuario desde el controller para que se le de la lista de sus registro de servicio
    public List<Servicio> obtenerSemanaActual(Usuario usuario) {
        LocalDate hoy = LocalDate.now();
        //DayOfWeek es un enum que representa los dias de la semana
        //Usamos minusDays para restar una cierta cantidad de dias (los de hoy) - 1

        LocalDate lunes = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1);

        //le sumamos 6 al dia lunes (es nuestro dia domingo)
        LocalDate domingo = lunes.plusDays(6);

        // Accedemos a el metodo de ServicioRepository para encontrar los servicios entre
        // el lunes y domingo

        List<Servicio> servicios = servicioRepository.findByUsuarioAndFechaServicioBetween(usuario, lunes, domingo);
         return servicios;
    }

    /** Metodo para obtener el historial del usuario, pasamos usuario por argumento */
    public List<Servicio> obtenerHistorial(Usuario usuario) {
        //Definimos el dia de hoy
        LocalDate hoy = LocalDate.now();
        //Definimos el dia lunes de semana actual
        LocalDate lunes = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1);
        //Pues el fin del historial sera lunes-1
        LocalDate finHistorial = lunes.minusDays(1);
        //El inicio del historial sera 30 dias menos apartir de hoy
        LocalDate inicioHistorial = hoy.minusDays(30);
        List<Servicio> servicios = servicioRepository.findByUsuarioAndFechaServicioBetween(usuario, inicioHistorial, finHistorial);
        return servicios;
    }

    /**creamos nuestro metodo para guardar la suma de montos y retorna un dto para las 3 variables */
    public ResumenDTO calcularResumen(List<Servicio> servicios) {
        //Inicializamos en 0 nuestras 3 variables
        BigDecimal totalCorporativo = BigDecimal.ZERO;
        BigDecimal totalB4 = BigDecimal.ZERO;
        BigDecimal peajesKusi = BigDecimal.ZERO;

        //Recorremos la lista
        for(Servicio servicio : servicios) {
            //Si el codigo del servicio contiene id, sumamos
            if(servicio.getCodigoServicio().contains("id")) {
                totalCorporativo = totalCorporativo.add(servicio.getMontoServicio());
            }
            //Si el codigo del servicio contiene unicamente numeros, sumamos
            if(servicio.getCodigoServicio().matches("[0-9]+")) {
                totalB4 = totalB4.add(servicio.getMontoServicio());
            }
            //Si el tipo de servicio es Kusi y Peaje es true, sumamos el monto del peaje de Kusi
            if("Kusi".equals(servicio.getTipoServicio()) && servicio.getPeaje()) {
                peajesKusi = peajesKusi.add(servicio.getMontoPeaje());
            }
        }
        //Creamos nuestro objeto resumen de ResumenDTO
        ResumenDTO resumen = new ResumenDTO();
        //Guardamos los valores
        resumen.setTotalCorporativo(totalCorporativo);
        resumen.setTotalB4(totalB4);
        resumen.setPeajesKusi(peajesKusi);
        return resumen;
    }
}
