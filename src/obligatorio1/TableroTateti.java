package obligatorio1;

public class TableroTateti {

    private String[][] tablero;
    private int[] miniCuadrantesGanados;

    public TableroTateti() {
        this.tablero = new String[9][9];
        this.miniCuadrantesGanados = new int[9];
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
        int fila, columna;
        switch (posicion) {
            case "A1" -> {
                fila = 0;
                columna = 0;
            }
            case "A2" -> {
                fila = 0;
                columna = 1;
            }
            case "A3" -> {
                fila = 0;
                columna = 2;
            }
            case "B1" -> {
                fila = 1;
                columna = 0;
            }
            case "B2" -> {
                fila = 1;
                columna = 1;
            }
            case "B3" -> {
                fila = 1;
                columna = 2;
            }
            case "C1" -> {
                fila = 2;
                columna = 0;
            }
            case "C2" -> {
                fila = 2;
                columna = 1;
            }
            case "C3" -> {
                fila = 2;
                columna = 2;
            }
            default -> {
                return false;
            }
        }

        int filaTablero = (cuadranteIndex / 3) * 3 + fila;
        int columnaTablero = (cuadranteIndex % 3) * 3 + columna;

        if (tablero[filaTablero][columnaTablero].equals(" ")) {
            tablero[filaTablero][columnaTablero] = simbolo.equals("X") ? "\u001B[31mX\u001B[0m" : "\u001B[34mO\u001B[0m";
            return true;
        } else {
            return false;
        }
    }

    public boolean verificarMiniCuadranteGanado(int cuadranteIndex, String simbolo) {
        int filaBase = (cuadranteIndex / 3) * 3;
        int colBase = (cuadranteIndex % 3) * 3;

        // Verificar filas
        for (int i = 0; i < 3; i++) {
            System.out.println("Verificando fila " + i + ": " + limpiarColor(tablero[filaBase + i][colBase]) + ", " + limpiarColor(tablero[filaBase + i][colBase + 1]) + ", " + limpiarColor(tablero[filaBase + i][colBase + 2]));
            if (limpiarColor(tablero[filaBase + i][colBase]).equals(simbolo)
                    && limpiarColor(tablero[filaBase + i][colBase + 1]).equals(simbolo)
                    && limpiarColor(tablero[filaBase + i][colBase + 2]).equals(simbolo)) {
                System.out.println("Ganó en fila");
                return true;
            }
        }

        // Verificar columnas
        for (int j = 0; j < 3; j++) {
            System.out.println("Verificando columna " + j + ": " + limpiarColor(tablero[filaBase][colBase + j]) + ", " + limpiarColor(tablero[filaBase + 1][colBase + j]) + ", " + limpiarColor(tablero[filaBase + 2][colBase + j]));
            if (limpiarColor(tablero[filaBase][colBase + j]).equals(simbolo)
                    && limpiarColor(tablero[filaBase + 1][colBase + j]).equals(simbolo)
                    && limpiarColor(tablero[filaBase + 2][colBase + j]).equals(simbolo)) {
                System.out.println("Ganó en columna");
                return true;
            }
        }

        // Verificar diagonal principal
        System.out.println("Verificando diagonal principal: " + limpiarColor(tablero[filaBase][colBase]) + ", " + limpiarColor(tablero[filaBase + 1][colBase + 1]) + ", " + limpiarColor(tablero[filaBase + 2][colBase + 2]));
        if (limpiarColor(tablero[filaBase][colBase]).equals(simbolo)
                && limpiarColor(tablero[filaBase + 1][colBase + 1]).equals(simbolo)
                && limpiarColor(tablero[filaBase + 2][colBase + 2]).equals(simbolo)) {
            System.out.println("Ganó en diagonal principal");
            return true;
        }

        // Verificar diagonal secundaria
        System.out.println("Verificando diagonal secundaria: " + limpiarColor(tablero[filaBase][colBase + 2]) + ", " + limpiarColor(tablero[filaBase + 1][colBase + 1]) + ", " + limpiarColor(tablero[filaBase + 2][colBase]));
        if (limpiarColor(tablero[filaBase][colBase + 2]).equals(simbolo)
                && limpiarColor(tablero[filaBase + 1][colBase + 1]).equals(simbolo)
                && limpiarColor(tablero[filaBase + 2][colBase]).equals(simbolo)) {
            System.out.println("Ganó en diagonal secundaria");
            return true;
        }

        System.out.println("No ganó en ninguna fila, columna o diagonal");
        return false;
    }

    public void marcarCuadranteGanado(int cuadranteIndex, String simbolo) {
        int filaBase = (cuadranteIndex / 3) * 3;
        int colBase = (cuadranteIndex % 3) * 3;
        String colorGanador = simbolo.equals("X") ? "\u001B[31mX\u001B[0m" : "\u001B[34mO\u001B[0m";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[filaBase + i][colBase + j] = colorGanador;
            }
        }
        miniCuadrantesGanados[cuadranteIndex] = simbolo.equals("X") ? 1 : 2;
    }

    public boolean verificarJuegoGanado(String simbolo) {
        int valorSimbolo = simbolo.equals("X") ? 1 : 2;

        // Verificar filas
        for (int i = 0; i < 3; i++) {
            if (miniCuadrantesGanados[i * 3] == valorSimbolo
                    && miniCuadrantesGanados[i * 3 + 1] == valorSimbolo
                    && miniCuadrantesGanados[i * 3 + 2] == valorSimbolo) {
                return true;
            }
        }

        // Verificar columnas
        for (int j = 0; j < 3; j++) {
            if (miniCuadrantesGanados[j] == valorSimbolo
                    && miniCuadrantesGanados[j + 3] == valorSimbolo
                    && miniCuadrantesGanados[j + 6] == valorSimbolo) {
                return true;
            }
        }

        // Verificar diagonales
        if (miniCuadrantesGanados[0] == valorSimbolo
                && miniCuadrantesGanados[4] == valorSimbolo
                && miniCuadrantesGanados[8] == valorSimbolo) {
            return true;
        }

        if (miniCuadrantesGanados[2] == valorSimbolo
                && miniCuadrantesGanados[4] == valorSimbolo
                && miniCuadrantesGanados[6] == valorSimbolo) {
            return true;
        }

        return false;
    }

    public boolean tableroCompleto() {
        for (int i = 0; i < 9; i++) {
            if (miniCuadrantesGanados[i] == 0) {
                return false;
            }
        }
        return true;
    }

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

    // Función auxiliar para limpiar los colores del símbolo
    private String limpiarColor(String valor) {
        return valor.replaceAll("\\u001B\\[[;\\d]*m", ""); // Elimina los códigos de colores ANSI
    }

}
