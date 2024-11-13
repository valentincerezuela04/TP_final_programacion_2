package manejo_json;

import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;
import org.json.JSONArray;
import org.json.JSONObject;
import usuario.Usuario;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

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
        return usuarioJson;
    }

    @Override
    public Usuario jsonToObject(JSONObject jsonObject) throws ContrasenaInvalidaException, EmailInvalidoException {
        String nombre = jsonObject.getString("nombre");
        String contraseña = jsonObject.getString("contraseña");
        String email = jsonObject.getString("email");
        int id = jsonObject.getInt("id");

        Usuario usuario = new Usuario(nombre, contraseña, email);
        usuario.setId(id); // Establecer el ID correcto
        return usuario;
    }

    // Guardar los usuarios en un archivo, ordenados por ID
    public void guardarUsuariosEnArchivo(HashMap<String, Usuario> usuarios) throws IOException {
        ArrayList<Usuario> listaUsuarios = new ArrayList<>(usuarios.values());

        // Ordenar la lista de usuarios por ID
        Collections.sort(listaUsuarios, Comparator.comparingInt(Usuario::getId));

        // Convertir la lista ordenada en un JSONArray
        JSONArray usuariosJson = new JSONArray();
        for (Usuario usuario : listaUsuarios) {
            usuariosJson.put(objectToJson(usuario));
        }

        // Guardar en el archivo
        writeJsonToFile(ARCHIVO_USUARIOS, new JSONObject().put("usuarios", usuariosJson));
    }

    // Cargar los usuarios desde el archivo
    public HashMap<String, Usuario> cargarUsuariosDesdeArchivo() throws IOException, ContrasenaInvalidaException, EmailInvalidoException {
        HashMap<String, Usuario> usuarios = new HashMap<>();

        // Leer el archivo como un JSONObject
        String content = new String(Files.readAllBytes(Paths.get(ARCHIVO_USUARIOS)), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(content);

        // Extraer el array de usuarios
        JSONArray usuariosJsonArray = jsonObject.getJSONArray("usuarios");

        // Iterar sobre el array de usuarios y convertirlos a objetos Usuario
        if (usuariosJsonArray != null) {
            for (int i = 0; i < usuariosJsonArray.length(); i++) {
                JSONObject usuarioJson = usuariosJsonArray.getJSONObject(i);
                Usuario usuario = jsonToObject(usuarioJson);
                usuarios.put(usuario.getNombre(), usuario);
            }
        }

        return usuarios;
    }

    // Método auxiliar para escribir JSON a un archivo
    public static void writeJsonToFile(String fileName, JSONObject jsonObject) {
        try {
            // Escribe el JSON en el archivo con formato (4 espacios de indentación)
            Files.write(Paths.get(fileName), jsonObject.toString(4).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            // Manejo de la excepción en caso de error al escribir en el archivo
            System.err.println("Error al escribir en el archivo " + fileName + ": " + e.getMessage());
        }
    }

}
