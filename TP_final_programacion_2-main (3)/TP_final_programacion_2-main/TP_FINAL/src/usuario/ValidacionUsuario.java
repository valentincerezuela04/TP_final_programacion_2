package usuario;

import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;

public class ValidacionUsuario {

    private static final String REGLA_CONTRASENA = "^(?=.*[A-Z])(?=.*\\d)(?=\\S+$)[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ.]{8,}$";
    private static final String REGLA_EMAIL = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,}$";

    public static boolean verificarContraseña(String contraseñaGuardada, String contraseñaIngresada) {
        return contraseñaGuardada.equals(contraseñaIngresada);
    }

    public static boolean esEmailValido(String email) throws EmailInvalidoException {
        if (email == null || !email.matches(REGLA_EMAIL)) {
            throw new EmailInvalidoException("El email no es válido.");
        }
        return true;
    }

    public static boolean esContraseñaValida(String contraseña) throws ContrasenaInvalidaException {
        if (!contraseña.matches(REGLA_CONTRASENA)) {
            throw new ContrasenaInvalidaException("La contraseña debe cumplir con los requisitos de seguridad.");
        }
        return true;
    }
}
