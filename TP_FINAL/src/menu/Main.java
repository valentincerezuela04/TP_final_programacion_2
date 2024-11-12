package menu;

import api.GetManga;
import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;
import excepciones.GestorExcepciones;
import gestores.GestorUsuarios;
import manejo_json.JsonUtil;
import manejo_json.JsonUtilUsuario;
import usuario.Usuario;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        String nombre = "juan123";
        String contraseña = "Contrasena1";  // Contraseña válida
        String email = "juan@example.com"; // Email válido

        GestorUsuarios gestorUsuarios = new GestorUsuarios();

        try {
            // Intentamos registrar al usuario
            Usuario nuevoUsuario = new Usuario(nombre, contraseña, email);

            // Si el registro es exitoso, se guarda el usuario en el archivo JSON
            JsonUtilUsuario jsonUtilUsuario = new JsonUtilUsuario();
            gestorUsuarios.registrarUsuario(nombre, contraseña, email);
            jsonUtilUsuario.guardarUsuariosEnArchivo(gestorUsuarios.getUsuariosRegistrados());  // Guardamos en el archivo

            System.out.println("Usuario registrado con éxito: " + nuevoUsuario);

        } catch (ContrasenaInvalidaException e) {
            // Si hay una excepción de contraseña inválida, la manejamos aquí
            GestorExcepciones.manejarContrasenaInvalida(e);

        } catch (EmailInvalidoException e) {
            // Si hay una excepción de email inválido, la manejamos aquí
            GestorExcepciones.manejarEmailInvalido(e);

        } catch (Exception e) {
            // Para cualquier otra excepción no controlada, usamos el manejador genérico
            GestorExcepciones.manejarExcepcion(e);
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