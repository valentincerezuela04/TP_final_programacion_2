package manejoJson;

import api.GetAnime;
import contenido.Anime;
import contenido.EstadoVisto;
import gestores.GestorExcepciones;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonUtilAnime extends JsonUtil {

    // Método que convierte un objeto Anime a un JSONObject (Usado para el archivo Anime.json)
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

    // Método modificado que convierte un objeto Anime a un JSONObject. (Usado para la lista personal)
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

    // Método estático que convierte una lista de objetos Anime a un JSONArray (Usado para Anime.json)
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

    // Método estático que convierte una lista de objetos Anime a un JSONArray (Usado para la lista personal)
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

            jsonArray.put(json);
        }

        return jsonArray;
    }

    // Método que convierte un JSONObject en un objeto Anime.
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

        String vistoONoString = jsonObject.optString("vistoONo", "NO_VISTO");
        EstadoVisto vistoONo = EstadoVisto.valueOf(vistoONoString);

        return new Anime(id, members, popularity, rank, score, status, synopsis, title, vistoONo, episodes);
    }

    //Método para crear un archivo Anime.json
    @Override
    public void crearArchivoPorDefecto() {

        GetAnime getAnime = new GetAnime();

        getAnime.obtenerYGuardarDataFiltrada("Anime.json");
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

            while ((linea = reader.readLine()) != null) {
                contenido.append(linea);
            }

            JSONArray jsonArray = new JSONArray(contenido.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject animeJson = jsonArray.getJSONObject(i);

                if (animeJson.has("id") && !animeJson.has("mal_id")) {
                    animeJson.put("mal_id", animeJson.getInt("id"));
                }
                animeJson.remove("images");

                Anime anime = (Anime) this.jsonToObject(animeJson);
                listaDeAnimes.add(anime);
            }
        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
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
