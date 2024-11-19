package gestores;

import manejoJson.JsonUtilUsuario;
import manejoJson.JsonUtilAnime;
import manejoJson.JsonUtilManga;

public class GestorArchivos {

    // Método para crear los archivos por defecto
    public static void crearArchivosPorDefecto() {

        JsonUtilUsuario jsonUtilUsuario = new JsonUtilUsuario();
        jsonUtilUsuario.crearArchivoPorDefecto();

        JsonUtilAnime jsonUtilAnime = new JsonUtilAnime();
        jsonUtilAnime.crearArchivoPorDefecto();

        JsonUtilManga jsonUtilManga = new JsonUtilManga();
        jsonUtilManga.crearArchivoPorDefecto();
    }
}

