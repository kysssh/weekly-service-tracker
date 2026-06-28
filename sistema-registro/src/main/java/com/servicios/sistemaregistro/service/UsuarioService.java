package com.servicios.sistemaregistro.service;

import com.servicios.sistemaregistro.model.Usuario;
import com.servicios.sistemaregistro.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.servicios.sistemaregistro.exception.UsuarioYaExisteException;

import java.util.Optional;

/**Le decimos a Spring que esta clase almacenara la logica del negocip
 * Encargate de gestionarla*/
@Service
public class UsuarioService {
    /**Asiganmos la variable una unica vez (es inmutable) */
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /** Creamos un cosntructor, le asignamos a la variable usuarioRepository el valor
     * que se pasa por el constructor
     */
    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public boolean validarAutenticacion(String nombreUsuario, String pin) {
        Optional<Usuario> usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if (usuario.isEmpty()) {
            return false;
        }
        Usuario usuarioEncontrado = usuario.get();
        return bCryptPasswordEncoder.matches(pin, usuarioEncontrado.getPinHash());
    }
    public void registrarUsuario(String nombreUsuario, String pin) {
        Optional<Usuario> usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if(usuario.isPresent()) {
            throw new UsuarioYaExisteException("El nombre de usuario ya está en uso.");
        }
        //El pin creado por el usuario lo hasheamos
        String pinHasheado = bCryptPasswordEncoder.encode(pin);
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
