package api;

    public interface IApis {
        // Método para obtener datos de la API en formato de cadena JSON
        String getDataApi();

        // Método para obtener datos de la API, filtrarlos y guardarlos en un archivo
        void obtenerYGuardarDataFiltrada(String archivoDestino);
    }


