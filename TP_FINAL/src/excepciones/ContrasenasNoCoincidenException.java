package excepciones;

public class ContrasenasNoCoincidenException extends Exception {
    public ContrasenasNoCoincidenException(String message) {
        super(message);
    }
}
