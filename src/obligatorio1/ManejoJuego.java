package obligatorio1;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// @author Valentina Giusti - 199747
// @author Bautista Landaboure - 316326

public class ManejoJuego {

    private static final TableroTateti tableroTateti = new TableroTateti();
    private static final Random random = new Random();

    public static void jugarPartida(Usuario jugador1, Usuario jugador2) {
        tableroTateti.inicializarTablero();
        tableroTateti.mostrarTablero("");

        String simboloActual = "X";
        Usuario jugadorActual = jugador1;
        boolean partidaActiva = true;
        int siguienteCuadrante = -1;
        boolean esContraComputadora = jugador2.getAlias().equals("Computadora");

        while (partidaActiva) {
            if (jugadorActual.getAlias().equals("Computadora")) {
                siguienteCuadrante = turnoComputadora(simboloActual, siguienteCuadrante);
            } else {
                siguienteCuadrante = turnoJugador(
                        jugadorActual, simboloActual, siguienteCuadrante,
                        esContraComputadora
                );
            }

            if (siguienteCuadrante == -2) {
                System.out.println(jugadorActual.getAlias() + " terminó la partida.");
                partidaActiva = false;
            }

            if (tableroTateti.verificarJuegoGanado(simboloActual)) {
                System.out.println("¡" + jugadorActual.getAlias() + " ha ganado!");
                jugadorActual.incrementarVictorias();
                partidaActiva = false;
            } else if (tableroTateti.tableroCompleto()) {
                System.out.println("El juego terminó en empate.");
                partidaActiva = false;
            }

            // cambiar de jugador y turno
            simboloActual = cambiarSimbolo(simboloActual);
            jugadorActual = cambiarJugador(simboloActual, jugador1, jugador2);
        }
    }

    private static int turnoJugador(
            Usuario jugador, String simbolo, int siguienteCuadrante,
            boolean esContraComputadora) {

        Scanner in = new Scanner(System.in);
        int cuadranteIndex;

    if (siguienteCuadrante != -1
            && !tableroTateti.estaCuadranteGanado(siguienteCuadrante)
            && !tableroTateti.estaCuadranteCompleto(siguienteCuadrante)) {
        cuadranteIndex = siguienteCuadrante;
    } else {
        cuadranteIndex = obtenerCuadranteValido(jugador, in, esContraComputadora);
        }

        if (cuadranteIndex == -2) {
            return -2; // Termina el juego.
        }

        tableroTateti.resaltarCuadrante(tableroTateti.obtenerLabelCuadrante(cuadranteIndex));
        tableroTateti.mostrarTablero("");

        int nuevoCuadrante = registrarJugada(jugador, simbolo, cuadranteIndex, in, esContraComputadora);

        if (nuevoCuadrante == -2) {
            return -2; // termina el juego
        }

        if (tableroTateti.estaCuadranteGanado(nuevoCuadrante) || tableroTateti.estaCuadranteCompleto(nuevoCuadrante)) {
            String cuadrante = tableroTateti.obtenerLabelCuadrante(nuevoCuadrante);
            tableroTateti.limpiarTodoResaltado();
            tableroTateti.mostrarTablero("");

            System.out.println("El cuadrante " + cuadrante + " está ganado o completo.");
            return -1; // permitir seleccionar cualquier cuadrante
        }

        return nuevoCuadrante; // devuelve el cuadrante para el siguiente turno.
    }


    private static int obtenerCuadranteValido(
            Usuario jugador, Scanner in, boolean esContraComputadora
    ) {
        int cuadranteIndex = -1;

        while (cuadranteIndex == -1) {
            String cuadranteElegido = obtenerCuadrante(jugador, -1, esContraComputadora, in);

            if (cuadranteElegido.equals("X")) {
                return -2;  // terminar el juego
            }
            cuadranteIndex = validarCuadrante(cuadranteElegido);
        }

        return cuadranteIndex;
    }

    private static int registrarJugada(
            Usuario jugador, String simbolo, int cuadranteIndex,
            Scanner in, boolean esContraComputadora) {

        boolean posicionValida = false;
        String posicion = "";

        while (!posicionValida) {
            posicion = obtenerPosicion(jugador, tableroTateti.obtenerLabelCuadrante(cuadranteIndex), in);

            if (posicion.equals("X")) {
                return -2; // Termina el juego.
            }

            if (posicion.equals("M")) {
                int siguienteCuadrante = realizarJugadaMagica(jugador, simbolo, esContraComputadora, cuadranteIndex);
                if (siguienteCuadrante >= 0) {
                    return siguienteCuadrante;
                } else {
                    System.out.println("Error al realizar la jugada mágica.");
                    continue; // vuelve a solicitar la jugada si hubo error
                }
        }

        if (tableroTateti.jugarEnCuadrante(cuadranteIndex, posicion, simbolo)) {
            posicionValida = true;
        } else {
            System.out.println("Posición no válida. Intenta nuevamente.");
        }
        }

        return tableroTateti.obtenerIndiceCuadrante(posicion);
    }

    private static int realizarJugadaMagica(Usuario jugador, String simbolo, boolean esContraComputadora, int cuadranteIndex) {
        if (esContraComputadora) {
            System.out.println("La jugada mágica no está disponible contra la computadora.");
            return -1;
        }

        if (jugador.getUsoJugadaMagica()) {
            System.out.println("¡Ya usaste tu jugada mágica! Elige otra opción.");
            return -1;
        }

        Scanner in = new Scanner(System.in);
        System.out.print("Seleccionaste la jugada mágica " + jugador.getAlias()
                + "! Selecciona la posición que quieres conservar en el cuadrante (A1, A2, ..., C3): ");
        String posicionPreservada = in.nextLine().toUpperCase();

        // Intenta jugar en la posición preservada
        if (!tableroTateti.jugarEnCuadrante(cuadranteIndex, posicionPreservada, simbolo)) {
            System.out.println("Error: La posición seleccionada no es válida.");
            return -1;
        }

        jugador.setUsoJugadaMagica(true);
        tableroTateti.limpiarJugadasCuadrante(cuadranteIndex, posicionPreservada);
        tableroTateti.mostrarTablero("");

        return tableroTateti.obtenerIndiceCuadrante(posicionPreservada);
    }

    private static int validarCuadrante(String cuadranteElegido) {
        int cuadranteIndex = tableroTateti.obtenerIndiceCuadrante(cuadranteElegido);
        if (cuadranteIndex < 0) {
            return -1;
        }

        if (tableroTateti.estaCuadranteGanado(cuadranteIndex) || tableroTateti.estaCuadranteCompleto(cuadranteIndex)) {
            System.out.println("El cuadrante " + cuadranteElegido + " ya está ganado o completo.");
            return -1;
        }

        return cuadranteIndex;
    }


    private static String obtenerCuadrante(Usuario jugador, int siguienteCuadrante, boolean esContraComputadora, Scanner in) {
        if (siguienteCuadrante == -1) {
            System.out.print(jugador.getAlias()
                    + ", elige un cuadrante (A1, A2, ..., C3)"
                    + (esContraComputadora ? " o 'X' para rendirte: " : ", 'M' para usar magia o 'X' para rendirte: "));
            return in.nextLine().toUpperCase();
        } else {
            return tableroTateti.obtenerLabelCuadrante(siguienteCuadrante);
        }
    }

    private static String obtenerPosicion(Usuario jugador, String cuadranteElegido, Scanner in) {
        System.out.print(jugador.getAlias() + " elige una posición en " + cuadranteElegido + " (A1, A2, ..., C3), 'M' para usar magia o 'X' para salir: ");
        return in.nextLine().toUpperCase();
    }

    private static String cambiarSimbolo(String simboloActual) {
        return simboloActual.equals("X") ? "O" : "X";
    }

    private static Usuario cambiarJugador(String simboloActual, Usuario jugador1, Usuario jugador2) {
        return simboloActual.equals("X") ? jugador1 : jugador2;
    }

    private static int turnoComputadora(String simbolo, int siguienteCuadrante) {
        System.out.println("Turno de la computadora.");

        if (siguienteCuadrante == -1 || tableroTateti.estaCuadranteCompleto(siguienteCuadrante)) {
            siguienteCuadrante = obtenerCuadranteAleatorioValido();
        }

        String posicion = obtenerPosicionAleatoriaValida(siguienteCuadrante);
        tableroTateti.jugarEnCuadrante(siguienteCuadrante, posicion, simbolo);
        tableroTateti.mostrarTablero("");

        String cuadrante = tableroTateti.obtenerLabelCuadrante(siguienteCuadrante);
        System.out.println("La computadora jugó en el cuadrante " + cuadrante + " en la posición " + posicion);

        return tableroTateti.obtenerIndiceCuadrante(posicion);
    }

    private static int obtenerCuadranteAleatorioValido() {
        ArrayList<Integer> cuadrantesLibres = tableroTateti.obtenerCuadrantesLibres();
        return cuadrantesLibres.get(random.nextInt(cuadrantesLibres.size()));
    }

    private static String obtenerPosicionAleatoriaValida(int cuadranteIndex) {
        ArrayList<String> posicionesLibres = tableroTateti.obtenerPosicionesLibresEnCuadrante(cuadranteIndex);
        return posicionesLibres.get(random.nextInt(posicionesLibres.size()));
    }
}
