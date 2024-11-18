package manejo_json;

import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;
import gestores.GestorExcepciones;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class JsonUtil {

    // Método para escribir un JSONObject a un archivo
    public static void writeJsonToFile(String fileName, JSONObject jsonObject) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(jsonObject.toString(4)); // Formato con indentación
        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
        }
    }

    // Método para leer un JSONObject de un archivo
    public static JSONObject readJsonFromFile(String fileName) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            return new JSONObject(content);
        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
            return null;
        }
    }

    // Método abstracto para convertir un objeto en un JSONObject (debe ser implementado en subclases)
    public abstract JSONObject objectToJson(Object obj);

    // Método abstracto para convertir un JSONObject en un objeto (debe ser implementado en subclases)
    public abstract Object jsonToObject(JSONObject jsonObject) throws ContrasenaInvalidaException, EmailInvalidoException;

    // Método abstracto para crear un archivo por defecto (debe ser implementado en subclases)
    public abstract void crearArchivoPorDefecto();
}
