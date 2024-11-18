package api;

import contenido.*;
import manejo_json.*;
import gestores.GestorExcepciones;
import excepciones.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

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
                throw new PeticionApiException("La solicitud GET falló con el código de estado: " + response.statusCode());
            }
        } catch (PeticionApiException e) {
            GestorExcepciones.manejarPeticionApiException(e);
            return e.getMessage();
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
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
                    JSONArray animesArray = jsonObject.getJSONArray("data");  // Acá están los animes

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
                    throw new RespuestaApiException("No se encontró el campo 'data' en la respuesta de la API.");
                }
            } else {
                throw new PeticionApiException("La solicitud GET falló con el código de estado: " + response.statusCode());
            }
        } catch (PeticionApiException e) {
            GestorExcepciones.manejarPeticionApiException(e);
        } catch (RespuestaApiException e) {
            GestorExcepciones.manejarRespuestaApiException(e);
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }
}