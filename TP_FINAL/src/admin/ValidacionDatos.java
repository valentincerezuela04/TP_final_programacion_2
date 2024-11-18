package admin;

import gestores.GestorExcepciones;

import java.util.Scanner;

public class ValidacionDatos {
    private static final Scanner scanner = new Scanner(System.in);

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

    public static double obtenerPuntuacion(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double puntuacion = Double.parseDouble(scanner.nextLine());
                if (puntuacion >= 1 && puntuacion <= 10) {
                    return puntuacion; // Es válida, se retorna
                } else {
                    System.out.println("La puntuación debe estar entre 1 y 10. Inténtalo nuevamente.");
                }
            } catch (NumberFormatException e) {
                GestorExcepciones.manejarNumberFormatException(e);
            }
        }
    }

    public static String obtenerString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim(); //.trim arregla los espacios colocados incorrectamente
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("La entrada no puede estar vacía. Inténtalo de nuevo.");
            }
        }
    }






}
