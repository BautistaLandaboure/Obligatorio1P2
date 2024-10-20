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

            // cambiar de jugador y turno
            simboloActual = cambiarSimbolo(simboloActual);
            jugadorActual = cambiarJugador(simboloActual, jugador1, jugador2);
        }
    }

    private static int turnoJugador(
            Usuario jugador, String simbolo, int siguienteCuadrante,
            boolean magiaJugador1, boolean magiaJugador2, boolean esContraComputadora
    ) {
        Scanner in = new Scanner(System.in);
        int cuadranteIndex;

        // verificar si el siguiente cuadrante es válido para jugar
    if (siguienteCuadrante != -1
                && !tableroTateti.estaCuadranteGanado(siguienteCuadrante)
                && !tableroTateti.estaCuadranteCompleto(siguienteCuadrante)) {
        cuadranteIndex = siguienteCuadrante;
    } else {
        // pedir al jugador que seleccione un cuadrante válido
        cuadranteIndex = obtenerCuadranteValido(jugador, in, esContraComputadora, magiaJugador1, magiaJugador2);
        }

        tableroTateti.resaltarCuadrante(tableroTateti.obtenerLabelCuadrante(cuadranteIndex));
        tableroTateti.mostrarTablero("");

        // procesar la posición dentro del cuadrante seleccionado
        int nuevoCuadrante = registrarJugada(jugador, simbolo, cuadranteIndex, in, magiaJugador1, magiaJugador2, esContraComputadora);

        // si el cuadrante está ganado/completo, pedir al siguiente jugador que elija uno nuevo
        if (tableroTateti.estaCuadranteGanado(nuevoCuadrante) || tableroTateti.estaCuadranteCompleto(nuevoCuadrante)) {
            String cuadrante = tableroTateti.obtenerLabelCuadrante(nuevoCuadrante);

            tableroTateti.limpiarTodoResaltado();
            tableroTateti.mostrarTablero("");

            System.out.println("El cuadrante " + cuadrante + " está ganado o completo.");
            return -1;  // indica que se debe seleccionar un nuevo cuadrante
        }

        return nuevoCuadrante;
    }

    private static int obtenerCuadranteValido(
            Usuario jugador, Scanner in, boolean esContraComputadora,
            boolean magiaJugador1, boolean magiaJugador2
    ) {
        int cuadranteIndex = -1;

        while (cuadranteIndex == -1) {
            String cuadranteElegido = obtenerCuadrante(jugador, -1, esContraComputadora, in);

            if (cuadranteElegido.equals("X")) {
                return -2;  // terminar el juego
            }
            if (cuadranteElegido.equals("M") && realizarJugadaMagica(jugador, magiaJugador1, magiaJugador2, esContraComputadora)) {
                return -1;  // permitir seleccionar cualquier cuadrante
        }

            cuadranteIndex = validarCuadrante(cuadranteElegido);
        }

        return cuadranteIndex;
    }

    private static int registrarJugada(
            Usuario jugador, String simbolo, int cuadranteIndex,
            Scanner in, boolean magiaJugador1, boolean magiaJugador2, boolean esContraComputadora
    ) {
        boolean posicionValida = false;
        String posicion = "";

        while (!posicionValida) {
            posicion = obtenerPosicion(jugador, tableroTateti.obtenerLabelCuadrante(cuadranteIndex), in);

            if (posicion.equals("X")) {
                return -2;  // terminar el juego
            }
            if (posicion.equals("M") && realizarJugadaMagica(jugador, magiaJugador1, magiaJugador2, esContraComputadora)) {
                return -1;  // permitir seleccionar cualquier cuadrante
        }

        if (tableroTateti.jugarEnCuadrante(cuadranteIndex, posicion, simbolo)) {
            posicionValida = true;
        } else {
            System.out.println("Posición no válida. Intenta nuevamente.");
        }
    }
        // determinar el siguiente cuadrante basado en la posición jugada
        return tableroTateti.obtenerIndiceCuadrante(posicion);
    }


    private static boolean realizarJugadaMagica(
            Usuario jugador, boolean magiaJugador1, boolean magiaJugador2,
            boolean esContraComputadora
    ) {
        if (esContraComputadora) {
            System.out.println("La jugada mágica no está disponible contra la computadora.");
            return false;
        }

        if ((jugador.getAlias().equals("Jugador 1") && magiaJugador1)
                || (jugador.getAlias().equals("Jugador 2") && magiaJugador2)) {
            System.out.println("¡Ya usaste tu jugada mágica! Elige otra opción.");
            return false;
        }

        Scanner in = new Scanner(System.in);
        System.out.print("Selecciona el mini cuadrante que deseas limpiar (A1, A2, ..., C3): ");
        String miniCuadrante = in.nextLine().toUpperCase();

        int cuadranteIndex = tableroTateti.obtenerIndiceCuadrante(miniCuadrante);
        if (cuadranteIndex < 0 || tableroTateti.estaCuadranteGanado(cuadranteIndex)) {
            System.out.println("El mini cuadrante no es válido o ya está ganado. Intenta de nuevo.");
            return false;
        }

        tableroTateti.limpiarMiniCuadrante(miniCuadrante);
        tableroTateti.mostrarTablero("");

        if (jugador.getAlias().equals("Jugador 1"))
            magiaJugador1 = true;
        else
            magiaJugador2 = true;

        return true;
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
