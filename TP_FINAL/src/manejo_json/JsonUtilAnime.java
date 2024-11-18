package manejo_json;

import contenido.Anime;
import contenido.EstadoVisto;
import gestores.GestorExcepciones;
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
        json.put("episodes", anime.getEpisodes());
        json.put("members", anime.getMembers());
        json.put("popularity", anime.getPopularity());
        json.put("rank", anime.getRank());
        json.put("synopsis", anime.getSynopsis());

        return json;
    }

    public JSONObject objectToJsonModificado(Object obj) {
        Anime anime = (Anime) obj;
        JSONObject json = new JSONObject();

        json.put("id", anime.getId());
        json.put("title", anime.getTitle());
        json.put("score", anime.getScore());
        json.put("popularity", anime.getPopularity());
        json.put("episodes", anime.getEpisodes());
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
            json.put("episodes", anime.getEpisodes());
            json.put("members", anime.getMembers());
            json.put("popularity", anime.getPopularity());
            json.put("rank", anime.getRank());
            json.put("synopsis", anime.getSynopsis());

            jsonArray.put(json);
        }

        return jsonArray;
    }

    public static JSONArray listToJsonUsuario(List<Anime> animeList) {
        JSONArray jsonArray = new JSONArray();

        for (Anime anime : animeList) {
            JSONObject json = new JSONObject();
            json.put("id", anime.getId());
            json.put("title", anime.getTitle());
            json.put("popularity", anime.getPopularity());
            json.put("score", anime.getScore());
            json.put("episodes", anime.getEpisodes());
            json.put("vistoONo", anime.getVistoONo());

            jsonArray.put(json); // Agrega el JSON del anime al JSONArray
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

        // Verificar si el campo "vistoONo" está presente y es válido
        String vistoONoString = jsonObject.optString("vistoONo", "NO_VISTO"); // Valor predeterminado
        EstadoVisto vistoONo = EstadoVisto.valueOf(vistoONoString);

        return new Anime(id, members, popularity, rank, score, status, synopsis, title, vistoONo, episodes);
    }

    // Método para guardar la lista de animes en un archivo JSON
    public static void guardarListaAnimeEnArchivo(List<Anime> anime, String archivoDestino) {
        JSONArray jsonObject = JsonUtilAnime.listToJson(anime);

        try (FileWriter file = new FileWriter(archivoDestino, false)) {
            file.write(jsonObject.toString(4));
            file.flush();
        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
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
            GestorExcepciones.manejarIOException(e);
        }

        return listaDeAnimes;
    }


}
