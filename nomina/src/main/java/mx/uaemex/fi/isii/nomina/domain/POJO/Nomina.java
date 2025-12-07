package mx.uaemex.fi.isii.nomina.domain.POJO;

public class Nomina {

    private double montoNomina;
    private double deducciones;
    private double nominaNeto;
    private double isr;

    public Nomina(double montoNomina, double deducciones, double isr, double nominaNeto) {
        this.montoNomina = montoNomina;
        this.deducciones = deducciones;
        this.nominaNeto = nominaNeto;
        this.isr = isr;
    }

    public double getDeducciones() {
        return deducciones;
    }

    public double getNominaNeto() {
        return nominaNeto;
    }

    public double getMontoNomina() {
        return montoNomina;
    }

    public double getIsr() {
        return isr;
    }

}
