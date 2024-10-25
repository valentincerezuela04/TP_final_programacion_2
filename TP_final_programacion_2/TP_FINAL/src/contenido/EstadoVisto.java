package contenido;

public enum EstadoVisto {
    VISTO ("Visto"),
    NO_VISTO ("No visto");

    private String descripcion;

    EstadoVisto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
