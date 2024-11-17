package contenido;
public class Genero {
    private int id;
    private String type;
    private String name;

    // Constructor
    public Genero(int id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}


