package manejo_json;

import contenido.Anime;
import contenido.Manga;
import contenido.EstadoVisto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtilManga extends JsonUtil {

    @Override
    public JSONObject objectToJson(Object obj) {
        Manga manga = (Manga) obj;
        JSONObject json = new JSONObject();

        json.put("id", manga.getId());
        json.put("title", manga.getTitle());
        json.put("score", manga.getScore());
        json.put("status", manga.getStatus());
        json.put("chapters", manga.getChapters());
        json.put("volumes", manga.getVolumes());
        json.put("members", manga.getMembers());
        json.put("popularity", manga.getPopularity());
        json.put("rank", manga.getRank());
        json.put("synopsis", manga.getSynopsis());
        json.put("vistoONo", manga.getVistoONo().name());

        return json;
    }



    @Override
    public Object jsonToObject(JSONObject jsonObject) {
        // Obtener datos desde el JSON con valores predeterminados
        int id = jsonObject.getInt("mal_id");
        String title = jsonObject.getString("title");
        double score = jsonObject.optDouble("score", 0.0);
        String status = jsonObject.optString("status", "Desconocido");
        int chapters = jsonObject.optInt("chapters", 0);
        int volumes = jsonObject.optInt("volumes", 0);
        int members = jsonObject.optInt("members", 0);
        int popularity = jsonObject.optInt("popularity", 0);
        int rank = jsonObject.optInt("rank", 0);
        String synopsis = jsonObject.optString("synopsis", "Sin sinopsis disponible");

        // Obtener el valor de "vistoONo" con optString y asignar el valor predeterminado "NO_VISTO" si no está presente
        String vistoONoValue = jsonObject.optString("vistoONo", "NO_VISTO");
        EstadoVisto vistoONo = EstadoVisto.valueOf(vistoONoValue);

        // Crear y devolver el objeto Manga con los datos extraídos
        return new Manga(id, members, title, popularity, rank, score, status, synopsis, title, vistoONo, chapters, volumes);
    }

    // para guardarListaAnimeEnArchivo(que se utiliza en la clase de api del manga)
    public static JSONArray listToJson(List<Manga> mangaList) {
        JSONArray jsonArray = new JSONArray();

        for (Manga manga : mangaList) {
            JSONObject json = new JSONObject();

            // Campos heredados de Contenido
            json.put("id", manga.getId());
            json.put("title", manga.getTitle());
            json.put("score", manga.getScore());
            json.put("status", manga.getStatus());
            json.put("members", manga.getMembers());
            json.put("popularity", manga.getPopularity());
            json.put("rank", manga.getRank());
            json.put("synopsis", manga.getSynopsis());
            json.put("vistoONo", manga.getVistoONo().name());
            json.put("chapters", manga.getChapters());
            json.put("volumes", manga.getVolumes());

            jsonArray.put(json);
        }

        return jsonArray;
    }

    // Método para escribir el objeto Manga en un archivo usando JsonUtilManga
    public static void guardarListaMangaEnArchivo(List<Manga> manga, String archivoDestino) {
        // Usar la clase JsonUtilManga para convertir el objeto Anime a JSON
        JSONArray mangaJson = JsonUtilManga.listToJson(manga);  // Convertir el objeto Manga a JSON

        try (FileWriter file = new FileWriter(archivoDestino, false)) {  // "false" para añadir sin sobrescribir
            file.write(mangaJson.toString(4));  // Escribir el JSON con indentación de 4 espacios
            file.flush();
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo.");
            e.printStackTrace();
        }
    }

    public List<Manga> cargarMangasDesdeArchivo(String archivoOrigen) {
        List<Manga> listaDeMangas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoOrigen))) {
            StringBuilder contenido = new StringBuilder();
            String linea;

            // Leer el archivo línea por línea y acumular el contenido
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea);
            }

            // Crear JSONArray a partir del contenido completo
            JSONArray mangasArray = new JSONArray(contenido.toString());

            // Procesar cada elemento del JSONArray
            for (int i = 0; i < mangasArray.length(); i++) {
                JSONObject mangaJson = mangasArray.getJSONObject(i);

                // Verificar y renombrar el campo "id" a "mal_id" si existe
                if (mangaJson.has("id") && !mangaJson.has("mal_id")) {
                    mangaJson.put("mal_id", mangaJson.getInt("id"));
                }

                // Omitir el campo "images" si está presente
                mangaJson.remove("images");

                // Convertir el JSONObject a un objeto Manga
                Manga manga = (Manga) new JsonUtilManga().jsonToObject(mangaJson);
                listaDeMangas.add(manga);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return listaDeMangas;
    }

    public void mostrarMangasConsola(String archivoDestino) {
       List<Manga> listaDeMangas = cargarMangasDesdeArchivo(archivoDestino);

        if (listaDeMangas != null && !listaDeMangas.isEmpty()) {
            System.out.println("---- Lista de Mangas ----");
            for (Manga manga : listaDeMangas) {
                System.out.printf("ID: %d%n", manga.getId());
                System.out.printf("Título: %s%n", manga.getTitle());
                System.out.printf("Puntaje: %.2f | Estado: %s%n", manga.getScore(), manga.getStatus());
                System.out.println("----------------------------------------");
            }
        } else {
            System.out.println("No se encontraron mangas para mostrar.");
        }
    }
}