package co.edu.unicauca.lisw2_t02_g03.access;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;

import java.security.Provider.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.Statement;


public class UsuarioRepository implements InterfaceUsuarioRepository {

     private Connection conn;

    public UsuarioRepository() {
        initDatabase();
    }

    @Override
    public boolean save(Usuario usuario) {
        // Implementación para guardar el usuario en la base de datos
        try {
            //Validar usuario
            if (usuario == null || usuario.getLogin().isBlank()) {
                return false;
            }

            String sql = "INSERT INTO Usuario ( Login, NombreCompleto, Rol, Estado, Contrasena ) "
                    + "VALUES ( ?, ?, ?, ?, ? )";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usuario.getLogin());
            pstmt.setString(2, usuario.getNombreCompleto());
            pstmt.setObject(3, usuario.getRol());
            pstmt.setObject(4, usuario.getEstado());
            pstmt.setString(5, usuario.getContrasena());
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<Usuario> list() {
        // Implementación para listar todos los usuarios desde la base de datos
        List<Usuario> usuarios = new ArrayList<>();
        try {

            String sql = "SELECT Login, NombreCompleto, Rol, Estado, Contrasena FROM Usuario";
            //this.connect();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Usuario newUsuario = new Usuario( );
                newUsuario.setLogin(rs.getString("Login"));
                newUsuario.setNombreCompleto(rs.getString("NombreCompleto"));
                newUsuario.setRol((String) rs.getObject("Rol"));
                newUsuario.setEstado((String) rs.getObject("Estado"));
                newUsuario.setContrasena(rs.getString("Contrasena"));
                usuarios.add(newUsuario);
            }
            //this.disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuarios;
    }

    @Override
    public Usuario findByLogin(String login) {
        // Implementación para buscar un usuario por su login en la base de datos
        return null; // Retorna el usuario encontrado o null si no existe
    }

    @Override
    public boolean update(Usuario usuario) {
        // Implementación para actualizar un usuario en la base de datos
        return false; // Retorna true si se actualiza correctamente, false en caso contrario
    }
    
}
