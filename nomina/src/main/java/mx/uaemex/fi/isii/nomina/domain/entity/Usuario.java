package mx.uaemex.fi.isii.nomina.domain.entity;

import jakarta.persistence.*;

    @Entity
    @Table(name = "usuario")
    public class Usuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int idusuario;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(
                name = "idempleado",
                nullable = false,
                foreignKey = @ForeignKey(name = "fk_usuario_empleado")
        )

        private Empleado empleado;

        @Column(nullable = false)
        private String password;

        public Usuario() {
        }

        public Empleado getEmpleado() {
            return empleado;
        }

        public String getPassword() {
            return password;
        }

        public int getIdusuario() {
            return idusuario;
        }

        public void setEmpleado(Empleado empleado) {
            this.empleado = empleado;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public void setIdusuario(int idusuario) {
            this.idusuario = idusuario;
        }
    }


