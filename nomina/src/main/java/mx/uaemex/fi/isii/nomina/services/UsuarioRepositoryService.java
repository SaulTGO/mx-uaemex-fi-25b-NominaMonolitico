package mx.uaemex.fi.isii.nomina.services;

import mx.uaemex.fi.isii.nomina.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

/**
 * Service. CRUD transactions for the {@code repository Usuario}
 * @see mx.uaemex.fi.isii.nomina.repository.UsuarioRepository
 */

@Service
public class UsuarioRepositoryService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioRepositoryService(UsuarioRepository userRepository) {
        this.usuarioRepository = userRepository;
    }

    /**
     * Check if the user exist on the DB, using the params
     * @param correo user's correo
     * @param password user's password
     * @return {@code true} if the user exists, returns {@code false} if not
     */

    public boolean validateLogin(String correo, String password){
        return usuarioRepository.findByPasswordAndEmpleadoCorreo(password, correo).isPresent();
    }

}
