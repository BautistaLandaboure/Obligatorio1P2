/* falta agregar la X para poder rendirse,  la  funcionalidad para ganar 
(cuando este eso arreglo los metodos de juego para que se incrementen las victorias), 
tambien falta la M para la jugada Magica y algunas excepciones que puedan haber.

*/

package obligatorio1;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;


public class Obligatorio1 {

    private static ManejoRegistro manejoRegistro = new ManejoRegistro();
    private static TableroTateti tableroTateti = new TableroTateti();

    public static void main(String[] args) {
        int opcion = 0;
        Scanner in = new Scanner(System.in);

        while (opcion != 5) {
            mostrarMenu();
            try {
                opcion = in.nextInt();
                in.nextLine(); // Limpiar el buffer
            } catch (Exception e) {
                System.out.println("Por favor, ingrese un número válido.");
                in.nextLine(); // Limpiar el buffer en caso de error
                continue; // Volver al inicio del bucle
            }

            switch (opcion) {
            case 1 -> registrarJugador();
            case 2 -> iniciarJuegoEntreDosPersonas();
            case 3 -> iniciarJuegoContraComputadora(); // Llamada a nueva función
            case 4 -> mostrarRanking();
            case 5 -> System.out.println("Gracias por jugar");
            default -> System.out.println("Opción no válida");
            }
        }
    }

    private static void mostrarMenu() {
    System.out.println("\nMenú Principal:");
    System.out.println("1. Registrar un jugador");
    System.out.println("2. Jugar al Gran Tateti entre 2 personas");
    System.out.println("3. Jugar al Gran Tateti contra la computadora"); // Nueva opción
    System.out.println("4. Ranking");
    System.out.println("5. Salir");
    System.out.print("Seleccione una opción: ");
}


    private static void registrarJugador() {
        Scanner in = new Scanner(System.in);

        String nombre = "";
        while (nombre.isEmpty()) {
            System.out.print("Ingrese nombre: ");
            nombre = in.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("El nombre no puede estar vacío. Por favor, ingrese un nombre.");
            }
        }

        int edad = 0;
        boolean edadValida = false;
        while (!edadValida) {
            System.out.print("Ingrese edad: ");
            try {
                edad = Integer.parseInt(in.nextLine());
                if (edad < 1 || edad > 120) {
                    System.out.println("Por favor, ingrese una edad válida entre 1 y 120 años.");
                } else {
                    edadValida = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número para la edad. Intente de nuevo.");
            }
        }

        String alias = "";
        boolean aliasValido = false;
        while (!aliasValido) {
            System.out.print("Ingrese alias: ");
            alias = in.nextLine().trim();
            if (alias.isEmpty()) {
                System.out.println("El alias no puede estar vacío. Por favor, ingrese un alias.");
            } else if (manejoRegistro.aliasExiste(alias)) {
                System.out.println("El alias ya está en uso. Intente con otro alias.");
            } else {
                aliasValido = true;
            }
        }

        Usuario nuevoUsuario = new Usuario(nombre, edad, alias);
        boolean registrado = manejoRegistro.registrarUsuario(nuevoUsuario);

        if (registrado) {
            System.out.println("Jugador registrado: Nombre: " + nombre + ", Edad: " + edad + ", Alias: " + alias);
        } else {
            System.out.println("No se pudo registrar el jugador.");
        }
    }

    private static void iniciarJuegoEntreDosPersonas() {
        Scanner in = new Scanner(System.in);

        if (manejoRegistro.getUsuarios().size() < 2) {
            System.out.println("Se necesitan al menos dos jugadores registrados para jugar.");
            return;
        }

        System.out.println("Jugadores disponibles:");
        ArrayList<Usuario> usuarios = manejoRegistro.getUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println((i + 1) + ". " + usuarios.get(i).getAlias());
        }

        System.out.print("Seleccione el jugador 1 (número): ");
        int jugador1Index = in.nextInt() - 1;
        System.out.print("Seleccione el jugador 2 (número): ");
        int jugador2Index = in.nextInt() - 1;
        in.nextLine(); // Limpiar el buffer

        Usuario jugador1 = usuarios.get(jugador1Index);
        Usuario jugador2 = usuarios.get(jugador2Index);

        System.out.println("¡Comienza el juego entre " + jugador1.getAlias() + " y " + jugador2.getAlias() + "!");
        tableroTateti.inicializarTablero();
        tableroTateti.mostrarTablero("");

        String jugadorActual = "X";
        Usuario jugadorActualRegistro = jugador1;
        int siguienteCuadrante = -1;
        boolean partidaActiva = true;

        while (partidaActiva) {
            String cuadranteElegido;
            if (siguienteCuadrante == -1) {
                System.out.println(jugadorActualRegistro.getAlias()
                        + ", elige un cuadrante (A1, A2, ..., C3) o presiona 'X' para rendirte: ");
                cuadranteElegido = in.nextLine().toUpperCase();
                if (cuadranteElegido.equals("X")) {
                    System.out.println(jugadorActualRegistro.getAlias() + " se rinde. "
                            + (jugadorActualRegistro.equals(jugador1) ? jugador2.getAlias() : jugador1.getAlias())
                            + " gana!");
                    partidaActiva = false;
                    continue;
                }
            } else {
                cuadranteElegido = indexACuadranteLabel(siguienteCuadrante);
                System.out.println(jugadorActualRegistro.getAlias()
                        + ", debes jugar en el cuadrante " + cuadranteElegido + ".");
            }

            int cuadranteIndex = obtenerCuadranteIndex(cuadranteElegido);
            if (cuadranteIndex == -1 || tableroTateti.estaCuadranteCompleto(cuadranteIndex)) {
                System.out.println("Cuadrante inválido o completo. Intente de nuevo.");
                continue;
            }

            // Lógica para jugar en el minicuadrante elegido
            tableroTateti.mostrarTablero(cuadranteElegido);
            System.out.println("Elige una posición en el cuadrante " + cuadranteElegido + " (A1, A2, ..., C3): ");
            String posicionElegida = in.nextLine().toUpperCase();

            if (tableroTateti.jugarEnCuadrante(cuadranteIndex, posicionElegida, jugadorActual)) {
                tableroTateti.mostrarTablero(cuadranteElegido);

                if (tableroTateti.verificarMiniCuadranteGanado(cuadranteIndex, jugadorActual)) {
                    tableroTateti.marcarCuadranteGanado(cuadranteIndex, jugadorActual);
                    if (tableroTateti.verificarJuegoGanado(jugadorActual)) {
                        System.out.println("¡" + jugadorActualRegistro.getAlias() + " ha ganado el juego!");
                        partidaActiva = false;
                        continue;
                    }
                }

                siguienteCuadrante = obtenerCuadranteIndexDesdePosicion(posicionElegida);
                jugadorActual = jugadorActual.equals("X") ? "O" : "X";
                jugadorActualRegistro = jugadorActual.equals("X") ? jugador1 : jugador2;
            } else {
                System.out.println("Posición no válida o ya ocupada. Intente de nuevo.");
            }

            if (tableroTateti.tableroCompleto()) {
                System.out.println("El juego ha terminado en empate.");
                partidaActiva = false;
            }
        }
    }

    private static int obtenerCuadranteIndex(String cuadrante) {
        return switch (cuadrante) {
            case "A1" ->
                0;
            case "A2" ->
                1;
            case "A3" ->
                2;
            case "B1" ->
                3;
            case "B2" ->
                4;
            case "B3" ->
                5;
            case "C1" ->
                6;
            case "C2" ->
                7;
            case "C3" ->
                8;
            default ->
                -1;
        };
    }

    private static int obtenerCuadranteIndexDesdePosicion(String posicion) {
        return switch (posicion) {
            case "A1" ->
                0;
            case "A2" ->
                1;
            case "A3" ->
                2;
            case "B1" ->
                3;
            case "B2" ->
                4;
            case "B3" ->
                5;
            case "C1" ->
                6;
            case "C2" ->
                7;
            case "C3" ->
                8;
            default ->
                -1;
        };
    }

    private static String indexACuadranteLabel(int index) {
        return switch (index) {
            case 0 ->
                "A1";
            case 1 ->
                "A2";
            case 2 ->
                "A3";
            case 3 ->
                "B1";
            case 4 ->
                "B2";
            case 5 ->
                "B3";
            case 6 ->
                "C1";
            case 7 ->
                "C2";
            case 8 ->
                "C3";
            default ->
                "Invalid";
        };
    }

    private static void mostrarRanking() {
        System.out.println("\nRanking de Jugadores:");
        ArrayList<Usuario> usuarios = manejoRegistro.getUsuarios();

        for (Usuario usuario : usuarios) {
            System.out.print(usuario.getAlias() + " | ");
            System.out.println("#".repeat(usuario.getVictorias()));
        }
    }
    
    private static void iniciarJuegoContraComputadora() {
    Scanner in = new Scanner(System.in);

    if (manejoRegistro.getUsuarios().size() < 1) {
        System.out.println("Se necesita al menos un jugador registrado para jugar.");
        return;
    }

    System.out.println("Jugadores disponibles:");
    ArrayList<Usuario> usuarios = manejoRegistro.getUsuarios();
    for (int i = 0; i < usuarios.size(); i++) {
        System.out.println((i + 1) + ". " + usuarios.get(i).getAlias());
    }

    System.out.print("Seleccione el jugador : ");
    int jugadorIndex = in.nextInt() - 1;
    in.nextLine(); // Limpiar el buffer

    Usuario jugador = usuarios.get(jugadorIndex);
    Usuario computadora = new Usuario("Computadora" , 0 , "Computadora"); // ESTO CAPAZ HABRIA QUE HACERLO DE OTRA FORMA;

    System.out.println("Comienza el juego entre " + jugador.getAlias() + " y " + computadora.getAlias() + "!");
    tableroTateti.inicializarTablero();
    tableroTateti.mostrarTablero("");

    String jugadorActual = "X";
    Usuario jugadorActualRegistro = jugador;
    int siguienteCuadrante = -1;
    boolean partidaActiva = true;

    while (partidaActiva) {
        String cuadranteElegido;
        if (siguienteCuadrante == -1) {
            System.out.println(jugadorActualRegistro.getAlias() + ", elige un cuadrante  o presiona 'X' para rendirte: ");
            cuadranteElegido = in.nextLine().toUpperCase();
            if (cuadranteElegido.equals("X")) {
                System.out.println(jugadorActualRegistro.getAlias() + " se rinde. "
                        + computadora.getAlias() + " gana!");
                partidaActiva = false;
                continue;
            }
        } else {
            cuadranteElegido = indexACuadranteLabel(siguienteCuadrante);
            System.out.println(jugadorActualRegistro.getAlias() + ", debes jugar en el cuadrante " + cuadranteElegido + ".");
        }

        int cuadranteIndex = obtenerCuadranteIndex(cuadranteElegido);
        if (cuadranteIndex == -1 || tableroTateti.estaCuadranteCompleto(cuadranteIndex)) {
            System.out.println("Cuadrante inválido o completo. Intente de nuevo.");
            continue;
        }

        // Lógica para jugar en el minicuadrante elegido
        tableroTateti.mostrarTablero(cuadranteElegido);
        System.out.println("Elige una posición en el cuadrante " + cuadranteElegido + " (A1, A2, ..., C3): ");
        String posicionElegida = in.nextLine().toUpperCase();

        if (tableroTateti.jugarEnCuadrante(cuadranteIndex, posicionElegida, jugadorActual)) {
            tableroTateti.mostrarTablero(cuadranteElegido);

            if (tableroTateti.verificarMiniCuadranteGanado(cuadranteIndex, jugadorActual)) {
                tableroTateti.marcarCuadranteGanado(cuadranteIndex, jugadorActual);
                if (tableroTateti.verificarJuegoGanado(jugadorActual)) {
                    System.out.println("¡" + jugadorActualRegistro.getAlias() + " ha ganado el juego!");
                    partidaActiva = false;
                    continue;
                }
            }

            // La computadora juega
            siguienteCuadrante = obtenerCuadranteIndexDesdePosicion(posicionElegida);
            jugadorActual = "O";
            jugadorActualRegistro = computadora;

            // Lógica de la computadora para elegir un cuadrante y posición
            cuadranteElegido = indexACuadranteLabel(siguienteCuadrante);
            System.out.println(computadora.getAlias() + " jugará en el cuadrante " + cuadranteElegido + ".");

            // Elige una posición aleatoria dentro del cuadrante
            ArrayList<String> posicionesLibres = tableroTateti.obtenerPosicionesLibresEnCuadrante(siguienteCuadrante);
            if (!posicionesLibres.isEmpty()) {
                String posicionElegidaComputadora = posicionesLibres.get(new Random().nextInt(posicionesLibres.size()));
                if (tableroTateti.jugarEnCuadrante(siguienteCuadrante, posicionElegidaComputadora, jugadorActual)) {
                    System.out.println(computadora.getAlias() + " ha jugado en " + posicionElegidaComputadora);
                    tableroTateti.mostrarTablero(cuadranteElegido);

                    if (tableroTateti.verificarMiniCuadranteGanado(siguienteCuadrante, jugadorActual)) {
                        tableroTateti.marcarCuadranteGanado(siguienteCuadrante, jugadorActual);
                        if (tableroTateti.verificarJuegoGanado(jugadorActual)) {
                            System.out.println("¡" + jugadorActualRegistro.getAlias() + " ha ganado el juego!");
                            partidaActiva = false;
                        }
                    }

                    siguienteCuadrante = obtenerCuadranteIndexDesdePosicion(posicionElegidaComputadora);
                    jugadorActual = "X";
                    jugadorActualRegistro = jugador;
                } else {
                    System.out.println("Posición no válida o ya ocupada. La computadora no puede jugar.");
                }
            }
        } else {
            System.out.println("Posición no válida o ya ocupada. Intente de nuevo.");
        }

        if (tableroTateti.tableroCompleto()) {
            System.out.println("El juego ha terminado en empate.");
            partidaActiva = false;
        }
    }
}

    private static int obtenerCuadranteAleatorioValido() {
    ArrayList<Integer> cuadrantesLibres = tableroTateti.obtenerCuadrantesLibres();
    int randomIndex = (int) (Math.random() * cuadrantesLibres.size());
    return cuadrantesLibres.get(randomIndex);
    }

    private static String obtenerPosicionAleatoriaValida(int cuadranteIndex) {
    ArrayList<String> posicionesLibres = tableroTateti.obtenerPosicionesLibresEnCuadrante(cuadranteIndex);
    int randomIndex = (int) (Math.random() * posicionesLibres.size());
    return posicionesLibres.get(randomIndex);
    }

}
