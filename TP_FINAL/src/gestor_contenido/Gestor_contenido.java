package gestor_contenido;

import contenido.Anime;
import contenido.Contenido;

import java.util.HashSet;

public class Gestor_contenido <T extends Contenido> {
    private HashSet<T> gestor_set;


    public Gestor_contenido(HashSet<T> gestor_set) {
        this.gestor_set = gestor_set;
    }



}
