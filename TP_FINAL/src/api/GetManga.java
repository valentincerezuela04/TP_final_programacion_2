package api;

import contenido.Manga;
import manejo_json.JsonUtilManga;
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

    @Override
    public void obtenerYGuardarDataFiltrada(String archivoDestino) {
        String apiUrl = "https://api.jikan.moe/v4/manga";  // URL de la API para mangas
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
                    JSONArray mangasArray = jsonObject.getJSONArray("data");  // Aquí están los mangas

                    List<Manga> lista_de_mangas = new ArrayList<>();
                    JsonUtilManga jsonUtilManga = new JsonUtilManga();

                    for (int i = 0; i < mangasArray.length(); i++) {
                        JSONObject mangaJson = mangasArray.getJSONObject(i);
                        Manga manga = (Manga) jsonUtilManga.jsonToObject(mangaJson);

                        lista_de_mangas.add(manga);
                    }

                    // Guardar el manga en un archivo usando JsonUtilManga
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

    @Override
    public String getDataApi() {
        try {
            String apiUrl = "https://api.jikan.moe/v4/manga"; // URL de la API
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
}
