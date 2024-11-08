package manejo_json;

import org.json.JSONObject;
import usuario.Usuario;

public class JsonUtilUsuario extends JsonUtil {

    // Convierte un objeto Usuario a JSONObject
    @Override
    public JSONObject objectToJson(Object obj) {
        Usuario usuario = (Usuario) obj;
        JSONObject json = new JSONObject();

        json.put("id", usuario.getId());
        json.put("nombre", usuario.getNombre());
        json.put("email", usuario.getEmail());

        // La contraseña no debería almacenarse en texto plano por seguridad
        // Aquí se puede encriptar si es necesario, o simplemente excluirla
        return json;
    }


    @Override
    public Object jsonToObject(JSONObject jsonObject) {
        String nombre = jsonObject.getString("nombre");
        String email = jsonObject.optString("email", "email@default.com");
        String contraseña = jsonObject.optString("contraseña", "defaultPass");  // Contraseña de prueba para no romper la lógica

        // Crear el objeto Usuario (valida contraseña y email)
        return new Usuario(nombre, contraseña, email);
    }
}
