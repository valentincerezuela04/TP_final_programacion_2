package manejo_json;

import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;
import org.json.JSONObject;
import usuario.Usuario;
import seguridad.EncriptacionUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class JsonUtilUsuario extends JsonUtil {

    // Convierte un objeto Usuario a JSONObject
    @Override
    public JSONObject objectToJson(Object obj) {
        Usuario usuario = (Usuario) obj;
        JSONObject json = new JSONObject();

        json.put("id", usuario.getId());
        json.put("nombre", usuario.getNombre());
        json.put("email", usuario.getEmail());

        // Encriptamos la contraseña antes de guardarla
        String contrasenaEncriptada = EncriptacionUtil.encriptar(usuario.getContraseña());
        json.put("contraseña", contrasenaEncriptada);

        return json;
    }

    @Override
    public Object jsonToObject(JSONObject jsonObject) throws ContrasenaInvalidaException, EmailInvalidoException {
        String nombre = jsonObject.getString("nombre");
        String email = jsonObject.optString("email", "email@default.com");
        String contraseña = jsonObject.optString("contraseña", "defaultPass");

        // Se devuelve el usuario sin verificar la contraseña, porque la encriptación ya se maneja
        return new Usuario(nombre, contraseña, email);
    }

    // Método para guardar usuarios en un archivo JSON
    public void guardarUsuariosEnArchivo(HashMap<String, Usuario> usuarios) throws IOException {
        JSONObject jsonUsuarios = new JSONObject();
        for (Usuario usuario : usuarios.values()) {
            jsonUsuarios.put(usuario.getNombre(), objectToJson(usuario));
        }

        try (FileWriter file = new FileWriter("usuarios.json")) {
            file.write(jsonUsuarios.toString(4));
            file.flush();
        } catch (IOException e) {
            throw new IOException("Error al guardar usuarios en archivo JSON", e);
        }
    }
}
