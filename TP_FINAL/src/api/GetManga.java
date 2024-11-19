package api;

import contenido.Manga;
import manejoJson.JsonUtilManga;
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

public class GetManga implements IApis {

    // Método para obtener datos de la API
    @Override
    public String getDataApi() {
        try {
            String apiUrl = "https://api.jikan.moe/v4/manga";
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
        String apiUrl = "https://api.jikan.moe/v4/manga";
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
                    JSONArray mangasArray = jsonObject.getJSONArray("data");

                    List<Manga> lista_de_mangas = new ArrayList<>();
                    JsonUtilManga jsonUtilManga = new JsonUtilManga();

                    for (int i = 0; i < mangasArray.length(); i++) {
                        JSONObject mangaJson = mangasArray.getJSONObject(i);
                        Manga manga = (Manga) jsonUtilManga.jsonToObject(mangaJson);

                        lista_de_mangas.add(manga);
                    }

                    JsonUtilManga.guardarListaMangaEnArchivo(lista_de_mangas, archivoDestino);
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
