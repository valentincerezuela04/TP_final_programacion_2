package menu;

import java.util.Scanner;

import excepciones.EmailInvalidoException;
import gestores.GestorUsuarios;
import usuario.Usuario;
import excepciones.ContrasenaInvalidaException;
import excepciones.UsuarioNoEncontradoException;
import excepciones.GestorExcepciones;
import usuario.ValidacionUsuario;

public class Menu {
    private GestorUsuarios gestorUsuarios;
    private Scanner scanner;

    public Menu() {
        this.gestorUsuarios = new GestorUsuarios();
        this.scanner = new Scanner(System.in);
    }

    // Método para mostrar el menú principal
    public void mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("---- Menú Principal ----");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Crear una cuenta");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    try {
                        iniciarSesion();
                    } catch (UsuarioNoEncontradoException e) {
                        GestorExcepciones.manejarUsuarioNoEncontrado(e); // Manejo de error de usuario no encontrado
                    } catch (ContrasenaInvalidaException e) {
                        GestorExcepciones.manejarContrasenaInvalida(e); // Manejo de error de contraseña inválida
                    }
                    break;
                case 2:
                    crearCuenta();
                    break;
                case 3:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 3);
    }

    // Método para iniciar sesión
    private void iniciarSesion() throws UsuarioNoEncontradoException, ContrasenaInvalidaException {
        System.out.print("Ingrese su nombre de usuario: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contraseña = scanner.nextLine();

        try {
            Usuario usuario = gestorUsuarios.iniciarSesion(nombre, contraseña);

            if (usuario == null) {
                throw new UsuarioNoEncontradoException("El nombre de usuario no existe.");
            }

            if (!usuario.getContraseña().equals(contraseña)) {
                throw new ContrasenaInvalidaException("Contraseña incorrecta.");
            }

            System.out.println("Inicio de sesión exitoso. Bienvenido, " + usuario.getNombre() + "!");
            // Aquí se podría agregar otro menú o funcionalidades adicionales después del inicio de sesión
        } catch (UsuarioNoEncontradoException e) {
            // Si el usuario no existe, manejar la excepción
            GestorExcepciones.manejarUsuarioNoEncontrado(e);
        } catch (ContrasenaInvalidaException e) {
            // Si la contraseña no es correcta, manejar la excepción
            GestorExcepciones.manejarContrasenaInvalida(e);
        }
    }

    // Método para crear una cuenta
    public void crearCuenta() {
        System.out.println("Crear nueva cuenta");

        System.out.print("Ingrese su nombre de usuario: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine();

        String contraseña, confirmacionContraseña;

        // Solicitar la contraseña dos veces
        do {
            System.out.print("Ingrese su contraseña: ");
            contraseña = scanner.nextLine();

            System.out.print("Confirme su contraseña: ");
            confirmacionContraseña = scanner.nextLine();

            if (!contraseña.equals(confirmacionContraseña)) {
                System.out.println("Las contraseñas no coinciden. Intente nuevamente.");
            }
        } while (!contraseña.equals(confirmacionContraseña)); // Repetir hasta que las contraseñas coincidan

        try {
            // Verificar que la contraseña cumpla con el patrón antes de proceder
            if (!ValidacionUsuario.esContraseñaValida(contraseña)) {
                System.out.println("Error: La contraseña no cumple con los requisitos de seguridad.");
                return; // Salir del método si la contraseña no es válida
            }

            // Validación del email
            ValidacionUsuario.esEmailValido(email);

            // Crear usuario y registrarlo
            Usuario nuevoUsuario = new Usuario(nombre, contraseña, email);
            Usuario usuarioRegistrado = gestorUsuarios.registrarUsuario(nuevoUsuario);

            if (usuarioRegistrado != null) {
                System.out.println("Cuenta creada exitosamente.");
            } else {
                System.out.println("No se pudo crear la cuenta. Es posible que el usuario ya esté registrado.");
            }
        } catch (EmailInvalidoException | ContrasenaInvalidaException e) {
            System.out.println("Error en la creación de la cuenta: " + e.getMessage());
        }
    }

}