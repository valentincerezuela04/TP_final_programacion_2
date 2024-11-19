package admin;

import gestores.GestorExcepciones;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GestorAdminManga extends GestorAdmin {
    public static final String PATH_MANGA = "Manga.json";

    public GestorAdminManga() {
        super(PATH_MANGA);
    }

    // Método para crear un nuevo manga en el archivo JSON
    @Override
    public void crear() {

        Scanner scanner = new Scanner(System.in);
        JSONObject nuevoManga = new JSONObject();
        nuevoManga = cargarDatos();

        try {
            JSONArray contenidos;
            try (FileReader reader = new FileReader(PATH_MANGA)) {
                JSONTokener tokener = new JSONTokener(reader);
                contenidos = new JSONArray(tokener);
            } catch (IOException e) {
                GestorExcepciones.manejarIOException(e);
                contenidos = new JSONArray();
            }


            boolean existe = false;
            for (int i = 0; i < contenidos.length(); i++) {
                JSONObject mangaExistente = contenidos.getJSONObject(i);
                if (mangaExistente.getInt("id") == nuevoManga.getInt("id")) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                contenidos.put(nuevoManga);
                try (FileWriter writer = new FileWriter(PATH_MANGA)) {
                    writer.write(contenidos.toString(4));
                    System.out.println("El manga ha sido creado exitosamente.");
                }
            } else {
                System.out.println("El manga ya existe en la base de datos.");
            }
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    // Método para cargar los datos de un nuevo manga desde la entrada del usuario
    // Devuelve un objeto JSON con los datos del nuevo manga
    @Override
    public JSONObject cargarDatos() {
        String titulo = ValidacionDatos.obtenerString("Ingrese el título del manga: ");
        int popularidad = ValidacionDatos.obtenerEnteros("Ingrese la popularidad: ");
        int rank = ValidacionDatos.obtenerEnteros("Ingrese el ranking: ");
        double score = ValidacionDatos.obtenerPuntuacion("Ingrese la puntuación (score): ");
        int members = ValidacionDatos.obtenerEnteros("Ingrese el número de miembros: ");
        int chapters = ValidacionDatos.obtenerEnteros("Ingrese el número de capítulos: ");
        int volumes = ValidacionDatos.obtenerEnteros("Ingrese el número de volúmenes: ");
        String status = ValidacionDatos.obtenerString("Ingrese el estado: ");
        String synopsis = ValidacionDatos.obtenerString("Ingrese la sinopsis: ");

        JSONObject nuevoManga = new JSONObject();
        nuevoManga.put("score", score);
        nuevoManga.put("members", members);
        nuevoManga.put("popularity", popularidad);
        nuevoManga.put("rank", rank);
        nuevoManga.put("id", obtenerSiguienteId());
        nuevoManga.put("synopsis", synopsis);
        nuevoManga.put("title", titulo);
        nuevoManga.put("chapters", chapters);
        nuevoManga.put("volumes", volumes);
        nuevoManga.put("status", status);

        return nuevoManga;
    }

    // Método para actualizar los datos de un manga existente en el archivo JSON
    @Override
    public void actualizar() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el ID del manga que desea actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try {
            JSONArray contenidos;
            try (FileReader reader = new FileReader(PATH_MANGA)) {
                JSONTokener tokener = new JSONTokener(reader);
                contenidos = new JSONArray(tokener);
            }

            JSONObject mangaExistente = null;
            int indice = -1;

            for (int i = 0; i < contenidos.length(); i++) {
                JSONObject manga = contenidos.getJSONObject(i);
                if (manga.getInt("id") == id) {
                    mangaExistente = manga;
                    indice = i;
                    break;
                }
            }

            if (mangaExistente == null) {
                System.out.println("El manga con ID " + id + " no existe.");
                return;
            }

            actualizarDatos(mangaExistente);

            contenidos.put(indice, mangaExistente);
            try (FileWriter writer = new FileWriter(PATH_MANGA)) {
                writer.write(contenidos.toString(4));
                System.out.println("El manga con ID " + id + " ha sido actualizado exitosamente.");
            }

        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    // Método para actualizar los datos de un manga existente
    public void actualizarDatos(JSONObject mangaExistente) {

        String titulo = ValidacionDatos.obtenerString("Ingrese el título del manga: ");
        int popularidad = ValidacionDatos.obtenerEnteros("Ingrese la popularidad: ");
        int rank = ValidacionDatos.obtenerEnteros("Ingrese el ranking: ");
        double score = ValidacionDatos.obtenerPuntuacion("Ingrese la puntuación (score): ");
        int members = ValidacionDatos.obtenerEnteros("Ingrese el número de miembros: ");
        int chapters = ValidacionDatos.obtenerEnteros("Ingrese el número de capítulos: ");
        int volumes = ValidacionDatos.obtenerEnteros("Ingrese el número de volúmenes: ");
        String status = ValidacionDatos.obtenerString("Ingrese el estado: ");
        String synopsis = ValidacionDatos.obtenerString("Ingrese la sinopsis: ");

        mangaExistente.put("title", titulo);
        mangaExistente.put("popularity", popularidad);
        mangaExistente.put("rank", rank);
        mangaExistente.put("score", score);
        mangaExistente.put("members", members);
        mangaExistente.put("chapters", chapters);
        mangaExistente.put("volumes", volumes);
        mangaExistente.put("status", status);
        mangaExistente.put("synopsis", synopsis);
    }
}
