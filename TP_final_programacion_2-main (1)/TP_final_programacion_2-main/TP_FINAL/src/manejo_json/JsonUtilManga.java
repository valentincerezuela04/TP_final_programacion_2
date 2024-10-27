package manejo_json;

import contenido.Manga;
import contenido.Estado_vistoONo_enum;
import contenido.Genre;
import org.json.JSONArray;
import org.json.JSONObject;

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
        json.put("imageUrl", manga.getUrL_image());
        json.put("vistoONo", manga.getVistoONo().name());

        return json;
    }

    @Override
    public Object jsonToObject(JSONObject jsonObject) {
        // Obtener datos desde el JSON
        int id = jsonObject.getInt("id");
        String title = jsonObject.getString("title");
        double score = jsonObject.getDouble("score");
        String status = jsonObject.getString("status");
        int chapters = jsonObject.getInt("chapters");
        int volumes = jsonObject.getInt("volumes");
        int members = jsonObject.getInt("members");
        int popularity = jsonObject.getInt("popularity");
        int rank = jsonObject.getInt("rank");
        String synopsis = jsonObject.getString("synopsis");
        String imageUrl = jsonObject.getString("imageUrl");
        Estado_vistoONo_enum vistoONo = Estado_vistoONo_enum.valueOf(jsonObject.getString("vistoONo"));


        // Crear el objeto Manga
        return new Manga(id, members, title, popularity, rank, score, status, synopsis, title, imageUrl, vistoONo, chapters, volumes);
    }
}
