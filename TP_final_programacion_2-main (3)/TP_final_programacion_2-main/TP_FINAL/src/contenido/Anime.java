package contenido;

public class Anime extends Contenido{
    private int episodios;

    public Anime(int id, int members, String name, int popularity, int rank, double score, String status, String synopsis, String title, EstadoVisto vistoONo, int episodios) {
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
        return super.toString() + ", Episodios: " + episodios;
    }

    @Override
    public void visto_o_no() {
        if (this.getVistoONo() == EstadoVisto.VISTO) {
            System.out.println("Este anime ha sido visto.");
        } else {
            System.out.println("Este anime no ha sido visto.");
        }
    }
}
