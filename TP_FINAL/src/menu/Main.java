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

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        // Crear un objeto JsonUtilUsuario para gestionar los archivos JSON
        JsonUtilUsuario jsonUtilUsuario = new JsonUtilUsuario();

        // Cargar los usuarios desde los archivos
        GestorUsuarios gestorUsuarios = new GestorUsuarios();

        try {
            // Crear nuevos usuarios
            Usuario nuevoUsuario = new Usuario("Pablo", "Contraseña1", "pablo@gmail.com");
            Usuario nuevoUsuario1 = new Usuario("Santiago", "Contraseña2", "santiago@gmail.com");
            Usuario nuevoUsuario2 = new Usuario("Emma", "Contraseña3", "emma@gmail.com");
            System.out.println("ID del nuevo usuario: " + nuevoUsuario.getId());
            System.out.println("ID del nuevo usuario: " + nuevoUsuario1.getId());
            System.out.println("ID del nuevo usuario: " + nuevoUsuario2.getId());

            // Registrar los usuarios en el gestor
            gestorUsuarios.registrarUsuario(nuevoUsuario);
            gestorUsuarios.registrarUsuario(nuevoUsuario1);
            gestorUsuarios.registrarUsuario(nuevoUsuario2);

            // Guardar los usuarios en el archivo
            jsonUtilUsuario.guardarUsuariosEnArchivo(gestorUsuarios.getUsuariosRegistrados());

        } catch (IOException | ContrasenaInvalidaException | EmailInvalidoException e) {
            e.printStackTrace();
        }



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