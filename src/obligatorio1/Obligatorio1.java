package obligatorio1;

import java.util.ArrayList;
import java.util.Scanner;

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
                if (edad < 1 || edad > 120) {  // Verificación de edad en un rango razonable
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
        String cuadranteElegido = "";

        tableroTateti.inicializarTablero(); // Reiniciar el tablero antes de iniciar el juego

        tableroTateti.mostrarTablero(cuadranteElegido);

        boolean partidaActiva = true;
        
        String jugadorActual = "X";  // Empieza el jugador 1
        Usuario jugadorActualRegistro = jugador1; // El jugador 1 inicia

        int siguienteCuadrante = -1; // -1 indica que cualquier cuadrante puede ser elegido

        while (partidaActiva) {
            if (siguienteCuadrante == -1) {
                // El jugador puede elegir cualquier cuadrante
                System.out.println(jugadorActualRegistro.getAlias() + ", elige un cuadrante (A1, A2, ..., C3): ");
                cuadranteElegido = in.nextLine().toUpperCase();

                int cuadranteIndex = obtenerCuadranteIndex(cuadranteElegido);

                if (cuadranteIndex == -1) {
                    System.out.println("Cuadrante inválido, por favor inténtalo de nuevo.");
                    continue;
                }

                if (tableroTateti.estaCuadranteCompleto(cuadranteIndex)) {
                    System.out.println("El cuadrante " + cuadranteElegido + " está completo. Elige otro cuadrante.");
                    continue;
                }

                System.out.println("Elige una posición en el cuadrante " + cuadranteElegido + " (A1, A2, ..., C3): ");
                String posicionElegida = in.nextLine().toUpperCase();

                boolean posicionValida = tableroTateti.jugarEnCuadrante(cuadranteIndex, posicionElegida, jugadorActual);
                
                if (posicionValida) {
                    tableroTateti.mostrarTablero(cuadranteElegido);
                    
                    // Determinar el siguiente cuadrante basado en la posición elegida
                    int nuevoCuadrante = obtenerCuadranteIndexDesdePosicion(posicionElegida);
                    if (nuevoCuadrante == -1) {
                        System.out.println("Posición inválida para determinar el siguiente cuadrante.");
                        siguienteCuadrante = -1;
                    } else {
                        if (tableroTateti.estaCuadranteCompleto(nuevoCuadrante)) {
                            System.out.println("El siguiente cuadrante (" + posicionElegida.charAt(0) + posicionElegida.charAt(1) + ") está completo. El siguiente jugador puede elegir cualquier cuadrante.");
                            siguienteCuadrante = -1;
                        } else {
                            siguienteCuadrante = nuevoCuadrante;
                        }
                    }
                    
                    // Cambia de jugador
                    if (jugadorActual.equals("X")) {
                        jugadorActual = "O";
                        jugadorActualRegistro = jugador2;
                    } else {
                        jugadorActual = "X";
                        jugadorActualRegistro = jugador1;
                    }
                } else {
                    System.out.println("Posición no válida o ya ocupada. Inténtalo de nuevo.");
                }
            } else {
                // El jugador debe jugar en el cuadrante especificado
                 cuadranteElegido = indexACuadranteLabel(siguienteCuadrante);
                System.out.println(jugadorActualRegistro.getAlias() + ", debes jugar en el cuadrante " + cuadranteElegido + ".");
                
                if (tableroTateti.estaCuadranteCompleto(siguienteCuadrante)) {
                    System.out.println("El cuadrante " + cuadranteElegido + " está completo. Debes elegir otro cuadrante.");
                    siguienteCuadrante = -1;
                    continue;
                }

                System.out.println("Elige una posición en el cuadrante " + cuadranteElegido + " (A1, A2, ..., C3): ");
                String posicionElegida = in.nextLine().toUpperCase();

                boolean posicionValida = tableroTateti.jugarEnCuadrante(siguienteCuadrante, posicionElegida, jugadorActual);
                
                if (posicionValida) {
                    tableroTateti.mostrarTablero(cuadranteElegido);
                    
                    // Determinar el siguiente cuadrante basado en la posición elegida
                    int nuevoCuadrante = obtenerCuadranteIndexDesdePosicion(posicionElegida);
                    if (nuevoCuadrante == -1) {
                        System.out.println("Posición inválida para determinar el siguiente cuadrante.");
                        siguienteCuadrante = -1;
                    } else {
                        if (tableroTateti.estaCuadranteCompleto(nuevoCuadrante)) {
                            System.out.println("El siguiente cuadrante (" + posicionElegida.charAt(0) + posicionElegida.charAt(1) + ") está completo. El siguiente jugador puede elegir cualquier cuadrante.");
                            siguienteCuadrante = -1;
                        } else {
                            siguienteCuadrante = nuevoCuadrante;
                        }
                    }
                    
                    // Cambia de jugador   
                    if (jugadorActual.equals("X")) {
                        jugadorActual = "O";
                        jugadorActualRegistro = jugador2;
                    } else {
                        jugadorActual = "X";
                        jugadorActualRegistro = jugador1;
                    }
                } else {
                    System.out.println("Posición no válida o ya ocupada. Inténtalo de nuevo.");
                }
            }
        }
    }

    // Método para convertir una posición (A1-C3) a un índice de cuadrante (0-8)
    private static int obtenerCuadranteIndexDesdePosicion(String posicion) {
        switch (posicion) {
            case "A1":
                return 0;
            case "A2":
                return 1;
            case "A3":
                return 2;
            case "B1":
                return 3;
            case "B2":
                return 4;
            case "B3":
                return 5;
            case "C1":
                return 6;
            case "C2":
                return 7;
            case "C3":
                return 8;
            default:
                return -1; // Posición inválida
        }
    }

    // Método para convertir un índice de cuadrante (0-8) a su label (A1-C3)
    private static String indexACuadranteLabel(int index) {
        switch (index) {
            case 0:
                return "A1";
            case 1:
                return "A2";
            case 2:
                return "A3";
            case 3:
                return "B1";
            case 4:
                return "B2";
            case 5:
                return "B3";
            case 6:
                return "C1";
            case 7:
                return "C2";
            case 8:
                return "C3";
            default:
                return "Invalid";
        }
    }

    private static int obtenerCuadranteIndex(String cuadrante) {
        switch (cuadrante) {
            case "A1":
                return 0;
            case "A2":
                return 1;
            case "A3":
                return 2;
            case "B1":
                return 3;
            case "B2":
                return 4;
            case "B3":
                return 5;
            case "C1":
                return 6;
            case "C2":
                return 7;
            case "C3":
                return 8;
            default:
                return -1; // Cuadrante inválido
        }
    }
}

//partidaActiva = false; 
            // para que salga rapido mientras no exista la logica para el el juego, a eliminar
            //  logica de turnos etc
            // pense tipo si pones la letra Q que pregunte si quiere que se termine la partida antes??
            // System.out.print("¿Terminar partida? (S/N): ");
            // String respuesta = in.nextLine();
            // if (respuesta.equalsIgnoreCase("S")) {
            //     partidaActiva = false;
            //     System.out.println("Partida terminada.");
            // }
