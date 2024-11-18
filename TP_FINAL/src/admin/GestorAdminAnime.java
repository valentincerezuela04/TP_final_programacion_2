package admin;

import contenido.EstadoVisto;
import gestores.GestorExcepciones;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class GestorAdminAnime extends  GestorAdmin{
    public static final String PATH_ANIME = "Anime.json";

    public GestorAdminAnime() {
        super(PATH_ANIME);
    }

    @Override
    public void crear() {

        JSONObject nuevoAnime = new JSONObject();
        nuevoAnime = cargarDatos();

        // Leer el archivo JSON existente o crear uno nuevo si no existe
        try {
            JSONArray contenidos;
            try (FileReader reader = new FileReader(PATH_ANIME)) {
                JSONTokener tokener = new JSONTokener(reader);
                contenidos = new JSONArray(tokener); // Leer contenido si es válido
            } catch (IOException e) {
                GestorExcepciones.manejarIOException(e);
                contenidos = new JSONArray(); // Inicializar un JSONArray vacío
            }

            // Verificar si el anime ya existe
            boolean existe = false;
            for (int i = 0; i < contenidos.length(); i++) {
                JSONObject animeExistente = contenidos.getJSONObject(i);
                if (animeExistente.getInt("id") == nuevoAnime.getInt("id")) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                contenidos.put(nuevoAnime); // Agregar el nuevo anime
                try (FileWriter writer = new FileWriter(PATH_ANIME)) {
                    writer.write(contenidos.toString(4)); // Guardar el contenido actualizado
                    System.out.println("El anime ha sido creado exitosamente.");
                }
            } else {
                System.out.println("El anime ya existe en la base de datos.");
            }
        } catch (Exception e) {
           GestorExcepciones.manejarExcepcion(e);
        }
    }


    @Override
    public JSONObject cargarDatos() {

        String titulo = ValidacionDatos.obtenerString("Ingrese el título del anime: ");
        int popularidad = ValidacionDatos.obtenerEnteros("Ingrese la popularidad: ");
        int rank = ValidacionDatos.obtenerEnteros("Ingrese el ranking: ");
        double score = ValidacionDatos.obtenerPuntuacion("Ingrese la puntuación (score): ");
        int members = ValidacionDatos.obtenerEnteros("Ingrese el número de miembros: ");
        int episodes = ValidacionDatos.obtenerEnteros("Ingrese el número de episodios: ");
        String status = ValidacionDatos.obtenerString("Ingrese el estado: ");
        String synopsis = ValidacionDatos.obtenerString("Ingrese la sinopsis: ");


        // Construir el objeto JSON en el orden especificado
        JSONObject nuevoAnime = new JSONObject();
        nuevoAnime.put("score", score);
        nuevoAnime.put("members", members);
        nuevoAnime.put("popularity", popularidad);
        nuevoAnime.put("rank", rank);
        nuevoAnime.put("id", obtenerSiguienteId());
        nuevoAnime.put("synopsis", synopsis);
        nuevoAnime.put("title", titulo);
        nuevoAnime.put("episodes", episodes);
        nuevoAnime.put("status", status);

        return  nuevoAnime;
    }

    @Override
    public void actualizar() {
        Scanner scanner = new Scanner(System.in);

        // Solicitar el ID del objeto a actualizar
        System.out.print("Ingrese el ID del anime que desea actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        try {
            // Leer el archivo JSON
            JSONArray contenidos;
            try (FileReader reader = new FileReader(PATH_ANIME)) {
                JSONTokener tokener = new JSONTokener(reader);
                contenidos = new JSONArray(tokener);
            }

            // Buscar el objeto con el ID especificado
            JSONObject animeExistente = null;

            //el -1 es una forma de no se agreguen los datos al Jsonobject si es q
            //no se encontro el anime en el archivo
            int indice = -1;

            for (int i = 0; i < contenidos.length(); i++) {
                JSONObject anime = contenidos.getJSONObject(i);
                if (anime.getInt("id") == id) {
                    animeExistente = anime;
                    indice = i;
                    break;
                }
            }

            if (animeExistente == null) {
                System.out.println("El anime con ID " + id + " no existe.");
                return;
            }

            // Actualizar los datos del anime
            actualizarDatosAnime(animeExistente);

            contenidos.put(indice, animeExistente);
            try (FileWriter writer = new FileWriter(PATH_ANIME)) {
                writer.write(contenidos.toString(4));
                System.out.println("El anime con ID " + id + " ha sido actualizado exitosamente.");
            }

        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
        } catch (Exception e) {
           GestorExcepciones.manejarExcepcion(e);
        }
    }


    public void actualizarDatosAnime(JSONObject animeExistente) {
        // Actualizamos los datos en el objeto JSON

        String titulo = ValidacionDatos.obtenerString("Ingrese el título del anime: ");
        int popularidad = ValidacionDatos.obtenerEnteros("Ingrese la popularidad: ");
        int rank = ValidacionDatos.obtenerEnteros("Ingrese el ranking: ");
        double score = ValidacionDatos.obtenerPuntuacion("Ingrese la puntuación (score): ");
        int members = ValidacionDatos.obtenerEnteros("Ingrese el número de miembros: ");
        int episodes = ValidacionDatos.obtenerEnteros("Ingrese el número de episodios: ");
        String status = ValidacionDatos.obtenerString("Ingrese el estado: ");
        String synopsis = ValidacionDatos.obtenerString("Ingrese la sinopsis: ");

        // Actualizamos el objeto JSON con los nuevos datos
        animeExistente.put("title", titulo);
        animeExistente.put("popularity", popularidad);
        animeExistente.put("rank", rank);
        animeExistente.put("score", score);
        animeExistente.put("members", members);
        animeExistente.put("episodes", episodes);
        animeExistente.put("status", status);
        animeExistente.put("synopsis", synopsis);
    }
}
