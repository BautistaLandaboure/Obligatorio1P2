package obligatorio1;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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
        boolean magiaJugador1 = false;
        boolean magiaJugador2 = false;

        boolean esContraComputadora = jugador2.getAlias().equals("Computadora");

        while (partidaActiva) {
            if (jugadorActual.getAlias().equals("Computadora")) {
                siguienteCuadrante = turnoComputadora(simboloActual, siguienteCuadrante);
            } else {
                siguienteCuadrante = turnoJugador(
                        jugadorActual, simboloActual, siguienteCuadrante,
                        magiaJugador1, magiaJugador2, esContraComputadora
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

            // Cambiar de jugador
            simboloActual = cambiarSimbolo(simboloActual);
            jugadorActual = cambiarJugador(simboloActual, jugador1, jugador2);
        }
    }

    private static int turnoJugador(
            Usuario jugador, String simbolo, int siguienteCuadrante,
            boolean magiaJugador1, boolean magiaJugador2, boolean esContraComputadora
    ) {
        Scanner in = new Scanner(System.in);

        String cuadranteElegido = obtenerCuadrante(jugador, siguienteCuadrante, esContraComputadora, in);
        if (cuadranteElegido.equals("X")) {
            return -2;  // Salir del juego
        }
        if (cuadranteElegido.equals("M")) {
            if (procesarJugadaMagica(jugador, magiaJugador1, magiaJugador2, esContraComputadora, in)) {
                return -1;  // Permitir elegir cualquier cuadrante
            }
            return turnoJugador(jugador, simbolo, siguienteCuadrante, magiaJugador1, magiaJugador2, esContraComputadora);
        }

        int cuadranteIndex = validarCuadrante(cuadranteElegido);
        if (cuadranteIndex == -1) {
            return turnoJugador(jugador, simbolo, -1, magiaJugador1, magiaJugador2, esContraComputadora);
        }

        tableroTateti.resaltarCuadrante(cuadranteElegido);
        tableroTateti.mostrarTablero("");

        String posicion = obtenerPosicion(jugador, cuadranteElegido, in);
        if (posicion.equals("X")) {
            return -2;  // Salir del juego
        }
        if (posicion.equals("M")) {
            if (procesarJugadaMagica(jugador, magiaJugador1, magiaJugador2, esContraComputadora, in)) {
                return -1;
            }
            return turnoJugador(jugador, simbolo, siguienteCuadrante, magiaJugador1, magiaJugador2, esContraComputadora);
        }

        if (tableroTateti.jugarEnCuadrante(cuadranteIndex, posicion, simbolo)) {
            return tableroTateti.obtenerIndiceCuadrante(posicion);
        } else {
            System.out.println("Posición no válida. Intenta de nuevo.");
            return turnoJugador(jugador, simbolo, siguienteCuadrante, magiaJugador1, magiaJugador2, esContraComputadora);
        }
    }

    private static boolean procesarJugadaMagica(
            Usuario jugador, boolean magiaJugador1, boolean magiaJugador2,
            boolean esContraComputadora, Scanner in
    ) {
        if (esContraComputadora) {
            System.out.println("La jugada mágica no está disponible contra la computadora.");
            return false;
        }

        if (jugador.getAlias().equals("Jugador 1") && magiaJugador1
                || jugador.getAlias().equals("Jugador 2") && magiaJugador2) {
            System.out.println("¡Ya usaste tu jugada mágica! Elige otra opción.");
            return false;
        }

        System.out.print("Selecciona el mini cuadrante que deseas limpiar (A1, A2, ..., C3): ");
        String miniCuadrante = in.nextLine().toUpperCase();

        int cuadranteIndex = tableroTateti.obtenerIndiceCuadrante(miniCuadrante);
        if (cuadranteIndex < 0 || tableroTateti.estaCuadranteGanado(cuadranteIndex)) {
            System.out.println("El mini cuadrante no es válido o ya está ganado. Intenta de nuevo.");
            return false;
        }

        tableroTateti.limpiarMiniCuadrante(miniCuadrante);
        System.out.println("¡Mini cuadrante " + miniCuadrante + " limpiado!");
        tableroTateti.mostrarTablero("");

        if (jugador.getAlias().equals("Jugador 1")) {
            magiaJugador1 = true;
        } else {
            magiaJugador2 = true;
        }

        return true;
    }

    private static int validarCuadrante(String cuadranteElegido) {
        int cuadranteIndex = tableroTateti.obtenerIndiceCuadrante(cuadranteElegido);
        if (cuadranteIndex < 0 || tableroTateti.estaCuadranteCompleto(cuadranteIndex)
                || tableroTateti.estaCuadranteGanado(cuadranteIndex)) {
            System.out.println("El cuadrante " + cuadranteElegido + " no es válido. Elige otro.");
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
        System.out.print("Elige una posición en " + cuadranteElegido + " (A1, A2, ..., C3), 'M' para usar magia o 'X' para salir: ");
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

        System.out.println("La computadora jugó en el cuadrante "
                + tableroTateti.obtenerLabelCuadrante(siguienteCuadrante) + " en la posición " + posicion);

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
