package mx.uaemex.fi.isii.nomina.services;

import mx.uaemex.fi.isii.nomina.domain.POJO.Nomina;
import mx.uaemex.fi.isii.nomina.util.Rango;
import mx.uaemex.fi.isii.nomina.util.TablaISR;
import org.springframework.stereotype.Service;

/**
 * Service. To calculate the Nomina values using the montoNomina
 * @see Nomina
 */
@Service
public class CalcularNominaService {

    private TablaISR tablaISR;

    /**
     * Set the tablaISR to further operations
     * @see TablaISR
     */
    public CalcularNominaService() {
        tablaISR = TablaISR.getTabla();
    }

    /**
     * Calculate the Nomina values
      * @param montoNomina double
     * @return in an object of the type {@code Nomina} the values calculated
     * @see Nomina
     * @see TablaISR
     */
    public Nomina calcularNomina(double montoNomina) {

        Rango rango = tablaISR.buscarRango(montoNomina);
        double isr = rango.getIsr();
        double deducciones = montoNomina * (isr*0.01);

        return new Nomina(montoNomina, deducciones, isr, montoNomina-deducciones);
    }
}
