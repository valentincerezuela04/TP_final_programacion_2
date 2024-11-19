package menu;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import contenido.Anime;
import contenido.Contenido;
import contenido.Manga;
import excepciones.*;
import gestores.GestorArchivos;
import gestores.GestorContenido;
import gestores.GestorExcepciones;
import gestores.GestorUsuarios;
import manejoJson.JsonUtilUsuario;
import usuario.Usuario;
import usuario.ValidacionUsuario;

public class Menu {
    private GestorUsuarios gestorUsuarios;
    private Scanner scanner;
    private GestorContenido gestorContenido;
    private static final String ARCHIVO_ANIMES = "Anime.json";
    private static final String ARCHIVO_MANGAS = "Manga.json";
    private static final String tipoAnime = "anime";
    private static final String tipoManga = "manga";

    public Menu() {
        this.gestorUsuarios = new GestorUsuarios();
        this.scanner = new Scanner(System.in);
        this.gestorContenido = new GestorContenido();

        GestorArchivos.crearArchivosPorDefecto();
    }

    // Método para mostrar el menú principal
    public void menuPrincipal() {
        int opcion;
        boolean continuar = true;
        do {

            System.out.println("---- Menú Principal ----");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Crear una cuenta");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        menuIniciarSesion();
                        break;
                    case 2:
                        menuCrearCuenta();
                        break;
                    case 3:
                        System.out.println("Saliendo del sistema...");
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } else {
                System.out.println("Entrada no válida. Intente nuevamente.");
                scanner.nextLine();
            }
        } while (continuar);
    }


    // Método para iniciar sesión
    public void menuIniciarSesion() {
        System.out.print("Ingrese su nombre de usuario: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contraseña = scanner.nextLine();

        try {
            Usuario usuario = gestorUsuarios.iniciarSesion(nombre, contraseña);

            System.out.println("Inicio de sesión exitoso. Bienvenido, " + usuario.getNombre() + "!");

            if ("admin01".equals(usuario.getNombre()) && "admin01".equals(contraseña)) {
                MenuAdmin menuAdmin = new MenuAdmin();
                menuAdmin.mostrarMenuAdmin();
            } else {
                menuUsuario(usuario);
            }

        } catch (LoginException e) {
            GestorExcepciones.manejarLoginException(e);
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    // Método para crear una cuenta
    public void menuCrearCuenta() {
        System.out.println("Crear nueva cuenta");

        System.out.print("Ingrese su nombre de usuario: ");
        String nombre = scanner.nextLine();

        String email;
        while (true) {
            System.out.print("Ingrese su email: ");
            email = scanner.nextLine();

            try {
                ValidacionUsuario.esEmailValido(email);
                break;
            } catch (EmailInvalidoException e) {
                GestorExcepciones.manejarEmailInvalido(e);
            }
        }

        String contraseña;
        while (true) {
            System.out.print("Ingrese su contraseña: ");
            contraseña = scanner.nextLine();

            try {
                ValidacionUsuario.esContraseñaValida(contraseña);
                break;
            } catch (ContrasenaInvalidaException e) {
                GestorExcepciones.manejarContrasenaInvalida(e);
            }
        }

        String confirmacionContraseña;
        while (true) {
            System.out.print("Confirme su contraseña: ");
            confirmacionContraseña = scanner.nextLine();

            if (!contraseña.equals(confirmacionContraseña)) {
                GestorExcepciones.manejarContrasenasNoCoinciden(new ContrasenasNoCoincidenException("Las contraseñas no coinciden."));
            } else {
                break;
            }
        }

        try {
            Usuario nuevoUsuario = new Usuario(nombre, contraseña, email);

            Usuario usuarioRegistrado = gestorUsuarios.registrarUsuario(nuevoUsuario);
            System.out.println("Cuenta creada exitosamente.");

        } catch (UsuarioRepetidoException e) {
            GestorExcepciones.manejarUsuarioRepetido(e);
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }
    // Método para mostrar el menú principal de usuario
    public void menuUsuario(Usuario usuario) {
        int opcion;
        boolean continuarEnMenuUsuario = true;

        do {
            System.out.println("---- Menú de Usuario ----");
            System.out.println("1. Administrar lista de Animes");
            System.out.println("2. Administrar lista de Mangas");
            System.out.println("3. Opciones de cuenta");
            System.out.println("4. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        menuAdministrarListaContenido(usuario, tipoAnime);
                        break;
                    case 2:
                        menuAdministrarListaContenido(usuario, tipoManga);
                        break;
                    case 3:
                        continuarEnMenuUsuario = menuOpcionesUsuario(usuario);
                        break;
                    case 4:
                        System.out.println("Cerrando sesión...");
                        continuarEnMenuUsuario = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } else {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                scanner.nextLine();
            }
        } while (continuarEnMenuUsuario);
    }

    // Método para administrar la lista de contenido (Anime/Manga)
    public void menuAdministrarListaContenido(Usuario usuario, String tipoContenido) {
        int opcion;
        boolean continuar = true;

        do {
            System.out.println("---- Menú Administrar Lista de " + tipoContenido.substring(0, 1).toUpperCase() + tipoContenido.substring(1) + "s ----");
            System.out.println("1. Ver mi lista");
            System.out.println("2. Agregar " + tipoContenido.substring(0, 1).toUpperCase() + tipoContenido.substring(1));
            System.out.println("3. Eliminar " + tipoContenido.substring(0, 1).toUpperCase() + tipoContenido.substring(1));
            System.out.println("4. Cambiar estado de " + tipoContenido.substring(0, 1).toUpperCase() + tipoContenido.substring(1));
            System.out.print("5. Atrás\nSeleccione una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        gestorContenido.mostrarContenidoUsuario(usuario, tipoContenido);

                        if (tipoContenido.equalsIgnoreCase(tipoAnime) && !usuario.getAnimes().isEmpty()) {
                            menuOrdenarListaPersonal(usuario, tipoAnime, Anime.class);
                        } else if (tipoContenido.equalsIgnoreCase(tipoManga) && !usuario.getMangas().isEmpty()) {
                            menuOrdenarListaPersonal(usuario, tipoManga, Manga.class);
                        }
                        break;
                    case 2:
                        if (tipoContenido.equalsIgnoreCase(tipoAnime)) {
                            menuAgregarContenido(usuario, tipoAnime);
                        } else if (tipoContenido.equalsIgnoreCase(tipoManga)) {
                            menuAgregarContenido(usuario, tipoManga);
                        }
                        break;
                    case 3:
                        if (tipoContenido.equalsIgnoreCase(tipoAnime)) {
                            menuEliminarContenido(usuario, tipoContenido, ARCHIVO_ANIMES, Anime.class);
                        } else if (tipoContenido.equalsIgnoreCase(tipoManga)) {
                            menuEliminarContenido(usuario, tipoContenido, ARCHIVO_MANGAS, Manga.class);
                        }
                        break;
                    case 4:
                        try {
                            menuCambiarEstadoContenido(usuario, tipoContenido);
                        } catch (ContenidoNoEncontradoException e) {
                            GestorExcepciones.manejarContenidoNoEncontrado(e);
                        } catch (IOException e) {
                            GestorExcepciones.manejarIOException(e);
                        } catch (Exception e) {
                            GestorExcepciones.manejarExcepcion(e);
                        }
                        break;
                    case 5:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } else {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                scanner.nextLine();
            }
        } while (continuar);
    }

    // Método para ordenar la lista personal de un tipo de contenido
    public <T extends Contenido> void menuOrdenarListaPersonal(Usuario usuario, String tipoContenido, Class<T> tipoClase) {
        int opcion;
        boolean continuar = true;

        do {
            System.out.println("---- Ordenar Mi Lista de " + tipoContenido.substring(0, 1).toUpperCase() + tipoContenido.substring(1) + "s ----");
            System.out.println("1. Ordenar por ID");
            System.out.println("2. Ordenar por Título");
            System.out.println("3. Ordenar por Popularidad");
            System.out.println("4. Ordenar por Puntuación");
            System.out.println("5. Atrás");
            System.out.print("Seleccione una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        List<T> listaOrdenadaPorId = gestorContenido.ordenarListaPersonal(usuario, tipoContenido, "id");
                        gestorContenido.mostrarContenidoOrdenado(listaOrdenadaPorId);
                        break;
                    case 2:
                        List<T> listaOrdenadaPorTitulo = gestorContenido.ordenarListaPersonal(usuario, tipoContenido, "titulo");
                        gestorContenido.mostrarContenidoOrdenado(listaOrdenadaPorTitulo);
                        break;
                    case 3:
                        List<T> listaOrdenadaPorPopularidad = gestorContenido.ordenarListaPersonal(usuario, tipoContenido, "popularidad");
                        gestorContenido.mostrarContenidoOrdenado(listaOrdenadaPorPopularidad);
                        break;
                    case 4:
                        List<T> listaOrdenadaPorScore = gestorContenido.ordenarListaPersonal(usuario, tipoContenido, "score");
                        gestorContenido.mostrarContenidoOrdenado(listaOrdenadaPorScore);
                        break;
                    case 5:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } else {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                scanner.nextLine();
            }
        } while (continuar);
    }

    // Método para agregar contenido (Anime/Manga) a la lista del usuario
    public void menuAgregarContenido(Usuario usuario, String tipoContenido) {
        int opcion;
        boolean continuar = true;

        do {
            System.out.println("---- Menú Agregar " + tipoContenido.substring(0, 1).toUpperCase() + tipoContenido.substring(1) + " ----");
            System.out.println("1. Buscar por ID");
            System.out.println("2. Buscar por Nombre");
            System.out.println("3. Ordenar por (Esto solo afecta la lista global)");
            System.out.println("4. Atrás");
            System.out.print("Seleccione una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        if (tipoContenido.equalsIgnoreCase(tipoAnime)) {
                            menuBuscarContenidoPorId(usuario, Anime.class);
                        } else if (tipoContenido.equalsIgnoreCase(tipoManga)) {
                            menuBuscarContenidoPorId(usuario, Manga.class);
                        }
                        break;
                    case 2:
                        if (tipoContenido.equalsIgnoreCase(tipoAnime)) {
                            menuBuscarContenidoPorNombre(usuario, Anime.class);
                        } else if (tipoContenido.equalsIgnoreCase(tipoManga)) {
                            menuBuscarContenidoPorNombre(usuario, Manga.class);
                        }
                        break;
                    case 3:
                        if (tipoContenido.equalsIgnoreCase(tipoAnime)) {
                            menuOrdenarContenido(ARCHIVO_ANIMES, Anime.class, tipoAnime);
                        } else if (tipoContenido.equalsIgnoreCase(tipoManga)) {
                            menuOrdenarContenido(ARCHIVO_MANGAS, Manga.class, tipoManga);
                        }
                        break;
                    case 4:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } else {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                scanner.nextLine();
            }
        } while (continuar);
    }

    // Método para buscar contenido por ID
    public void menuBuscarContenidoPorId(Usuario usuario, Class<?> tipoContenido) {
        gestorContenido.mostrarContenidoDisponible(tipoContenido);

        int idContenido = -1;
        boolean entradaValida = false;

        while (!entradaValida) {
            System.out.print("Ingrese el ID del contenido que desea agregar: ");

            if (scanner.hasNextInt()) {
                idContenido = scanner.nextInt();
                scanner.nextLine();
                entradaValida = true;
            } else {
                System.out.println("Entrada no válida. Por favor, ingrese un número entero.");
                scanner.nextLine();
            }
        }

        try {
            String archivoContenido = tipoContenido.equals(Anime.class) ? ARCHIVO_ANIMES : tipoContenido.equals(Manga.class) ? ARCHIVO_MANGAS : null;

            if (archivoContenido == null) {
                throw new IllegalArgumentException("Tipo de contenido no válido.");
            }

            Contenido contenidoSeleccionado = gestorContenido.buscarContenidoPorId(archivoContenido, idContenido, tipoContenido);

            if (contenidoSeleccionado != null) {
                gestorContenido.agregarContenido(usuario, contenidoSeleccionado);
            }
        } catch (ContenidoNoEncontradoException e) {
            GestorExcepciones.manejarContenidoNoEncontrado(e);
        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    // Método que permite al usuario buscar un contenido por nombre (anime o manga)
    public void menuBuscarContenidoPorNombre(Usuario usuario, Class<?> tipoContenido) {
        String nombreContenido = "";
        boolean entradaValida = false;

        while (!entradaValida) {
            System.out.print("Ingrese el nombre del contenido que desea agregar: ");
            nombreContenido = scanner.nextLine().trim();

            if (!nombreContenido.isEmpty()) {
                entradaValida = true;
            } else {
                System.out.println("El nombre no puede estar vacío. Por favor, ingrese un nombre válido.");
            }
        }

        try {
            String archivoContenido = tipoContenido.equals(Anime.class) ? ARCHIVO_ANIMES : tipoContenido.equals(Manga.class) ? ARCHIVO_MANGAS : null;

            if (archivoContenido == null) {
                throw new IllegalArgumentException("Tipo de contenido no válido.");
            }

            Contenido contenidoSeleccionado = gestorContenido.buscarContenidoPorNombre(archivoContenido, nombreContenido, tipoContenido);

            if (contenidoSeleccionado != null) {
                gestorContenido.agregarContenido(usuario, contenidoSeleccionado);
            }
        } catch (ContenidoNoEncontradoException e) {
            GestorExcepciones.manejarContenidoNoEncontrado(e);
        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    // Método que permite al usuario ordenar el contenido por diferentes criterios (ID, título, popularidad, puntuación)
    public <T> void menuOrdenarContenido(String archivo, Class<T> tipoContenido, String tipo) {
        int opcion = -1;
        boolean continuar = true;

        do {
            System.out.println("---- Menú Ordenar " + tipo + " ----");
            System.out.println("1. Ordenar por ID");
            System.out.println("2. Ordenar por Título");
            System.out.println("3. Ordenar por Popularidad");
            System.out.println("4. Ordenar por Puntuación");
            System.out.println("5. Atrás");
            System.out.print("Seleccione una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();

                if (opcion < 1 || opcion > 5) {
                    System.out.println("Opción no válida. Por favor, seleccione una opción entre 1 y 5.");
                    opcion = -1;
                }
            } else {
                System.out.println("Entrada no válida. Por favor, ingrese un número entero.");
                scanner.nextLine();
            }

            if (opcion != -1) {
                switch (opcion) {
                    case 1:
                        List<T> contenidoOrdenadoPorId = gestorContenido.ordenarContenido(archivo, "id", tipoContenido);
                        gestorContenido.mostrarContenidoOrdenado(contenidoOrdenadoPorId);
                        break;
                    case 2:
                        List<T> contenidoOrdenadoPorTitulo = gestorContenido.ordenarContenido(archivo, "titulo", tipoContenido);
                        gestorContenido.mostrarContenidoOrdenado(contenidoOrdenadoPorTitulo);
                        break;
                    case 3:
                        List<T> contenidoOrdenadoPorPopularidad = gestorContenido.ordenarContenido(archivo, "popularidad", tipoContenido);
                        gestorContenido.mostrarContenidoOrdenado(contenidoOrdenadoPorPopularidad);
                        break;
                    case 4:
                        List<T> contenidoOrdenadoPorScore = gestorContenido.ordenarContenido(archivo, "score", tipoContenido);
                        gestorContenido.mostrarContenidoOrdenado(contenidoOrdenadoPorScore);
                        break;
                    case 5:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            }

        } while (continuar);
    }

    // Método que permite al usuario eliminar contenido de su lista (anime o manga)
    public <T> void menuEliminarContenido(Usuario usuario, String tipoContenido, String archivo, Class<T> claseContenido) {
        gestorContenido.mostrarContenidoUsuario(usuario, tipoContenido);

        if ((tipoContenido.equalsIgnoreCase("anime") && usuario.getAnimes().isEmpty()) ||
                (tipoContenido.equalsIgnoreCase("manga") && usuario.getMangas().isEmpty())) {
            System.out.println("No tienes " + tipoContenido + "s en tu lista para eliminar.");
            return;
        }

        int idContenido = -1;

        while (idContenido == -1) {
            System.out.print("Ingrese el ID del " + tipoContenido + " que desea eliminar: ");

            if (scanner.hasNextInt()) {
                idContenido = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Entrada no válida. Por favor, ingrese un número entero.");
                scanner.nextLine();
            }
        }

        try {
            gestorContenido.eliminarContenido(usuario, idContenido, claseContenido);

            JsonUtilUsuario.modificarUsuarioEnArchivo(usuario);
        } catch (ContenidoNoEncontradoException e) {
            GestorExcepciones.manejarContenidoNoEncontrado(e);
        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    // Método que permite al usuario cambiar el estado de un contenido (anime o manga)
    public void menuCambiarEstadoContenido(Usuario usuario, String tipoContenido) throws ContenidoNoEncontradoException, IllegalArgumentException, IOException {
        gestorContenido.mostrarContenidoUsuario(usuario, tipoContenido);

        int id = -1;
        while (id == -1) {
            System.out.print("Ingrese el ID del contenido cuyo estado desea cambiar: ");

            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Entrada no válida. Por favor, ingrese un número entero.");
                scanner.nextLine();
            }
        }

        JsonUtilUsuario jsonUtilUsuario = new JsonUtilUsuario();

        try {
            String archivoContenido = tipoContenido.equalsIgnoreCase("Anime") ? ARCHIVO_ANIMES : tipoContenido.equalsIgnoreCase("Manga") ? ARCHIVO_MANGAS : null;

            if (archivoContenido == null) {
                throw new IllegalArgumentException("Tipo de contenido no válido. Debe ser 'Anime' o 'Manga'.");
            }

            Contenido contenidoSeleccionado = gestorContenido.buscarContenidoPorId(archivoContenido, id, tipoContenido.equalsIgnoreCase("Anime") ? Anime.class : Manga.class);

            if (contenidoSeleccionado != null) {
                gestorContenido.asignarEstadoContenido(contenidoSeleccionado, usuario);

                jsonUtilUsuario.modificarUsuarioEnArchivo(usuario);

                System.out.println("Nuevo estado asignado exitosamente.");
            } else {
                throw new ContenidoNoEncontradoException("No se encontró contenido con el ID proporcionado.");
            }
        } catch (EstadoRepetidoException e) {
            System.out.println("Error de estado repetido: " + e.getMessage());
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    // Método que muestra un menú con opciones para que el usuario cambie su contraseña, email, nombre de usuario o elimine su cuenta
    public boolean menuOpcionesUsuario(Usuario usuario) {
        int opcion;
        boolean continuarEnMenu = true;

        do {
            System.out.println("---- Opciones de Usuario ----");
            System.out.println("1. Cambiar contraseña");
            System.out.println("2. Cambiar email");
            System.out.println("3. Cambiar nombre de usuario");
            System.out.println("4. Eliminar cuenta");
            System.out.println("5. Volver al menú de usuario");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    opcionCambiarContrasena(usuario);
                    break;
                case 2:
                    opcionCambiarEmail(usuario);
                    break;
                case 3:
                    opcionCambiarNombreUsuario(usuario);
                    break;
                case 4:
                    opcionEliminarCuenta(usuario);
                    continuarEnMenu = false;
                    break;
                case 5:
                    menuUsuario(usuario);
                    continuarEnMenu = false;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (continuarEnMenu);

        return continuarEnMenu;
    }

    // Método que permite cambiar la contraseña del usuario
    public void opcionCambiarContrasena(Usuario usuario) {
        System.out.print("Ingrese su contraseña actual: ");
        String contrasenaActual = scanner.nextLine();

        if (!usuario.getContraseña().equals(contrasenaActual)) {
            System.out.println("La contraseña actual no es correcta. No se puede cambiar la contraseña.");
            return;
        }

        System.out.print("Ingrese la nueva contraseña: ");
        String nuevaContrasena = scanner.nextLine();

        try {
            usuario.cambiarContraseña(contrasenaActual, nuevaContrasena);
            System.out.println("Contraseña actualizada correctamente.");

            JsonUtilUsuario.modificarUsuarioEnArchivo(usuario);
        } catch (ContrasenaInvalidaException e) {
            GestorExcepciones.manejarContrasenaInvalida(e);
        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
        }
    }

    // Método que permite cambiar el email del usuario
    public void opcionCambiarEmail(Usuario usuario) {
        System.out.print("Ingrese el nuevo email: ");
        String nuevoEmail = scanner.nextLine();
        System.out.print("Ingrese su contraseña actual: ");
        String contrasenaActual = scanner.nextLine();
        try {
            usuario.cambiarEmail(contrasenaActual, nuevoEmail);
            System.out.println("Email actualizado correctamente.");
            JsonUtilUsuario.modificarUsuarioEnArchivo(usuario);
        } catch (ContrasenaInvalidaException e) {
            GestorExcepciones.manejarContrasenaInvalida(e);
        } catch (EmailInvalidoException e) {
            GestorExcepciones.manejarEmailInvalido(e);
        } catch (IOException e) {
           GestorExcepciones.manejarIOException(e);
        }
    }
    // Método que permite cambiar el nombre de usuario
    public void opcionCambiarNombreUsuario(Usuario usuario) {
        System.out.print("Ingrese el nuevo nombre de usuario: ");
        String nuevoNombre = scanner.nextLine();
        System.out.print("Ingrese su contraseña actual: ");
        String contrasenaActual = scanner.nextLine();
        try {
            usuario.cambiarNombreUsuario(contrasenaActual, nuevoNombre, gestorUsuarios);
        } catch (ContrasenaInvalidaException e) {
            GestorExcepciones.manejarContrasenaInvalida(e);
        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    // Método que permite eliminar la cuenta del usuario
    public void opcionEliminarCuenta(Usuario usuario) {
        System.out.print("Ingrese su contraseña actual para confirmar eliminación: ");
        String contrasenaActual = scanner.nextLine();
        try {
            if (usuario.getContraseña().equals(contrasenaActual)) {
                gestorUsuarios.eliminarUsuario(usuario);
            } else {
                throw new ContrasenaInvalidaException("Contraseña incorrecta. No se pudo eliminar la cuenta.");
            }
        } catch (ContrasenaInvalidaException e) {
            GestorExcepciones.manejarContrasenaInvalida(e);
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }
}