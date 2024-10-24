package usuario;

public class Usuario {

    private static int contadorId = 1;
    private int id;
    private String nombre;
    private String contraseña;
    private String email;

    public Usuario() {
    }

    public Usuario(String nombre, String contraseña, String email) {
        this.id = contadorId++;
        this.nombre = nombre;
        if (ValidacionUsuario.esContraseñaValida(contraseña)) { //Verifica si la contraseña es valida
            this.contraseña = contraseña;
        } else {
            throw new IllegalArgumentException("Contraseña no válida. Asegúrate de que tenga al menos 8 caracteres, una mayúscula, un número, y no contenga espacios ni caracteres especiales (excepto el punto).");
        }
        if (ValidacionUsuario.esEmailValido(email)) { //Verifica si el email es valido
            this.email = email;
        } else {
            throw new IllegalArgumentException("Formato de email inválido."); //Pasar a una excepcion personalizada
        }
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setContraseña(String contraseña) {
        if (ValidacionUsuario.esContraseñaValida(contraseña)) {
            this.contraseña = contraseña;
        } else {
            //Cambiar por la excepcion creada mas arriba
            throw new IllegalArgumentException("Contraseña no válida. Asegúrate de que tenga al menos 8 caracteres, una mayúscula, un número, y no contenga espacios ni caracteres especiales (excepto el punto).");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (ValidacionUsuario.esEmailValido(email)) {
            this.email = email;
        } else {
            System.out.println("Formato de email inválido."); //Cambiar por la excepcion creada mas arriba
        }
    }

    public boolean verificarContraseña(String contraseña) {
        return ValidacionUsuario.verificarContraseña(this.contraseña, contraseña);
    }

    public void cambiarContraseña(String contraseñaAntigua, String nuevaContraseña) {
        if (ValidacionUsuario.verificarContraseña(this.contraseña, contraseñaAntigua)) {
            if (ValidacionUsuario.esContraseñaValida(nuevaContraseña)) {
                this.contraseña = nuevaContraseña;
            } else {
                System.out.println("Nueva contraseña no válida. Asegúrate de que tenga al menos 8 caracteres, una mayúscula, un número y no contenga espacios o caracteres especiales.");
                //Pasar a una excepcion personalizada
            }
        } else {
            System.out.println("Contraseña incorrecta");
        }
    }


    @Override
    public String toString() {
        return "Usuario{" +
                "email='" + email + '\'' +
                ", id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
