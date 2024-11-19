package api;

import contenido.*;
import manejoJson.*;
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

    // Método para obtener datos de la API
    @Override
    public String getDataApi() {
        try {
            String apiUrl = "https://api.jikan.moe/v4/anime";
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

    // Método para obtener y guardar los datos filtrados de la API en un archivo
    @Override
    public void obtenerYGuardarDataFiltrada(String archivoDestino) {
        String apiUrl = "https://api.jikan.moe/v4/anime";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .header("Accept", "application/json")
                .build();

        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String jsonResponse = response.body();

                JSONObject jsonObject = new JSONObject(jsonResponse);

                if (jsonObject.has("data")) {
                    JSONArray animesArray = jsonObject.getJSONArray("data");

                    List<Anime> lista_de_animes = new ArrayList<>();
                    JsonUtilAnime jsonUtilAnime = new JsonUtilAnime();

                    for (int i = 0; i < animesArray.length(); i++) {
                        JSONObject animeJson = animesArray.getJSONObject(i);
                        Anime anime = (Anime) jsonUtilAnime.jsonToObject(animeJson);

                        lista_de_animes.add(anime);
                    }

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
