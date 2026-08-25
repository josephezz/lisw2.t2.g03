package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;
import co.edu.unicauca.lisw2_t02_g03.services.AuthServices;
import co.edu.unicauca.lisw2_t02_g03.services.UsuarioServices;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuFrame extends JFrame {

    private final Usuario usuario;
    private final UsuarioServices usuarioServices;
    private final AuthServices authServices;

    private JTextArea areaUsuarios;

    public MenuFrame(
            Usuario usuario,
            UsuarioServices usuarioServices,
            AuthServices authServices) {

        this.usuario = usuario;
        this.usuarioServices = usuarioServices;
        this.authServices = authServices;

        configurarVentana();
        crearInterfaz();
    }

    private void configurarVentana() {

        setTitle("Sistema - " + usuario.getRol());
        setSize(700, 550);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);
    }

    private void crearInterfaz() {

        JPanel principal =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        principal.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 25, 20, 25
                )
        );

        // ==============================
        // INFORMACION DEL USUARIO
        // ==============================

        JLabel bienvenida =
                new JLabel(
                        "Bienvenido, "
                                + usuario.getNombreCompleto()
                                + " | Rol: "
                                + usuario.getRol(),
                        SwingConstants.CENTER
                );

        bienvenida.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        principal.add(
                bienvenida,
                BorderLayout.NORTH
        );

        // ==============================
        // AREA DE USUARIOS
        // ==============================

        areaUsuarios =
                new JTextArea();

        areaUsuarios.setEditable(false);

        areaUsuarios.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        JScrollPane scroll =
                new JScrollPane(
                        areaUsuarios
                );

        principal.add(
                scroll,
                BorderLayout.CENTER
        );

        // ==============================
        // BOTONES
        // ==============================

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout()
                );

        JButton btnListar =
                new JButton(
                        "Listar usuarios"
                );

        JButton btnActivar =
                new JButton(
                        "Activar usuario"
                );

        JButton btnDesactivar =
                new JButton(
                        "Desactivar usuario"
                );

        JButton btnCerrarSesion =
                new JButton(
                        "Cerrar sesión"
                );

        /*
         * Solo el administrador podrá
         * gestionar estados.
         */
        if (
                usuario.getRol()
                        .name()
                        .equals("ADMINISTRADOR")
        ) {

            panelBotones.add(btnActivar);
            panelBotones.add(btnDesactivar);
        }

        panelBotones.add(btnListar);
        panelBotones.add(btnCerrarSesion);

        principal.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        // ==============================
        // EVENTOS
        // ==============================

        btnListar.addActionListener(
                e -> listarUsuarios()
        );

        btnActivar.addActionListener(
                e -> cambiarEstado(
                        EstadoUsuario.ACTIVO
                )
        );

        btnDesactivar.addActionListener(
                e -> cambiarEstado(
                        EstadoUsuario.INACTIVO
                )
        );

        btnCerrarSesion.addActionListener(
                e -> cerrarSesion()
        );

        add(principal);
    }

    private void listarUsuarios() {

        List<Usuario> usuarios =
                usuarioServices.listarUsuarios();

        areaUsuarios.setText("");

        areaUsuarios.append(
                String.format(
                        "%-20s %-25s %-18s %-12s%n",
                        "LOGIN",
                        "NOMBRE",
                        "ROL",
                        "ESTADO"
                )
        );

        areaUsuarios.append(
                "--------------------------------------------------------------------------\n"
        );

        for (Usuario u : usuarios) {

            areaUsuarios.append(
                    String.format(
                            "%-20s %-25s %-18s %-12s%n",
                            u.getLogin(),
                            u.getNombreCompleto(),
                            u.getRol(),
                            u.getEstado()
                    )
            );
        }
    }

    private void cambiarEstado(
            EstadoUsuario nuevoEstado) {

        String login =
                JOptionPane.showInputDialog(
                        this,
                        "Ingrese el login del usuario:"
                );

        if (
                login == null
                        || login.isBlank()
        ) {
            return;
        }

        boolean actualizado =
                usuarioServices.actualizarEstado(
                        login.trim(),
                        nuevoEstado
                );

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Estado actualizado correctamente."
            );

            listarUsuarios();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el usuario.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cerrarSesion() {

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea cerrar sesión?",
                        "Cerrar sesión",
                        JOptionPane.YES_NO_OPTION
                );

        if (
                respuesta
                        == JOptionPane.YES_OPTION
        ) {

            dispose();

            LoginFrame loginFrame =
                    new LoginFrame(
                            authServices,
                            usuarioServices
                    );

            loginFrame.setVisible(true);
        }
    }
}