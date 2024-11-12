package manejo_json;

import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;
import org.json.JSONArray;
import org.json.JSONObject;
import usuario.Usuario;

import java.io.IOException;
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
        JSONArray usuariosJsonArray = readJsonFromFileArray(ARCHIVO_USUARIOS);

        if (usuariosJsonArray != null) {
            for (int i = 0; i < usuariosJsonArray.length(); i++) {
                JSONObject usuarioJson = usuariosJsonArray.getJSONObject(i);
                Usuario usuario = jsonToObject(usuarioJson);
                usuarios.put(usuario.getNombre(), usuario);
            }
        }

        return usuarios;
    }
}
