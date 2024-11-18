package gestores;

import excepciones.*;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class GestorExcepciones {

    // Método para manejar la excepción de email inválido
    public static void manejarEmailInvalido(EmailInvalidoException e) {
        manejarExcepcionGenerica(e, "Error en el email: ");
    }

    // Método para manejar la excepción de contraseña inválida
    public static void manejarContrasenaInvalida(ContrasenaInvalidaException e) {
        manejarExcepcionGenerica(e, "Error en la contraseña: ");
    }

    // Método para manejar la excepción de contraseñas no coincidentes
    public static void manejarContrasenasNoCoinciden(ContrasenasNoCoincidenException e) {
        manejarExcepcionGenerica(e, "Error en las contraseñas: ");
    }

    public static void manejarLoginException(LoginException e) {
        manejarExcepcionGenerica(e, "Error de inicio de sesión: ");
    }

    // Método para manejar la excepción de usuario repetido
    public static void manejarUsuarioRepetido(UsuarioRepetidoException e) {
        manejarExcepcionGenerica(e, "Error de usuario repetido: ");
    }

    public static void manejarUsuarioNoEncontrado(UsuarioNoEncontradoException e) {
        manejarExcepcionGenerica(e, "Error de usuario no encontrado: ");
    }

    public static void manejarContenidoNoEncontrado(ContenidoNoEncontradoException e) {
        manejarExcepcionGenerica(e, "Error: ");
    }

    public static void manejarContenidoDuplicado(ContenidoDuplicadoException e) {
        manejarExcepcionGenerica(e, "Error: ");
    }

    public static void manejarEstadoRepetidoException(EstadoRepetidoException e) {
        manejarExcepcionGenerica(e, "Error de estado repetido: ");
    }

    public static void manejarIllegalArgumentException(IllegalArgumentException e) {
        manejarExcepcionGenerica(e, "Error: ");
    }

    // Método para manejar excepciones de I/O (por ejemplo, error al guardar archivo)
    public static void manejarIOException(IOException e) {
        manejarExcepcionGenerica(e, "Error de entrada/salida: ");
    }

    // Método general para manejar excepciones no controladas
    public static void manejarExcepcion(Exception e) {
        manejarExcepcionGenerica(e, "Error general: ");
    }

    public static void manejarPeticionApiException(PeticionApiException e) {
        manejarExcepcionGenerica(e, "Error en la petición API: ");
    }

    // Método para manejar la excepción de respuesta de API inválida
    public static void manejarRespuestaApiException(RespuestaApiException e) {
        manejarExcepcionGenerica(e, "Error en la respuesta API: ");
    }

    // Método genérico para manejar las excepciones
    private static void manejarExcepcionGenerica(Exception e, String mensajePrefix) {
        String mensaje = mensajePrefix + e.getMessage();
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
