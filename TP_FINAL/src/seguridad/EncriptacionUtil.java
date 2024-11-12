package seguridad;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncriptacionUtil {

    // Método para encriptar una cadena utilizando SHA-256
    public static String encriptar(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(texto.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error en el algoritmo de encriptación", e);
        }
    }
}
