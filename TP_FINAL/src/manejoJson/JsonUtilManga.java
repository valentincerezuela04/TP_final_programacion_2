package manejoJson;

import api.GetManga;
import contenido.Manga;
import contenido.EstadoVisto;
import gestores.GestorExcepciones;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtilManga extends JsonUtil {


    //Método para convertir un objeto Manga a un JSONObject (Usado para Manga.json)
    @Override
    public JSONObject objectToJson(Object obj) {
        Manga manga = (Manga) obj;
        JSONObject json = new JSONObject();

        json.put("id", manga.getId());
        json.put("title", manga.getTitle());
        json.put("score", manga.getScore());
        json.put("status", manga.getStatus());
        json.put("chapters", manga.getChapters());
        json.put("volumes", manga.getVolumes());
        json.put("members", manga.getMembers());
        json.put("popularity", manga.getPopularity());
        json.put("rank", manga.getRank());
        json.put("synopsis", manga.getSynopsis());

        return json;
    }

    //Método para convertir un objeto Manga a un JSONObject (Usado para la lista personal)
    public JSONObject objectToJsonModificado(Object obj) {
        Manga manga = (Manga) obj;
        JSONObject json = new JSONObject();

        json.put("id", manga.getId());
        json.put("title", manga.getTitle());
        json.put("score", manga.getScore());
        json.put("popularity", manga.getPopularity());
        json.put("volumes", manga.getVolumes());
        json.put("chapters", manga.getChapters());
        json.put("vistoONo", manga.getVistoONo().name());

        return json;
    }

    //Método para convertir un JSONObject en un objeto Manga
    @Override
    public Object jsonToObject(JSONObject jsonObject) {
        int id = jsonObject.optInt("mal_id", jsonObject.optInt("id", 0));
        String title = jsonObject.getString("title");
        double score = jsonObject.optDouble("score", 0.0);
        String status = jsonObject.optString("status", "Desconocido");
        int chapters = jsonObject.optInt("chapters", 0);
        int volumes = jsonObject.optInt("volumes", 0);
        int members = jsonObject.optInt("members", 0);
        int popularity = jsonObject.optInt("popularity", 0);
        int rank = jsonObject.optInt("rank", 0);
        String synopsis = jsonObject.optString("synopsis", "Sin sinopsis disponible");

        String vistoONoString = jsonObject.optString("vistoONo", "NO_VISTO");
        EstadoVisto vistoONo = EstadoVisto.valueOf(vistoONoString);

        return new Manga(id, members, popularity, rank, score, status, synopsis, title, vistoONo, chapters, volumes);
    }

    // Método para crear un archivo Manga.json
    @Override
    public void crearArchivoPorDefecto() {
        GetManga getManga = new GetManga();

        getManga.obtenerYGuardarDataFiltrada("Manga.json");
    }

    // Método para convertir una lista de objetos Manga a un JSONArray (Usado para Manga.json)
    public static JSONArray listToJson(List<Manga> mangaList) {
        JSONArray jsonArray = new JSONArray();

        for (Manga manga : mangaList) {
            JSONObject json = new JSONObject();

            json.put("id", manga.getId());
            json.put("title", manga.getTitle());
            json.put("score", manga.getScore());
            json.put("status", manga.getStatus());
            json.put("members", manga.getMembers());
            json.put("popularity", manga.getPopularity());
            json.put("rank", manga.getRank());
            json.put("synopsis", manga.getSynopsis());
            json.put("chapters", manga.getChapters());
            json.put("volumes", manga.getVolumes());

            jsonArray.put(json);
        }

        return jsonArray;
    }

    // Método para convertir una lista de objetos Manga a un JSONArray (Usado para la lista personal)
    public static JSONArray listToJsonUsuario(List<Manga> mangaList) {
        JSONArray jsonArray = new JSONArray();

        for (Manga manga : mangaList) {
            JSONObject json = new JSONObject();
            json.put("id", manga.getId());
            json.put("title", manga.getTitle());
            json.put("popularity", manga.getPopularity());
            json.put("score", manga.getScore());
            json.put("volumes", manga.getVolumes());
            json.put("chapters", manga.getChapters());
            json.put("vistoONo", manga.getVistoONo());

            jsonArray.put(json);
        }

        return jsonArray;
    }

    // Método para escribir el objeto Manga en un archivo usando JsonUtilManga
    public static void guardarListaMangaEnArchivo(List<Manga> manga, String archivoDestino) {
        JSONArray mangaJson = JsonUtilManga.listToJson(manga);

        try (FileWriter file = new FileWriter(archivoDestino, false)) {
            file.write(mangaJson.toString(4));
            file.flush();
        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
        }
    }

    // Método para cargar una lista de mangas desde un archivo JSON
    public List<Manga> cargarMangasDesdeArchivo(String archivoOrigen) {
        List<Manga> listaDeMangas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoOrigen))) {
            StringBuilder contenido = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                contenido.append(linea);
            }

            JSONArray mangasArray = new JSONArray(contenido.toString());

            for (int i = 0; i < mangasArray.length(); i++) {
                JSONObject mangaJson = mangasArray.getJSONObject(i);

                if (mangaJson.has("id") && !mangaJson.has("mal_id")) {
                    mangaJson.put("mal_id", mangaJson.getInt("id"));
                }

                mangaJson.remove("images");

                Manga manga = (Manga) new JsonUtilManga().jsonToObject(mangaJson);
                listaDeMangas.add(manga);
            }
        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
        }
        return listaDeMangas;
    }

    // Método para mostrar en consola la lista de mangas cargados desde un archivo
    public void mostrarMangasConsola(String archivoDestino) {
        List<Manga> listaDeMangas = cargarMangasDesdeArchivo(archivoDestino);

        if (listaDeMangas != null && !listaDeMangas.isEmpty()) {
            System.out.println("---- Lista de Mangas ----");
            for (Manga manga : listaDeMangas) {
                System.out.printf("ID: %d%n", manga.getId());
                System.out.printf("Título: %s%n", manga.getTitle());
                System.out.printf("Puntaje: %.2f | Estado: %s%n", manga.getScore(), manga.getStatus());
                System.out.println("----------------------------------------");
            }
        } else {
            System.out.println("No se encontraron mangas para mostrar.");
        }
    }
}