package gestores;

import excepciones.*;
import manejoJson.JsonUtilUsuario;
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
            GestorExcepciones.manejarIOException(e);
        } catch (ContrasenaInvalidaException e) {
            GestorExcepciones.manejarContrasenaInvalida(e);
        } catch (EmailInvalidoException e) {
            GestorExcepciones.manejarEmailInvalido(e);
        }
    }

    public HashMap<String, Usuario> getUsuariosRegistrados() {
        return usuariosRegistrados;
    }

    // Método para registrar un nuevo usuario en el sistema
    public Usuario registrarUsuario(Usuario usuario) throws UsuarioRepetidoException {

        if (!usuariosRegistrados.containsKey(usuario.getNombre())) {
            try {
                usuariosRegistrados.put(usuario.getNombre(), usuario);
                jsonUtilUsuario.guardarUsuariosEnArchivo(usuariosRegistrados);
                return usuario;
            } catch (IOException e) {
                GestorExcepciones.manejarIOException(e);
                return null;
            }
        } else {
            throw new UsuarioRepetidoException("El usuario ya está registrado.");
        }
    }

    //Método para iniciar sesión de un usuario
    public Usuario iniciarSesion(String nombreUsuario, String contraseña) throws LoginException {

        if ("admin01".equals(nombreUsuario)) {

            if (!"admin01".equals(contraseña)) {
                throw new LoginException("La contraseña de administrador es incorrecta.");
            }
            return new Usuario("admin01", "admin01");
        }

        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new LoginException("El nombre de usuario no puede estar vacío.");
        }

        Usuario usuario = usuariosRegistrados.get(nombreUsuario);

        if (usuario == null) {
            throw new LoginException("No existe una cuenta con el nombre de usuario ingresado.");
        }

        if (!ValidacionUsuario.verificarContraseña(usuario.getContraseña(), contraseña)) {
            throw new LoginException("La contraseña ingresada es incorrecta.");
        }

        return usuario;
    }

    //Método para eliminar un usuario del sistema
    public void eliminarUsuario(Usuario usuario) {
        try {
            if (usuariosRegistrados.containsKey(usuario.getNombre())) {

                usuariosRegistrados.remove(usuario.getNombre());

                actualizarIdsUsuarios();
                jsonUtilUsuario.guardarUsuariosEnArchivo(usuariosRegistrados);

                System.out.println("Usuario eliminado correctamente.");
            } else {
                throw new UsuarioNoEncontradoException("Usuario no encontrado en el sistema.");
            }
        } catch (UsuarioNoEncontradoException e) {
            GestorExcepciones.manejarUsuarioNoEncontrado(e);
        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    // Método para actualizar los IDs de los usuarios registrados.
    public void actualizarIdsUsuarios() {
        int nuevoId = 1;
        for (Usuario usuario : usuariosRegistrados.values()) {
            usuario.setId(nuevoId++);
        }
    }

    // Método para actualizar el nombre de usuario de un usuario existente
    public void actualizarNombreUsuario(String nombreAnterior, String nuevoNombre) throws IOException {
        if (usuariosRegistrados.containsKey(nombreAnterior)) {
            Usuario usuario = usuariosRegistrados.remove(nombreAnterior);
            usuario.setNombre(nuevoNombre);
            usuariosRegistrados.put(nuevoNombre, usuario);

            jsonUtilUsuario.guardarUsuariosEnArchivo(usuariosRegistrados);
        } else {
            System.out.println("El usuario con el nombre anterior no existe en el sistema.");
        }
    }

    // Método para listar todos los usuarios registrados en el sistema
    public void listarUsuarios() {
        for (Usuario usuario : usuariosRegistrados.values()) {
            System.out.println(usuario);
        }
    }
}
