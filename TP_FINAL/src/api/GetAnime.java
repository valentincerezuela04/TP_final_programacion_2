package api;
import contenido.*;
import manejo_json.*;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class GetAnime implements IApis {

    @Override
    public String getDataApi() {
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

    @Override
    public void obtenerYGuardarDataFiltrada(String archivoDestino) {
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
                // Aseguramos que el objeto contiene el campo "data"
                if (jsonObject.has("data")) {
                    JSONArray animesArray = jsonObject.getJSONArray("data");  // Aquí están los animes

                    List<Anime> lista_de_animes = new ArrayList<>();
                    JsonUtilAnime jsonUtilAnime = new JsonUtilAnime();

                    for (int i = 0; i < animesArray.length(); i++) {
                        JSONObject animeJson = animesArray.getJSONObject(i);
                        Anime anime = (Anime) jsonUtilAnime.jsonToObject(animeJson);

                        lista_de_animes.add(anime);
                    }

                    // Guardar el anime en un archivo usando JsonUtilAnime
                    JsonUtilAnime.guardarListaAnimeEnArchivo(lista_de_animes, archivoDestino);
                } else {
                    System.out.println("No se encontró el campo 'data' en la respuesta de la API.");
                }
            } else {
                System.out.println("La solicitud GET falló con el código de estado: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Anime> cargarAnimesDesdeArchivo(String archivoDestino) {
        List<Anime> listaDeAnimes = new ArrayList<>();

        try (FileReader reader = new FileReader(archivoDestino)) {
            JSONArray jsonArray = new JSONArray(new JSONTokener(reader));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Verificar y renombrar el campo "id" a "mal_id" si existe
                if (jsonObject.has("id") && !jsonObject.has("mal_id")) {
                    jsonObject.put("mal_id", jsonObject.getInt("id"));
                }

                // Omitir el campo "images" si está presente
                jsonObject.remove("images");

                Anime anime = (Anime) new JsonUtilAnime().jsonToObject(jsonObject);
                listaDeAnimes.add(anime);
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo.");
            e.printStackTrace();
        }

        return listaDeAnimes;
    }


    public void mostrarAnimes() {
        String archivoDestino = "pruebaAnime.json"; // Asegúrate de que esta ruta es correcta
        List<Anime> listaDeAnimes = cargarAnimesDesdeArchivo(archivoDestino);

        if (listaDeAnimes != null && !listaDeAnimes.isEmpty()) {
            System.out.println("---- Lista de Animes ----");
            for (Anime anime : listaDeAnimes) {
                System.out.println("ID: " + anime.getId());
                System.out.println("Título: " + anime.getTitle());
                System.out.println("Puntaje: " + anime.getScore());
                System.out.println("Estado: " + anime.getStatus());
                System.out.println("Sinopsis: " + anime.getSynopsis());
                System.out.println("------------------------------------------------");
            }
        } else {
            System.out.println("No se encontraron animes para mostrar.");
        }
    }
}
