// Clase JsonUtil
package manejo_json;

import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class JsonUtil {

    public static void writeJsonToFile(String fileName, JSONObject jsonObject) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(jsonObject.toString(4));
            System.out.println("Archivo JSON guardado correctamente en " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject readJsonFromFile(String fileName) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            return new JSONObject(content);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
            return null;
        }
    }

    public static JSONArray readJsonArrayFromFile(String fileName) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            return new JSONArray(content);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
            return null;
        }
    }

    public abstract JSONObject objectToJson(Object obj);

    public abstract Object jsonToObject(JSONObject jsonObject) throws ContrasenaInvalidaException, EmailInvalidoException;
}