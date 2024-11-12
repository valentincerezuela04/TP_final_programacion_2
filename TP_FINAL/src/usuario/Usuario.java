package usuario;

import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;
import seguridad.EncriptacionUtil;

public class Usuario {

    private static int contadorId = 1;
    private int id;
    private String nombre;
    private String contraseña;
    private String email;

    public Usuario() {
    }

    public Usuario(String nombre, String contraseña, String email) throws ContrasenaInvalidaException, EmailInvalidoException {
        this.id = contadorId++;
        this.nombre = nombre;
        if (ValidacionUsuario.esContraseñaValida(contraseña)) {
            // Encriptar la contraseña antes de guardarla
            this.contraseña = EncriptacionUtil.encriptar(contraseña);
        } else {
            throw new ContrasenaInvalidaException("Contraseña no válida. Asegúrate de que tenga al menos 8 caracteres, una mayúscula, un número, y no contenga espacios ni caracteres especiales (excepto el punto).");
        }
        if (ValidacionUsuario.esEmailValido(email)) {
            this.email = email;
        } else {
            throw new EmailInvalidoException("Formato de email inválido.");
        }
    }

    public void setContraseña(String contraseña) throws ContrasenaInvalidaException {
        if (ValidacionUsuario.esContraseñaValida(contraseña)) {
            // Encriptar la nueva contraseña antes de guardarla
            this.contraseña = EncriptacionUtil.encriptar(contraseña);
        } else {
            throw new ContrasenaInvalidaException("Contraseña no válida. Asegúrate de que tenga al menos 8 caracteres, una mayúscula, un número, y no contenga espacios ni caracteres especiales (excepto el punto).");
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
        // Verificar la contraseña encriptada
        if (ValidacionUsuario.verificarContraseña(this.contraseña, EncriptacionUtil.encriptar(contraseñaAntigua))) {
            if (ValidacionUsuario.esContraseñaValida(nuevaContraseña)) {
                // Encriptar la nueva contraseña antes de guardarla
                this.contraseña = EncriptacionUtil.encriptar(nuevaContraseña);
            } else {
                throw new ContrasenaInvalidaException("Nueva contraseña no válida. Asegúrate de que tenga al menos 8 caracteres, una mayúscula, un número y no contenga espacios o caracteres especiales.");
            }
        } else {
            throw new ContrasenaInvalidaException("Contraseña incorrecta");
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
