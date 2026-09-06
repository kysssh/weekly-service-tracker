package com.servicios.sistemaregistro.service;

import com.servicios.sistemaregistro.dto.ResumenDTO;
import com.servicios.sistemaregistro.dto.ServicioDTO;
import com.servicios.sistemaregistro.exception.ServicioNoExisteException;
import com.servicios.sistemaregistro.exception.ValidacionException;
import com.servicios.sistemaregistro.model.Servicio;
import com.servicios.sistemaregistro.model.Usuario;
import com.servicios.sistemaregistro.repository.ServicioRepository;
import com.servicios.sistemaregistro.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {
    private final ServicioRepository servicioRepository;

    private final UsuarioRepository usuarioRepository;

    public ServicioService(ServicioRepository servicioRepository, UsuarioRepository usuarioRepository) {
        this.servicioRepository = servicioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Servicio registrarServicio(ServicioDTO dto, String nombreUsuario) {
        //Validamos que el codigo del servicio no sea vacio
        if (dto.getCodigo() == null || dto.getCodigo().isBlank()) {
            throw new ValidacionException("El codigo de servicio es obligatorio.");
        }

        //Limpiamos la llegada del codigo del servicio
        String codigoLimpio = dto.getCodigo()
                .toLowerCase()
                .replace(" ", "")
                .trim();
        //Validamos que distrito no sea vacio
        if (dto.getDistrito() == null || dto.getDistrito().isBlank()) {
            throw new ValidacionException("El distrito es obligatorio.");
        }
        //Validamos que el tipo de servicio no sea vacio
        if (dto.getTipoServicio() == null || dto.getTipoServicio().isBlank()) {
            throw new ValidacionException("El tipo de servicio es obligatorio.");
        }
        //Validamos que peaje sea true o false
        if (dto.getPeaje() == null) {
            throw new ValidacionException("Debe indicar si hay peaje o no");
        }
        //Validamos que monto peaje no sea vacio y sea estrictamente mayor a 0
        if (dto.getPeaje() && (dto.getMontoPeaje() == null ||
                dto.getMontoPeaje().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new ValidacionException("El monto del peaje debe ser mayor a 0.");
        }
        //Validamos que monto servicio no sea vacio y sea estrictamente mayor a 0
        if (dto.getMontoServicio() == null ||
                dto.getMontoServicio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacionException("El monto del servicio debe ser mayor a 0");
        }
        //Creamos nuestro maximo y minimo de registro de fechas
        LocalDate hoy = LocalDate.now();
        LocalDate minPermitido = hoy.minusWeeks(1);
        //Validamos que el registro de la fecha no este vacia
        if (dto.getFechaServicio() == null) {
            throw new ValidacionException("La fecha del servicio es obligatoria.");
        }
        //Validamos que el registro de los servicios sea como maximo hoy
        if (dto.getFechaServicio().isAfter(hoy)) {
            throw new ValidacionException("la fecha no puede ser futura");
        }
        //Validamos que el registro de los servicios sea como minimo una semana anterior
        if (dto.getFechaServicio().isBefore(minPermitido)) {
            throw new ValidacionException("La fecha no puede ser mayor a 1 semana atras");
        }

        // NUEVO: buscamos el objeto Usuario completo a partir del nombreUsuario
        // que viene del JWT (vía SecurityContextHolder en el controller).
        // Esto es lo que faltaba: sin esto, servicio.setUsuario() nunca se llamaba,
        // y la columna id_usuario llegaba null a la base de datos.
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new ServicioNoExisteException("Usuario no encontrado."));

        //Creamos el objeto servicio, se guardara en la base de datos
        //Guardamos los campos
        Servicio servicio = new Servicio();
        servicio.setCodigoServicio(codigoLimpio);
        servicio.setDistrito(dto.getDistrito());
        servicio.setTipoServicio(dto.getTipoServicio());
        servicio.setPeaje(dto.getPeaje());
        servicio.setMontoServicio(dto.getMontoServicio());
        servicio.setFechaServicio(dto.getFechaServicio());
        servicio.setUsuario(usuario); // NUEVO: asignamos el dueño del servicio antes de guardar

        //Logica del peaje, guardamos el monto del peaje si peaje es true
        if (dto.getPeaje()) {
            servicio.setMontoPeaje(dto.getMontoPeaje());
        } else {
            servicio.setMontoPeaje(BigDecimal.ZERO);
        }
        //Guardamos el objeto y nos devuelve el objeto con su ID generado
        return servicioRepository.save(servicio);
    }

    //Esperamos que recba el usuario desde el controller para que se le de la lista de sus registro de servicio
    public List<Servicio> obtenerSemanaActual(String nombreUsuario) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new ServicioNoExisteException("Usuario no encontrado."));

        LocalDate hoy = LocalDate.now();
        LocalDate lunes = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1);
        LocalDate domingo = lunes.plusDays(6);

        List<Servicio> servicios = servicioRepository.findByUsuarioAndFechaServicioBetween(usuario, lunes, domingo);
        return servicios;
    }

    /**
     * Metodo para obtener el historial del usuario, pasamos usuario por argumento
     */
    public List<Servicio> obtenerHistorial(String nombreUsuario) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new ServicioNoExisteException("Usuario no encontrado."));

        LocalDate hoy = LocalDate.now();
        LocalDate lunes = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1);
        LocalDate finHistorial = lunes.minusDays(1);
        LocalDate inicioHistorial = hoy.minusDays(30);
        List<Servicio> servicios = servicioRepository.findByUsuarioAndFechaServicioBetween(usuario, inicioHistorial, finHistorial);
        return servicios;
    }

    // Porcentaje que se descuenta de la base para obtener la ganancia neta.
    private static final BigDecimal DESCUENTO_GANANCIA = new BigDecimal("0.25");

    /**
     * Calcula los sub-totales por origen y la ganancia de la semana.
     *
     * La ganancia se rige por:
     *   base     = suma de montoServicio de los servicios que son Kusi, CMV
     *              o cuyo codigo contiene "id"
     *   ganancia = base - 25% de la base
     * Cada servicio que califica se cuenta una sola vez, aunque cumpla mas de
     * una condicion (por ejemplo un CMV con codigo "ID-...").
     */
    public ResumenDTO calcularResumen(List<Servicio> servicios) {
        BigDecimal totalCorporativo = BigDecimal.ZERO;
        BigDecimal totalB4 = BigDecimal.ZERO;
        BigDecimal peajesKusi = BigDecimal.ZERO;
        BigDecimal baseGanancia = BigDecimal.ZERO;

        for (Servicio servicio : servicios) {
            String codigo = servicio.getCodigoServicio();
            String tipo = servicio.getTipoServicio();

            if (codigo.contains("id")) {
                totalCorporativo = totalCorporativo.add(servicio.getMontoServicio());
            }
            if (codigo.matches("[0-9]+")) {
                totalB4 = totalB4.add(servicio.getMontoServicio());
            }
            if ("Kusi".equals(tipo) && servicio.getPeaje()) {
                peajesKusi = peajesKusi.add(servicio.getMontoPeaje());
            }

            boolean cuentaParaGanancia =
                    "Kusi".equals(tipo) || "CMV".equals(tipo) || codigo.contains("id");
            if (cuentaParaGanancia) {
                baseGanancia = baseGanancia.add(servicio.getMontoServicio());
            }
        }

        BigDecimal totalBruto = baseGanancia.setScale(2, RoundingMode.HALF_UP);
        BigDecimal ganancia = baseGanancia
                .subtract(baseGanancia.multiply(DESCUENTO_GANANCIA))
                .setScale(2, RoundingMode.HALF_UP);

        ResumenDTO resumen = new ResumenDTO();
        resumen.setTotalCorporativo(totalCorporativo);
        resumen.setTotalB4(totalB4);
        resumen.setPeajesKusi(peajesKusi);
        resumen.setTotalBruto(totalBruto);
        resumen.setGanancia(ganancia);
        return resumen;
    }

    // Método: obtiene los servicios de la semana actual
    // y calcula el resumen sobre ellos, en un solo paso.
    public ResumenDTO obtenerResumenSemanaActual(String nombreUsuario) {
        List<Servicio> serviciosSemana = obtenerSemanaActual(nombreUsuario);
        return calcularResumen(serviciosSemana);
    }

    public void eliminarServicio(Long id, String nombreUsuario) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ServicioNoExisteException("El servicio no existe."));

        // Verificamos que el servicio encontrado pertenezca al usuario autenticado.
        // Si no coincide, respondemos EXACTAMENTE igual que si no existiera —
        // así no revelamos a un atacante que el id sí existe, solo que no es suyo.
        if (!servicio.getUsuario().getNombreUsuario().equals(nombreUsuario)) {
            throw new ServicioNoExisteException("El servicio no existe.");
        }

        servicioRepository.deleteById(id);
    }


    public Servicio editarServicio(Long id, ServicioDTO servicioDto, String nombreUsuario) {
        Optional<Servicio> servicio = servicioRepository.findById(id);
        if (servicio.isEmpty()) {
            throw new ServicioNoExisteException("El servicio no existe.");
        }

        Servicio servicioExiste = servicio.get();

        // Misma verificación de propiedad antes de permitir la edición.
        if (!servicioExiste.getUsuario().getNombreUsuario().equals(nombreUsuario)) {
            throw new ServicioNoExisteException("El servicio no existe.");
        }

        servicioExiste.setFechaServicio(servicioDto.getFechaServicio());
        servicioExiste.setCodigoServicio(servicioDto.getCodigo());
        servicioExiste.setTipoServicio(servicioDto.getTipoServicio());
        servicioExiste.setMontoServicio(servicioDto.getMontoServicio());
        servicioExiste.setPeaje(servicioDto.getPeaje());
        servicioExiste.setDistrito(servicioDto.getDistrito());
        servicioExiste.setMontoPeaje(servicioDto.getMontoPeaje());
        servicioRepository.save(servicioExiste);
        return servicioExiste;
    }
}