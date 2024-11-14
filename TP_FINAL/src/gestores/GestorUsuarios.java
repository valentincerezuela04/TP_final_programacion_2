package gestores;

import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;
import excepciones.UsuarioNoEncontradoException;
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
        } catch (ContrasenaInvalidaException e) {
            System.out.println("Error de contraseña inválida: " + e.getMessage());
        } catch (EmailInvalidoException e) {
            System.out.println("Error de email inválido: " + e.getMessage());
        }
    }

    public HashMap<String, Usuario> getUsuariosRegistrados() {
        return usuariosRegistrados;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (!usuariosRegistrados.containsKey(usuario.getNombre())) {
            try {
                ValidacionUsuario.esEmailValido(usuario.getEmail());
                ValidacionUsuario.esContraseñaValida(usuario.getContraseña());
                usuariosRegistrados.put(usuario.getNombre(), usuario);
                jsonUtilUsuario.guardarUsuariosEnArchivo(usuariosRegistrados);
                return usuario;
            } catch (EmailInvalidoException | ContrasenaInvalidaException e) {
                System.out.println("Error de validación: " + e.getMessage());
                return null;
            } catch (IOException e) {
                System.out.println("Error al guardar el usuario: " + e.getMessage());
                return null;
            }
        } else {
            System.out.println("El usuario ya está registrado.");
            return null;
        }
    }

    public Usuario iniciarSesion(String nombreUsuario, String contraseña) throws UsuarioNoEncontradoException, ContrasenaInvalidaException {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new UsuarioNoEncontradoException("El nombre de usuario no puede estar vacío.");
        }

        Usuario usuario = usuariosRegistrados.get(nombreUsuario);

        if (usuario == null) {
            throw new UsuarioNoEncontradoException("No existe una cuenta con el nombre de usuario ingresado.");
        }

        if (!ValidacionUsuario.verificarContraseña(usuario.getContraseña(), contraseña)) {
            throw new ContrasenaInvalidaException("La contraseña ingresada es incorrecta.");
        }

        return usuario;
    }

    public void eliminarUsuario(Usuario usuario) {
        if (usuariosRegistrados.containsKey(usuario.getNombre())) {
            usuariosRegistrados.remove(usuario.getNombre());
            actualizarIdsUsuarios();
            try {
                jsonUtilUsuario.guardarUsuariosEnArchivo(usuariosRegistrados);
            } catch (IOException e) {
                System.out.println("Error al actualizar el archivo de usuarios: " + e.getMessage());
            }
        } else {
            System.out.println("Usuario no encontrado en el sistema.");
        }
    }

    private void actualizarIdsUsuarios() {
        int nuevoId = 1;
        for (Usuario usuario : usuariosRegistrados.values()) {
            usuario.setId(nuevoId++);
        }
    }

    public void listarUsuarios() {
        for (Usuario usuario : usuariosRegistrados.values()) {
            System.out.println(usuario);
        }
    }
}
