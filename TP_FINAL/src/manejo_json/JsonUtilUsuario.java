package manejo_json;

import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;
import gestores.GestorExcepciones;
import org.json.JSONArray;
import org.json.JSONObject;
import usuario.Usuario;
import contenido.Anime;
import contenido.Manga;

import java.io.File;
import java.io.FileWriter;
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

    private static final String ARCHIVO_USUARIOS = "usuarios.json"; // Archivo donde se guardarán los usuarios

    @Override
    public JSONObject objectToJson(Object obj) {
        Usuario usuario = (Usuario) obj;
        JSONObject usuarioJson = new JSONObject();
        usuarioJson.put("id", usuario.getId());
        usuarioJson.put("nombre", usuario.getNombre());
        usuarioJson.put("contraseña", usuario.getContraseña());
        usuarioJson.put("email", usuario.getEmail());

        JsonUtilAnime jsonUtilAnime = new JsonUtilAnime();
        // Incluir listas de animes o mangas si existen
        if (usuario.getAnimes() != null && !usuario.getAnimes().isEmpty()) {
            JSONArray animeJsonArray = new JSONArray();
            for (Anime anime : usuario.getAnimes()) {
                animeJsonArray.put(jsonUtilAnime.objectToJsonModificado(anime));
            }
            usuarioJson.put("listaAnime", animeJsonArray);
        }

        JsonUtilManga jsonUtilManga = new JsonUtilManga();
        // Asegurarse de incluir la listaManga vacía si no hay mangas
        JSONArray mangaJsonArray = new JSONArray();
        if (usuario.getMangas() != null && !usuario.getMangas().isEmpty()) {
            for (Manga manga : usuario.getMangas()) {
                mangaJsonArray.put(jsonUtilManga.objectToJsonModificado(manga));
            }
        }
        usuarioJson.put("listaManga", mangaJsonArray); // Siempre incluir listaManga

        return usuarioJson;
    }


    @Override
    public Usuario jsonToObject(JSONObject jsonObject) throws ContrasenaInvalidaException, EmailInvalidoException {
        String nombre = jsonObject.getString("nombre");
        String contraseña = jsonObject.getString("contraseña");
        String email = jsonObject.getString("email");
        int id = jsonObject.getInt("id");

        // Inicializamos el usuario con nombre, contraseña y email
        Usuario usuario = new Usuario(nombre, contraseña, email);
        usuario.setId(id); // Establecer el ID correcto

        JsonUtilAnime jsonUtilAnime = new JsonUtilAnime();
        // Cargar listas de anime si están presentes
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
        // Cargar listas de manga si están presentes
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

    public static void modificarUsuarioEnArchivo(Usuario usuarioModificado) throws IOException {
        // Leer el archivo JSON que contiene los usuarios
        JSONObject usuariosJson = readJsonFromFile(ARCHIVO_USUARIOS);
        JSONArray usuariosArray = usuariosJson.getJSONArray("usuarios");

        // Buscar el usuario en el array de usuarios
        for (int i = 0; i < usuariosArray.length(); i++) {
            JSONObject usuarioJson = usuariosArray.getJSONObject(i);
            int id = usuarioJson.getInt("id");

            // Si encontramos el usuario con el mismo ID, lo modificamos
            if (id == usuarioModificado.getId()) {
                // Llamar al método objectToJson() para convertir el usuario modificado a un JSONObject
                JsonUtilUsuario jsonUtilUsuario = new JsonUtilUsuario();
                JSONObject usuarioModificadoJson = jsonUtilUsuario.objectToJson(usuarioModificado);

                // Reemplazar el usuario en la posición correspondiente
                usuariosArray.put(i, usuarioModificadoJson);
                break;
            }
        }

        // Guardar los usuarios modificados de vuelta al archivo
        writeJsonToFile(ARCHIVO_USUARIOS, new JSONObject().put("usuarios", usuariosArray));
    }


    // Implementación del método guardarUsuariosEnArchivo según tu estructura
    public static void guardarUsuariosEnArchivo(HashMap<String, Usuario> usuarios) throws IOException {
        ArrayList<Usuario> listaUsuarios = new ArrayList<>(usuarios.values());

        // Ordenar la lista de usuarios por ID
        Collections.sort(listaUsuarios, Comparator.comparingInt(Usuario::getId));

        JsonUtilAnime jsonUtilAnime = new JsonUtilAnime(); // Crear instancia de JsonUtilAnime
        JsonUtilManga jsonUtilManga = new JsonUtilManga(); // Crear instancia de JsonUtilManga

        // Convertir la lista ordenada en un JSONArray
        JSONArray usuariosJson = new JSONArray();
        for (Usuario usuario : listaUsuarios) {
            JSONObject usuarioJson = new JSONObject();
            usuarioJson.put("id", usuario.getId());
            usuarioJson.put("nombre", usuario.getNombre());
            usuarioJson.put("email", usuario.getEmail());
            usuarioJson.put("contraseña", usuario.getContraseña());

            // Convertir la lista de animes del usuario usando JsonUtilAnime.listToJsonUsuario
            JSONArray listaAnimeJson = jsonUtilAnime.listToJsonUsuario(usuario.getAnimes());
            usuarioJson.put("listaAnime", listaAnimeJson);

            JSONArray listaMangaJson = jsonUtilManga.listToJsonUsuario(usuario.getMangas());
            usuarioJson.put("listaManga", listaMangaJson);

            usuariosJson.put(usuarioJson); // Agrega el JSON del usuario al JSONArray
        }

        // Guardar en el archivo
        writeJsonToFile(ARCHIVO_USUARIOS, new JSONObject().put("usuarios", usuariosJson));
    }

    // Cargar los usuarios desde el archivo
    public static HashMap<String, Usuario> cargarUsuariosDesdeArchivo() throws IOException, ContrasenaInvalidaException, EmailInvalidoException {
        HashMap<String, Usuario> usuarios = new HashMap<>();

        // Verificar si el archivo existe
        File archivo = new File(ARCHIVO_USUARIOS);
        if (!archivo.exists()) {
            System.err.println("El archivo de usuarios no existe.");
            return usuarios; // Retornar un HashMap vacío si no existe el archivo
        }

        try {
            // Leer el archivo como un JSONObject
            String content = new String(Files.readAllBytes(Paths.get(ARCHIVO_USUARIOS)), StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(content);

            // Extraer el array de usuarios
            JSONArray usuariosJsonArray = jsonObject.getJSONArray("usuarios");

            JsonUtilUsuario jsonUtilUsuario = new JsonUtilUsuario();
            // Iterar sobre el array de usuarios y convertirlos a objetos Usuario
            if (usuariosJsonArray != null) {
                for (int i = 0; i < usuariosJsonArray.length(); i++) {
                    JSONObject usuarioJson = usuariosJsonArray.getJSONObject(i);
                    Usuario usuario = jsonUtilUsuario.jsonToObject(usuarioJson); // Llamar a jsonToObject estático
                    usuarios.put(usuario.getNombre(), usuario);
                }
            }
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }

        return usuarios;
    }

    // Actualizar un usuario específico en el archivo
    public static void actualizarUsuarioEnArchivo(Usuario usuarioActualizado) throws IOException, ContrasenaInvalidaException, EmailInvalidoException {
        // Cargar todos los usuarios desde el archivo
        HashMap<String, Usuario> usuarios = cargarUsuariosDesdeArchivo();

        // Actualizar el usuario en el HashMap si existe
        if (usuarios.containsKey(usuarioActualizado.getNombre())) {
            // Si el usuario ya existe, actualizamos sus datos en el HashMap
            usuarios.put(usuarioActualizado.getNombre(), usuarioActualizado);
        } else {
            System.err.println("Usuario no encontrado en el archivo para actualizar: " + usuarioActualizado.getNombre());
            return;
        }

        // Guardar los usuarios actualizados en el archivo, ordenados por ID
        guardarUsuariosEnArchivo(usuarios);
    }
}
