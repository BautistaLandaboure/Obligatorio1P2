package obligatorio1;

public class TableroTateti {

    private String[][] tablero;

    public TableroTateti() {
        this.tablero = new String[9][9];
        inicializarTablero();
    }

    public void inicializarTablero() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tablero[i][j] = " ";
            }
        }
    }
// seleccionar por cuadrante estaria funcionando bien, para revisar y ver maneras de repetir menos codigo

    public void mostrarTablero(String cuadranteSeleccionado) {
        for (int i = 0; i < 3; i++) {
            imprimirBordeDivisor(cuadranteSeleccionado, i);
            imprimirFilas(cuadranteSeleccionado, i);
        }
        imprimirBordeDivisor(cuadranteSeleccionado, 4);
    }

    private void imprimirBordeDivisor(String cuadranteSeleccionado, int filas) {
        String bordeGeneral = "\u001B[42m*******************\u001B[0m";
        String bordeResaltado = "\u001B[43m*******\u001B[0m";
        String parteA = "\u001B[42m******\u001B[0m";
        String parteB = "\u001B[42m************\u001B[0m";

        boolean esCuadrante1 = cuadranteSeleccionado.contains("1");
        boolean esCuadrante2 = cuadranteSeleccionado.contains("2");
        boolean esCuadrante3 = cuadranteSeleccionado.contains("3");
        boolean esParteA = cuadranteSeleccionado.contains("A");
        boolean esParteB = cuadranteSeleccionado.contains("B");
        boolean esParteC = cuadranteSeleccionado.contains("C");

        // definimos cuando usar borde resaltado
        boolean imprimirResaltado = (esParteA && (filas == 0 || filas == 1))
                || (esParteB && (filas == 1 || filas == 2))
                || (esParteC && (filas == 2 || filas == 3 || filas == 4));

        if (esCuadrante1) {
            if (imprimirResaltado) {
                System.out.println(bordeResaltado + parteB);
            } else {
                System.out.println(bordeGeneral);
            }
        } else if (esCuadrante2) {
            if (imprimirResaltado) {
                System.out.println(parteA + bordeResaltado + parteA);
            } else {
                System.out.println(bordeGeneral);
            }
        } else if (esCuadrante3) {
            if (imprimirResaltado) {
                System.out.println(parteB + bordeResaltado);
            } else {
                System.out.println(bordeGeneral);
            }
        } else {
            System.out.println(bordeGeneral);
        }
    }

    private void imprimirFilas(String cuadranteSeleccionado, int superFila) {

        for (int fila = 0; fila < 3; fila++) {
            if (cuadranteSeleccionado.contains("A") && !cuadranteSeleccionado.contains("2") && !cuadranteSeleccionado.contains("3") && superFila == 0) {

                System.out.print("\u001B[43m*\u001B[0m");
            } else if (cuadranteSeleccionado.contains("B") && !cuadranteSeleccionado.contains("2") && !cuadranteSeleccionado.contains("3") && superFila == 1) {
                System.out.print("\u001B[43m*\u001B[0m");

            } else if (cuadranteSeleccionado.contains("C") && !cuadranteSeleccionado.contains("2") && !cuadranteSeleccionado.contains("3") && superFila == 2) {
                System.out.print("\u001B[43m*\u001B[0m");

            } else {
                System.out.print("\u001B[42m*\u001B[0m");

            }

            for (int superColumna = 0; superColumna < 3; superColumna++) {
                System.out.print(tablero[superFila * 3 + fila][superColumna * 3] + "|"
                        + tablero[superFila * 3 + fila][superColumna * 3 + 1] + "|"
                        + tablero[superFila * 3 + fila][superColumna * 3 + 2]);

                if (superColumna != 2) {
                    if (cuadranteSeleccionado.contains("A") && superFila == 0) {
                        if ((cuadranteSeleccionado.contains("1") || cuadranteSeleccionado.contains("2")) && superColumna == 0 && (fila == 0 || fila == 1 || fila == 2)) {
                            System.out.print("\u001B[43m*\u001B[0m");
                        } else if ((cuadranteSeleccionado.contains("2") || cuadranteSeleccionado.contains("3")) && superColumna == 1 && (fila == 0 || fila == 1 || fila == 2)) {
                            System.out.print("\u001B[43m*\u001B[0m");
                        } else {
                            System.out.print("\u001B[42m*\u001B[0m");  // Asterisco con fondo verde
                        }

                    } else if (cuadranteSeleccionado.contains("B") && superFila == 1) {
                        if ((cuadranteSeleccionado.contains("1") || cuadranteSeleccionado.contains("2")) && superColumna == 0 && (fila == 0 || fila == 1 || fila == 2)) {
                            System.out.print("\u001B[43m*\u001B[0m");
                        } else if ((cuadranteSeleccionado.contains("2") || cuadranteSeleccionado.contains("3")) && superColumna == 1 && (fila == 0 || fila == 1 || fila == 2)) {
                            System.out.print("\u001B[43m*\u001B[0m");
                        } else {
                            System.out.print("\u001B[42m*\u001B[0m");
                        }

                    } else if (cuadranteSeleccionado.contains("C") && superFila == 2) {
                        if ((cuadranteSeleccionado.contains("1") || cuadranteSeleccionado.contains("2")) && superColumna == 0 && (fila == 0 || fila == 1 || fila == 2)) {
                            System.out.print("\u001B[43m*\u001B[0m");
                        } else if ((cuadranteSeleccionado.contains("2") || cuadranteSeleccionado.contains("3")) && superColumna == 1 && (fila == 0 || fila == 1 || fila == 2)) {
                            System.out.print("\u001B[43m*\u001B[0m");
                        } else {
                            System.out.print("\u001B[42m*\u001B[0m");
                        }
                    } else {
                        System.out.print("\u001B[42m*\u001B[0m");
                    }

                }
            }
            if (cuadranteSeleccionado.contains("A") && !cuadranteSeleccionado.contains("2") && !cuadranteSeleccionado.contains("1") && superFila == 0) {

                System.out.println("\u001B[43m*\u001B[0m");
                // System.out.print("hi");
            } else if (cuadranteSeleccionado.contains("B") && !cuadranteSeleccionado.contains("2") && !cuadranteSeleccionado.contains("1") && superFila == 1) {

                System.out.println("\u001B[43m*\u001B[0m");
            } else if (cuadranteSeleccionado.contains("C") && !cuadranteSeleccionado.contains("2") && !cuadranteSeleccionado.contains("1") && superFila == 2) {

                System.out.println("\u001B[43m*\u001B[0m");
            } else {
                System.out.println("\u001B[42m*\u001B[0m");  // Asterisco final con fondo verde
            }
            // if (fila != 2) {
            // }
            if (fila != 2) {
                String borde1, borde2, borde3, borde4;

                if (cuadranteSeleccionado.contains("A") && superFila == 0 && (fila == 0 || fila == 1)) {
                    if (cuadranteSeleccionado.contains("1")) {
                        System.out.println("\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m");
                    }
                    if (cuadranteSeleccionado.contains("2")) {
                        System.out.println("\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m");
                    }
                    if (cuadranteSeleccionado.contains("3")) {
                        System.out.println("\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m");
                    }

                } else if (cuadranteSeleccionado.contains("B") && superFila == 1 && (fila == 0 || fila == 1)) {
                    if (cuadranteSeleccionado.contains("1")) {
                        System.out.println("\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m");
                    }
                    if (cuadranteSeleccionado.contains("2")) {
                        System.out.println("\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m");
                    }
                    if (cuadranteSeleccionado.contains("3")) {
                        System.out.println("\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m");
                    }

                } else if (cuadranteSeleccionado.contains("C") && superFila == 2 && (fila == 0 || fila == 1)) {
                    if (cuadranteSeleccionado.contains("1")) {
                        System.out.println("\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m");
                    }
                    if (cuadranteSeleccionado.contains("2")) {
                        System.out.println("\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m");
                    }
                    if (cuadranteSeleccionado.contains("3")) {
                        System.out.println("\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m" + "-+-+-" + "\u001B[43m*\u001B[0m");
                    }
                } else {

                    System.out.println("\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m" + "-+-+-" + "\u001B[42m*\u001B[0m");  // Sin fondo verde en esta línea

                }
            }

        }
    }

    public boolean jugarEnCuadrante(int cuadranteIndex, String posicion, String simbolo) {
        // Convierte la posición (A1, A2, etc.) a coordenadas en el array
        int fila, columna;
        switch (posicion) {
            case "A1":
                fila = 0;
                columna = 0;
                break;
            case "A2":
                fila = 0;
                columna = 1;
                break;
            case "A3":
                fila = 0;
                columna = 2;
                break;
            case "B1":
                fila = 1;
                columna = 0;
                break;
            case "B2":
                fila = 1;
                columna = 1;
                break;
            case "B3":
                fila = 1;
                columna = 2;
                break;
            case "C1":
                fila = 2;
                columna = 0;
                break;
            case "C2":
                fila = 2;
                columna = 1;
                break;
            case "C3":
                fila = 2;
                columna = 2;
                break;
            default:
                return false; // Posición inválida
        }

        int filaTablero = (cuadranteIndex / 3) * 3 + fila; // Obtener fila en el tablero
        int columnaTablero = (cuadranteIndex % 3) * 3 + columna; // Obtener columna en el tablero

        if (tablero[filaTablero][columnaTablero].equals(" ")) {
            tablero[filaTablero][columnaTablero] = simbolo.equals("X") ? "\u001B[34mX\u001B[0m" : "\u001B[31mO\u001B[0m"; // Colores para X y O
            return true; // Jugada exitosa
        } else {
            return false; // Posición ya ocupada
        }
    }

    // Método para verificar si un cuadrante está completo
    public boolean estaCuadranteCompleto(int cuadranteIndex) {
        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 3; col++) {
                int filaTablero = (cuadranteIndex / 3) * 3 + fila;
                int columnaTablero = (cuadranteIndex % 3) * 3 + col;
                if (tablero[filaTablero][columnaTablero].equals(" ")) {
                    return false; // Hay al menos una posición libre
                }
            }
        }
        return true; // Todas las posiciones están ocupadas
    }
}
