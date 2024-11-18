package gestores;

import manejo_json.JsonUtilUsuario;
import manejo_json.JsonUtilAnime;
import manejo_json.JsonUtilManga;

public class GestorArchivos {

    // Método para crear los archivos por defecto
    public static void crearArchivosPorDefecto() {
        // Crear archivo de usuarios
        JsonUtilUsuario jsonUtilUsuario = new JsonUtilUsuario();
        jsonUtilUsuario.crearArchivoPorDefecto(); // Crea "usuarios.json"

        // Crear archivo de animes
        JsonUtilAnime jsonUtilAnime = new JsonUtilAnime();
        jsonUtilAnime.crearArchivoPorDefecto(); // Crea "anime.json"

        // Crear archivo de mangas
        JsonUtilManga jsonUtilManga = new JsonUtilManga();
        jsonUtilManga.crearArchivoPorDefecto(); // Crea "manga.json"
    }
}

