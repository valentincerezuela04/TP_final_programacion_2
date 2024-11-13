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

    // Método para registrar un nuevo usuario
    public Usuario registrarUsuario(Usuario usuario) {
        if (!usuariosRegistrados.containsKey(usuario.getNombre())) {
            try {
                // Validar el email y la contraseña
                ValidacionUsuario.esEmailValido(usuario.getEmail());
                ValidacionUsuario.esContraseñaValida(usuario.getContraseña());

                // Registrar al usuario en el HashMap
                usuariosRegistrados.put(usuario.getNombre(), usuario);

                // Guardar los usuarios en el archivo
                jsonUtilUsuario.guardarUsuariosEnArchivo(usuariosRegistrados); // Guardar de inmediato

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


    // Método para iniciar sesión
    public Usuario iniciarSesion(String nombreUsuario, String contraseña) throws UsuarioNoEncontradoException, ContrasenaInvalidaException {
        // Validar la contraseña ingresada
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new UsuarioNoEncontradoException("El nombre de usuario no puede estar vacío.");
        }

        // Intentar obtener el usuario directamente del HashMap usando el nombre de usuario
        Usuario usuario = usuariosRegistrados.get(nombreUsuario);

        // Verificar si el usuario no fue encontrado
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("No existe una cuenta con el nombre de usuario ingresado.");
        }

        // Validar la contraseña
        if (!ValidacionUsuario.verificarContraseña(usuario.getContraseña(), contraseña)) {
            throw new ContrasenaInvalidaException("La contraseña ingresada es incorrecta.");
        }

        return usuario;  // Retornar el usuario si la autenticación es exitosa
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
