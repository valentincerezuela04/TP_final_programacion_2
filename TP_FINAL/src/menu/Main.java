package menu;

import api.Consumiendo_api;
import manejo_json.JsonUtil;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

//        System.out.println(Consumiendo_api.getAnime());





        // Guardar los datos JSON en un archivo llamado "anime.json"
//        GuardarJsonStringEnArchivo(jsonResponse, "anime.json");
//        JsonUtil.readJsonFromFile("anime.json");

//          Consumiendo_api.obtenerYGuardarAnimeFiltrado("Animesss.json");
        // Leer los animes desde el archivo


//        List<Anime> listaAnimes = leerAnimesDesdeArchivo("Animesss.json");
//
//        // Imprimir la lista de animes
//        for (Anime anime : listaAnimes) {
//            System.out.println(anime);
//        }


      Consumiendo_api.obtenerYGuardarDataFiltrada("prueba.json");
        System.out.println(JsonUtil.readJsonFromFileArray("prueba.json"));
    }
}