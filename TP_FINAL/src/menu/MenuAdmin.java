// Clase MenuAdmin sin el método main
package menu;

import admin.GestorAdminAnime;
import admin.GestorAdminManga;
import manejo_json.JsonUtilAnime;
import manejo_json.JsonUtilManga;
import java.util.Scanner;

public class MenuAdmin {

    public static final String PATH_MANGA = "Manga.json";
    public static final String PATH_ANIME = "Anime.json";
    private Scanner scanner = new Scanner(System.in);

    public void mostrarMenuAdmin() {
        // Usa el scanner de la clase para evitar crear uno nuevo cada vez
        boolean exit = false;

        while (!exit) {
            // Menú principal
            System.out.println("\n=== Menú de Administración ===");
            System.out.println("1. Gestionar Animes");
            System.out.println("2. Gestionar Mangas");
            System.out.println("3. Salir");
            System.out.print("Selecciona una opción: ");

            // Verifica si el siguiente input es un entero
            if (scanner.hasNextInt()) {
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

                switch (opcion) {
                    case 1: // Gestionar Animes
                        gestionarAnimes(scanner);
                        break;

                    case 2: // Gestionar Mangas
                        gestionarMangas(scanner);
                        break;

                    case 3: // Salir
                        exit = true;
                        break;

                    default:
                        System.out.println("Opción inválida. Inténtalo de nuevo.");
                }
            } else {
                // Manejo de entrada no válida
                System.out.println("Entrada inválida. Por favor ingresa un número.");
                scanner.nextLine(); // Limpiar el buffer en caso de entrada incorrecta
            }
        }
    }


    private void gestionarAnimes(Scanner scanner) {
        boolean salirAnime = false;
        while (!salirAnime) {
            // Limpiar la pantalla solo en Windows
            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println("\n=== Menú de Gestión de Animes ===");
            System.out.println("1. Crear Anime");
            System.out.println("2. Eliminar Anime");
            System.out.println("3. Actualizar Anime");
            System.out.println("4. Regresar");
            System.out.print("Selecciona una opción: ");

            int subOpcionAnime = scanner.nextInt();
            scanner.nextLine();

            switch (subOpcionAnime) {
                case 1:
                    GestorAdminAnime gestorAnime = new GestorAdminAnime();
                    gestorAnime.crear();
                    JsonUtilAnime utilsanime = new JsonUtilAnime();
                    utilsanime.mostrarAnimesConsola(PATH_ANIME);
                    break;
                case 2:
                    eliminarAnime(scanner);
                    break;
                case 3:
                    GestorAdminAnime gestorActualizarAnime = new GestorAdminAnime();
                    gestorActualizarAnime.actualizar();
                    break;
                case 4:
                    salirAnime = true;
                    break;
                default:
                    System.out.println("Opción inválida para Animes.");
            }
        }
    }

    private void gestionarMangas(Scanner scanner) {
        boolean salirManga = false;
        while (!salirManga) {
            // Limpiar la pantalla solo en Windows
            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println("\n=== Menú de Gestión de Mangas ===");
            System.out.println("1. Crear Manga");
            System.out.println("2. Eliminar Manga");
            System.out.println("3. Actualizar Manga");
            System.out.println("4. Regresar");
            System.out.print("Selecciona una opción: ");

            int subOpcionManga = scanner.nextInt();
            scanner.nextLine();

            switch (subOpcionManga) {
                case 1:
                    GestorAdminManga gestorManga = new GestorAdminManga();
                    gestorManga.crear();
                    JsonUtilManga utilsManga = new JsonUtilManga();
                    utilsManga.mostrarMangasConsola(PATH_MANGA);
                    break;
                case 2:
                    eliminarManga(scanner);
                    break;
                case 3:
                    GestorAdminManga gestorActualizarManga = new GestorAdminManga();
                    gestorActualizarManga.actualizar();
                    break;
                case 4:
                    salirManga = true;
                    break;
                default:
                    System.out.println("Opción inválida para Mangas.");
            }
        }
    }

    // Función para eliminar anime
    public static void eliminarAnime(Scanner scanner) {
        boolean salirEliminarAnime = false;

        while (!salirEliminarAnime) {
            System.out.println("\n=== Eliminar Anime ===");
            System.out.println("1. Eliminar por ID");
            System.out.println("2. Eliminar por Título");
            System.out.println("3. Salir");
            System.out.print("Selecciona una opción: ");
            int opcionEliminarAnime = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcionEliminarAnime) {
                case 1:
                    System.out.print("Ingrese el ID del anime a eliminar: ");
                    int idAnime = scanner.nextInt();
                    GestorAdminAnime gestorEliminarPorId = new GestorAdminAnime();
                    gestorEliminarPorId.eliminar_por_id(idAnime);
                    break;
                case 2:
                    System.out.print("Ingrese el título del anime a eliminar: ");
                    String tituloAnime = scanner.nextLine();
                    GestorAdminAnime gestorEliminarPorTitulo = new GestorAdminAnime();
                    gestorEliminarPorTitulo.eliminar_por_titulo(tituloAnime);
                    break;
                case 3:
                    salirEliminarAnime = true;
                    break;
                default:
                    System.out.println("Opción inválida para eliminar anime.");
            }
        }
    }

    // Función para eliminar manga
    public static void eliminarManga(Scanner scanner) {
        boolean salirEliminarManga = false;

        while (!salirEliminarManga) {
            System.out.println("\n=== Eliminar Manga ===");
            System.out.println("1. Eliminar por ID");
            System.out.println("2. Eliminar por Título");
            System.out.println("3. Salir");
            System.out.print("Selecciona una opción: ");
            int opcionEliminarManga = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcionEliminarManga) {
                case 1:
                    System.out.print("Ingrese el ID del manga a eliminar: ");
                    int idManga = scanner.nextInt();
                    GestorAdminManga gestorEliminarPorId = new GestorAdminManga();
                    gestorEliminarPorId.eliminar_por_id(idManga);
                    break;
                case 2:
                    System.out.print("Ingrese el título del manga a eliminar: ");
                    String tituloManga = scanner.nextLine();
                    GestorAdminManga gestorEliminarPorTitulo = new GestorAdminManga();
                    gestorEliminarPorTitulo.eliminar_por_titulo(tituloManga);
                    break;
                case 3:
                    salirEliminarManga = true;
                    break;
                default:
                    System.out.println("Opción inválida para eliminar manga.");
            }
        }
    }
}
