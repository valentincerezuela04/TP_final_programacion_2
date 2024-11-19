package admin;

import gestores.GestorExcepciones;
import java.util.Scanner;

public class ValidacionDatos {
    private static final Scanner scanner = new Scanner(System.in);

    // Método para obtener un valor entero del usuario
    public static int obtenerEnteros(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                GestorExcepciones.manejarNumberFormatException(e);
            }
        }
    }

    // Método para obtener un valor double del usuario
    public static double obtenerDoubles(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                GestorExcepciones.manejarNumberFormatException(e);
            }
        }
    }

    // Método para obtener una puntuación entre 1 y 10
    public static double obtenerPuntuacion(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double puntuacion = Double.parseDouble(scanner.nextLine());
                if (puntuacion >= 1 && puntuacion <= 10) {
                    return puntuacion;
                } else {
                    System.out.println("La puntuación debe estar entre 1 y 10. Inténtalo nuevamente.");
                }
            } catch (NumberFormatException e) {
                GestorExcepciones.manejarNumberFormatException(e);
            }
        }
    }

    // Método para obtener una cadena no vacía del usuario
    public static String obtenerString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("La entrada no puede estar vacía. Inténtalo de nuevo.");
            }
        }
    }
}
