package co.edu.unicauca.lisw2_t02_g03;

import co.edu.unicauca.lisw2_t02_g03.access.DataBaseInitializer;
import co.edu.unicauca.lisw2_t02_g03.access.DataBaseManager;
import co.edu.unicauca.lisw2_t02_g03.access.InterfaceUsuarioRepository;
import co.edu.unicauca.lisw2_t02_g03.access.UsuarioRepository;

import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;
import co.edu.unicauca.lisw2_t02_g03.model.Rol;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;

import co.edu.unicauca.lisw2_t02_g03.services.AuthServices;
import co.edu.unicauca.lisw2_t02_g03.services.PasswordHasher;
import co.edu.unicauca.lisw2_t02_g03.services.PasswordValidator;
import co.edu.unicauca.lisw2_t02_g03.services.UsuarioServices;

import java.util.List;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("       PRUEBA GENERAL DEL PROYECTO");
        System.out.println("==============================================");

        // =========================================================
        // 1. INICIALIZAR BASE DE DATOS
        // =========================================================

        System.out.println("\n[1] INICIALIZANDO BASE DE DATOS...");

        DataBaseManager databaseManager =
                new DataBaseManager();

        DataBaseInitializer databaseInitializer =
                new DataBaseInitializer(databaseManager);

        databaseInitializer.initialize();

        System.out.println("Base de datos inicializada correctamente.");


        // =========================================================
        // 2. CREAR REPOSITORIO
        // =========================================================

        System.out.println("\n[2] CREANDO REPOSITORIO...");

        InterfaceUsuarioRepository usuarioRepository =
                new UsuarioRepository(databaseManager);

        System.out.println("Repositorio creado correctamente.");


        // =========================================================
        // 3. CREAR DEPENDENCIAS
        // =========================================================

        System.out.println("\n[3] CREANDO SERVICIOS...");

        PasswordValidator passwordValidator =
                new PasswordValidator();

        PasswordHasher passwordHasher =
                new PasswordHasher();

        /*
         * Inyección de dependencias.
         *
         * UsuarioServices no depende directamente de
         * PasswordValidator ni de PasswordHasher.
         *
         * Se pasan sus métodos como estrategias.
         */
        UsuarioServices usuarioServices =
                new UsuarioServices(
                        usuarioRepository,
                        passwordValidator::isValid,
                        passwordHasher::hash
                );

        /*
         * AuthServices tampoco depende directamente de
         * PasswordHasher.
         */
        AuthServices authServices =
                new AuthServices(
                        usuarioRepository,
                        passwordHasher::verify
                );

        System.out.println("Servicios creados correctamente.");


        // =========================================================
        // 4. PROBAR PASSWORD VALIDATOR
        // =========================================================

        System.out.println("\n==============================================");
        System.out.println("[4] PRUEBA DE VALIDACION DE CONTRASEÑA");
        System.out.println("==============================================");

        String passwordValida = "Admin123!";
        String passwordInvalida = "abc";

        boolean valida =
                passwordValidator.isValid(passwordValida);

        boolean invalida =
                passwordValidator.isValid(passwordInvalida);

        System.out.println(
                "Contraseña 'Admin123!' válida: "
                        + valida
        );

        System.out.println(
                "Contraseña 'abc' válida: "
                        + invalida
        );


        // =========================================================
        // 5. CREAR USUARIO
        // =========================================================

        System.out.println("\n==============================================");
        System.out.println("[5] PRUEBA DE CREACION DE USUARIO");
        System.out.println("==============================================");

        /*
         * Generamos un login único para poder ejecutar
         * el Main varias veces sin tener problemas
         * con la clave primaria Login.
         */
        String loginPrueba =
                "admin_test_"
                        + UUID.randomUUID()
                        .toString()
                        .substring(0, 8);

        String passwordPrueba =
                "Admin123!";

        Usuario nuevoUsuario =
                new Usuario(
                        loginPrueba,
                        "Administrador de Prueba",
                        Rol.ADMINISTRADOR,
                        EstadoUsuario.ACTIVO,
                        passwordPrueba
                );

        System.out.println(
                "Login: " + loginPrueba
        );

        System.out.println(
                "Contraseña original: "
                        + passwordPrueba
        );

        boolean usuarioCreado =
                usuarioServices.crearUsuario(
                        nuevoUsuario
                );

        System.out.println(
                "Usuario creado: "
                        + usuarioCreado
        );


        // =========================================================
        // 6. COMPROBAR QUE LA CONTRASEÑA FUE HASHEADA
        // =========================================================

        System.out.println("\n==============================================");
        System.out.println("[6] PRUEBA DE HASH DE CONTRASEÑA");
        System.out.println("==============================================");

        Usuario usuarioGuardado =
                usuarioServices.buscarPorLogin(
                        loginPrueba
                );

        if (usuarioGuardado != null) {

            System.out.println(
                    "Contraseña almacenada en BD:"
            );

            System.out.println(
                    usuarioGuardado.getContrasena()
            );

            boolean esHashDiferente =
                    !passwordPrueba.equals(
                            usuarioGuardado.getContrasena()
                    );

            System.out.println(
                    "La contraseña NO está almacenada "
                            + "en texto plano: "
                            + esHashDiferente
            );

        } else {

            System.out.println(
                    "ERROR: No se encontró el usuario."
            );
        }


        // =========================================================
        // 7. LOGIN CORRECTO
        // =========================================================

        System.out.println("\n==============================================");
        System.out.println("[7] PRUEBA DE LOGIN CORRECTO");
        System.out.println("==============================================");

        Usuario usuarioAutenticado =
                authServices.iniciarSesion(
                        loginPrueba,
                        passwordPrueba
                );

        if (usuarioAutenticado != null) {

            System.out.println(
                    "LOGIN CORRECTO"
            );

            System.out.println(
                    "Usuario: "
                            + usuarioAutenticado.getLogin()
            );

            System.out.println(
                    "Nombre: "
                            + usuarioAutenticado
                            .getNombreCompleto()
            );

            System.out.println(
                    "Rol: "
                            + usuarioAutenticado.getRol()
            );

            System.out.println(
                    "Estado: "
                            + usuarioAutenticado.getEstado()
            );

        } else {

            System.out.println(
                    "ERROR: El login debería ser correcto."
            );
        }


        // =========================================================
        // 8. LOGIN CON CONTRASEÑA INCORRECTA
        // =========================================================

        System.out.println("\n==============================================");
        System.out.println("[8] PRUEBA DE CONTRASEÑA INCORRECTA");
        System.out.println("==============================================");

        Usuario loginIncorrecto =
                authServices.iniciarSesion(
                        loginPrueba,
                        "ContraseñaIncorrecta123!"
                );

        if (loginIncorrecto == null) {

            System.out.println(
                    "LOGIN RECHAZADO CORRECTAMENTE."
            );

        } else {

            System.out.println(
                    "ERROR: Se aceptó una contraseña incorrecta."
            );
        }


        // =========================================================
        // 9. CAMBIAR USUARIO A INACTIVO
        // =========================================================

        System.out.println("\n==============================================");
        System.out.println("[9] PRUEBA DE USUARIO INACTIVO");
        System.out.println("==============================================");

        boolean estadoActualizado =
                usuarioServices.actualizarEstado(
                        loginPrueba,
                        EstadoUsuario.INACTIVO
                );

        System.out.println(
                "Estado actualizado: "
                        + estadoActualizado
        );


        // =========================================================
        // 10. INTENTAR LOGIN CON USUARIO INACTIVO
        // =========================================================

        System.out.println("\n==============================================");
        System.out.println("[10] LOGIN CON USUARIO INACTIVO");
        System.out.println("==============================================");

        Usuario loginInactivo =
                authServices.iniciarSesion(
                        loginPrueba,
                        passwordPrueba
                );

        if (loginInactivo == null) {

            System.out.println(
                    "LOGIN RECHAZADO CORRECTAMENTE."
            );

            System.out.println(
                    "El usuario está INACTIVO."
            );

        } else {

            System.out.println(
                    "ERROR: Se permitió ingresar "
                            + "a un usuario inactivo."
            );
        }


        // =========================================================
        // 11. BUSCAR USUARIO
        // =========================================================

        System.out.println("\n==============================================");
        System.out.println("[11] PRUEBA DE BUSQUEDA DE USUARIO");
        System.out.println("==============================================");

        Usuario usuarioEncontrado =
                usuarioServices.buscarPorLogin(
                        loginPrueba
                );

        if (usuarioEncontrado != null) {

            System.out.println(
                    "Usuario encontrado:"
            );

            System.out.println(
                    usuarioEncontrado
            );

        } else {

            System.out.println(
                    "ERROR: No se encontró el usuario."
            );
        }


        // =========================================================
        // 12. LISTAR USUARIOS
        // =========================================================

        System.out.println("\n==============================================");
        System.out.println("[12] PRUEBA DE LISTADO DE USUARIOS");
        System.out.println("==============================================");

        List<Usuario> usuarios =
                usuarioServices.listarUsuarios();

        System.out.println(
                "Cantidad de usuarios registrados: "
                        + usuarios.size()
        );

        for (Usuario usuario : usuarios) {

            System.out.println(
                    "------------------------------------------"
            );

            System.out.println(
                    "Login: "
                            + usuario.getLogin()
            );

            System.out.println(
                    "Nombre: "
                            + usuario.getNombreCompleto()
            );

            System.out.println(
                    "Rol: "
                            + usuario.getRol()
            );

            System.out.println(
                    "Estado: "
                            + usuario.getEstado()
            );
        }


        // =========================================================
        // 13. FINAL
        // =========================================================

        System.out.println("\n==============================================");
        System.out.println("       PRUEBA GENERAL FINALIZADA");
        System.out.println("==============================================");

        System.out.println(
                "\nSi las pruebas anteriores fueron correctas,"
        );

        System.out.println(
                "la capa de acceso, modelo y servicios "
                        + "está funcionando correctamente."
        );
    }
}