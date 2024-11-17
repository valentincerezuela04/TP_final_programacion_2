package excepciones;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class GestorExcepciones {

    // Método para manejar la excepción de email inválido
    public static void manejarEmailInvalido(EmailInvalidoException e) {
        String mensaje = "Error en el email: " + e.getMessage();
        System.out.println(mensaje);
        registrarEnLog(mensaje);
    }

    // Método para manejar la excepción de contraseña inválida
    public static void manejarContrasenaInvalida(ContrasenaInvalidaException e) {
        String mensaje = "Error en la contraseña: " + e.getMessage();
        System.out.println(mensaje);
        registrarEnLog(mensaje);
    }

    // Método para manejar la excepción de usuario no encontrado
    public static void manejarUsuarioNoEncontrado(UsuarioNoEncontradoException e) {
        String mensaje = "Error en el usuario: " + e.getMessage();
        System.out.println(mensaje);
        registrarEnLog(mensaje);
    }

    // Método para manejar excepciones de I/O (por ejemplo, error al guardar archivo)
    public static void manejarIOException(IOException e) {
        String mensaje = "Error de entrada/salida: " + e.getMessage();
        System.out.println(mensaje);
        registrarEnLog(mensaje);
    }

    // Método general para manejar excepciones no controladas
    public static void manejarExcepcion(Exception e) {
        String mensaje = "Error general: " + e.getMessage();
        System.out.println(mensaje);
        registrarEnLog(mensaje);
    }

    // Método privado para registrar errores en un archivo de log
    private static void registrarEnLog(String mensaje) {
        try (FileWriter logWriter = new FileWriter("log_errores.txt", true)) {
            logWriter.write(LocalDateTime.now() + " - " + mensaje + "\n");
        } catch (IOException ex) {
            System.out.println("No se pudo escribir en el archivo de log: " + ex.getMessage());
        }
    }
}
