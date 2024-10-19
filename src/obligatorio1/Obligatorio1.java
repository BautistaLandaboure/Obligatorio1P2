package obligatorio1;

import java.util.ArrayList;
import java.util.Scanner;

public class Obligatorio1 {

    private static ManejoRegistro manejoRegistro = new ManejoRegistro();
    private static TableroTateti tableroTateti = new TableroTateti();

    public static void main(String[] args) {
        int opcion = 0;
        Scanner in = new Scanner(System.in);

        while (opcion != 4) {
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
                case 1 ->
                    registrarJugador();
                case 2 ->
                    iniciarJuegoEntreDosPersonas();
                case 3 ->
                    //JUGAR CON LA COMPUTADORA
                    System.out.println("Opción no implementada");
                case 4 ->
                    mostrarRanking();
                case 5 ->
                    System.out.println("Gracias por jugar");
                default ->
                    System.out.println("Opción no válida");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\nMenú Principal:");
        System.out.println("1. Registrar un jugador");
        System.out.println("2. Jugar al Gran Tateti entre 2 personas");
        System.out.println("3. Jugar al Gran Tateti con la computadora");
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
                    if (jugadorActualRegistro.equals(jugador1)) {
                        jugador2.incrementarVictorias();
                    } else {
                        jugador1.incrementarVictorias();
                    }
                    partidaActiva = false;
                    continue;
                }
            } else {
                cuadranteElegido = indexACuadranteLabel(siguienteCuadrante);
                System.out.println(jugadorActualRegistro.getAlias()
                        + ", debes jugar en el cuadrante " + cuadranteElegido + ".");
            }

            int cuadranteIndex = obtenerCuadranteIndex(cuadranteElegido);

            // **Nueva validación:** No permitir jugadas en cuadrantes ganados
            if (tableroTateti.estaCuadranteGanado(cuadranteIndex)) {
                System.out.println("El cuadrante " + cuadranteElegido + " ya ha sido ganado. Elige otro cuadrante.");
                continue; // Volver al inicio del bucle para elegir de nuevo
            }

            tableroTateti.mostrarTablero(cuadranteElegido);
            System.out.println("Elige una posición en el cuadrante " + cuadranteElegido + " (A1, A2, ..., C3): ");
            String posicionElegida = in.nextLine().toUpperCase();

            if (tableroTateti.jugarEnCuadrante(cuadranteIndex, posicionElegida, jugadorActual)) {
                tableroTateti.mostrarTablero(cuadranteElegido);

                // Verificar si se ha ganado el mini cuadrante actual
                boolean miniCuadranteGanado = tableroTateti.verificarMiniCuadranteGanado(cuadranteIndex, jugadorActual);

                if (miniCuadranteGanado) {
                    if (tableroTateti.verificarJuegoGanado(jugadorActual)) {
                        System.out.println("¡" + jugadorActualRegistro.getAlias() + " ha ganado el juego!");
                        partidaActiva = false;
                        continue;
                    }
                    System.out.println("posicionElegida" + posicionElegida);
                    System.out.println("cuadranteElegido" + cuadranteElegido);
                    System.out.println("obtenerCuadranteIndexDesdePosicion(posicionElegida)" + obtenerCuadranteIndexDesdePosicion(posicionElegida));

                    if (posicionElegida.equals(cuadranteElegido)) {
                        siguienteCuadrante = -1; // Permitir al próximo jugador elegir cualquier cuadrante
                    } else {
                        siguienteCuadrante = obtenerCuadranteIndexDesdePosicion(posicionElegida); // Permitir al próximo jugador elegir cualquier cuadrante
                    }
                } else {
                    int nuevoCuadrante = obtenerCuadranteIndexDesdePosicion(posicionElegida);
                    if (!tableroTateti.estaCuadranteGanado(nuevoCuadrante)) {
                        siguienteCuadrante = nuevoCuadrante;
                    }
                }

                // Resaltar el siguiente cuadrante para la próxima jugada
                String cuadranteLabel = indexACuadranteLabel(siguienteCuadrante);
                tableroTateti.mostrarTablero(cuadranteLabel);


                // Cambiar de jugador
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
}
