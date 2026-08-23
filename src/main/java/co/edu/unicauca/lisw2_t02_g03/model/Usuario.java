package main.java.co.edu.unicauca.lisw2_t02_g03.model;

public class Usuario {
    private String login;
    private String nombreCompleto;
    private Rol rol;
    private EstadoUsuario estado;
    private String contrasena;
    
    public Usuario(String login, String nombreCompleto, Rol rol, EstadoUsuario estado, String contrasena) {
        this.login = login;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.estado = estado;
        this.contrasena = contrasena;
    }

    public String getLogin() {
        return login;
    }

    private void setLogin(String login) { 
        this.login = login;
    }

    private void setNombreCompleto(String nombreCompleto) { 
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombreCompleto() { 
        return nombreCompleto;
    }

    private void setRol(Rol rol) { 
        this.rol = rol;
    }

    public Rol getRol() { 
        return rol;
    }

    private void setEstado(EstadoUsuario estado) { 
        this.estado = estado;
    }

    public EstadoUsuario getEstado() { 
        return estado;
    }

    private void setContrasena(String contrasena) { 
        this.contrasena = contrasena;
    }

    public String getContrasena() { 
        return contrasena;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "login='" + login + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", rol=" + rol +
                ", estado=" + estado +
                ", contrasena='" + contrasena + '\'' +
                '}';
    }

}
