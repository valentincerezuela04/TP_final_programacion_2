package admin;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public abstract class GestorAdmin {
    private final String PATH;

    public GestorAdmin(String PATH) {
        this.PATH = PATH;
    }

    public abstract void crear();

    public abstract  void actualizar();

    public abstract JSONObject cargarDatos();




    public void eliminar_por_id(int id) {
        try {
            // Leer el archivo JSON
            JSONArray contenidos;
            try (FileReader reader = new FileReader(PATH)) {
                JSONTokener tokener = new JSONTokener(reader);
                contenidos = new JSONArray(tokener);
            } catch (IOException e) {
                System.out.println("El archivo no existe o está vacío.");
                return;
            }

            // Verificar si el objeto con el ID existe
            boolean encontrado = false;
            for (int i = 0; i < contenidos.length(); i++) {
                JSONObject anime = contenidos.getJSONObject(i);
                if (anime.getInt("id") == id) {
                    // Si se encuentra, eliminar el objeto
                    contenidos.remove(i);
                    encontrado = true;
                    break;
                }
            }

            // Guardar el archivo actualizado si el objeto fue encontrado
            if (encontrado) {
                try (FileWriter writer = new FileWriter(PATH)) {
                    writer.write(contenidos.toString(4)); // Formato bonito
                    System.out.println("El objeto con ID " + id + " ha sido eliminado exitosamente.");
                }
            } else {
                System.out.println("No se encontró ningún objeto con ID " + id + ".");
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar el objeto: " + e.getMessage());
        }
    }


    public void eliminar_por_titulo(String titulo) {
        try {
            // Leer el archivo JSON
            JSONArray contenidos;
            try (FileReader reader = new FileReader(PATH)) {
                JSONTokener tokener = new JSONTokener(reader);
                contenidos = new JSONArray(tokener);
            } catch (IOException e) {
                System.out.println("El archivo no existe o está vacío.");
                return;
            }

            // Verificar si el objeto con el ID existe
            boolean encontrado = false;
            for (int i = 0; i < contenidos.length(); i++) {
                JSONObject anime = contenidos.getJSONObject(i);
                if (Objects.equals(anime.getString("title"), titulo)) {
                    // Si se encuentra, eliminar el objeto
                    contenidos.remove(i);
                    encontrado = true;
                    break;
                }
            }

            // Guardar el archivo actualizado si el objeto fue encontrado
            if (encontrado) {
                try (FileWriter writer = new FileWriter(PATH)) {
                    writer.write(contenidos.toString(4)); // Formato bonito
                    System.out.println("El objeto con titulo " + titulo + " ha sido eliminado exitosamente.");
                }
            } else {
                System.out.println("No se encontró ningún objeto con titulo " + titulo + ".");
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar el objeto: " + e.getMessage());
        }
    }





    public int obtenerSiguienteId() {
        //lo casteamos por nuestro atributos de id es un int
        return Math.abs((int) UUID.randomUUID().getMostSignificantBits());
    }

}
