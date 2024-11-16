package menu;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import contenido.Anime;
import contenido.Contenido;
import contenido.Manga;
import excepciones.*;
import gestores.GestorContenido;
import gestores.GestorUsuarios;
import manejo_json.JsonUtilUsuario;
import usuario.Usuario;
import usuario.ValidacionUsuario;

public class Menu {
    private GestorUsuarios gestorUsuarios;
    private Scanner scanner;
    private GestorContenido gestorContenido;
    private static final String ARCHIVO_ANIMES = "pruebaAnime.json";
    private static final String ARCHIVO_MANGAS = "pruebaManga.json";
    private static final String tipoAnime = "anime";
    private static final String tipoManga = "manga";

    public Menu() {
        this.gestorUsuarios = new GestorUsuarios();
        this.scanner = new Scanner(System.in);
        this.gestorContenido = new GestorContenido();
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
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    try {
                        menuIniciarSesion();
                    } catch (UsuarioNoEncontradoException e) {
                        GestorExcepciones.manejarUsuarioNoEncontrado(e); // Manejo de error de usuario no encontrado
                    } catch (ContrasenaInvalidaException e) {
                        GestorExcepciones.manejarContrasenaInvalida(e); // Manejo de error de contraseña inválida
                    }
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
        } while (continuar);
        System.exit(0);
    }


    // Método para iniciar sesión
    public void menuIniciarSesion() throws UsuarioNoEncontradoException, ContrasenaInvalidaException {
        System.out.print("Ingrese su nombre de usuario: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contraseña = scanner.nextLine();

        try {
            Usuario usuario = gestorUsuarios.iniciarSesion(nombre, contraseña);

            if (usuario == null) {
                throw new UsuarioNoEncontradoException("El nombre de usuario no existe.");
            }

            if (!usuario.getContraseña().equals(contraseña)) {
                throw new ContrasenaInvalidaException("Contraseña incorrecta.");
            }

            System.out.println("Inicio de sesión exitoso. Bienvenido, " + usuario.getNombre() + "!");
            menuUsuario(usuario);
        } catch (UsuarioNoEncontradoException e) {
            // Si el usuario no existe, manejar la excepción
            GestorExcepciones.manejarUsuarioNoEncontrado(e);
        } catch (ContrasenaInvalidaException e) {
            // Si la contraseña no es correcta, manejar la excepción
            GestorExcepciones.manejarContrasenaInvalida(e);
        }
    }

    // Método para crear una cuenta
    public void menuCrearCuenta() {
        System.out.println("Crear nueva cuenta");

        System.out.print("Ingrese su nombre de usuario: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine();

        String contraseña, confirmacionContraseña;

        // Solicitar la contraseña dos veces
        do {
            System.out.print("Ingrese su contraseña: ");
            contraseña = scanner.nextLine();

            System.out.print("Confirme su contraseña: ");
            confirmacionContraseña = scanner.nextLine();

            if (!contraseña.equals(confirmacionContraseña)) {
                System.out.println("Las contraseñas no coinciden. Intente nuevamente.");
            }
        } while (!contraseña.equals(confirmacionContraseña)); // Repetir hasta que las contraseñas coincidan

        try {
            // Verificar que la contraseña cumpla con el patrón antes de proceder
            if (!ValidacionUsuario.esContraseñaValida(contraseña)) {
                System.out.println("Error: La contraseña no cumple con los requisitos de seguridad.");
                return; // Salir del método si la contraseña no es válida
            }

            // Validación del email
            ValidacionUsuario.esEmailValido(email);

            // Crear usuario y registrarlo
            Usuario nuevoUsuario = new Usuario(nombre, contraseña, email);
            Usuario usuarioRegistrado = gestorUsuarios.registrarUsuario(nuevoUsuario);

            if (usuarioRegistrado != null) {
                System.out.println("Cuenta creada exitosamente.");
            } else {
                System.out.println("No se pudo crear la cuenta. Es posible que el usuario ya esté registrado.");
            }
        } catch (EmailInvalidoException | ContrasenaInvalidaException e) {
            System.out.println("Error en la creación de la cuenta: " + e.getMessage());
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
                        System.out.println("Error: " + e.getMessage()); // Imprimimos el mensaje de la excepción
                    } catch (IOException e) {
                        System.out.println("Error al guardar los cambios: " + e.getMessage()); // En caso de error al guardar en el archivo
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


    public void menuAgregarContenido(Usuario usuario, String tipoContenido) {
        int opcion;
        boolean continuar = true;

        do {
            System.out.println("---- Menú Agregar " + tipoContenido.substring(0, 1).toUpperCase() + tipoContenido.substring(1) + " ----");
            System.out.println("1. Buscar por ID");
            System.out.println("2. Buscar por Nombre");
            System.out.println("3. Ordenar por");
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
            String archivoContenido;
            if (tipoContenido.equals(Anime.class)) {
                archivoContenido = ARCHIVO_ANIMES;
            } else if (tipoContenido.equals(Manga.class)) {
                archivoContenido = ARCHIVO_MANGAS;
            } else {
                throw new IllegalArgumentException("Tipo de contenido no válido");
            }

            // Usar el método genérico buscarContenidoPorId para buscar el contenido seleccionado
            Contenido contenidoSeleccionado = gestorContenido.buscarContenidoPorId(archivoContenido, idContenido, tipoContenido);

            if (contenidoSeleccionado != null) {
                // Asignar el estado utilizando el método genérico asignarEstadoContenido
                gestorContenido.asignarEstadoContenido(contenidoSeleccionado);

                // Agregar el contenido al usuario usando el método genérico agregarContenido
                gestorContenido.agregarContenido(usuario, contenidoSeleccionado);
            } else {
                System.out.println("No se encontró el contenido con el ID ingresado.");
            }
        } catch (ContenidoNoEncontradoException e) {
            System.err.println("Error: " + e.getMessage() + ". Por favor, intente nuevamente con un ID válido.");
        } catch (ContenidoDuplicadoException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al guardar los cambios en el archivo JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    public void menuBuscarContenidoPorNombre(Usuario usuario, Class<?> tipoContenido) {
        System.out.print("Ingrese el nombre del contenido que desea agregar: ");
        String nombreContenido = scanner.nextLine();

        try {
            String archivoContenido = "";  // Variable para almacenar el archivo correcto

            // Determinar el archivo correcto según el tipo de contenido
            if (tipoContenido == Anime.class) {
                archivoContenido = ARCHIVO_ANIMES;
            } else if (tipoContenido == Manga.class) {
                archivoContenido = ARCHIVO_MANGAS;
            } else {
                throw new IllegalArgumentException("Tipo de contenido no válido.");
            }

            // Intentar buscar el contenido por nombre utilizando el archivo correcto
            Contenido contenidoSeleccionado = gestorContenido.buscarContenidoPorNombre(archivoContenido, nombreContenido, tipoContenido);

            if (contenidoSeleccionado != null) {
                // Asignar el estado al contenido utilizando el método genérico
                gestorContenido.asignarEstadoContenido(contenidoSeleccionado);

                // Agregar el contenido al usuario utilizando el método genérico
                gestorContenido.agregarContenido(usuario, contenidoSeleccionado);
            } else {
                System.out.println("No se encontró un contenido con el nombre ingresado.");
            }
        } catch (ContenidoNoEncontradoException e) {
            System.err.println("Error: " + e.getMessage() + ". Por favor, intente nuevamente con un nombre válido.");
        } catch (IOException e) {
            System.err.println("Error al guardar los cambios en el archivo JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado: " + e.getMessage());
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
            System.out.println("4. Ordenar por Score");
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
        if (tipoContenido.equalsIgnoreCase("anime") && usuario.getAnimes().isEmpty() ||
                tipoContenido.equalsIgnoreCase("manga") && usuario.getMangas().isEmpty()) {
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
            // Manejo de la excepción si el contenido no se encuentra
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            // Manejo de error en el archivo JSON
            System.err.println("Error al guardar los cambios en el archivo JSON: " + e.getMessage());
        }
    }

    public void menuCambiarEstadoContenido(Usuario usuario, String tipoContenido) throws ContenidoNoEncontradoException, IOException {
        gestorContenido.mostrarContenidoUsuario(usuario, tipoContenido);

        System.out.print("Ingrese el ID del contenido cuyo estado desea cambiar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer
        JsonUtilUsuario jsonUtilUsuario = new JsonUtilUsuario();


        if (tipoContenido.equalsIgnoreCase("Anime")) {
            // Buscar el anime por ID utilizando un bucle tradicional
            Anime animeSeleccionado = null;
            for (Anime anime : usuario.getAnimes()) {
                if (anime.getId() == id) {
                    animeSeleccionado = anime;
                    break;  // Encontramos el anime, salimos del bucle
                }
            }

            if (animeSeleccionado != null) {
                // Cambiar el estado del anime
                gestorContenido.asignarEstadoContenido(animeSeleccionado);
                // Modificar el usuario en el archivo con el estado actualizado
                jsonUtilUsuario.modificarUsuarioEnArchivo(usuario);
            } else {
                throw new ContenidoNoEncontradoException("No se encontró un anime con el ID proporcionado.");
            }
        } else if (tipoContenido.equalsIgnoreCase("Manga")) {
            // Buscar el manga por ID utilizando un bucle tradicional
            Manga mangaSeleccionado = null;
            for (Manga manga : usuario.getMangas()) {
                if (manga.getId() == id) {
                    mangaSeleccionado = manga;
                    break;  // Encontramos el manga, salimos del bucle
                }
            }

            if (mangaSeleccionado != null) {
                // Cambiar el estado del manga
                gestorContenido.asignarEstadoContenido(mangaSeleccionado);
                // Modificar el usuario en el archivo con el estado actualizado
                jsonUtilUsuario.modificarUsuarioEnArchivo(usuario);
            } else {
                throw new ContenidoNoEncontradoException("No se encontró un manga con el ID proporcionado.");
            }
        } else {
            System.out.println("Tipo de contenido no válido.");
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
        System.out.print("Ingrese la nueva contraseña: ");
        String nuevaContrasena = scanner.nextLine();

        try {
            // Intentamos cambiar la contraseña del usuario
            usuario.cambiarContraseña(contrasenaActual, nuevaContrasena);
            System.out.println("Contraseña actualizada correctamente.");

            // Intentamos actualizar el usuario en el archivo
            JsonUtilUsuario.actualizarUsuarioEnArchivo(usuario);

        } catch (ContrasenaInvalidaException e) {
            // Si la contraseña es inválida
            System.out.println("Error: " + e.getMessage());
        } catch (EmailInvalidoException e) {
            // Si el email es inválido
            System.out.println("Error en el email: " + e.getMessage());
        } catch (IOException e) {
            // Si hay problemas con el archivo (lectura o escritura)
            System.out.println("Error al actualizar el archivo: " + e.getMessage());
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
            JsonUtilUsuario.actualizarUsuarioEnArchivo(usuario);
        } catch (ContrasenaInvalidaException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (EmailInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al actualizar el archivo: " + e.getMessage());
        }
    }


    public void opcionCambiarNombreUsuario(Usuario usuario) {
        System.out.print("Ingrese el nuevo nombre de usuario: ");
        String nuevoNombre = scanner.nextLine();
        System.out.print("Ingrese su contraseña actual: ");
        String contrasenaActual = scanner.nextLine();

        try {
            usuario.cambiarNombreUsuario(contrasenaActual, nuevoNombre);
            System.out.println("Nombre de usuario actualizado correctamente.");
            // Intentamos actualizar el usuario en el archivo
            JsonUtilUsuario.actualizarUsuarioEnArchivo(usuario);
        } catch (ContrasenaInvalidaException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (EmailInvalidoException e) {
            System.out.println("Error en el email: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al actualizar el archivo: " + e.getMessage());
        }
    }

    public void opcionEliminarCuenta(Usuario usuario) {
        System.out.print("Ingrese su contraseña actual para confirmar eliminación: ");
        String contrasenaActual = scanner.nextLine();
        try {
            if (usuario.getContraseña().equals(contrasenaActual)) {
                gestorUsuarios.eliminarUsuario(usuario);  // Llama al método en GestorUsuarios para eliminar
                System.out.println("Cuenta eliminada correctamente.");
            } else {
                throw new ContrasenaInvalidaException("Contraseña incorrecta. No se pudo eliminar la cuenta.");
            }
        } catch (ContrasenaInvalidaException e) {
            System.out.println(e.getMessage());
        }
    }



}