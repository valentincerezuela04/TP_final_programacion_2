package manejo_json;
import contenido.Anime;
import contenido.Estado_vistoONo_enum;
import contenido.Genre;
import org.json.JSONArray;
import org.json.JSONObject;

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
}