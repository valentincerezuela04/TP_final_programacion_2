package admin;

import gestores.GestorExcepciones;
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

    public abstract void actualizar();

    public abstract JSONObject cargarDatos();

    // Eliminar por ID con manejo de excepciones
    public void eliminar_por_id(int id) {
        try {
            // Leer el archivo JSON
            JSONArray contenidos;
            try (FileReader reader = new FileReader(PATH)) {
                JSONTokener tokener = new JSONTokener(reader);
                contenidos = new JSONArray(tokener);
            } catch (IOException e) {
                GestorExcepciones.manejarIOException(e);
                return; // Retornamos al manejar la excepción
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
                } catch (IOException e) {
                    GestorExcepciones.manejarIOException(e);
                }
            } else {
                System.out.println("No se encontró ningún objeto con ID " + id + ".");
            }
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    // Eliminar por título con manejo de excepciones
    public void eliminar_por_titulo(String titulo) {
        try {
            // Leer el archivo JSON
            JSONArray contenidos;
            try (FileReader reader = new FileReader(PATH)) {
                JSONTokener tokener = new JSONTokener(reader);
                contenidos = new JSONArray(tokener);
            } catch (IOException e) {
                GestorExcepciones.manejarIOException(e);
                return; // Retornamos al manejar la excepción
            }

            // Verificar si el objeto con el título existe
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
                    System.out.println("El objeto con título " + titulo + " ha sido eliminado exitosamente.");
                } catch (IOException e) {
                    GestorExcepciones.manejarIOException(e);
                }
            } else {
                System.out.println("No se encontró ningún objeto con título " + titulo + ".");
            }
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    // Obtener siguiente ID con manejo de excepciones
    public int obtenerSiguienteId() {
        try {
            // El siguiente ID es un valor absoluto de un UUID
            return Math.abs((int) UUID.randomUUID().getMostSignificantBits());
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
            return -1; // Retornamos -1 en caso de error
        }
    }
}
