package gestores;


import manejo_json.JsonUtil;
import usuario.Usuario;
import usuario.ValidacionUsuario;

import java.util.HashMap;


public class GestorUsuarios {
        private HashMap<String, Usuario> usuariosRegistrados;

        public GestorUsuarios() {
            // Crear metodo para cargar los usuarios registrados desde el archivo JSON
            // this.usuariosRegistrados = JsonUtil.cargarUsuariosDesdeArchivo();
        }

    // Método para registrar un nuevo usuario
    public Usuario registrarUsuario(String nombre, String contraseña, String email) {
        // Verifica si el usuario ya está registrado
        if (!usuariosRegistrados.containsKey(nombre)) {
            // Verifica que el email tenga el formato correcto
            if (!ValidacionUsuario.esEmailValido(email)) {
                System.out.println("Email no válido.");
                return null;
            }
            // Verifica si la contraseña cumple las reglas
            if (!ValidacionUsuario.esContraseñaValida(contraseña)) {
                System.out.println("Contraseña no válida. Debe tener al menos 8 caracteres, una mayúscula, un número, y no contener espacios ni caracteres especiales.");
                return null;
            }

            Usuario nuevoUsuario = new Usuario(nombre, contraseña, email);
            usuariosRegistrados.put(nombre, nuevoUsuario); // Registra el usuario
            // JsonUtil.guardarUsuariosEnArchivo(usuariosRegistrados); // Guarda en el archivo JSON (Crear metodo para guardar usuarios en el archivo)
            return nuevoUsuario;
        } else {
            System.out.println("Usuario ya registrado");
            return null;
        }
    }

    //Metodo para iniciar sesion
    public Usuario iniciarSesion(String nombre, String contraseña) {
        Usuario usuario = usuariosRegistrados.get(nombre);
        if (usuario != null && usuario.verificarContraseña(contraseña)) {
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
            // Verifica si la contraseña antigua es correcta
            if (usuario.verificarContraseña(contraseñaAntigua)) {
                // Verifica si la nueva contraseña cumple con las reglas
                if (ValidacionUsuario.esContraseñaValida(nuevaContraseña)) {
                    usuario.cambiarContraseña(contraseñaAntigua, nuevaContraseña);
                    //JsonUtil.guardarUsuariosEnArchivo(usuariosRegistrados); // Guarda el cambio en el archivo JSON (Falta metodo)
                    System.out.println("Contraseña cambiada con éxito");
                } else {
                    System.out.println("Nueva contraseña no válida. Debe cumplir con las reglas de validación."); //Cambiar por excepcion
                }
            } else {
                System.out.println("Contraseña antigua incorrecta"); //Cambiar por excepcion
            }
        } else {
            System.out.println("Usuario no encontrado"); //Cambiar por excepcion
        }
    }

    // Método para listar todos los usuarios
    public void listarUsuarios() {
        for (Usuario usuario : usuariosRegistrados.values()) {
            System.out.println(usuario);
        }
    }


}
