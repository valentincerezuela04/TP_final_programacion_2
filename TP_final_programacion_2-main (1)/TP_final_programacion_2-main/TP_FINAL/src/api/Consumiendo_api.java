package api;
import contenido.*;
import manejo_json.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;


public class Consumiendo_api {
//version vieja de como consumir una API igualmente funciona
//    public static void get_animes(){
//        try{
//            URL url = new URL("https://api.jikan.moe/v4/anime");
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//            con.connect();
//
//            int responseCode = con.getResponseCode();
//            if(responseCode != 200){
//                throw new RuntimeException("Ocurrio un errror" + responseCode);
//            }else{
//                StringBuilder informationString = new StringBuilder();
//                Scanner scanner = new Scanner(url.openStream());
//
//                while(scanner.hasNext()){
//                    informationString.append(scanner.nextLine());
//                }
//                scanner.close();
//
//                System.out.println(informationString);
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

public static String getAnime() {
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

    // Método para guardar la respuesta JSON en un archivo
    //se puede guardar igualmente aunque este como str no deja de ser un JSON
//    public static void GuardarJsonStringEnArchivo(String jsonData, String fileName) {
//        try {
//            // Convertir el String a un objeto JSONObject
//            JSONObject jsonObject = new JSONObject(jsonData);
//
//
//            FileWriter file = new FileWriter(fileName);
//
//            // Escribir el JSONObject formateado
//            file.write(jsonObject.toString(4)); // El parámetro 4 indica que se usa indentación de 4 espacios
//
//            file.flush();
//            file.close();
//
//            System.out.println("JSON guardado en: " + fileName);
//
//        } catch (IOException e) {
//            System.out.println("Error al guardar el archivo JSON: " + e.getMessage());
//        }
//    }

//    public static void GuardarJsonStringEnArchivo(String jsonData, String fileName) {
//        try {
//            // Intentar convertir el String en un JSONObject
//            Object json;
//            if (jsonData.trim().startsWith("{")) {
//                json = new JSONObject(jsonData);  // Es un objeto JSON
//            } else if (jsonData.trim().startsWith("[")) {
//                json = new JSONArray(jsonData);   // Es un arreglo JSON
//            } else {
//                throw new IllegalArgumentException("El formato de respuesta no es JSON válido");
//            }
//
//            FileWriter file = new FileWriter(fileName);
//
//            // Escribir el JSON formateado (objeto o arreglo)
//            if (json instanceof JSONObject) {
//                file.write(((JSONObject) json).toString(4)); // Indentación de 4 espacios
//            } else if (json instanceof JSONArray) {
//                file.write(((JSONArray) json).toString(4));  // Indentación de 4 espacios
//            }
//
//            file.flush();
//            file.close();
//
//            System.out.println("JSON guardado en: " + fileName);
//
//        } catch (IOException e) {
//            System.out.println("Error al guardar el archivo JSON: " + e.getMessage());
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }


//    // Método para convertir el JSON en objetos de tipo Anime
//    public static List<Anime> parseAnimeListFromJson() {
//        JSONArray animeArray = jsonToObject();
//        return JsonUtilAnime.convertirJsonArrayAAnime(animeArray);
//    }
//
//    // Método para guardar la lista de Anime en un archivo JSON
//    public static void guardarAnimeListEnArchivo(List<Anime> animeList, String fileName) {
//        JSONArray animeArray = JsonUtilAnime.convertirAnimeListaAJsonArray(animeList);
//
//        // Guardar el JSONArray en un archivo
//        try (FileWriter file = new FileWriter(fileName)) {
//            file.write(animeArray.toString(4));  // Indentación de 4 espacios
//            System.out.println("Archivo JSON guardado en: " + fileName);
//        } catch (IOException e) {
//            System.out.println("Error al guardar el archivo JSON: " + e.getMessage());
//        }
//    }

//
//    public static void buscarAnimePorTituloYGuardarEnArchivo(String titulo, String archivoDestino) {
//        // URL de la API de Jikan con búsqueda de anime por título
//        String apiUrl = "https://api.jikan.moe/v4/anime?q=" + titulo.replace(" ", "%20");
//
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(apiUrl))
//                .GET()
//                .header("Accept", "application/json")
//                .build();
//
//        try {
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            if (response.statusCode() == 200) {
//                JSONObject jsonResponse = new JSONObject(response.body());
//                JSONArray data = jsonResponse.getJSONArray("data");
//
//                if (data.length() > 0) {
//                    // Procesar el primer resultado
//                    JSONObject animeData = data.getJSONObject(0);
//
//                    // Crear un nuevo objeto JSON solo con los campos que queremos guardar
//                    JSONObject animeFiltrado = new JSONObject();
//
//                    animeFiltrado.put("titulo", animeData.optString("title", "Título no disponible"));
//                    animeFiltrado.put("sinopsis", animeData.optString("synopsis", "Sinopsis no disponible"));
//                    animeFiltrado.put("estado", animeData.optString("status", "Estado no disponible"));
//                    animeFiltrado.put("episodios", animeData.optInt("episodes", 0));
//                    animeFiltrado.put("puntaje", animeData.optDouble("score", 0.0));
//                    animeFiltrado.put("imagen", animeData.getJSONObject("images").getJSONObject("jpg").optString("image_url", "Imagen no disponible"));
//
//                    // Guardar el objeto filtrado en un archivo
//                    try (FileWriter file = new FileWriter(archivoDestino)) {
//                        file.write(animeFiltrado.toString(4));  // Formato JSON con indentación de 4 espacios
//                        System.out.println("Archivo JSON guardado en: " + archivoDestino);
//                    } catch (IOException e) {
//                        System.out.println("Error al guardar el archivo: " + e.getMessage());
//                    }
//
//                } else {
//                    System.out.println("No se encontraron animes con el título: " + titulo);
//                }
//            } else {
//                System.out.println("La solicitud GET falló con el código de estado: " + response.statusCode());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void obtenerAnimesYGuardarEnArchivo(String archivoDestino) {
//        // URL de la API de Jikan para obtener todos los animes
//        String apiUrl = "https://api.jikan.moe/v4/anime";  // Obtenemos la lista completa de animes
//
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(apiUrl))
//                .GET()
//                .header("Accept", "application/json")
//                .build();
//
//        try {
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            if (response.statusCode() == 200) {
//                // Parseamos el JSON completo devuelto por la API
//                JSONObject jsonResponse = new JSONObject(response.body());
//                JSONArray data = jsonResponse.getJSONArray("data");
//
//                if (data.length() > 0) {
//                    // Guardar el JSON completo en un archivo
//                    try (FileWriter file = new FileWriter(archivoDestino)) {
//                        file.write(jsonResponse.toString(4));  // Formato JSON con indentación de 4 espacios
//                        System.out.println("Archivo JSON guardado en: " + archivoDestino);
//                    } catch (IOException e) {
//                        System.out.println("Error al guardar el archivo: " + e.getMessage());
//                    }
//
//                } else {
//                    System.out.println("No se encontraron datos en la API.");
//                }
//            } else {
//                System.out.println("La solicitud GET falló con el código de estado: " + response.statusCode());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
public static void obtenerYGuardarAnimeFiltrado(String archivoDestino) {
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

            // Convertir cada anime a objeto de clase Anime
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
                String imageUrl = animeJson.optString("image_url", "");

                // Crear el objeto Anime usando la clase Anime
                Anime anime = new Anime(id, members, title, popularity, rank, score, status, synopsis, title, imageUrl, Estado_vistoONo_enum.NO_VISTO, episodes);

                // Guardar el anime en un archivo usando JsonUtilAnime
                guardarAnimeEnArchivo(anime, archivoDestino);
            }

        } else {
            System.out.println("La solicitud GET falló con el código de estado: " + response.statusCode());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // Método para guardar el objeto Anime en un archivo usando JsonUtilAnime
    private static void guardarAnimeEnArchivo(Anime anime, String archivoDestino) {
        // Usar la clase JsonUtilAnime para convertir el objeto Anime a JSON
        JsonUtilAnime jsonUtil = new JsonUtilAnime();
        JSONObject animeJson = jsonUtil.objectToJson(anime);  // Convertir el objeto Anime a JSON

        try (FileWriter file = new FileWriter(archivoDestino, true)) {  // "true" para añadir sin sobrescribir
            file.write(animeJson.toString(4));  // Guardar el JSON con indentación de 4 espacios
            file.write("\n");  // Añadir una nueva línea entre cada anime para legibilidad
            System.out.println("Anime guardado en: " + archivoDestino);
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    public static List<Anime> leerAnimesDesdeArchivo(String archivoOrigen) {
        List<Anime> listaAnimes = new ArrayList<>();
        JsonUtilAnime jsonUtilAnime = new JsonUtilAnime();

        try (Scanner scanner = new Scanner(new FileReader(archivoOrigen))) {
            StringBuilder jsonStringBuilder = new StringBuilder();

            // Leer todo el archivo y almacenarlo como un String
            while (scanner.hasNextLine()) {
                jsonStringBuilder.append(scanner.nextLine());
            }

            // Convertir el String en un JSONArray (o en un objeto JSON si es un solo anime)
            String jsonString = jsonStringBuilder.toString().trim();
            JSONArray animesJsonArray = new JSONArray(jsonString);

            // Convertir cada JSONObject en un objeto Anime y añadirlo a la lista
            for (int i = 0; i < animesJsonArray.length(); i++) {
                JSONObject animeJsonObject = animesJsonArray.getJSONObject(i);
                Anime anime = (Anime) jsonUtilAnime.jsonToObject(animeJsonObject);
                listaAnimes.add(anime);
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al procesar el archivo JSON: " + e.getMessage());
        }

        return listaAnimes;
    }
}
