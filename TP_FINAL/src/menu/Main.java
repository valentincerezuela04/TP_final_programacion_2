package menu;

import api.GetManga;
import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;
import excepciones.GestorExcepciones;
import gestores.GestorUsuarios;
import manejo_json.JsonUtil;
import manejo_json.JsonUtilUsuario;
import usuario.Usuario;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {

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
        System.out.println(JsonUtil.readJsonArrayFromFile("pruebaManga.json"));
    }
}