package contenido;

import java.util.Objects;

public class Manga extends Contenido {
    private int chapters;
    private int volumes;

    public Manga(int id, int members, int popularity, int rank, double score, String status, String synopsis, String title, EstadoVisto vistoONo, int chapters, int volumes) {
        super(id, members, popularity, rank, score, status, synopsis, title, vistoONo);
        this.chapters = chapters;
        this.volumes = volumes;
    }

    public Manga() {
        super();
    }

    public int getChapters() {
        return chapters;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
    }

    public int getVolumes() {
        return volumes;
    }

    public void setVolumes(int volumes) {
        this.volumes = volumes;
    }

    @Override
    public String toString() {
        return "Manga{} " + super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        // Verificamos si los objetos son exactamente iguales
        if (this == obj) return true;

        // Verificamos si el objeto es nulo o pertenece a una clase diferente
        if (obj == null || getClass() != obj.getClass()) return false;

        // Si son del mismo tipo, comparamos por el id (suponiendo que el id es único)
        Manga manga = (Manga) obj;
        return getId() == manga.getId();
    }

    @Override
    public int hashCode() {
        // Usamos el id para generar el código hash
        return Objects.hash(getId());
    }

    @Override
    public void visto_o_no() {
        if (getVistoONo() == EstadoVisto.VISTO) {
            System.out.println("Has visto el manga: " + getTitle());
        } else {
            System.out.println("No has visto el manga: " + getTitle());
        }
    }
}
