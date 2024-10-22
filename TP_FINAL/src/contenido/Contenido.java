package contenido;

import java.util.Objects;
import java.util.PrimitiveIterator;

public abstract class Contenido {
    private int id;
    private String titulo;
    private String fecha_estreno ;
    private double puntuacion;
    private Estado_emision_enum emision_state;
    private Estado_vistoONo_enum vistoONo;

    public Contenido(Estado_emision_enum emision_state, String fecha_estreno, int id, double puntuacion, String titulo) {
        this.emision_state = emision_state;
        this.fecha_estreno = fecha_estreno;
        this.id = id;
        this.puntuacion = puntuacion;
        this.titulo = titulo;
        this.vistoONo = Estado_vistoONo_enum.NO_VISTO;
    }

    public Estado_emision_enum getEmision_state() {
        return emision_state;
    }

    public void setEmision_state(Estado_emision_enum emision_state) {
        this.emision_state = emision_state;
    }

    public String getFecha_estreno() {
        return fecha_estreno;
    }

    public void setFecha_estreno(String fecha_estreno) {
        this.fecha_estreno = fecha_estreno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Estado_vistoONo_enum getVistoONo() {
        return vistoONo;
    }

    public void setVistoONo(Estado_vistoONo_enum vistoONo) {
        this.vistoONo = vistoONo;
    }

    @Override
    public String toString() {
        return "Contenido{" +
                "emision_state=" + emision_state +
                ", id=" + id +
                ", titulo='" + titulo + '\'' +
                ", fecha_estreno='" + fecha_estreno + '\'' +
                ", puntuacion=" + puntuacion +
                ", vistoONo=" + vistoONo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contenido contenido = (Contenido) o;
        return id == contenido.id && Objects.equals(titulo, contenido.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo);
    }

    public abstract void visto_o_no();
}
