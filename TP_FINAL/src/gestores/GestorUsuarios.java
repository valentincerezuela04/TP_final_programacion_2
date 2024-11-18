package gestores;

import excepciones.*;
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

    public Usuario registrarUsuario(Usuario usuario) throws UsuarioRepetidoException {
        // Verificar si el usuario ya está registrado
        if (!usuariosRegistrados.containsKey(usuario.getNombre())) {
            try {
                // Registrar el nuevo usuario
                usuariosRegistrados.put(usuario.getNombre(), usuario);
                // Guardar los cambios en el archivo
                jsonUtilUsuario.guardarUsuariosEnArchivo(usuariosRegistrados);
                return usuario; // Devolver el usuario registrado
            } catch (IOException e) {
                // Manejar el error de entrada/salida
                GestorExcepciones.manejarIOException(e);
                return null; // Retornar null si ocurrió un error
            }
        } else {
            // Lanzar una excepción de usuario repetido
            throw new UsuarioRepetidoException("El usuario ya está registrado.");
        }
    }

    public Usuario iniciarSesion(String nombreUsuario, String contraseña) throws LoginException {
        // Verificar si el nombre de usuario es admin01
        if ("admin01".equals(nombreUsuario)) {
            // Si el usuario es admin01, validamos solo la contraseña
            if (!"admin01".equals(contraseña)) {
                throw new LoginException("La contraseña de administrador es incorrecta.");
            }

            // Retornar un usuario administrador usando el constructor sin validaciones
            return new Usuario("admin01", "admin01");
        }

        // Si no es admin01, verificamos el nombre de usuario en el archivo .json
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new LoginException("El nombre de usuario no puede estar vacío.");
        }

        Usuario usuario = usuariosRegistrados.get(nombreUsuario);

        if (usuario == null) {
            throw new LoginException("No existe una cuenta con el nombre de usuario ingresado.");
        }

        // Verificación de la contraseña ingresada contra la almacenada para usuarios comunes
        if (!ValidacionUsuario.verificarContraseña(usuario.getContraseña(), contraseña)) {
            throw new LoginException("La contraseña ingresada es incorrecta.");
        }

        return usuario;
    }




    public void eliminarUsuario(Usuario usuario) {
        try {
            if (usuariosRegistrados.containsKey(usuario.getNombre())) {
                // Primero eliminamos del mapa
                usuariosRegistrados.remove(usuario.getNombre());
                // Actualizamos los IDs antes de guardar
                actualizarIdsUsuarios();
                // Guardamos en el archivo JSON
                jsonUtilUsuario.guardarUsuariosEnArchivo(usuariosRegistrados);
                // Mensaje exitoso solo si todo se realiza correctamente
                System.out.println("Usuario eliminado correctamente.");
            } else {
                throw new UsuarioNoEncontradoException("Usuario no encontrado en el sistema.");
            }
        } catch (UsuarioNoEncontradoException e) {
            // Manejo de excepciones específico
            GestorExcepciones.manejarUsuarioNoEncontrado(e);
        } catch (IOException e) {
            // Manejo de errores relacionados con IO
            GestorExcepciones.manejarIOException(e);
        } catch (Exception e) {
            // Manejo de cualquier error imprevisto
            GestorExcepciones.manejarExcepcion(e);
        }
    }


    private void actualizarIdsUsuarios() {
        int nuevoId = 1;
        for (Usuario usuario : usuariosRegistrados.values()) {
            usuario.setId(nuevoId++);
        }
    }

    public void actualizarNombreUsuario(String nombreAnterior, String nuevoNombre) throws IOException {
        if (usuariosRegistrados.containsKey(nombreAnterior)) {
            Usuario usuario = usuariosRegistrados.remove(nombreAnterior);
            usuario.setNombre(nuevoNombre); // Cambiar el nombre en el objeto
            usuariosRegistrados.put(nuevoNombre, usuario);

            // Guardar los cambios en el archivo JSON
            jsonUtilUsuario.guardarUsuariosEnArchivo(usuariosRegistrados);
        } else {
            System.out.println("El usuario con el nombre anterior no existe en el sistema.");
        }
    }


    public void listarUsuarios() {
        for (Usuario usuario : usuariosRegistrados.values()) {
            System.out.println(usuario);
        }
    }
}
