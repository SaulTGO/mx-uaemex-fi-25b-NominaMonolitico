package mx.uaemex.fi.isii.nomina.controller;

import mx.uaemex.fi.isii.nomina.domain.POJO.Nomina;
import mx.uaemex.fi.isii.nomina.domain.entity.Empleado;
import mx.uaemex.fi.isii.nomina.services.CalcularNominaService;
import mx.uaemex.fi.isii.nomina.services.EmpleadoRepositoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CalcularNominaController {

    private final CalcularNominaService calcularNominaService;
    private final EmpleadoRepositoryService empleadoRepositoryService;

    public CalcularNominaController(CalcularNominaService calcularNominaService,  EmpleadoRepositoryService empleadoRepositoryService) {
        this.calcularNominaService = calcularNominaService;
        this.empleadoRepositoryService = empleadoRepositoryService;
    }

    @GetMapping("/calcular-nomina")
    public String calcularNomina(
            @CookieValue(value = "session", required = false) String value,
            @RequestParam(value = "empleadoNombre", required = false) String nombre,
            @RequestParam(value = "empleadoApellidos", required = false) String apellidos,
            @RequestParam(value = "empleadoRFC", required = false) String rfc,
            @RequestParam(value = "empleadoCorreo", required = false) String correo,
            @RequestParam(value = "montoNomina", required = false) Double montoNomina,
            @RequestParam(value = "isr", required = false) Double isr,
            @RequestParam(value = "deducciones", required = false) Double deducciones,
            @RequestParam(value = "nominaNeto", required = false) Double nominaNeto,
            Model model

    ){
        List<Empleado> empleados = new ArrayList<Empleado>();
        empleados = empleadoRepositoryService.findAllEmpleados();

        model.addAttribute("empleados", empleados);

        return "calcular-nomina";
    }

    @PostMapping("/calcular-nomina")
    public String calcularNomina(
            @CookieValue(value = "session", required = false) String value,
            @RequestParam("empleadoNombre") String nombre,
            @RequestParam("empleadoApellidos") String apellidos,
            @RequestParam("empleadoRFC") String rfc,
            @RequestParam("empleadoCorreo") String correo,
            @RequestParam("montoNomina") Double montoNomina,
            Model model
    ) {
        // Calcular la nómina
        try {
            Nomina nominaDatos = calcularNominaService.calcularNomina(montoNomina);

            List<Empleado> empleados = empleadoRepositoryService.findAllEmpleados();

            // Agregar todos los datos al modelo
            model.addAttribute("empleados", empleados);
            model.addAttribute("empleadoNombre", nombre);
            model.addAttribute("empleadoApellidos", apellidos);
            model.addAttribute("empleadoRFC", rfc);
            model.addAttribute("empleadoCorreo", correo);
            model.addAttribute("montoNomina", nominaDatos.getMontoNomina());
            model.addAttribute("isr", nominaDatos.getIsr());
            model.addAttribute("deducciones", nominaDatos.getDeducciones());
            model.addAttribute("nominaNeto", nominaDatos.getNominaNeto());
            model.addAttribute("mostrarResultados", true);

        } catch (Exception e) {
            return "redirect:/calcular-nomina";
        }

        return "calcular-nomina";
    }

}