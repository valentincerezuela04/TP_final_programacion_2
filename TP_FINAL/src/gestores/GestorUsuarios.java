package gestores;

import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;
import manejo_json.JsonUtilUsuario;
import usuario.Usuario;
import usuario.ValidacionUsuario;

import java.io.IOException;
import java.util.HashMap;

public class GestorUsuarios {
    private HashMap<String, Usuario> usuariosRegistrados;
    private JsonUtilUsuario jsonUtilUsuario;

    public GestorUsuarios() {
        usuariosRegistrados = new HashMap<>();
        jsonUtilUsuario = new JsonUtilUsuario();

        try {
            usuariosRegistrados = jsonUtilUsuario.cargarUsuariosDesdeArchivo();
        } catch (IOException e) {
            System.out.println("Error al cargar los usuarios desde el archivo: " + e.getMessage());
        }
    }

    public HashMap<String, Usuario> getUsuariosRegistrados() {
        return usuariosRegistrados;
    }

    // Método para registrar un nuevo usuario
    public Usuario registrarUsuario(Usuario usuario) {
        if (!usuariosRegistrados.containsKey(usuario.getNombre())) {
            try {
                // Validar el email y la contraseña
                ValidacionUsuario.esEmailValido(usuario.getEmail());
                ValidacionUsuario.esContraseñaValida(usuario.getContraseña());

                usuariosRegistrados.put(usuario.getNombre(), usuario);

                // Guardar los usuarios en el archivo
                try {
                    jsonUtilUsuario.guardarUsuariosEnArchivo(usuariosRegistrados);
                } catch (IOException e) {
                    System.out.println("Error al guardar el usuario: " + e.getMessage());
                }

                return usuario;

            } catch (EmailInvalidoException | ContrasenaInvalidaException e) {
                System.out.println("Error de validación: " + e.getMessage());
                return null;
            }
        } else {
            System.out.println("Usuario ya registrado");
            return null;
        }
    }

    // Método para iniciar sesión
    public Usuario iniciarSesion(String nombre, String contraseña) {
        Usuario usuario = usuariosRegistrados.get(nombre);
        if (usuario != null && ValidacionUsuario.verificarContraseña(usuario.getContraseña(), contraseña)) {
            return usuario;
        } else {
            System.out.println("Nombre o contraseña incorrectos");
            return null;
        }
    }

    // Método para cambiar la contraseña
    public void cambiarContraseña(String nombre, String contraseñaAntigua, String nuevaContraseña) {
        Usuario usuario = usuariosRegistrados.get(nombre);
        if (usuario != null) {
            if (ValidacionUsuario.verificarContraseña(usuario.getContraseña(), contraseñaAntigua)) {
                try {
                    if (ValidacionUsuario.esContraseñaValida(nuevaContraseña)) {
                        usuario.cambiarContraseña(contraseñaAntigua, nuevaContraseña);
                        jsonUtilUsuario.guardarUsuariosEnArchivo(usuariosRegistrados);
                        System.out.println("Contraseña cambiada con éxito");
                    } else {
                        throw new ContrasenaInvalidaException("Nueva contraseña no válida.");
                    }
                } catch (ContrasenaInvalidaException | IOException e) {
                    System.out.println("Error al cambiar contraseña: " + e.getMessage());
                }
            } else {
                System.out.println("Contraseña antigua incorrecta");
            }
        } else {
            System.out.println("Usuario no encontrado");
        }
    }

    // Método para listar todos los usuarios
    public void listarUsuarios() {
        for (Usuario usuario : usuariosRegistrados.values()) {
            System.out.println(usuario);
        }
    }
}
