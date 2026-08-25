package co.edu.unicauca.lisw2_t02_g03;

import co.edu.unicauca.lisw2_t02_g03.access.DataBaseInitializer;
import co.edu.unicauca.lisw2_t02_g03.access.DataBaseManager;
import co.edu.unicauca.lisw2_t02_g03.access.InterfaceUsuarioRepository;
import co.edu.unicauca.lisw2_t02_g03.access.UsuarioRepository;

import co.edu.unicauca.lisw2_t02_g03.services.AuthServices;
import co.edu.unicauca.lisw2_t02_g03.services.PasswordHasher;
import co.edu.unicauca.lisw2_t02_g03.services.PasswordValidator;
import co.edu.unicauca.lisw2_t02_g03.services.UsuarioServices;

import co.edu.unicauca.lisw2_t02_g03.presentation.LoginFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        // ==========================================
        // BASE DE DATOS
        // ==========================================

        DataBaseManager databaseManager =
                new DataBaseManager();

        DataBaseInitializer databaseInitializer =
                new DataBaseInitializer(
                        databaseManager
                );

        databaseInitializer.initialize();

        // ==========================================
        // REPOSITORY
        // ==========================================

        InterfaceUsuarioRepository
                usuarioRepository =
                new UsuarioRepository(
                        databaseManager
                );

        // ==========================================
        // SERVICIOS
        // ==========================================

        PasswordValidator passwordValidator =
                new PasswordValidator();

        PasswordHasher passwordHasher =
                new PasswordHasher();

        UsuarioServices usuarioServices =
                new UsuarioServices(
                        usuarioRepository,
                        passwordValidator::isValid,
                        passwordHasher::hash
                );

        AuthServices authServices =
                new AuthServices(
                        usuarioRepository,
                        passwordHasher::verify
                );

        // ==========================================
        // INTERFAZ GRAFICA
        // ==========================================

        SwingUtilities.invokeLater(() -> {

            LoginFrame loginFrame =
                    new LoginFrame(
                            authServices,
                            usuarioServices
                    );

            loginFrame.setVisible(true);
        });
    }
}