package menu;

import java.io.IOException;
import java.util.Scanner;

import contenido.Anime;
import excepciones.EmailInvalidoException;
import gestores.GestorContenido;
import gestores.GestorUsuarios;
import manejo_json.JsonUtilUsuario;
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
        boolean continuar = true;
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
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (continuar);
        System.exit(0);
    }


    // Método para iniciar sesión
    public void iniciarSesion() throws UsuarioNoEncontradoException, ContrasenaInvalidaException {
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
            mostrarMenuUsuario(usuario);
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

    public void mostrarMenuUsuario(Usuario usuario) {
        int opcion;
        boolean continuarEnMenuUsuario = true; // Bandera para controlar el menú de usuario

        do {
            System.out.println("---- Menú de Usuario ----");
            System.out.println("1. Ver mi lista de animes y mangas");
            System.out.println("2. Opciones de usuario");
            System.out.println("3. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    break;
                case 2:
                    // Aquí, verificamos si el usuario fue eliminado
                    continuarEnMenuUsuario = mostrarOpcionesUsuario(usuario);
                    break;
                case 3:
                    System.out.println("Cerrando sesión...");
                    continuarEnMenuUsuario = false; // Salir del menú de usuario
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }

        } while (continuarEnMenuUsuario); // El ciclo continuará si la bandera es true

        // Después de salir del menú de usuario, retornamos al menú principal
        mostrarMenuPrincipal();
    }


    public boolean mostrarOpcionesUsuario(Usuario usuario) {
        int opcion;
        boolean continuarEnMenu = true; // Bandera para saber si seguir en el menú

        do {
            System.out.println("---- Opciones de Usuario ----");
            System.out.println("1. Cambiar contraseña");
            System.out.println("2. Cambiar email");
            System.out.println("3. Cambiar nombre de usuario");
            System.out.println("4. Eliminar cuenta");
            System.out.println("5. Volver al menú anterior");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    opcionCambiarContrasena(usuario);
                    break;
                case 2:
                    opcionCambiarEmail(usuario);
                    break;
                case 3:
                    opcionCambiarNombreUsuario(usuario);
                    break;
                case 4:
                    opcionEliminarCuenta(usuario);
                    // Si la cuenta fue eliminada, no continuar en el menú
                    continuarEnMenu = false;
                    break;
                case 5:
                    System.out.println("Volviendo al menú de usuario...");
                    continuarEnMenu = false; // Si elige la opción 5, también salimos del menú
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (continuarEnMenu); // Si continuarEnMenu es false, salimos del ciclo

        return continuarEnMenu; // Retorna si se debe continuar o no en el menú
    }



    public void opcionCambiarContrasena(Usuario usuario) {
        System.out.print("Ingrese su contraseña actual: ");
        String contrasenaActual = scanner.nextLine();
        System.out.print("Ingrese la nueva contraseña: ");
        String nuevaContrasena = scanner.nextLine();

        try {
            // Intentamos cambiar la contraseña del usuario
            usuario.cambiarContraseña(contrasenaActual, nuevaContrasena);
            System.out.println("Contraseña actualizada correctamente.");

            // Intentamos actualizar el usuario en el archivo
            JsonUtilUsuario.actualizarUsuarioEnArchivo(usuario);

        } catch (ContrasenaInvalidaException e) {
            // Si la contraseña es inválida
            System.out.println("Error: " + e.getMessage());
        } catch (EmailInvalidoException e) {
            // Si el email es inválido
            System.out.println("Error en el email: " + e.getMessage());
        } catch (IOException e) {
            // Si hay problemas con el archivo (lectura o escritura)
            System.out.println("Error al actualizar el archivo: " + e.getMessage());
        }
    }

    public void opcionCambiarEmail(Usuario usuario) {
        System.out.print("Ingrese el nuevo email: ");
        String nuevoEmail = scanner.nextLine();
        System.out.print("Ingrese su contraseña actual: ");
        String contrasenaActual = scanner.nextLine();
        try {
            usuario.cambiarEmail(contrasenaActual, nuevoEmail);
            System.out.println("Email actualizado correctamente.");
            // Intentamos actualizar el usuario en el archivo
            JsonUtilUsuario.actualizarUsuarioEnArchivo(usuario);
        } catch (ContrasenaInvalidaException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (EmailInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al actualizar el archivo: " + e.getMessage());
        }
    }


    public void opcionCambiarNombreUsuario(Usuario usuario) {
        System.out.print("Ingrese el nuevo nombre de usuario: ");
        String nuevoNombre = scanner.nextLine();
        System.out.print("Ingrese su contraseña actual: ");
        String contrasenaActual = scanner.nextLine();

        try {
            usuario.cambiarNombreUsuario(contrasenaActual, nuevoNombre);
            System.out.println("Nombre de usuario actualizado correctamente.");
            // Intentamos actualizar el usuario en el archivo
            JsonUtilUsuario.actualizarUsuarioEnArchivo(usuario);
        } catch (ContrasenaInvalidaException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (EmailInvalidoException e) {
            System.out.println("Error en el email: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al actualizar el archivo: " + e.getMessage());
        }
    }



    public void opcionEliminarCuenta(Usuario usuario) {
        System.out.print("Ingrese su contraseña actual para confirmar eliminación: ");
        String contrasenaActual = scanner.nextLine();
        try {
            if (usuario.getContraseña().equals(contrasenaActual)) {
                gestorUsuarios.eliminarUsuario(usuario);  // Llama al método en GestorUsuarios para eliminar
                System.out.println("Cuenta eliminada correctamente.");
            } else {
                throw new ContrasenaInvalidaException("Contraseña incorrecta. No se pudo eliminar la cuenta.");
            }
        } catch (ContrasenaInvalidaException e) {
            System.out.println(e.getMessage());
        }
    }



}