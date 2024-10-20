package obligatorio1;

import java.util.*;

public class Obligatorio1 {

    private static final ManejoRegistro manejoRegistro = new ManejoRegistro();
    private static final TableroTateti tableroTateti = new TableroTateti();
    private static final ManejoJuego manejoJuego = new ManejoJuego();
    public static void main(String[] args) {
        int opcion = 0;
        Scanner in = new Scanner(System.in);

        // mostrarAnimacionBienvenida();

        while (opcion != 5) {
            mostrarMenu();
            opcion = leerOpcion(in);

            switch (opcion) {
                case 1 ->
                    registrarJugador();
                case 2 ->
                    iniciarJuegoEntreDosPersonas();
                case 3 ->
                    iniciarJuegoContraComputadora();
                case 4 ->
                    mostrarRanking();
                case 5 ->
                    System.out.println("Gracias por jugar");
                default ->
                    System.out.println("Opción no válida");
            }
        }
    }

    private static void mostrarAnimacionBienvenida() {
        String mensaje = "Bienvenidos al Gran Tateti";
        for (int i = 0; i < mensaje.length(); i++) {
            System.out.print(mensaje.charAt(i));
            try {
                Thread.sleep(100);  // wsperar 100 milisegundos entre cada letra
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
        System.out.println("3. Jugar al Gran Tateti con la computadora");
        System.out.println("4. Ver Ranking");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static int leerOpcion(Scanner in) {
        try {
            return in.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Por favor, ingrese un número válido.");
            in.nextLine(); // Limpiar buffer
            return -1;
        }
    }

    private static void registrarJugador() {
        Scanner in = new Scanner(System.in);

        System.out.print("Ingrese nombre: ");
        String nombre = in.nextLine().trim();

        int edad = 0;
        boolean edadValida = false;

        // Bucle para validar la edad como un número válido
        while (!edadValida) {
            System.out.print("Ingrese edad: ");
            String entradaEdad = in.nextLine().trim();

            try {
                edad = Integer.parseInt(entradaEdad);
                if (edad > 0) {  // Verifica que la edad sea positiva
                    edadValida = true;
                } else {
                    System.out.println("La edad debe ser un número positivo. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
            }
        }

        String alias;
        // Bucle para validar el alias como no vacío y único
        do {
            System.out.print("Ingrese alias: ");
            alias = in.nextLine().trim();

            if (alias.isEmpty()) {
                System.out.println("El alias no puede estar vacío. Intente de nuevo.");
            } else if (manejoRegistro.aliasExiste(alias)) {
                System.out.println("El alias ya está en uso. Intente con otro.");
            }
        } while (alias.isEmpty() || manejoRegistro.aliasExiste(alias));

        // Crear y registrar al nuevo usuario
        Usuario nuevoUsuario = new Usuario(nombre, edad, alias);
        manejoRegistro.registrarUsuario(nuevoUsuario);
        System.out.println("Jugador registrado: " + alias);
    }

    private static void iniciarJuegoEntreDosPersonas() {
        if (manejoRegistro.getUsuarios().size() < 2) {
            System.out.println("Se necesitan al menos dos jugadores registrados para jugar.");
            return;
        }

        mostrarJugadores();
        Scanner in = new Scanner(System.in);

        Usuario jugador1 = seleccionarJugador(in, "Seleccione el jugador 1: ");
        Usuario jugador2;

        // Asegurarse de que el jugador 2 no sea el mismo que el jugador 1
        do {
            jugador2 = seleccionarJugador(in, "Seleccione el jugador 2 (diferente al jugador 1): ");
            if (jugador2.equals(jugador1)) {
                System.out.println("No puedes seleccionar al mismo jugador dos veces. Intenta de nuevo.");
            }
        } while (jugador2.equals(jugador1));

        ManejoJuego.jugarPartida(jugador1, jugador2);
    }

    private static void iniciarJuegoContraComputadora() {
        if (manejoRegistro.getUsuarios().isEmpty()) {
            System.out.println("Se necesita al menos un jugador registrado.");
            return;
        }

        mostrarJugadores();
        Scanner in = new Scanner(System.in);

        Usuario jugador = seleccionarJugador(in, "Seleccione el jugador: ");
        Usuario computadora = new Usuario("Computadora", 0, "Computadora");

        ManejoJuego.jugarPartida(jugador, computadora);
    }

    private static Usuario seleccionarJugador(Scanner in, String mensaje) {
        int index = -1;
        ArrayList<Usuario> usuarios = manejoRegistro.getUsuarios();

        // Repetir hasta recibir un valor válido
        while (true) {
            System.out.print(mensaje);
            try {
                index = in.nextInt() - 1;  // Convertimos a índice (restamos 1)
                if (index >= 0 && index < usuarios.size()) {
                    return usuarios.get(index);
                } else {
                    System.out.println("Por favor, ingrese un número válido.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Debe ser un número.");
                in.nextLine();  // Limpiar el buffer de entrada
            }
        }
    }
    private static void mostrarJugadores() {
        ArrayList<Usuario> usuarios = manejoRegistro.getUsuarios();
        System.out.println("Jugadores disponibles:");
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println((i + 1) + ". " + usuarios.get(i).getAlias());
        }
    }

    private static void mostrarRanking() {
        ArrayList<Usuario> usuarios = manejoRegistro.getUsuarios();
        usuarios.sort(Comparator.comparingInt(Usuario::getVictorias).reversed());

        System.out.println("\nRanking de Jugadores:");
        for (Usuario usuario : usuarios) {
            System.out.println(usuario.getAlias() + " | Victorias: " + usuario.getVictorias());
        }
    }
}
