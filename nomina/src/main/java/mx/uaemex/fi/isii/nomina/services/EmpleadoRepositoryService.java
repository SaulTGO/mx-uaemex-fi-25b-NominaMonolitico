package mx.uaemex.fi.isii.nomina.services;

import mx.uaemex.fi.isii.nomina.domain.entity.Empleado;
import mx.uaemex.fi.isii.nomina.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service. CRUD transactions for the {@code Repository Empleado}
 * @see mx.uaemex.fi.isii.nomina.repository.EmpleadoRepository
 */

@Service
public class EmpleadoRepositoryService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoRepositoryService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    /**
     *  For getting a list of all the employees
     * @return List of all Empleado in the BD
     */
    public List<Empleado> findAllEmpleados() {
        return empleadoRepository.findAll();
    }

}
