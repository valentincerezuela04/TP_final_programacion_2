package gestores;

import contenido.Contenido;

import java.util.ArrayList;
import java.util.List;

public class GestorContenido<T extends Contenido> {
    private List<T> contenidoList;

    // Constructor
    public GestorContenido() {
        contenidoList = new ArrayList<>();
    }

    // Método para agregar un contenido
    public void agregarContenido(T contenido) {
        contenidoList.add(contenido);
    }

    // Método para obtener todo el contenido
    public List<T> obtenerContenido() {
        return contenidoList;
    }

    // Método para eliminar contenido por ID
    public void eliminarContenidoPorId(int id) {
        contenidoList.removeIf(contenido -> contenido.getId() == id);
    }

    // Método para mostrar el contenido
    public void mostrarContenido() {
        for (T contenido : contenidoList) {
            System.out.println(contenido);
        }
    }
}
