package excepciones;

public class PeticionApiException extends Exception {
    public PeticionApiException(String message) {
        super(message);
    }
}
