package menu;

import contenido.Anime;
import contenido.EstadoVisto;
import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;
import manejo_json.JsonUtilUsuario;
import usuario.Usuario;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) {

        try {
            // Crear un anime
            Anime anime = new Anime(1, 5000, "Naruto", 855, 1, 8.7, "Airing", "Naruto es un joven ninja...",
                    "Naruto", EstadoVisto.NO_VISTO, 220);

            // Crear un usuario
            Usuario usuario = new Usuario("juan123", "Argentina10", "juan@example.com");

            // Agregar el anime a la lista del usuario
            usuario.getAnimes().add(anime);

            HashMap<String, Usuario> mapaUsuarios = new HashMap<>();
            mapaUsuarios.put(usuario.getNombre(), usuario);
            // Guardar el usuario con el anime en un archivo JSON
            JsonUtilUsuario.guardarUsuariosEnArchivo(mapaUsuarios);

            System.out.println("Usuario con anime agregado y datos guardados en el archivo JSON.");
        } catch (ContrasenaInvalidaException | EmailInvalidoException | IOException e) {
            System.err.println("Error al guardar el usuario: " + e.getMessage());
        }

//        GetAnime getAnime = new GetAnime();
//        getAnime.obtenerYGuardarDataFiltrada("pruebaAnime.json");
//        JsonUtilAnime jsonUtilAnime = new JsonUtilAnime();
//        jsonUtilAnime.mostrarAnimesConsola("pruebaAnime.json");
//
//        GetManga getManga = new GetManga();
//        getManga.obtenerYGuardarDataFiltrada("pruebaManga.json");
//        JsonUtilManga jsonUtilManga = new JsonUtilManga();
//        jsonUtilManga.mostrarMangasConsola("pruebaManga.json");
//        Menu menu = new Menu();
//       menu.mostrarMenuPrincipal();


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

//        System.out.println(Get_Manga.getDataApi())
    }
}