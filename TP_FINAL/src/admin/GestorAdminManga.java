package admin;

import contenido.EstadoVisto;
import manejo_json.JsonUtilManga;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class GestorAdminManga extends GestorAdmin {
    public static final String PATH_MANGA = "Mangas.json";

    public GestorAdminManga() {
        super(PATH_MANGA);
    }


    @Override
    public void crear() {

        Scanner scanner = new Scanner(System.in);
        JSONObject nuevoManga = new JSONObject();
        nuevoManga = cargarDatos();

        // Leer el archivo JSON existente o crear uno nuevo si no existe
        try {
            JSONArray contenidos;
            try (FileReader reader = new FileReader(PATH_MANGA)) {
                JSONTokener tokener = new JSONTokener(reader);
                contenidos = new JSONArray(tokener); // Leer contenido si es válido
            } catch (IOException e) {
                System.out.println("El archivo no existe o está vacío. Creando uno nuevo...");
                contenidos = new JSONArray(); // Inicializar un JSONArray vacío
            }

            // Verificar si el manga ya existe
            boolean existe = false;
            for (int i = 0; i < contenidos.length(); i++) {
                JSONObject mangaExistente = contenidos.getJSONObject(i);
                if (mangaExistente.getInt("id") == nuevoManga.getInt("id")) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                contenidos.put(nuevoManga); // Agregar el nuevo manga
                try (FileWriter writer = new FileWriter(PATH_MANGA)) {
                    writer.write(contenidos.toString(4)); // Guardar el contenido actualizado
                    System.out.println("El manga ha sido creado exitosamente.");
                }
            } else {
                System.out.println("El manga ya existe en la base de datos.");
            }
        } catch (Exception e) {
            System.err.println("Error al crear el manga: " + e.getMessage());
        }
    }

    @Override
    public JSONObject cargarDatos() {
        // Solicitar los datos para el manga
        String titulo = ValidacionDatos.obtenerString("Ingrese el título del manga: ");
        int popularidad = ValidacionDatos.obtenerEnteros("Ingrese la popularidad: ");
        int rank = ValidacionDatos.obtenerEnteros("Ingrese el ranking: ");
        double score = ValidacionDatos.obtenerPuntuacion("Ingrese la puntuación (score): ");
        int members = ValidacionDatos.obtenerEnteros("Ingrese el número de miembros: ");
        int chapters = ValidacionDatos.obtenerEnteros("Ingrese el número de capítulos: ");
        int volumes = ValidacionDatos.obtenerEnteros("Ingrese el número de volúmenes: ");
        String status = ValidacionDatos.obtenerString("Ingrese el estado: ");
        String synopsis = ValidacionDatos.obtenerString("Ingrese la sinopsis: ");

        // Construir el objeto JSON con todos los datos
        JSONObject nuevoManga = new JSONObject();
        nuevoManga.put("score", score);
        nuevoManga.put("members", members);
        nuevoManga.put("popularity", popularidad);
        nuevoManga.put("rank", rank);
        nuevoManga.put("vistoONo", EstadoVisto.NO_VISTO);
        nuevoManga.put("id", obtenerSiguienteId());
        nuevoManga.put("synopsis", synopsis);
        nuevoManga.put("title", titulo);
        nuevoManga.put("chapters", chapters);
        nuevoManga.put("volumes", volumes);
        nuevoManga.put("status", status);

        return nuevoManga;
    }

    @Override
    public void actualizar() {
        Scanner scanner = new Scanner(System.in);

        // Solicitar el ID del manga a actualizar
        System.out.print("Ingrese el ID del manga que desea actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        try {
            // Leer el archivo JSON
            JSONArray contenidos;
            try (FileReader reader = new FileReader(PATH_MANGA)) {
                JSONTokener tokener = new JSONTokener(reader);
                contenidos = new JSONArray(tokener);
            }

            // Buscar el objeto con el ID especificado
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

            // Llamar a la función para actualizar los datos
            actualizarDatos(mangaExistente);

            // Sobrescribir el archivo JSON con los datos actualizados
            contenidos.put(indice, mangaExistente);
            try (FileWriter writer = new FileWriter(PATH_MANGA)) {
                writer.write(contenidos.toString(4));
                System.out.println("El manga con ID " + id + " ha sido actualizado exitosamente.");
            }

        } catch (IOException e) {
            System.err.println("Error al leer o escribir el archivo JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }

    // Función separada para actualizar los datos del manga
    public void actualizarDatos(JSONObject mangaExistente) {
        // Solicitar nuevos datos para el manga
        String titulo = ValidacionDatos.obtenerString("Ingrese el título del manga: ");
        int popularidad = ValidacionDatos.obtenerEnteros("Ingrese la popularidad: ");
        int rank = ValidacionDatos.obtenerEnteros("Ingrese el ranking: ");
        double score = ValidacionDatos.obtenerPuntuacion("Ingrese la puntuación (score): ");
        int members = ValidacionDatos.obtenerEnteros("Ingrese el número de miembros: ");
        int chapters = ValidacionDatos.obtenerEnteros("Ingrese el número de capítulos: ");
        int volumes = ValidacionDatos.obtenerEnteros("Ingrese el número de volúmenes: ");
        String status = ValidacionDatos.obtenerString("Ingrese el estado: ");
        String synopsis = ValidacionDatos.obtenerString("Ingrese la sinopsis: ");

        // Actualizar los datos en el objeto JSON
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


