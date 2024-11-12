package manejo_json;

import org.json.JSONArray;
import org.json.JSONObject;
import usuario.Usuario;

import java.io.*;
import java.util.HashMap;

public class JsonUtilUsuario {

    private static final String ARCHIVO_USUARIOS = "usuarios.json"; // Archivo donde se guardarán los usuarios

    // Guardar los usuarios en un archivo
    public void guardarUsuariosEnArchivo(HashMap<String, Usuario> usuarios) throws IOException {
        JSONArray usuariosJson = new JSONArray();
        for (Usuario usuario : usuarios.values()) {
            JSONObject usuarioJson = new JSONObject();
            usuarioJson.put("id", usuario.getId());
            usuarioJson.put("nombre", usuario.getNombre());
            usuarioJson.put("contraseña", usuario.getContraseña());
            usuarioJson.put("email", usuario.getEmail());
            usuariosJson.put(usuarioJson);
        }

        // Guardar usuarios
        try (FileWriter file = new FileWriter(ARCHIVO_USUARIOS)) {
            file.write(usuariosJson.toString(4));
        }
    }

    // Leer los usuarios desde el archivo
    public HashMap<String, Usuario> cargarUsuariosDesdeArchivo() throws IOException {
        HashMap<String, Usuario> usuarios = new HashMap<>();

        File archivoUsuarios = new File(ARCHIVO_USUARIOS);
        if (archivoUsuarios.exists()) {
            String contenido = new String(java.nio.file.Files.readAllBytes(archivoUsuarios.toPath()));
            JSONArray usuariosJson = new JSONArray(contenido);
            for (int i = 0; i < usuariosJson.length(); i++) {
                JSONObject usuarioJson = usuariosJson.getJSONObject(i);
                String nombre = usuarioJson.getString("nombre");
                String contraseña = usuarioJson.getString("contraseña");
                String email = usuarioJson.getString("email");
                int id = usuarioJson.getInt("id");

                // Crear el usuario y agregarlo
                try {
                    Usuario usuario = new Usuario(nombre, contraseña, email);
                    usuario.setId(id);  // Establecer el id correcto
                    usuarios.put(nombre, usuario);
                } catch (Exception e) {
                    e.printStackTrace(); // Si algo sale mal con un usuario, lo ignoramos
                }
            }
        }

        return usuarios;
    }
}
