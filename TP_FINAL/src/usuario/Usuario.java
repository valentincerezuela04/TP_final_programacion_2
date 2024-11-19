package usuario;

import contenido.Anime;
import contenido.Manga;
import excepciones.ContrasenaInvalidaException;
import excepciones.EmailInvalidoException;
import gestores.GestorUsuarios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Usuario {

    private static int contadorId = 1;
    private int id;
    private String nombre;
    private String contraseña;
    private String email;
    private List<Anime> listaAnime;
    private List<Manga> listaManga;

    public Usuario() {
    }

    public Usuario(String nombre, String contraseña, String email) throws ContrasenaInvalidaException, EmailInvalidoException {
        this.id = contadorId++;
        this.nombre = nombre;

        if (ValidacionUsuario.esContraseñaValida(contraseña)) {
            this.contraseña = contraseña;
        } else {
            throw new ContrasenaInvalidaException("Contraseña no válida. Asegúrate de que tenga al menos 8 caracteres, una mayúscula, un número, y no contenga espacios ni caracteres especiales (excepto el punto).");
        }

        if (ValidacionUsuario.esEmailValido(email)) {
            this.email = email;
        } else {
            throw new EmailInvalidoException("Formato de email inválido.");
        }

        this.listaAnime = new ArrayList<>();
        this.listaManga = new ArrayList<>();
    }

    public Usuario(String nombre, String contraseña) {
        this.nombre = nombre;
        this.contraseña = contraseña;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Método para establecer la contraseña, validando si es correcta
    public void setContraseña(String contraseña) throws ContrasenaInvalidaException {
        if (ValidacionUsuario.esContraseñaValida(contraseña)) {
            this.contraseña = contraseña;
        } else {
            throw new ContrasenaInvalidaException("Contraseña no válida.");
        }
    }

    // Método para establecer el email, validando su formato
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    // Método para cambiar la contraseña del usuario, validando la contraseña anterior
    public void cambiarContraseña(String contraseñaAntigua, String nuevaContraseña) throws ContrasenaInvalidaException {
        if (this.contraseña.equals(contraseñaAntigua)) {
            if (ValidacionUsuario.esContraseñaValida(nuevaContraseña)) {
                this.contraseña = nuevaContraseña;
            } else {
                throw new ContrasenaInvalidaException("Nueva contraseña no válida.");
            }
        } else {
            throw new ContrasenaInvalidaException("Contraseña incorrecta");
        }
    }

    // Método para cambiar el email, validando la contraseña actual y el nuevo email
    public void cambiarEmail(String contraseñaActual, String nuevoEmail) throws ContrasenaInvalidaException, EmailInvalidoException {
        if (this.contraseña.equals(contraseñaActual)) {

            if (ValidacionUsuario.esEmailValido(nuevoEmail)) {
                this.email = nuevoEmail;
            }
        } else {
            throw new ContrasenaInvalidaException("Contraseña incorrecta. No se pudo cambiar el email.");
        }
    }

    // Método para cambiar el nombre de usuario, validando la contraseña y actualizando la base de datos
    public void cambiarNombreUsuario(String contraseñaActual, String nuevoNombre, GestorUsuarios gestorUsuarios) throws ContrasenaInvalidaException, IOException {
        if (this.contraseña.equals(contraseñaActual)) {
            String nombreAnterior = this.nombre;
            this.nombre = nuevoNombre;

            gestorUsuarios.actualizarNombreUsuario(nombreAnterior, nuevoNombre);

            System.out.println("Nombre de usuario actualizado correctamente.");
        } else {
            throw new ContrasenaInvalidaException("Contraseña incorrecta. No se pudo cambiar el nombre de usuario.");
        }
    }

    public List<Anime> getAnimes() {
        return listaAnime;
    }

    public void setAnimes(List<Anime> listaAnime) {
        this.listaAnime = listaAnime;
    }

    public List<Manga> getMangas() {
        return listaManga;
    }

    public void setMangas(List<Manga> listaManga) {
        this.listaManga = listaManga;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", listaAnime=" + listaAnime +
                ", listaManga=" + listaManga +
                '}';
    }
}
