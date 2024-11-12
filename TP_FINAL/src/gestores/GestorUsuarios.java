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
    }

    public HashMap<String, Usuario> getUsuariosRegistrados() {
        return usuariosRegistrados;
    }

    // Método para registrar un nuevo usuario
    public Usuario registrarUsuario(String nombre, String contraseña, String email) {
        if (!usuariosRegistrados.containsKey(nombre)) {
            if (!ValidacionUsuario.esEmailValido(email)) {
                System.out.println("Email no válido.");
                return null;
            }
            if (!ValidacionUsuario.esContraseñaValida(contraseña)) {
                System.out.println("Contraseña no válida. Debe cumplir las reglas de validación.");
                return null;
            }

            try {
                Usuario nuevoUsuario = new Usuario(nombre, contraseña, email);
                usuariosRegistrados.put(nombre, nuevoUsuario);
                try {
                    jsonUtilUsuario.guardarUsuariosEnArchivo(usuariosRegistrados);
                } catch (IOException e) {
                    System.out.println("Error al guardar el usuario: " + e.getMessage());
                }
                return nuevoUsuario;
            } catch (ContrasenaInvalidaException | EmailInvalidoException e) {
                System.out.println("Error al registrar el usuario: " + e.getMessage());
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
