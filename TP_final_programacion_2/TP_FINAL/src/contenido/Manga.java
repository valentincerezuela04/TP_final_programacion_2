package contenido;

public class Manga extends Contenido {
    public Manga(EstadoEmision emision_state, String fecha_estreno, int id, double puntuacion, String titulo) {
        super(emision_state, fecha_estreno, id, puntuacion, titulo);
    }

    @Override
    public String toString() {
        return "Manga{} " + super.toString();
    }


    @Override
    public void visto_o_no() {

    }
}
