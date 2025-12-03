package mx.uaemex.fi.isii.nomina.repository;

import mx.uaemex.fi.isii.nomina.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

    Optional<Usuario> findByPasswordAndEmpleadoCorreo(String password, String correo);

}
