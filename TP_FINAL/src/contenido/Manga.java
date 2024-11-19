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
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        Manga manga = (Manga) obj;
        return getId() == manga.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    // Método para imprimir si el manga ha sido visto o no, según el estado de 'vistoONo'
    @Override
    public void visto_o_no() {
        if (getVistoONo() == EstadoVisto.VISTO) {
            System.out.println("Has visto el manga: " + getTitle());
        } else {
            System.out.println("No has visto el manga: " + getTitle());
        }
    }
}
