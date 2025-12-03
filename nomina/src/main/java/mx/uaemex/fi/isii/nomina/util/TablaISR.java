package mx.uaemex.fi.isii.nomina.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for defining the values of the table according to the ones on the SAT's specifications.
 */
public class TablaISR {

    private static final TablaISR tabla = new TablaISR();
    private final List<Rango> rangos;

    /**
     * Constructor. Builds the table according to a list of Ranges
     * @see Rango
     */
    private TablaISR() {
        rangos = new ArrayList<>();

        rangos.add(new Rango(0.0, 8364.0, 0));
        rangos.add(new Rango(8364.01, 8952.49, 1.92));
        rangos.add(new Rango(8952.50, 75984.55, 6.40));
        rangos.add(new Rango(75984.56, 133536.07, 10.88));
        rangos.add(new Rango(133536.08, 155229.80, 16.00));
        rangos.add(new Rango(155229.81, 185852.57, 17.92));
        rangos.add(new Rango(185852.58, 374837.88, 21.36));
        rangos.add(new Rango(374837.89, 590795.99, 23.52));
        rangos.add(new Rango(590796.00, 1127926.84, 30.00));
        rangos.add(new Rango(1127926.85, 1503902.46, 32.00));
        rangos.add(new Rango(1503902.47, 4511707.37, 34.00));
        rangos.add(new Rango(4511707.38, Double.MAX_VALUE, 35.00));

    }

    /**
     * For looking the nomina's range on the table
     * @param nomina double nomina value
     * @return If the param is in range, returns a {@code rango} if not, returns {@code null}
     */
    public Rango buscarRango(double nomina) {
        for (Rango rango : rangos) {
            if (rango.estaEnRango(nomina)) {
                return rango;
            }
        }
        return null;
    }

    public static TablaISR getTabla() {
        return tabla;
    }
}
