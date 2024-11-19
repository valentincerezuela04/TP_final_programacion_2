package contenido;

public abstract class Contenido {
    private int id;
    private String title;
    private String status;
    private double score;
    private int rank;
    private int popularity;
    private int members;
    private String synopsis;
    private EstadoVisto vistoONo;

    public Contenido(int id, int members, int popularity, int rank, double score, String status, String synopsis, String title, EstadoVisto vistoONo) {
        this.id = id;
        this.members = members;
        this.popularity = popularity;
        this.rank = rank;
        this.score = score;
        this.status = status;
        this.synopsis = synopsis;
        this.title = title;
        this.setVistoONo(vistoONo);
    }


    public Contenido (){

    }

    public Contenido(int id, String title, double score, int popularity, EstadoVisto vistoONo){
        this.id = id;
        this.title = title;
        this.score = score;
        this.popularity = popularity;
        this.setVistoONo(vistoONo);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EstadoVisto getVistoONo() {
        return vistoONo;
    }

    public void setVistoONo(EstadoVisto vistoONo) {
        this.vistoONo = vistoONo;
    }

    //Metodo abstracto para implementar en las clases Anime y Manga
    public abstract void visto_o_no();

    @Override
    public String toString() {
        return "Contenido{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", score=" + score +
                ", rank=" + rank +
                ", popularity=" + popularity +
                ", members=" + members +
                ", synopsis='" + synopsis + '\'' +
                ", vistoONo=" + vistoONo +
                '}';
    }
}