package contenido;

public class Anime extends Contenido{
    public Anime(EstadoEmision emision_state, String fecha_estreno, int id, double puntuacion, String titulo) {
        super(emision_state, fecha_estreno, id, puntuacion, titulo);
    }

    @Override
    public String toString() {
        return "Anime{} " + super.toString();
    }

    @Override
    public void visto_o_no() {

    }
}
