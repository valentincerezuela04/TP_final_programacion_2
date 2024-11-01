package api;
import contenido.*;
import manejo_json.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class Consumiendo_api {

public static String getDataApi() {
    try {
        String apiUrl = "https://api.jikan.moe/v4/anime"; // URL de la API
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return ("Response: " + response.body());
        } else {
            return ("GET request failed: " + response.statusCode());
        }
    } catch (Exception e) {
        return e.getMessage();
    }
}


public static void obtenerYGuardarDataFiltrada(String archivoDestino) {
    String apiUrl = "https://api.jikan.moe/v4/anime";  // URL de la API

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .GET()
            .header("Accept", "application/json")
            .build();

    try {
        // Enviar la solicitud HTTP y obtener la respuesta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String jsonResponse = response.body();  // Obtener el cuerpo de la respuesta como un String

            // Procesar el JSON y extraer datos relevantes
              JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray animesArray = jsonObject.getJSONArray("data");  // Aquí están los animes
//            System.out.println(animesArray.toString());
            // Convertir cada anime a objeto de clase Anime

            List<Anime> lista_de_animes = new ArrayList<>();
            for (int i = 0; i < animesArray.length(); i++) {
                JSONObject animeJson = animesArray.getJSONObject(i);

                // Extraer los campos relevantes
                int id = animeJson.getInt("mal_id");
                String title = animeJson.getString("title");
                double score = animeJson.optDouble("score", 0.0);
                String status = animeJson.optString("status", "Desconocido");
                int episodes = animeJson.optInt("episodes", 0);
                int members = animeJson.optInt("members", 0);
                int popularity = animeJson.optInt("popularity", 0);
                int rank = animeJson.optInt("rank", 0);
                String synopsis = animeJson.optString("synopsis", "Sin sinopsis disponible");
                String imageUrl = animeJson.getJSONObject("images").getJSONObject("jpg").optString("image_url", "");


                // Crear el objeto Anime usando la clase Anime

                lista_de_animes.add(new Anime(id, members, title, popularity, rank, score, status, synopsis, title, imageUrl, Estado_vistoONo_enum.NO_VISTO, episodes));


            }

            // Guardar el anime en un archivo usando JsonUtilAnime
            JsonUtilAnime.guardarListaAnimeEnArchivo(lista_de_animes,archivoDestino);
        } else {
            System.out.println("La solicitud GET falló con el código de estado: " + response.statusCode());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}




}
