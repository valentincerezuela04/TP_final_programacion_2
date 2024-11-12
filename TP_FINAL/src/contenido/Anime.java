package contenido;

public class Anime extends Contenido{
    private int episodios;

    public Anime(int id, int members, String name, int popularity, int rank, double score, String status, String synopsis, String title, String urL_image, EstadoVisto vistoONo, int episodios) {
        super(
                id,
                members,
                name,
                popularity,
                rank,
                score,
                status,
                synopsis,
                title,
                urL_image,
                vistoONo);
        this.episodios = episodios;
    }

public Anime(){
    super();

};
    public int getEpisodios() {
        return episodios;
    }

    public void setEpisodios(int episodios) {
        this.episodios = episodios;
    }

    @Override
    public String toString() {
        return "Anime{} " + super.toString();
    }

    @Override
    public void visto_o_no() {

    }
}
