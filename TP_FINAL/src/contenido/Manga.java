package contenido;

public class Manga extends Contenido {
    private int chapters;
    private int volumes;

    public Manga(int id, int members, String name, int popularity, int rank, double score, String status, String synopsis, String title, EstadoVisto vistoONo, int chapters, int volumes) {
        super(id, members, name, popularity, rank, score, status, synopsis, title, vistoONo);
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
    public void visto_o_no() {
        if (getVistoONo() == EstadoVisto.VISTO) {
            System.out.println("Has visto el manga: " + getTitle());
        } else {
            System.out.println("No has visto el manga: " + getTitle());
        }
    }
}
