package usuario;

import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;

public class ValidacionUsuario {

    private static final String REGLA_CONTRASENA = "^(?=.*[A-Z])(?=.*\\d)(?=\\S+$)[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ.]{8,}$";

    private static final String DESCRIPCION_REGLA_CONTRASENA =
            "La contraseña debe cumplir con los siguientes requisitos:\n" +
                    "- Al menos una letra mayúscula.\n" +
                    "- Al menos un número.\n" +
                    "- Sin espacios.\n" +
                    "- Al menos 8 caracteres.";


    private static final String REGLA_EMAIL = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,}$";

    // Método para verificar si la contraseña guardada coincide con la ingresada
    public static boolean verificarContraseña(String contraseñaGuardada, String contraseñaIngresada) {
        return contraseñaGuardada.equals(contraseñaIngresada);
    }

    // Método para validar si el formato de un email es correcto
    public static boolean esEmailValido(String email) throws EmailInvalidoException {
        if (email == null || !email.matches(REGLA_EMAIL)) {
            throw new EmailInvalidoException("El email no es válido.");
        }
        return true;
    }

    // Método para validar si la contraseña cumple con los requisitos de seguridad establecidos
    public static boolean esContraseñaValida(String contraseña) throws ContrasenaInvalidaException {
        if (!contraseña.matches(REGLA_CONTRASENA)) {
            throw new ContrasenaInvalidaException(
                    "La contraseña debe cumplir con los requisitos de seguridad:\n" + DESCRIPCION_REGLA_CONTRASENA
            );
        }
        return true;
    }
}
