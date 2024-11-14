package manejo_json;

import contenido.Anime;
import contenido.EstadoVisto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
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

    // Para la API
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
        // Obtener datos desde el JSON
        int id = jsonObject.optInt("mal_id", jsonObject.optInt("id", 0));
        String title = jsonObject.optString("title", "Sin título");
        double score = jsonObject.optDouble("score", 0.0);
        String status = jsonObject.optString("status", "Desconocido");
        int episodes = jsonObject.optInt("episodes", 0);
        int members = jsonObject.optInt("members", 0);
        int popularity = jsonObject.optInt("popularity", 0);
        int rank = jsonObject.optInt("rank", 0);
        String synopsis = jsonObject.optString("synopsis", "Sin sinopsis disponible");

        // Para la consola, se omite "images", así que `imageUrl` se define como una cadena vacía
        String imageUrl = "";

        // Obtener el valor de "vistoONo" con optString, que devuelve un valor por defecto si la clave no está presente
        String vistoONoValue = jsonObject.optString("vistoONo", "NO_VISTO");
        EstadoVisto vistoONo = EstadoVisto.valueOf(vistoONoValue);

        // Crear el objeto Anime
        return new Anime(id, members, title, popularity, rank, score, status, synopsis, title, imageUrl, vistoONo, episodes);
    }

    // Método para guardar el objeto Anime en un archivo usando JsonUtilAnime
    public static void guardarListaAnimeEnArchivo(List<Anime> anime, String archivoDestino) {
        // Usar la clase JsonUtilAnime para convertir el objeto Anime a JSON
        JSONArray jsonObject = JsonUtilAnime.listToJson(anime);  // Convertir el objeto Anime a JSON

        try (FileWriter file = new FileWriter(archivoDestino, false)) {  // "false" para sobrescribir el archivo
            file.write(jsonObject.toString(4));  // Escribir el JSON con indentación de 4 espacios
            file.flush();
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo.");
            e.printStackTrace();
        }
    }
}
