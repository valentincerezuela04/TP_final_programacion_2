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
        // Verificamos si los objetos son exactamente iguales
        if (this == obj) return true;

        // Verificamos si el objeto es nulo o pertenece a una clase diferente
        if (obj == null || getClass() != obj.getClass()) return false;

        // Si son del mismo tipo, comparamos por el id (suponiendo que el id es único)
        Anime anime = (Anime) obj;
        return getId() == anime.getId();
    }

    @Override
    public int hashCode() {
        // Usamos el id para generar el código hash
        return Objects.hash(getId());
    }


}
