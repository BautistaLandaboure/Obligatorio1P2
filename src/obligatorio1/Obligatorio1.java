package obligatorio1;

import java.util.Scanner;
import java.util.ArrayList;

public class Obligatorio1 {

    private static ManejoRegistro manejoRegistro = new ManejoRegistro();
    private static TableroTateti tableroTateti = new TableroTateti();

    public static void main(String[] args) {
        mostrarAnimacionBienvenida();

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
                // mostrar ranking 
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
        System.out.println("3. Ranking");
        System.out.println("4. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void registrarJugador() {
        Scanner in = new Scanner(System.in);

        System.out.print("Ingrese nombre: ");
        String nombre = in.nextLine();
        System.out.print("Ingrese edad: ");
        int edad = in.nextInt();
        in.nextLine();
        System.out.print("Ingrese alias: ");
        String alias = in.nextLine();

        boolean registrado = manejoRegistro.registrarUsuario(nombre, edad, alias);

        if (!registrado) {
            System.out.println("No se pudo registrar el jugador.");
        } else {
            System.out.println("Jugador registrado: Nombre: " + nombre + ", Edad: " + edad + ", Alias: " + alias);
        }
    }

    private static void iniciarJuegoEntreDosPersonas() {
        Scanner in = new Scanner(System.in);

        if (manejoRegistro.getUsuarios().size() < 2) {
            System.out.println("Se necesitan al menos dos jugadores registrados para jugar.");
            return;
        }

        System.out.println("Jugadores disponibles:");
        ArrayList<RegistroUsuarios> usuarios = manejoRegistro.getUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println((i + 1) + ". " + usuarios.get(i).getAlias());
        }

        System.out.print("Seleccione el jugador 1 (número): ");
        int jugador1Index = in.nextInt() - 1;
        System.out.print("Seleccione el jugador 2 (número): ");
        int jugador2Index = in.nextInt() - 1;

        RegistroUsuarios jugador1 = usuarios.get(jugador1Index);
        RegistroUsuarios jugador2 = usuarios.get(jugador2Index);

        System.out.println("¡Comienza el juego entre " + jugador1.getAlias() + " y " + jugador2.getAlias() + "!");

        tableroTateti.mostrarTablero();

        boolean partidaActiva = true;
        
        while (partidaActiva) {
            partidaActiva = false; // para que salga rapido mientras no exista la logica para el el juego, a eliminar
            //  logica de turnos etc
            // pense tipo si pones la letra Q que pregunte si quiere que se termine la partida antes??
            // System.out.print("¿Terminar partida? (S/N): ");
            // String respuesta = in.nextLine();
            // if (respuesta.equalsIgnoreCase("S")) {
            //     partidaActiva = false;
            //     System.out.println("Partida terminada.");
            // }
        }
    }
}
