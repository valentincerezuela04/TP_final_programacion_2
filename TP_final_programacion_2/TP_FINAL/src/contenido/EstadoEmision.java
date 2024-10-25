package contenido;

public enum EstadoEmision {
    EMISION ("En emision"),
    ABANDONADO ("Abandonado"),
    FINALIZADO ("Finalizado");

    private String descripcion;

    EstadoEmision(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }


}
