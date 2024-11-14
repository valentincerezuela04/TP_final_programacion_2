package usuario;

import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;

public class Usuario {

    private static int contadorId = 1;
    private int id;
    private String nombre;
    private String contraseña;
    private String email;
    public Usuario() {
    }

    // Constructor con parámetros
    public Usuario(String nombre, String contraseña, String email) throws ContrasenaInvalidaException, EmailInvalidoException {
        this.id = contadorId++;  // Usamos el método para asignar el ID
        this.nombre = nombre;

        // Validar la contraseña (ya no se encripta)
        if (ValidacionUsuario.esContraseñaValida(contraseña)) {
            this.contraseña = contraseña;  // Se guarda tal cual se ingresa
        } else {
            throw new ContrasenaInvalidaException("Contraseña no válida. Asegúrate de que tenga al menos 8 caracteres, una mayúscula, un número, y no contenga espacios ni caracteres especiales (excepto el punto).");
        }

        // Validar el email
        if (ValidacionUsuario.esEmailValido(email)) {
            this.email = email;
        } else {
            throw new EmailInvalidoException("Formato de email inválido.");
        }
    }

    // Métodos de la clase Usuario
    public void setId(int id) {
        this.id = id;
    }

    public void setContraseña(String contraseña) throws ContrasenaInvalidaException {
        if (ValidacionUsuario.esContraseñaValida(contraseña)) {
            this.contraseña = contraseña;  // Se guarda tal cual se ingresa
        } else {
            throw new ContrasenaInvalidaException("Contraseña no válida.");
        }
    }

    public void setEmail(String email) throws EmailInvalidoException {
        if (ValidacionUsuario.esEmailValido(email)) {
            this.email = email;
        } else {
            throw new EmailInvalidoException("Formato de email inválido.");
        }
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getEmail() {
        return email;
    }

    public void cambiarContraseña(String contraseñaAntigua, String nuevaContraseña) throws ContrasenaInvalidaException {
        // Verificar que la contraseña antigua coincida
        if (this.contraseña.equals(contraseñaAntigua)) {
            if (ValidacionUsuario.esContraseñaValida(nuevaContraseña)) {
                this.contraseña = nuevaContraseña;  // Se guarda tal cual la nueva contraseña
            } else {
                throw new ContrasenaInvalidaException("Nueva contraseña no válida.");
            }
        } else {
            throw new ContrasenaInvalidaException("Contraseña incorrecta");
        }
    }

    public void cambiarEmail(String contraseñaActual, String nuevoEmail) throws ContrasenaInvalidaException, EmailInvalidoException {
        // Verificar que la contraseña ingresada coincida con la actual
        if (this.contraseña.equals(contraseñaActual)) {
            // Validar el nuevo email
            if (ValidacionUsuario.esEmailValido(nuevoEmail)) {
                this.email = nuevoEmail;  // Actualizar el email
                System.out.println("Email actualizado correctamente.");
            }
        } else {
            throw new ContrasenaInvalidaException("Contraseña incorrecta. No se pudo cambiar el email.");
        }
    }

    public void cambiarNombreUsuario(String contraseñaActual, String nuevoNombre) throws ContrasenaInvalidaException {
        // Verificar que la contraseña ingresada coincida con la actual
        if (this.contraseña.equals(contraseñaActual)) {
            this.nombre = nuevoNombre;  // Actualizar el nombre de usuario
            System.out.println("Nombre de usuario actualizado correctamente.");
        } else {
            throw new ContrasenaInvalidaException("Contraseña incorrecta. No se pudo cambiar el nombre de usuario.");
        }
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "email='" + email + '\'' +
                ", id=" + id +
                ", nombre='" + nombre + '\'' +
                '}'; // No se muestra la contraseña por razones de seguridad
    }
}
