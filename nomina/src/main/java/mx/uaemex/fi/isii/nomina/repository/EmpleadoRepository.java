package mx.uaemex.fi.isii.nomina.repository;

import mx.uaemex.fi.isii.nomina.domain.entity.Empleado;
import org.hibernate.event.internal.EmptyEventManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    List<Empleado> findAll();
}
