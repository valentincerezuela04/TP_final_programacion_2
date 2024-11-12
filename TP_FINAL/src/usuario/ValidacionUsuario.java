package usuario;

import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;

import java.util.regex.Pattern; //Importacion de la clase 'Pattern', ideal para comprobar si una cadena cumple con una expresión regular


public class ValidacionUsuario {

    /* Reglas de la contraseña:
    * 8 caracteres
    * 1 letra mayuscula
    * 1 numero
    * Sin espacios
    * Sin caracteres especiales (excepto el punto)
    */
    private static final String REGLA_CONTRASENA = "^(?=.*[A-Z])(?=.*\\d)(?=\\S+$)[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ.]{8,}$";

    // String que contiene las reglas tipicas de una direccion e-mail.
    private static final String REGLA_EMAIL = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,}$";

    //Metodo para confirmar coincidencias en la contraseña cuando el usuario se registra o cambia contraseña. (Usada cuando hay que ingresar 2 veces la misma)
    public static boolean validarContraseña(String contraseña, String contraseñaConfirmacion) {
        return contraseña != null && contraseña.equals(contraseñaConfirmacion); //Comprueba que la contraseña no sea nula y que sea la misma que la de la confirmacion
    }

    //Metodo para el inicio de sesión. Compara la contraseña almacenada y la ingresada para confirmar coincidencias.
    public static boolean verificarContraseña(String contraseñaGuardada, String contraseñaIngresada) {
        return contraseñaGuardada.equals(contraseñaIngresada);
    }

    public static boolean esEmailValido(String email) throws EmailInvalidoException {
        if (email == null || !email.contains("@")) {
            throw new EmailInvalidoException("El email no es válido.");
        }
        return true;
    }

    public static boolean esContraseñaValida(String contraseña) throws ContrasenaInvalidaException {
        if (contraseña == null || contraseña.length() < 6) {
            throw new ContrasenaInvalidaException("La contraseña debe tener al menos 6 caracteres.");
        }
        return true;
    }
}

