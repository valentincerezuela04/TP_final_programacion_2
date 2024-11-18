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

    public <T extends Contenido> List<T> ordenarListaPersonal(Usuario usuario, String tipoContenido, String criterio) {
        List<T> listaPersonal = null;

        if (tipoContenido.equalsIgnoreCase("anime")) {
            listaPersonal = (List<T>) usuario.getAnimes();
        } else if (tipoContenido.equalsIgnoreCase("manga")) {
            listaPersonal = (List<T>) usuario.getMangas();
        }

        if (listaPersonal == null || listaPersonal.isEmpty()) {
            System.out.println("Tu lista está vacía.");
            return new ArrayList<>();  // Devolver una lista vacía
        }

        switch (criterio.toLowerCase()) {
            case "id":
                listaPersonal.sort(Comparator.comparingInt(Contenido::getId));
                break;
            case "titulo":
                listaPersonal.sort(Comparator.comparing(Contenido::getTitle, String.CASE_INSENSITIVE_ORDER));
                break;
            case "popularidad":
                listaPersonal.sort((c1, c2) -> Integer.compare(c2.getPopularity(), c1.getPopularity()));
                break;
            case "score":
                listaPersonal.sort((c1, c2) -> Double.compare(c2.getScore(), c1.getScore()));
                break;
            default:
                System.out.println("Criterio de ordenamiento no válido.");
                return new ArrayList<>();  // Devolver una lista vacía en caso de criterio no válido
        }

        return listaPersonal;  // Devolver la lista ordenada
    }


    // Método para agregar contenido (Anime o Manga) a la lista del usuario
    public <T> void agregarContenido(Usuario usuario, T contenido) throws IOException, ContenidoDuplicadoException {
        // Verificar si el contenido ya está en la lista de animes o mangas
        if (contenido instanceof Anime) {
            // Preguntar por el estado del anime antes de agregarlo
            System.out.println("Seleccione el estado para el anime:");
            System.out.println("1. VISTO");
            System.out.println("2. NO_VISTO");
            System.out.print("Opción: ");
            int opcion = new Scanner(System.in).nextInt();
            EstadoVisto estado = (opcion == 1) ? EstadoVisto.VISTO : EstadoVisto.NO_VISTO;

            // Verificamos si el anime ya está en la lista de animes del usuario
            if (usuario.getAnimes().contains(contenido)) {
                throw new ContenidoDuplicadoException("Este anime ya está en tu lista.");
            }

            // Asignar el estado y agregar el anime a la lista
            ((Anime) contenido).setVistoONo(estado);
            usuario.getAnimes().add((Anime) contenido);

        } else if (contenido instanceof Manga) {
            // Preguntar por el estado del manga antes de agregarlo
            System.out.println("Seleccione el estado para el manga:");
            System.out.println("1. VISTO");
            System.out.println("2. NO_VISTO");
            System.out.print("Opción: ");
            int opcion = new Scanner(System.in).nextInt();
            EstadoVisto estado = (opcion == 1) ? EstadoVisto.VISTO : EstadoVisto.NO_VISTO;

            // Verificamos si el manga ya está en la lista de mangas del usuario
            if (usuario.getMangas().contains(contenido)) {
                throw new ContenidoDuplicadoException("Este manga ya está en tu lista.");
            }

            // Asignar el estado y agregar el manga a la lista
            ((Manga) contenido).setVistoONo(estado);
            usuario.getMangas().add((Manga) contenido);
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

    public <T extends Contenido> void asignarEstadoContenido(T contenido, Usuario usuario) {
        System.out.println("Seleccione el estado para el contenido:");
        System.out.println("1. VISTO");
        System.out.println("2. NO_VISTO");
        System.out.print("Opción: ");
        int opcion = new Scanner(System.in).nextInt();

        boolean contenidoEncontrado = false;

        try {
            // Determinar a qué lista pertenece el contenido (Anime o Manga)
            if (contenido instanceof Anime) {
                // Verificar en la lista de animes del usuario
                for (Anime animeEnLista : usuario.getAnimes()) {
                    if (animeEnLista.getId() == contenido.getId()) {
                        contenidoEncontrado = true;

                        // Verificar si el estado ya coincide
                        if ((opcion == 1 && animeEnLista.getVistoONo() == EstadoVisto.VISTO) ||
                                (opcion == 2 && animeEnLista.getVistoONo() == EstadoVisto.NO_VISTO)) {
                            throw new EstadoRepetidoException("El anime ya tiene el estado seleccionado en tu lista personal.");
                        }

                        // Actualizar el estado del anime existente
                        animeEnLista.setVistoONo(opcion == 1 ? EstadoVisto.VISTO : EstadoVisto.NO_VISTO);
                        break;
                    }
                }

                // Si no está en la lista, agregarlo
                if (!contenidoEncontrado) {
                    contenido.setVistoONo(opcion == 1 ? EstadoVisto.VISTO : EstadoVisto.NO_VISTO);
                    usuario.getAnimes().add((Anime) contenido);
                }

            } else if (contenido instanceof Manga) {
                // Verificar en la lista de mangas del usuario
                for (Manga mangaEnLista : usuario.getMangas()) {
                    if (mangaEnLista.getId() == contenido.getId()) {
                        contenidoEncontrado = true;

                        // Verificar si el estado ya coincide
                        if ((opcion == 1 && mangaEnLista.getVistoONo() == EstadoVisto.VISTO) ||
                                (opcion == 2 && mangaEnLista.getVistoONo() == EstadoVisto.NO_VISTO)) {
                            throw new EstadoRepetidoException("El manga ya tiene el estado seleccionado en tu lista personal.");
                        }

                        // Actualizar el estado del manga existente
                        mangaEnLista.setVistoONo(opcion == 1 ? EstadoVisto.VISTO : EstadoVisto.NO_VISTO);
                        break;
                    }
                }

                // Si no está en la lista, agregarlo
                if (!contenidoEncontrado) {
                    contenido.setVistoONo(opcion == 1 ? EstadoVisto.VISTO : EstadoVisto.NO_VISTO);
                    usuario.getMangas().add((Manga) contenido);
                }
            } else {
                throw new IllegalArgumentException("Tipo de contenido no soportado.");
            }

        } catch (EstadoRepetidoException e) {
            GestorExcepciones.manejarEstadoRepetidoException(e);
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
                // Verificamos si el contenido es un anime o un manga
                if (contenido instanceof Anime) {
                    Anime anime = (Anime) contenido;
                    System.out.println("ID: " + anime.getId());
                    System.out.println("Nombre: " + anime.getTitle());
                    System.out.println("Puntuación: " + anime.getScore());
                    System.out.println("Popularidad: " + anime.getPopularity());
                    System.out.println("Episodios: " + anime.getEpisodes());
                    System.out.println("------------------------------");
                } else if (contenido instanceof Manga) {
                    Manga manga = (Manga) contenido;
                    System.out.println("ID: " + manga.getId());
                    System.out.println("Nombre: " + manga.getTitle());
                    System.out.println("Puntuación: " + manga.getScore());
                    System.out.println("Popularidad: " + manga.getPopularity());
                    System.out.println("Capítulos: " + manga.getChapters());
                    System.out.println("Volúmenes: " + manga.getVolumes());
                    System.out.println("------------------------------");
                }
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
                    System.out.println("Episodios: " + anime.getEpisodes());
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
                    System.out.println("Capitulos: " + manga.getChapters());
                    System.out.println("Volúmenes: " + manga.getVolumes());
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
            if (contenido instanceof Anime) {
                Anime anime = (Anime) contenido;
                System.out.println("ID: " + anime.getId());
                System.out.println("Nombre: " + anime.getTitle());
                System.out.println("Puntuación: " + anime.getScore());
                System.out.println("Popularidad: " + anime.getPopularity());
                System.out.println("Episodios: " + anime.getEpisodes());
                System.out.println("---------------------");
            } else if (contenido instanceof Manga) {
                Manga manga = (Manga) contenido;
                System.out.println("ID: " + manga.getId());
                System.out.println("Nombre: " + manga.getTitle());
                System.out.println("Puntuación: " + manga.getScore());
                System.out.println("Popularidad: " + manga.getPopularity());
                System.out.println("Capítulos: " + manga.getChapters());
                System.out.println("Volúmenes: " + manga.getVolumes());
                System.out.println("Estado: " + manga.getVistoONo());
                System.out.println("---------------------");
            }
        }
    }


}
