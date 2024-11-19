package manejoJson;

import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;
import gestores.GestorExcepciones;
import org.json.JSONArray;
import org.json.JSONObject;
import usuario.Usuario;
import contenido.Anime;
import contenido.Manga;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class JsonUtilUsuario extends JsonUtil {

    private static final String ARCHIVO_USUARIOS = "usuarios.json";

    // Método para convertir un objeto Usuario en un JSONObject con sus propiedades.
    @Override
    public JSONObject objectToJson(Object obj) {
        Usuario usuario = (Usuario) obj;
        JSONObject usuarioJson = new JSONObject();
        usuarioJson.put("id", usuario.getId());
        usuarioJson.put("nombre", usuario.getNombre());
        usuarioJson.put("contraseña", usuario.getContraseña());
        usuarioJson.put("email", usuario.getEmail());

        JsonUtilAnime jsonUtilAnime = new JsonUtilAnime();

        if (usuario.getAnimes() != null && !usuario.getAnimes().isEmpty()) {
            JSONArray animeJsonArray = new JSONArray();
            for (Anime anime : usuario.getAnimes()) {
                animeJsonArray.put(jsonUtilAnime.objectToJsonModificado(anime));
            }
            usuarioJson.put("listaAnime", animeJsonArray);
        }

        JsonUtilManga jsonUtilManga = new JsonUtilManga();

        JSONArray mangaJsonArray = new JSONArray();
        if (usuario.getMangas() != null && !usuario.getMangas().isEmpty()) {
            for (Manga manga : usuario.getMangas()) {
                mangaJsonArray.put(jsonUtilManga.objectToJsonModificado(manga));
            }
        }
        usuarioJson.put("listaManga", mangaJsonArray);

        return usuarioJson;
    }

    // Método para convertir un JSONObject en un objeto Usuario.
    @Override
    public Usuario jsonToObject(JSONObject jsonObject) throws ContrasenaInvalidaException, EmailInvalidoException {
        String nombre = jsonObject.getString("nombre");
        String contraseña = jsonObject.getString("contraseña");
        String email = jsonObject.getString("email");
        int id = jsonObject.getInt("id");

        Usuario usuario = new Usuario(nombre, contraseña, email);
        usuario.setId(id);

        JsonUtilAnime jsonUtilAnime = new JsonUtilAnime();

        if (jsonObject.has("listaAnime")) {
            JSONArray animeJsonArray = jsonObject.getJSONArray("listaAnime");
            List<Anime> listaAnime = new ArrayList<>();
            for (int i = 0; i < animeJsonArray.length(); i++) {
                Anime anime = (Anime) jsonUtilAnime.jsonToObject(animeJsonArray.getJSONObject(i));
                listaAnime.add(anime);
            }
            usuario.setAnimes(listaAnime);
        }

        JsonUtilManga jsonUtilManga = new JsonUtilManga();

        if (jsonObject.has("listaManga")) {
            JSONArray mangaJsonArray = jsonObject.getJSONArray("listaManga");
            List<Manga> listaManga = new ArrayList<>();
            for (int i = 0; i < mangaJsonArray.length(); i++) {
                Manga manga = (Manga) jsonUtilManga.jsonToObject(mangaJsonArray.getJSONObject(i));
                listaManga.add(manga);
            }
            usuario.setMangas(listaManga);
        }

        return usuario;
    }

    // Método para crear un archivo por defecto con una lista vacía de usuarios.
    @Override
    public void crearArchivoPorDefecto() {
        File archivo = new File(ARCHIVO_USUARIOS);

        if (archivo.exists()) {
            return;
        }

        JSONObject defaultUsuarios = new JSONObject();
        defaultUsuarios.put("usuarios", new JSONArray());

        JsonUtil.writeJsonToFile(ARCHIVO_USUARIOS, defaultUsuarios);
        System.out.println("Archivo por defecto creado exitosamente.");
    }

    // Método para modificar un usuario en el archivo "usuarios.json".
    public static void modificarUsuarioEnArchivo(Usuario usuarioModificado) throws IOException {
        JSONObject usuariosJson = readJsonFromFile(ARCHIVO_USUARIOS);
        JSONArray usuariosArray = usuariosJson.getJSONArray("usuarios");

        for (int i = 0; i < usuariosArray.length(); i++) {
            JSONObject usuarioJson = usuariosArray.getJSONObject(i);
            int id = usuarioJson.getInt("id");

            if (id == usuarioModificado.getId()) {
                JsonUtilUsuario jsonUtilUsuario = new JsonUtilUsuario();
                JSONObject usuarioModificadoJson = jsonUtilUsuario.objectToJson(usuarioModificado);

                usuariosArray.put(i, usuarioModificadoJson);
                break;
            }
        }

        writeJsonToFile(ARCHIVO_USUARIOS, new JSONObject().put("usuarios", usuariosArray));
    }

    // Método para guardar todos los usuarios en el archivo "usuarios.json".
    public static void guardarUsuariosEnArchivo(HashMap<String, Usuario> usuarios) throws IOException {
        ArrayList<Usuario> listaUsuarios = new ArrayList<>(usuarios.values());

        Collections.sort(listaUsuarios, Comparator.comparingInt(Usuario::getId));

        JsonUtilAnime jsonUtilAnime = new JsonUtilAnime();
        JsonUtilManga jsonUtilManga = new JsonUtilManga();

        JSONArray usuariosJson = new JSONArray();
        for (Usuario usuario : listaUsuarios) {
            JSONObject usuarioJson = new JSONObject();
            usuarioJson.put("id", usuario.getId());
            usuarioJson.put("nombre", usuario.getNombre());
            usuarioJson.put("email", usuario.getEmail());
            usuarioJson.put("contraseña", usuario.getContraseña());

            JSONArray listaAnimeJson = jsonUtilAnime.listToJsonUsuario(usuario.getAnimes());
            usuarioJson.put("listaAnime", listaAnimeJson);

            JSONArray listaMangaJson = jsonUtilManga.listToJsonUsuario(usuario.getMangas());
            usuarioJson.put("listaManga", listaMangaJson);

            usuariosJson.put(usuarioJson);
        }

        writeJsonToFile(ARCHIVO_USUARIOS, new JSONObject().put("usuarios", usuariosJson));
    }

    // Método para cargar los usuarios desde el archivo "usuarios.json".
    public static HashMap<String, Usuario> cargarUsuariosDesdeArchivo() throws IOException, ContrasenaInvalidaException, EmailInvalidoException {
        HashMap<String, Usuario> usuarios = new HashMap<>();

        File archivo = new File(ARCHIVO_USUARIOS);
        if (!archivo.exists()) {
            return usuarios;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(ARCHIVO_USUARIOS)), StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(content);

            JSONArray usuariosJsonArray = jsonObject.getJSONArray("usuarios");

            JsonUtilUsuario jsonUtilUsuario = new JsonUtilUsuario();

            if (usuariosJsonArray != null) {
                for (int i = 0; i < usuariosJsonArray.length(); i++) {
                    JSONObject usuarioJson = usuariosJsonArray.getJSONObject(i);
                    Usuario usuario = jsonUtilUsuario.jsonToObject(usuarioJson);
                    usuarios.put(usuario.getNombre(), usuario);
                }
            }
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }

        return usuarios;
    }

    // Método para actualizar un usuario en el archivo "usuarios.json".
    public static void actualizarUsuarioEnArchivo(Usuario usuarioActualizado) throws IOException, ContrasenaInvalidaException, EmailInvalidoException {

        HashMap<String, Usuario> usuarios = cargarUsuariosDesdeArchivo();

        if (usuarios.containsKey(usuarioActualizado.getNombre())) {
            usuarios.put(usuarioActualizado.getNombre(), usuarioActualizado);
        } else {
            System.err.println("Usuario no encontrado en el archivo para actualizar: " + usuarioActualizado.getNombre());
            return;
        }

        guardarUsuariosEnArchivo(usuarios);
    }
}
