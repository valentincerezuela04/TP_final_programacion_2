package manejo_json;

import contenido.Anime;
import contenido.EstadoVisto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonUtilAnime extends JsonUtil {

    @Override
    public JSONObject objectToJson(Object obj) {
        Anime anime = (Anime) obj;
        JSONObject json = new JSONObject();

        json.put("id", anime.getId());
        json.put("title", anime.getTitle());
        json.put("score", anime.getScore());
        json.put("status", anime.getStatus());
        json.put("episodes", anime.getEpisodios());
        json.put("members", anime.getMembers());
        json.put("popularity", anime.getPopularity());
        json.put("rank", anime.getRank());
        json.put("synopsis", anime.getSynopsis());
        json.put("vistoONo", anime.getVistoONo().name());

        return json;
    }

    public static JSONArray listToJson(List<Anime> animeList) {
        JSONArray jsonArray = new JSONArray();

        for (Anime anime : animeList) {
            JSONObject json = new JSONObject();

            json.put("id", anime.getId());
            json.put("title", anime.getTitle());
            json.put("score", anime.getScore());
            json.put("status", anime.getStatus());
            json.put("episodes", anime.getEpisodios());
            json.put("members", anime.getMembers());
            json.put("popularity", anime.getPopularity());
            json.put("rank", anime.getRank());
            json.put("synopsis", anime.getSynopsis());
            json.put("vistoONo", anime.getVistoONo().name());

            jsonArray.put(json);
        }

        return jsonArray;
    }

    @Override
    public Object jsonToObject(JSONObject jsonObject) {
        int id = jsonObject.optInt("mal_id", jsonObject.optInt("id", 0));
        String title = jsonObject.optString("title", "Sin título");
        double score = jsonObject.optDouble("score", 0.0);
        String status = jsonObject.optString("status", "Desconocido");
        int episodes = jsonObject.optInt("episodes", 0);
        int members = jsonObject.optInt("members", 0);
        int popularity = jsonObject.optInt("popularity", 0);
        int rank = jsonObject.optInt("rank", 0);
        String synopsis = jsonObject.optString("synopsis", "Sin sinopsis disponible");

        String vistoONoValue = jsonObject.optString("vistoONo", "NO_VISTO");
        EstadoVisto vistoONo = EstadoVisto.valueOf(vistoONoValue);

        return new Anime(id, members, title, popularity, rank, score, status, synopsis, title, vistoONo, episodes);
    }

    // Método para guardar la lista de animes en un archivo JSON
    public static void guardarListaAnimeEnArchivo(List<Anime> anime, String archivoDestino) {
        JSONArray jsonObject = JsonUtilAnime.listToJson(anime);

        try (FileWriter file = new FileWriter(archivoDestino, false)) {
            file.write(jsonObject.toString(4));
            file.flush();
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo.");
            e.printStackTrace();
        }
    }

    // Método para cargar animes desde un archivo JSON
    public List<Anime> cargarAnimesDesdeArchivo(String archivoDestino) {
        List<Anime> listaDeAnimes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoDestino))) {
            StringBuilder contenido = new StringBuilder();
            String linea;

            // Leer el archivo línea por línea y acumular el contenido
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea);
            }

            // Crear JSONArray a partir del contenido completo
            JSONArray jsonArray = new JSONArray(contenido.toString());

            // Procesar cada elemento del JSONArray
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject animeJson = jsonArray.getJSONObject(i);

                // Verificar y renombrar el campo "id" a "mal_id" si existe
                if (animeJson.has("id") && !animeJson.has("mal_id")) {
                    animeJson.put("mal_id", animeJson.getInt("id"));
                }

                // Omitir el campo "images" si está presente
                animeJson.remove("images");

                // Convertir el JSONObject a un objeto Anime
                Anime anime = (Anime) this.jsonToObject(animeJson);
                listaDeAnimes.add(anime);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return listaDeAnimes;
    }

    // Método para mostrar animes cargados desde un archivo JSON
    public void mostrarAnimesConsola(String archivoDestino) {
        List<Anime> listaDeAnimes = cargarAnimesDesdeArchivo(archivoDestino);

        if (listaDeAnimes != null && !listaDeAnimes.isEmpty()) {
            System.out.println("---- Lista de Animes ----");
            for (Anime anime : listaDeAnimes) {
                System.out.printf("ID: %d%n", anime.getId());
                System.out.printf("Título: %s%n", anime.getTitle());
                System.out.printf("Puntaje: %.2f | Estado: %s%n", anime.getScore(), anime.getStatus());
                System.out.println("----------------------------------------");
            }
        } else {
            System.out.println("No se encontraron animes para mostrar.");
        }
    }

}
