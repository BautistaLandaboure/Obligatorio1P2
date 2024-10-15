package obligatorio1;

import java.util.ArrayList;
import java.util.Scanner;

public class Obligatorio1 {

    private static ManejoRegistro manejoRegistro = new ManejoRegistro();
    private static TableroTateti tableroTateti = new TableroTateti();

    public static void main(String[] args) {
        // mostrarAnimacionBienvenida();

        int opcion = 0;
        Scanner in = new Scanner(System.in);

        while (opcion != 4) {
            mostrarMenu();
            opcion = in.nextInt();
            in.nextLine();

            if (opcion == 1) {
                registrarJugador();
            } else if (opcion == 2) {
                iniciarJuegoEntreDosPersonas();
            } else if (opcion == 3) {
                mostrarRanking(); // Implementación del ranking
            } else if (opcion == 4) {
                System.out.println("Gracias por jugar");
            } else {
                System.out.println("Opción no válida");
            }
        }
    }

    private static void mostrarAnimacionBienvenida() {
        String mensaje = "Bienvenidos al Gran Tateti";

        for (int i = 0; i < mensaje.length(); i++) {
            System.out.print(mensaje.charAt(i));
            try {
                Thread.sleep(100);  // esperar 100 milisegundos entre cada letra
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(); 

        // barra de carga simulada
        System.out.print("Cargando");
        for (int i = 0; i < 10; i++) {
            System.out.print(".");
            try {
                Thread.sleep(300);  // simular la carga con un retardo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n¡Listo para comenzar!");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void mostrarMenu() {
        System.out.println("\nMenú Principal:");
        System.out.println("1. Registrar un jugador");
        System.out.println("2. Jugar al Gran Tateti entre 2 personas");
        System.out.println("3. Ranking");
        System.out.println("4. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void registrarJugador() {
        Scanner in = new Scanner(System.in);

        // Validación del nombre
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

        // Validación del alias
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

        tableroTateti.inicializarTablero(); // Reiniciar el tablero antes de iniciar el juego
        tableroTateti.mostrarTablero(""); // Mostrar tablero inicial

        String jugadorActual = "X";  // Empieza el jugador 1 (X es rojo)
        Usuario jugadorActualRegistro = jugador1;

        int siguienteCuadrante = -1; // -1 indica que cualquier cuadrante puede ser elegido
        boolean partidaActiva = true;

        while (partidaActiva) {
            String cuadranteElegido;
            if (siguienteCuadrante == -1) {
                System.out.println(jugadorActualRegistro.getAlias() + ", elige un cuadrante (A1, A2, ..., C3) o presiona 'x' para rendirte: ");
                cuadranteElegido = in.nextLine().toUpperCase();
            } else {
                cuadranteElegido = indexACuadranteLabel(siguienteCuadrante);
                System.out.println(jugadorActualRegistro.getAlias() + ", debes jugar en el cuadrante " + cuadranteElegido + " o presiona 'x' para rendirte.");
                cuadranteElegido = in.nextLine().toUpperCase();
            }

            // Si el jugador presiona 'x', el otro jugador gana
            if (cuadranteElegido.equals("X")) {
                Usuario ganador = jugadorActual.equals("X") ? jugador2 : jugador1;
                ganador.incrementarVictorias(); // Incrementar victorias del ganador
                System.out.println("¡" + ganador.getAlias() + " ha ganado el juego porque " + jugadorActualRegistro.getAlias() + " se rindió!");
                partidaActiva = false;
                continue;
            }

            int cuadranteIndex = obtenerCuadranteIndex(cuadranteElegido);
            if (cuadranteIndex == -1 || tableroTateti.estaCuadranteCompleto(cuadranteIndex)) {
                System.out.println("Cuadrante inválido o completo. Intente de nuevo.");
                continue;
            }

            tableroTateti.mostrarTablero(cuadranteElegido);

            System.out.println("Elige una posición en el cuadrante " + cuadranteElegido + " (A1, A2, ..., C3) o presiona 'x' para rendirte: ");
            String posicionElegida = in.nextLine().toUpperCase();

            // Si el jugador presiona 'x' en la posición, se rinde
            if (posicionElegida.equals("X")) {
                Usuario ganador = jugadorActual.equals("X") ? jugador2 : jugador1;
                ganador.incrementarVictorias(); // Incrementar victorias del ganador
                System.out.println("¡" + ganador.getAlias() + " ha ganado el juego porque " + jugadorActualRegistro.getAlias() + " se rindió!");
                partidaActiva = false;
                continue;
            }

            boolean posicionValida = tableroTateti.jugarEnCuadrante(cuadranteIndex, posicionElegida, jugadorActual);

            if (posicionValida) {
                tableroTateti.mostrarTablero(cuadranteElegido);

                if (tableroTateti.verificarMiniCuadranteGanado(cuadranteIndex, jugadorActual)) {
                    tableroTateti.marcarCuadranteGanado(cuadranteIndex, jugadorActual);
                    if (tableroTateti.verificarJuegoGanado(jugadorActual)) {
                        System.out.println("¡" + jugadorActualRegistro.getAlias() + " ha ganado el juego!");
                        // jugadorActualRegistro.incrementarVictorias(); // Esta línea ha sido eliminada
                        partidaActiva = false;
                        continue;
                    }
                }

                siguienteCuadrante = obtenerCuadranteIndexDesdePosicion(posicionElegida);

                if (jugadorActual.equals("X")) {
                    jugadorActual = "O";
                    jugadorActualRegistro = jugador2;
                } else {
                    jugadorActual = "X";
                    jugadorActualRegistro = jugador1;
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

    private static void mostrarRanking() {
        
    System.out.println("\nRanking de Jugadores:");
    
    ArrayList<Usuario> usuarios = manejoRegistro.getUsuarios();
    
    for (Usuario usuario : usuarios) {
        
        System.out.print(usuario.getAlias() + " | ");
        
        for (int i = 0; i < usuario.getVictorias(); i++) {
            
            System.out.print("#");
        }
        System.out.println(); 
    }
}

}
