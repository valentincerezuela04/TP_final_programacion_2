package api;

import contenido.Anime;
import contenido.Manga;
import manejo_json.JsonUtilAnime;
import manejo_json.JsonUtilManga;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Get_Manga implements IApis {

        @Override
        public  String getDataApi() {
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
                    return ("GET request failed: " + response.statusCode());
                }
            } catch (Exception e) {
                return e.getMessage();
            }
        }

    @Override
        public  void obtenerYGuardarDataFiltrada(String archivoDestino) {
            String apiUrl = "https://api.jikan.moe/v4/manga";  // URL de la API

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
                    JSONArray mangaArray = jsonObject.getJSONArray("data");  // Aquí están los animes
//            System.out.println(mangaArray.toString());

                    List<Manga> lista_de_mangas = new ArrayList<>();
                    JsonUtilManga jsonUtilManga = new JsonUtilManga();
                    for (int i = 0; i < mangaArray.length(); i++) {
                        JSONObject mangaJson = mangaArray.getJSONObject(i);

                        Manga manga = (Manga) jsonUtilManga.jsonToObject(mangaJson);

                        lista_de_mangas.add(manga);

                    }

                    // Guardar el anime en un archivo usando JsonUtilAnime
                    JsonUtilManga.guardarListaAnimeEnArchivo(lista_de_mangas,archivoDestino);
                } else {
                    System.out.println("La solicitud GET falló con el código de estado: " + response.statusCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




    }



