package gestores;

import contenido.Contenido;
import contenido.Anime;
import contenido.EstadoVisto;
import contenido.Manga;
import excepciones.ContenidoDuplicadoException;
import excepciones.ContenidoNoEncontradoException;
import excepciones.EstadoRepetidoException;
import manejo_json.JsonUtilAnime;
import manejo_json.JsonUtilManga;
import manejo_json.JsonUtilUsuario;
import usuario.Usuario;

import java.io.IOException;
import java.util.*;

public class GestorContenido<T extends Contenido> {

    private JsonUtilAnime jsonUtilAnime;
    private JsonUtilManga jsonUtilManga;
    private Scanner scanner;

    public GestorContenido() {
        this.jsonUtilAnime = new JsonUtilAnime();
        this.jsonUtilManga = new JsonUtilManga();
        this.scanner = new Scanner(System.in);
    }

    // Método para buscar contenido por ID (Anime o Manga)
    public T buscarContenidoPorId(String archivo, int id, Class<T> tipoContenido) throws ContenidoNoEncontradoException {
        List<T> listaContenido = cargarContenidoDesdeArchivo(archivo, tipoContenido);

        for (T contenido : listaContenido) {
            if (contenido.getId() == id) {
                return contenido;
            }
        }
        throw new ContenidoNoEncontradoException("Contenido con ID " + id + " no encontrado.");
    }

    public <T extends Contenido> T buscarContenidoPorNombre(String archivo, String title, Class<T> tipoContenido) throws ContenidoNoEncontradoException {
        List<T> listaContenido = cargarContenidoDesdeArchivo(archivo, tipoContenido);

        for (T contenido : listaContenido) {
            if (contenido.getTitle().equalsIgnoreCase(title)) {
                return contenido;
            }
        }
        throw new ContenidoNoEncontradoException("Contenido con nombre '" + title + "' no encontrado.");
    }

    // Método para cargar contenido desde archivo (genérico para Anime o Manga)
    private <T extends Contenido> List<T> cargarContenidoDesdeArchivo(String archivo, Class<T> tipoContenido) {
        if (tipoContenido == Anime.class) {
            // Cargar Animes
            List<Anime> animes = jsonUtilAnime.cargarAnimesDesdeArchivo(archivo);
            return (List<T>) animes; // Convertimos la lista de Anime a List<T>
        } else if (tipoContenido == Manga.class) {
            // Cargar Mangas
            List<Manga> mangas = jsonUtilManga.cargarMangasDesdeArchivo(archivo);
            return (List<T>) mangas; // Convertimos la lista de Manga a List<T>
        }
        return new ArrayList<>(); // Retornar una lista vacía si no es un tipo válido
    }

    // Método para ordenar contenidos por un criterio específico
    public List<T> ordenarContenido(String archivo, String criterio, Class<T> tipoContenido) {
        List<T> listaContenido = cargarContenidoDesdeArchivo(archivo, tipoContenido);

        if (criterio.equalsIgnoreCase("id")) {
            listaContenido.sort(Comparator.comparingInt(Contenido::getId));
        } else if (criterio.equalsIgnoreCase("titulo")) {
            listaContenido.sort(Comparator.comparing(Contenido::getTitle, String.CASE_INSENSITIVE_ORDER));
        } else if (criterio.equalsIgnoreCase("popularidad")) {
            listaContenido.sort((c1, c2) -> Integer.compare(c2.getPopularity(), c1.getPopularity()));
        } else if (criterio.equalsIgnoreCase("score")) {
            listaContenido.sort((c1, c2) -> Double.compare(c2.getScore(), c1.getScore()));
        }

        return listaContenido;
    }

    // Método para agregar contenido (Anime o Manga) a la lista del usuario
    public <T> void agregarContenido(Usuario usuario, T contenido) throws IOException, ContenidoDuplicadoException {
        // Verificar si el contenido ya está en la lista de animes o mangas
        if (contenido instanceof Anime) {
            if (usuario.getAnimes().contains(contenido)) {
                throw new ContenidoDuplicadoException("Este anime ya está en tu lista.");
            }
            usuario.getAnimes().add((Anime) contenido); // Agregar el anime a la lista
        } else if (contenido instanceof Manga) {
            if (usuario.getMangas().contains(contenido)) {
                throw new ContenidoDuplicadoException("Este manga ya está en tu lista.");
            }
            usuario.getMangas().add((Manga) contenido); // Agregar el manga a la lista
        }

        // Guardar los cambios en el archivo
        JsonUtilUsuario.modificarUsuarioEnArchivo(usuario);
        System.out.println("Contenido agregado exitosamente a tu lista.");
    }


    // Método para eliminar contenido (Anime o Manga) de la lista del usuario
    public void eliminarContenido(Usuario usuario, int id, Class<T> tipoContenido) throws ContenidoNoEncontradoException, IOException {
        T contenidoAEliminar = null;

        // Eliminar contenido de la lista correspondiente (animes o mangas)
        if (tipoContenido == Anime.class) {
            for (Anime contenido : usuario.getAnimes()) {  // Cambié el tipo explícito a Anime
                if (contenido.getId() == id) {
                    contenidoAEliminar = (T) contenido;
                    break;
                }
            }
        } else if (tipoContenido == Manga.class) {
            for (Manga contenido : usuario.getMangas()) {  // Cambié el tipo explícito a Manga
                if (contenido.getId() == id) {
                    contenidoAEliminar = (T) contenido;
                    break;
                }
            }
        }

        if (contenidoAEliminar != null) {
            // Eliminar contenido de la lista correspondiente
            if (tipoContenido == Anime.class) {
                usuario.getAnimes().remove(contenidoAEliminar);
            } else if (tipoContenido == Manga.class) {
                usuario.getMangas().remove(contenidoAEliminar);
            }

            // Guardar los cambios en el archivo
            JsonUtilUsuario.modificarUsuarioEnArchivo(usuario);
            System.out.println("Contenido eliminado exitosamente.");
        } else {
            throw new ContenidoNoEncontradoException("No se encontró el contenido con el ID " + id + " en tu lista.");
        }
    }

    public <T extends Contenido> void asignarEstadoContenido(T contenido) {
        System.out.println("Seleccione el estado para el contenido:");
        System.out.println("1. VISTO");
        System.out.println("2. NO_VISTO");
        System.out.print("Opción: ");
        int opcion = new Scanner(System.in).nextInt();

        try {
            // Verificar el estado actual
            EstadoVisto estadoActual = contenido.getVistoONo();

            switch (opcion) {
                case 1:
                    if (estadoActual == EstadoVisto.VISTO) {
                        throw new EstadoRepetidoException("El contenido ya está marcado como VISTO.");
                    }
                    contenido.setVistoONo(EstadoVisto.VISTO);
                    break;

                case 2:
                    if (estadoActual == EstadoVisto.NO_VISTO) {
                        throw new EstadoRepetidoException("El contenido ya está marcado como NO_VISTO.");
                    }
                    contenido.setVistoONo(EstadoVisto.NO_VISTO);
                    break;

                default:
                    System.out.println("Opción inválida. No se cambió el estado.");
                    break;
            }
        } catch (EstadoRepetidoException e) {
            System.out.println("Error: " + e.getMessage()); // Mensaje de la excepción
        }
    }


    public static <T> List<T> cargarContenidoDesdeArchivo(String archivo, String tipoContenido) {
        List<T> contenidos = new ArrayList<>();

        if (tipoContenido.equalsIgnoreCase("anime")) {
            JsonUtilAnime jsonUtilAnime = new JsonUtilAnime();
            contenidos = (List<T>) jsonUtilAnime.cargarAnimesDesdeArchivo(archivo);
        } else if (tipoContenido.equalsIgnoreCase("manga")) {
            JsonUtilManga jsonUtilManga = new JsonUtilManga();
            contenidos = (List<T>) jsonUtilManga.cargarMangasDesdeArchivo(archivo);
        }

        return contenidos;
    }


    // Método para mostrar los contenidos disponibles (Anime o Manga)
    public void mostrarContenidoDisponible(Class<T> tipoContenido) {
        // Dependiendo del tipo de contenido, seleccionamos el tipo adecuado ("anime" o "manga")
        String tipoContenidoStr = "";
        if (tipoContenido == Anime.class) {
            tipoContenidoStr = "anime"; // Tipo de contenido: anime
        } else if (tipoContenido == Manga.class) {
            tipoContenidoStr = "manga"; // Tipo de contenido: manga
        }

        // Usamos el método cargarContenidoDesdeArchivo para cargar los contenidos
        List<T> contenidos = cargarContenidoDesdeArchivo("prueba" + tipoContenidoStr + ".json", tipoContenidoStr);

        // Mostrar los contenidos disponibles
        if (contenidos.isEmpty()) {
            System.out.println("No hay contenidos disponibles.");
        } else {
            System.out.println("---- Contenidos Disponibles ----");
            for (T contenido : contenidos) {
                // Como el tipo T puede ser Anime o Manga, debes asegurarte de que los métodos sean accesibles
                // Puedes cast a los tipos adecuados si es necesario
                System.out.println("ID: " + contenido.getId());
                System.out.println("Nombre: " + contenido.getTitle());
                System.out.println("Puntuación: " + contenido.getScore());
                System.out.println("Popularidad: " + contenido.getPopularity());
                System.out.println("Estado: " + contenido.getVistoONo());
                System.out.println("------------------------------");
            }
        }
    }


    // Método para mostrar los contenidos del usuario (Anime o Manga)
    public void mostrarContenidoUsuario(Usuario usuario, String tipoContenido) {
        // Mostrar contenidos según el tipo (Anime o Manga)
        if (tipoContenido.equalsIgnoreCase("Anime")) {
            if (usuario.getAnimes().isEmpty()) {
                System.out.println("Tu lista de animes está vacía.");
            } else {
                System.out.println("---- Tus Animes ----");
                for (Anime anime : usuario.getAnimes()) {
                    System.out.println("ID: " + anime.getId());
                    System.out.println("Nombre: " + anime.getTitle());
                    System.out.println("Puntuación: " + anime.getScore());
                    System.out.println("Popularidad: " + anime.getPopularity());
                    System.out.println("Estado: " + anime.getVistoONo());
                    System.out.println("---------------------");
                }
            }
        } else if (tipoContenido.equalsIgnoreCase("Manga")) {
            if (usuario.getMangas().isEmpty()) {
                System.out.println("Tu lista de mangas está vacía.");
            } else {
                System.out.println("---- Tus Mangas ----");
                for (Manga manga : usuario.getMangas()) {
                    System.out.println("ID: " + manga.getId());
                    System.out.println("Nombre: " + manga.getTitle());
                    System.out.println("Puntuación: " + manga.getScore());
                    System.out.println("Popularidad: " + manga.getPopularity());
                    System.out.println("Estado: " + manga.getVistoONo());
                    System.out.println("---------------------");
                }
            }
        }
    }

    public <T extends Contenido> void mostrarContenidoOrdenado(List<T> contenidoOrdenado) {
        if (contenidoOrdenado == null || contenidoOrdenado.isEmpty()) {
            System.out.println("No hay contenido disponible para mostrar.");
            return;
        }

        System.out.println("---- Contenido Ordenado ----");
        for (T contenido : contenidoOrdenado) {
            System.out.println(contenido);
        }
    }

}
