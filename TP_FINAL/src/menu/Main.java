package menu;

import api.GetAnime;
import api.GetManga;


public class Main {
    public static void main(String[] args) {

        GetAnime getAnime = new GetAnime();
        getAnime.obtenerYGuardarDataFiltrada("pruebaAnime.json");
        getAnime.mostrarAnimes();
       Menu menu = new Menu();
       menu.mostrarMenuPrincipal();


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

//
//      Consumiendo_api.obtenerYGuardarDataFiltrada("prueba.json");
//        System.out.println(JsonUtil.readJsonFromFileArray("prueba.json"));

//        System.out.println(Get_Manga.getDataApi());
        GetManga getManga = new GetManga();
        getManga.obtenerYGuardarDataFiltrada("pruebaManga.json");
    }
}