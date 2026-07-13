package com.servicios.sistemaregistro.service;

import com.servicios.sistemaregistro.model.Usuario;
import com.servicios.sistemaregistro.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.servicios.sistemaregistro.exception.UsuarioYaExisteException;

import java.util.Optional;

/**Le decimos a Spring que esta clase almacenara la logica del negocip
 * Encargate de gestionarla*/
@Service
public class UsuarioService {
    /**Asiganmos la variable una unica vez (es inmutable) */
    private final UsuarioRepository usuarioRepository;

    // 2️⃣ tipo del campo cambiado a la interfaz PasswordEncoder
    private final PasswordEncoder passwordEncoder;

    /** Creamos un cosntructor, le asignamos a la variable usuarioRepository el valor
     * que se pasa por el constructor
     */
    // 3️⃣ tipo del parámetro cambiado a PasswordEncoder — esto es lo que Spring usa
    // para encontrar el bean correcto al inyectar (matchea con el @Bean de SecurityConfig)
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validarAutenticacion(String nombreUsuario, String pin) {
        Optional<Usuario> usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if (usuario.isEmpty()) {
            return false;
        }
        Usuario usuarioEncontrado = usuario.get();
        // El método .matches() existe igual en la interfaz PasswordEncoder,
        // por eso esta línea no necesitó ningún cambio.
        return passwordEncoder.matches(pin, usuarioEncontrado.getPinHash());
    }

    public void registrarUsuario(String nombreUsuario, String pin) {
        Optional<Usuario> usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if(usuario.isPresent()) {
            throw new UsuarioYaExisteException("El nombre de usuario ya está en uso.");
        }
        //El pin creado por el usuario lo hasheamos
        // .encode() también existe en la interfaz, así que tampoco cambió nada aquí.
        String pinHasheado = passwordEncoder.encode(pin);
        //Creamos un nuevo objeto Usuario
        Usuario newUsuario = new Usuario();
        //Le asignamos el nombre nombreUsuario
        newUsuario.setNombreUsuario(nombreUsuario);
        //Le asignamos a pinHash al nuevo usuario
        newUsuario.setPinHash(pinHasheado);
        //Guardamos el nuevo usuario NewUsuario
        usuarioRepository.save(newUsuario);
    }

}