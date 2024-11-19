package admin;

import gestores.GestorExcepciones;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GestorAdminAnime extends GestorAdmin {
    public static final String PATH_ANIME = "Anime.json";

    public GestorAdminAnime() {
        super(PATH_ANIME);
    }

    // Método para crear un nuevo anime en el archivo JSON
    @Override
    public void crear() {
        JSONObject nuevoAnime = new JSONObject();
        nuevoAnime = cargarDatos();

        try {
            JSONArray contenidos;
            try (FileReader reader = new FileReader(PATH_ANIME)) {
                JSONTokener tokener = new JSONTokener(reader);
                contenidos = new JSONArray(tokener);
            } catch (IOException e) {
                GestorExcepciones.manejarIOException(e);
                contenidos = new JSONArray();
            }

            boolean existe = false;
            for (int i = 0; i < contenidos.length(); i++) {
                JSONObject animeExistente = contenidos.getJSONObject(i);
                if (animeExistente.getInt("id") == nuevoAnime.getInt("id")) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                contenidos.put(nuevoAnime);
                try (FileWriter writer = new FileWriter(PATH_ANIME)) {
                    writer.write(contenidos.toString(4));
                    System.out.println("El anime ha sido creado exitosamente.");
                }
            } else {
                System.out.println("El anime ya existe en la base de datos.");
            }
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    // Método para cargar los datos de un nuevo anime desde la entrada del usuario
    // Devuelve un objeto JSON con los datos del nuevo anime
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

        return nuevoAnime;
    }

    // Método para actualizar los datos de un anime existente en el archivo JSON
    @Override
    public void actualizar() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el ID del anime que desea actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try {
            JSONArray contenidos;
            try (FileReader reader = new FileReader(PATH_ANIME)) {
                JSONTokener tokener = new JSONTokener(reader);
                contenidos = new JSONArray(tokener);
            }

            JSONObject animeExistente = null;
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

    // Método para actualizar los datos de un anime existente
    public void actualizarDatosAnime(JSONObject animeExistente) {
        String titulo = ValidacionDatos.obtenerString("Ingrese el título del anime: ");
        int popularidad = ValidacionDatos.obtenerEnteros("Ingrese la popularidad: ");
        int rank = ValidacionDatos.obtenerEnteros("Ingrese el ranking: ");
        double score = ValidacionDatos.obtenerPuntuacion("Ingrese la puntuación (score): ");
        int members = ValidacionDatos.obtenerEnteros("Ingrese el número de miembros: ");
        int episodes = ValidacionDatos.obtenerEnteros("Ingrese el número de episodios: ");
        String status = ValidacionDatos.obtenerString("Ingrese el estado: ");
        String synopsis = ValidacionDatos.obtenerString("Ingrese la sinopsis: ");

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
