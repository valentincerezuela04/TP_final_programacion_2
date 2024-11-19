package contenido;

import java.util.Objects;

public class Anime extends Contenido {
    private int episodes;

    public Anime(int id, int members, int popularity, int rank, double score, String status, String synopsis, String title, EstadoVisto vistoONo,  int episodes) {
        super(
                id,
                members,
                popularity,
                rank,
                score,
                status,
                synopsis,
                title,
                vistoONo);
        this.episodes = episodes;
    }

    public Anime(int id, String title, double score, int popularity, EstadoVisto estadoVisto) {
        super(id, title, score, popularity, estadoVisto);
    }

    public Anime() {
        super();
    }


    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return super.toString() + ", Episodios: " + episodes;
    }


    // Método para imprimir si el anime ha sido visto o no, según el estado de 'vistoONo'
    @Override
    public void visto_o_no() {
        if (getVistoONo() == EstadoVisto.VISTO) {
            System.out.println("Has visto el anime: " + getTitle());
        } else {
            System.out.println("No has visto el anime: " + getTitle());
        }
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        Anime anime = (Anime) obj;
        return getId() == anime.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


}
