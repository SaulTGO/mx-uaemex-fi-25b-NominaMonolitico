# mx-uaemex-fi-25b-NominaMonolitico
Sistema de calculo de nomina.

## 🚀 Equipo de desarrollo

Este proyecto fue desarrollado por el siguiente equipo:

|Saul Gonzalez | sgonzalezo005@alumno.uaemex.mx | gonzalezolisaul23@gmail.com |

## 📝 Descripción del Proyecto
Aplicacion web, que permite el calculo de la nomia de diversos empleados.
## ⚙️ Tecnologías Utilizadas
 * Java 17
 * Spring boot
 * Maven
 * Apache tomcat
 * HTML/CSS/JS
 * Thymeleaf

## Instalación

Requiere de docker instalado y acondicionado.
1. Abrir docker desktop, o asegurarse de que este ejecutandose. Para asegurarse de que se esta ejecutando docker, desde la terminal ingrese el comanndo `docker ps`.
2. Descargar el contenedor. Ingresar el comando:
```
docker pull saulgonzalez23/nomina-monolitico:v1.0.0
```
3. Ejecutar el contenedor. Ingresar el comando:
```
docker run -d -p 1234:1234 --name nomina-monolitico saulgonzalez23/nomina-monolitico:v1.0.0
```
4. Asegúrese de que el contenedor se esté ejecutando, al ingresar nuevamente el comando `docker ps`. En la columna de name debería mostrarse `nomina-monolitico`.
5. Cerrar el contenedor: ingrese en la línea de comandos:
```
docker stop nomina-monolitico
```
## 📄 Licencia
Este proyecto está bajo la Licencia MIT. Esto te permite usar, copiar, modificar y distribuir el software sin restricciones, siempre que se mantenga el aviso de derechos de autor y la licencia original.
