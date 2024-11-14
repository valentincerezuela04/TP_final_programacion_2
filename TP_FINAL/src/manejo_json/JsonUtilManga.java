package manejo_json;

import contenido.Manga;
import contenido.EstadoVisto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
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
        String imageUrl = jsonObject.getJSONObject("images").getJSONObject("jpg").optString("image_url", "");

        // Obtener el valor de "vistoONo" con optString y asignar el valor predeterminado "NO_VISTO" si no está presente
        String vistoONoValue = jsonObject.optString("vistoONo", "NO_VISTO");
        EstadoVisto vistoONo = EstadoVisto.valueOf(vistoONoValue);

        // Crear y devolver el objeto Manga con los datos extraídos
        return new Manga(id, members, title, popularity, rank, score, status, synopsis, title, imageUrl, vistoONo, chapters, volumes);
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
            json.put("imageUrl", manga.getUrL_image());
            json.put("vistoONo", manga.getVistoONo().name());
            json.put("chapters", manga.getChapters());
            json.put("volumes", manga.getVolumes());

            jsonArray.put(json);
        }

        return jsonArray;
    }


    public static void guardarListaAnimeEnArchivo(List<Manga> manga, String archivoDestino) {
        // Usar la clase JsonUtilAnime para convertir el objeto Anime a JSON
        JSONArray mangaJson = JsonUtilManga.listToJson(manga);  // Convertir el objeto Anime a JSON

        try (FileWriter file = new FileWriter(archivoDestino, true)) {  // "true" para añadir sin sobrescribir
            file.write(mangaJson.toString(4));  // Guardar el JSON con indentación de 4 espacios
            file.write(",");  // Añadir una nueva línea entre cada anime para legibilidad
            System.out.println("Anime guardado en: " + archivoDestino);
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }
}