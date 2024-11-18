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
import manejo_json.JsonUtilUsuario;
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

            // Verifica si hay un número entero disponible en el flujo
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

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
                // Si no hay una opción válida, muestra un mensaje de error y consume el valor incorrecto
                System.out.println("Entrada no válida. Intente nuevamente.");
                scanner.nextLine(); // Limpiar el buffer de cualquier entrada no válida
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
            // Intentamos iniciar sesión con el nombre y la contraseña
            Usuario usuario = gestorUsuarios.iniciarSesion(nombre, contraseña);

            // Si todo está bien, iniciamos sesión
            System.out.println("Inicio de sesión exitoso. Bienvenido, " + usuario.getNombre() + "!");

            // Verificar si el usuario es admin01 y su contraseña también es admin01
            if ("admin01".equals(usuario.getNombre()) && "admin01".equals(contraseña)) {
                // Si es admin01 y la contraseña también es admin01, mostramos el menú de administración
                MenuAdmin menuAdmin = new MenuAdmin();
                menuAdmin.mostrarMenuAdmin(); // Mostrar el menú de administración
            } else {
                // Si no es admin01 o la contraseña no es admin01, mostramos el menú de usuario regular
                menuUsuario(usuario);
            }

        } catch (LoginException e) {
            // Manejo de la excepción LoginException
            GestorExcepciones.manejarLoginException(e);
        } catch (Exception e) {
            // Manejo de cualquier otra excepción no prevista
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

            // Intentar registrar el usuario (este método lanza la excepción si el usuario ya existe)
            Usuario usuarioRegistrado = gestorUsuarios.registrarUsuario(nuevoUsuario);
            System.out.println("Cuenta creada exitosamente.");

        } catch (UsuarioRepetidoException e) {
            // Manejo de excepción de usuario repetido
            GestorExcepciones.manejarUsuarioRepetido(e);
        } catch (Exception e) {
            // Manejo de otras excepciones genéricas
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    public void menuUsuario(Usuario usuario) {
        int opcion;
        boolean continuarEnMenuUsuario = true; // Bandera para controlar el menú de usuario

        do {
            System.out.println("---- Menú de Usuario ----");
            System.out.println("1. Administrar lista de Animes");
            System.out.println("2. Administrar lista de Mangas");
            System.out.println("3. Opciones de cuenta");
            System.out.println("4. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    menuAdministrarListaContenido(usuario, tipoAnime); // Llamamos al método unificado para gestionar la lista de animes
                    break;
                case 2:
                    menuAdministrarListaContenido(usuario, tipoManga); // Llamamos al método unificado para gestionar la lista de mangas
                    break;
                case 3:
                    // Verificamos si el usuario fue eliminado y llamamos al menú de opciones de cuenta
                    continuarEnMenuUsuario = menuOpcionesUsuario(usuario);
                    break;
                case 4:
                    System.out.println("Cerrando sesión...");
                    continuarEnMenuUsuario = false; // Salir del menú de usuario
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }

        } while (continuarEnMenuUsuario); // El ciclo continuará si la bandera es true

        // Después de salir del menú de usuario, retornamos al menú principal
        menuPrincipal();
    }


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
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    gestorContenido.mostrarContenidoUsuario(usuario, tipoContenido);

                    // Comprobar si la lista está vacía antes de mostrar el menú de ordenar
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
                        menuCambiarEstadoContenido(usuario, tipoContenido); // Llamamos al método que puede lanzar la excepción
                    } catch (ContenidoNoEncontradoException e) {
                        GestorExcepciones.manejarContenidoNoEncontrado(e); // Delegamos el manejo a GestorExcepciones
                    } catch (IOException e) {
                        GestorExcepciones.manejarIOException(e); // Delegamos el manejo a GestorExcepciones
                    } catch (Exception e) {
                        GestorExcepciones.manejarExcepcion(e); // Manejo de cualquier otra excepción no prevista
                    }
                    break;
                case 5:
                    continuar = false; // Volver atrás
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (continuar);
    }


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
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    // Ordenar por ID
                    List<T> listaOrdenadaPorId = gestorContenido.ordenarListaPersonal(usuario, tipoContenido, "id");
                    gestorContenido.mostrarContenidoOrdenado(listaOrdenadaPorId);
                    break;
                case 2:
                    // Ordenar por Título
                    List<T> listaOrdenadaPorTitulo = gestorContenido.ordenarListaPersonal(usuario, tipoContenido, "titulo");
                    gestorContenido.mostrarContenidoOrdenado(listaOrdenadaPorTitulo);
                    break;
                case 3:
                    // Ordenar por Popularidad
                    List<T> listaOrdenadaPorPopularidad = gestorContenido.ordenarListaPersonal(usuario, tipoContenido, "popularidad");
                    gestorContenido.mostrarContenidoOrdenado(listaOrdenadaPorPopularidad);
                    break;
                case 4:
                    // Ordenar por Puntuación
                    List<T> listaOrdenadaPorScore = gestorContenido.ordenarListaPersonal(usuario, tipoContenido, "score");
                    gestorContenido.mostrarContenidoOrdenado(listaOrdenadaPorScore);
                    break;
                case 5:
                    continuar = false; // Volver atrás
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (continuar);
    }


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
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

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
                        menuOrdenarContenido(usuario, ARCHIVO_ANIMES, Anime.class, tipoAnime);
                    } else if (tipoContenido.equalsIgnoreCase(tipoManga)) {
                        menuOrdenarContenido(usuario, ARCHIVO_MANGAS, Manga.class, tipoManga);
                    }
                    break;
                case 4:
                    continuar = false; // Volver al menú anterior
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (continuar);
    }

    public void menuBuscarContenidoPorId(Usuario usuario, Class<?> tipoContenido) {
        // Mostrar los contenidos disponibles según el tipo (Anime o Manga)
        gestorContenido.mostrarContenidoDisponible(tipoContenido);

        System.out.print("Ingrese el ID del contenido que desea agregar: ");
        int idContenido = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        try {
            // Determinar el archivo a usar según el tipo de contenido
            String archivoContenido = tipoContenido.equals(Anime.class) ? ARCHIVO_ANIMES : tipoContenido.equals(Manga.class) ? ARCHIVO_MANGAS : null;

            if (archivoContenido == null) {
                throw new IllegalArgumentException("Tipo de contenido no válido.");
            }

            // Buscar el contenido por ID
            Contenido contenidoSeleccionado = gestorContenido.buscarContenidoPorId(archivoContenido, idContenido, tipoContenido);

            if (contenidoSeleccionado != null) {
                // Asignar el estado y agregar el contenido al usuario, verificando duplicados
                gestorContenido.agregarContenido(usuario, contenidoSeleccionado);
            }
        } catch (ContenidoNoEncontradoException e) {
            GestorExcepciones.manejarContenidoNoEncontrado(e);
        } catch (ContenidoDuplicadoException e) {
            GestorExcepciones.manejarContenidoDuplicado(e);
        } catch (IOException e) {
            GestorExcepciones.manejarIOException(e);
        } catch (IllegalArgumentException e) {
            GestorExcepciones.manejarIllegalArgumentException(e);
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    public void menuBuscarContenidoPorNombre(Usuario usuario, Class<?> tipoContenido) {
        System.out.print("Ingrese el nombre del contenido que desea agregar: ");
        String nombreContenido = scanner.nextLine();

        try {
            // Determinar el archivo a usar según el tipo de contenido
            String archivoContenido = tipoContenido.equals(Anime.class) ? ARCHIVO_ANIMES : tipoContenido.equals(Manga.class) ? ARCHIVO_MANGAS : null;

            if (archivoContenido == null) {
                throw new IllegalArgumentException("Tipo de contenido no válido.");
            }

            // Buscar el contenido por nombre
            Contenido contenidoSeleccionado = gestorContenido.buscarContenidoPorNombre(archivoContenido, nombreContenido, tipoContenido);

            if (contenidoSeleccionado != null) {
                // Asignar el estado y agregar el contenido al usuario, verificando duplicados
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

    public <T> void menuOrdenarContenido(Usuario usuario, String archivo, Class<T> tipoContenido, String tipo) {
        int opcion;
        boolean continuar = true;

        do {
            System.out.println("---- Menú Ordenar " + tipo + " ----");
            System.out.println("1. Ordenar por ID");
            System.out.println("2. Ordenar por Título");
            System.out.println("3. Ordenar por Popularidad");
            System.out.println("4. Ordenar por Puntuación");
            System.out.println("5. Atrás");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    // Ordenar por ID
                    List<T> contenidoOrdenadoPorId = gestorContenido.ordenarContenido(archivo, "id", tipoContenido);
                    gestorContenido.mostrarContenidoOrdenado(contenidoOrdenadoPorId);
                    break;
                case 2:
                    // Ordenar por Título
                    List<T> contenidoOrdenadoPorTitulo = gestorContenido.ordenarContenido(archivo, "titulo", tipoContenido);
                    gestorContenido.mostrarContenidoOrdenado(contenidoOrdenadoPorTitulo);
                    break;
                case 3:
                    // Ordenar por Popularidad
                    List<T> contenidoOrdenadoPorPopularidad = gestorContenido.ordenarContenido(archivo, "popularidad", tipoContenido);
                    gestorContenido.mostrarContenidoOrdenado(contenidoOrdenadoPorPopularidad);
                    break;
                case 4:
                    // Ordenar por Score
                    List<T> contenidoOrdenadoPorScore = gestorContenido.ordenarContenido(archivo, "score", tipoContenido);
                    gestorContenido.mostrarContenidoOrdenado(contenidoOrdenadoPorScore);
                    break;
                case 5:
                    continuar = false; // Volver atrás
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (continuar);
    }

    public <T> void menuEliminarContenido(Usuario usuario, String tipoContenido, String archivo, Class<T> claseContenido) {
        // Mostrar la lista del contenido del usuario
        gestorContenido.mostrarContenidoUsuario(usuario, tipoContenido);

        // Verificar si la lista está vacía
        if ((tipoContenido.equalsIgnoreCase("anime") && usuario.getAnimes().isEmpty()) ||
                (tipoContenido.equalsIgnoreCase("manga") && usuario.getMangas().isEmpty())) {
            System.out.println("No tienes " + tipoContenido + "s en tu lista para eliminar.");
            return;
        }

        System.out.print("Ingrese el ID del " + tipoContenido + " que desea eliminar: ");
        int idContenido = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        try {
            // Llamar al método eliminarContenido para eliminar el contenido
            gestorContenido.eliminarContenido(usuario, idContenido, claseContenido);

            // Guardar los cambios en el archivo JSON
            JsonUtilUsuario.modificarUsuarioEnArchivo(usuario);
            System.out.println("Los cambios han sido guardados en el archivo JSON.");
        } catch (ContenidoNoEncontradoException e) {
            // Centralizar el manejo de la excepción en un gestor
            GestorExcepciones.manejarContenidoNoEncontrado(e);
        } catch (IOException e) {
            // Manejar error al guardar cambios en el archivo JSON
            GestorExcepciones.manejarIOException(e);
        } catch (Exception e) {
            // Manejo general de excepciones no previstas
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    public void menuCambiarEstadoContenido(Usuario usuario, String tipoContenido) throws ContenidoNoEncontradoException, IllegalArgumentException, IOException {
        // Mostrar los contenidos del usuario según el tipo
        gestorContenido.mostrarContenidoUsuario(usuario, tipoContenido);

        System.out.print("Ingrese el ID del contenido cuyo estado desea cambiar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        JsonUtilUsuario jsonUtilUsuario = new JsonUtilUsuario();

        try {
            // Determinar el archivo a usar según el tipo de contenido
            String archivoContenido = tipoContenido.equalsIgnoreCase("Anime") ? ARCHIVO_ANIMES : tipoContenido.equalsIgnoreCase("Manga") ? ARCHIVO_MANGAS : null;

            if (archivoContenido == null) {
                throw new IllegalArgumentException("Tipo de contenido no válido. Debe ser 'Anime' o 'Manga'.");
            }

            // Buscar el contenido por ID
            Contenido contenidoSeleccionado = gestorContenido.buscarContenidoPorId(archivoContenido, id, tipoContenido.equalsIgnoreCase("Anime") ? Anime.class : Manga.class);

            if (contenidoSeleccionado != null) {
                // Cambiar el estado del contenido
                gestorContenido.asignarEstadoContenido(contenidoSeleccionado, usuario);
                System.out.println("Nuevo estado asignado exitosamente.");

                // Guardar los cambios del usuario en el archivo
                jsonUtilUsuario.modificarUsuarioEnArchivo(usuario);
            } else {
                throw new ContenidoNoEncontradoException("No se encontró contenido con el ID proporcionado.");
            }
        } catch (Exception e) {
            GestorExcepciones.manejarExcepcion(e);
        }
    }

    public boolean menuOpcionesUsuario(Usuario usuario) {
        int opcion;
        boolean continuarEnMenu = true; // Bandera para saber si seguir en el menú

        do {
            System.out.println("---- Opciones de Usuario ----");
            System.out.println("1. Cambiar contraseña");
            System.out.println("2. Cambiar email");
            System.out.println("3. Cambiar nombre de usuario");
            System.out.println("4. Eliminar cuenta");
            System.out.println("5. Volver al menú de usuario"); // Cambio el mensaje aquí
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

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
                    continuarEnMenu = false; // Esto termina el ciclo actual para evitar una llamada recursiva infinita
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (continuarEnMenu); // Si continuarEnMenu es false, salimos del ciclo

        return continuarEnMenu; // Retorna si se debe continuar o no en el menú
    }

    public void opcionCambiarContrasena(Usuario usuario) {
        System.out.print("Ingrese su contraseña actual: ");
        String contrasenaActual = scanner.nextLine();

        // Verificar si la contraseña actual es correcta
        if (!usuario.getContraseña().equals(contrasenaActual)) {
            // Si la contraseña no coincide, se notifica al usuario y se termina el proceso
            System.out.println("La contraseña actual no es correcta. No se puede cambiar la contraseña.");
            return; // Termina el método, evitando que el usuario ingrese una nueva contraseña
        }

        // Si la contraseña actual es correcta, pedir la nueva contraseña
        System.out.print("Ingrese la nueva contraseña: ");
        String nuevaContrasena = scanner.nextLine();

        try {
            // Intentamos cambiar la contraseña del usuario
            usuario.cambiarContraseña(contrasenaActual, nuevaContrasena);
            System.out.println("Contraseña actualizada correctamente.");

            // Intentamos actualizar el usuario en el archivo
            JsonUtilUsuario.modificarUsuarioEnArchivo(usuario);

        } catch (ContrasenaInvalidaException e) {
            // Si la nueva contraseña es inválida, manejar el error
            GestorExcepciones.manejarContrasenaInvalida(e);
        } catch (IOException e) {
            // Si hay problemas con el archivo (lectura o escritura), manejar el error
            GestorExcepciones.manejarIOException(e);
        }
    }

    public void opcionCambiarEmail(Usuario usuario) {
        System.out.print("Ingrese el nuevo email: ");
        String nuevoEmail = scanner.nextLine();
        System.out.print("Ingrese su contraseña actual: ");
        String contrasenaActual = scanner.nextLine();
        try {
            usuario.cambiarEmail(contrasenaActual, nuevoEmail);
            System.out.println("Email actualizado correctamente.");
            // Intentamos actualizar el usuario en el archivo
            JsonUtilUsuario.modificarUsuarioEnArchivo(usuario);
        } catch (ContrasenaInvalidaException e) {
            GestorExcepciones.manejarContrasenaInvalida(e);
        } catch (EmailInvalidoException e) {
            GestorExcepciones.manejarEmailInvalido(e);
        } catch (IOException e) {
           GestorExcepciones.manejarIOException(e);
        }
    }
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


    public void opcionEliminarCuenta(Usuario usuario) {
        System.out.print("Ingrese su contraseña actual para confirmar eliminación: ");
        String contrasenaActual = scanner.nextLine();
        try {
            if (usuario.getContraseña().equals(contrasenaActual)) {
                gestorUsuarios.eliminarUsuario(usuario);  // Llama al método en GestorUsuarios
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