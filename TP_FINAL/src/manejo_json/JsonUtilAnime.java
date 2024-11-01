package manejo_json;
import contenido.Anime;
import contenido.Estado_vistoONo_enum;
import contenido.Genre;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
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
        json.put("imageUrl", anime.getUrL_image());
        json.put("vistoONo", anime.getVistoONo().name());

        return json;
    }

    //para la api
    public  static JSONArray listToJson(List<Anime> animeList) {
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
            json.put("imageUrl", anime.getUrL_image());
            json.put("vistoONo", anime.getVistoONo().name());

            jsonArray.put(json);
        }

        return jsonArray;
    }

    @Override
    public Object jsonToObject(JSONObject jsonObject) {
        // Obtener datos desde el JSON
        int id = jsonObject.getInt("id");
        String title = jsonObject.getString("title");
        double score = jsonObject.getDouble("score");
        String status = jsonObject.getString("status");
        int episodes = jsonObject.getInt("episodes");
        int members = jsonObject.getInt("members");
        int popularity = jsonObject.getInt("popularity");
        int rank = jsonObject.getInt("rank");
        String synopsis = jsonObject.getString("synopsis");
        String imageUrl = jsonObject.getString("imageUrl");
        Estado_vistoONo_enum vistoONo = Estado_vistoONo_enum.valueOf(jsonObject.getString("vistoONo"));


        // Crear el objeto Anime
        return new Anime( id, members, title, popularity, rank, score, status, synopsis, title, imageUrl, vistoONo, episodes);
    }

    // Método para guardar el objeto Anime en un archivo usando JsonUtilAnime
    public static void guardarListaAnimeEnArchivo(List<Anime> anime, String archivoDestino) {
        // Usar la clase JsonUtilAnime para convertir el objeto Anime a JSON
        JSONArray animeJson = JsonUtilAnime.listToJson(anime);  // Convertir el objeto Anime a JSON

        try (FileWriter file = new FileWriter(archivoDestino, true)) {  // "true" para añadir sin sobrescribir
            file.write(animeJson.toString(4));  // Guardar el JSON con indentación de 4 espacios
            file.write(",");  // Añadir una nueva línea entre cada anime para legibilidad
            System.out.println("Anime guardado en: " + archivoDestino);
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }
}
