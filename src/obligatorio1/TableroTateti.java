package obligatorio1;

public class TableroTateti {

    private String[][] tablero;
    private int[] miniCuadrantesGanados; // 0 = no ganado, 1 = ganado por X, 2 = ganado por O

    public TableroTateti() {
        this.tablero = new String[19][19];
        this.miniCuadrantesGanados = new int[9];
        inicializarTablero();
    }

    public void inicializarTablero() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                tablero[i][j] = " ";
            }
        }
        for (int i = 0; i < 9; i++) {
            miniCuadrantesGanados[i] = 0;
        }
    }

    public int obtenerCuadrante(int fila, int columna) {
        if (fila < 6) {
            if (columna < 6) {
                return 1;
            } else if (columna < 12) {
                return 2;
            } else {
                return 3;
            }
        } else if (fila < 12) {
            if (columna < 6) {
                return 4;
            } else if (columna < 12) {
                return 5;
            } else {
                return 6;
            }
        } else {
            if (columna < 6) {
                return 7;
            } else if (columna < 12) {
                return 8;
            } else {
                return 9;
            }
        }
    }


    public void mostrarTablero(String cuadranteSeleccionado) {
        int tamano = 19;

        // Construir el tablero con símbolos y asteriscos
        for (int i = 0; i < tamano; i++) {
            for (int j = 0; j < tamano; j++) {
                // Verificar si ya hay un símbolo colocado en esta posición
                if (!limpiarColor(tablero[i][j]).equals(" ")) {
                    continue;  // Mantener el símbolo sin sobrescribirlo
                }

                // Perímetro
                if (i == 0 || i == tamano - 1 || j == 0 || j == tamano - 1) {
                    tablero[i][j] = "\u001B[42m*\u001B[0m";
                }
                // Filas y columnas en posiciones 6 y 12
                else if (i % 6 == 0 || j % 6 == 0) {
                    tablero[i][j] = "\u001B[42m*\u001B[0m";
                }
                // Filas alternas con barras '|'
                else if ((j == 2 || j == 4 || j == 8 || j == 10 || j == 14 || j == 16) && i % 2 == 1) {
                    tablero[i][j] = "|";
                }
                // Filas alternas con '-' y '+'
                else if (i % 2 == 0 && j % 2 == 1) {
                    tablero[i][j] = "-";
                } else if (i % 2 == 0 && j % 2 == 0 && j != 0 && j != tamano - 1) {
                    tablero[i][j] = "+";
                } else {
                    tablero[i][j] = " ";  // Relleno de espacios
                }
            }
        }

        // Resaltar cuadrante si se ha seleccionado uno
        if (!cuadranteSeleccionado.isEmpty()) {
            limpiarResaltado();
            resaltarCuadrante(tablero, cuadranteSeleccionado);
        }

        // Imprimir el tablero completo
        for (int i = 0; i < tamano; i++) {
            for (int j = 0; j < tamano; j++) {
                System.out.print(tablero[i][j]);
            }
            System.out.println();
        }
    }

    private void resaltarCuadrante(String[][] tablero, String cuadranteSeleccionado) {

        // convertir el nombre del cuadrante a índices
        int filaCuadrante = cuadranteSeleccionado.charAt(0) - 'A'; // 'A' -> 0, 'B' -> 1, 'C' -> 2
        int colCuadrante = cuadranteSeleccionado.charAt(1) - '1'; // '1' -> 0, '2' -> 1, '3' -> 2

        // Calcular las posiciones de inicio y fin del cuadrante seleccionado
        int filaInicio = filaCuadrante * 6;
        int filaFin = filaInicio + 6;

        int colInicio = colCuadrante * 6;
        int colFin = colInicio + 6;

        // Ajustes adicionales para los bordes derecho e inferior
        if (colCuadrante == 2 && colFin < 18) { // A3, B3, C3 (extiende el borde derecho si está dentro del límite)
            colFin++;
        }
        if (filaCuadrante == 2 && filaFin < 18) { // C1, C2, C3 (extiende el borde inferior si está dentro del límite)
            filaFin++;
        }

        // Aplicar color amarillo al perímetro del cuadrante seleccionado
        for (int i = filaInicio; i <= filaFin; i++) {
            for (int j = colInicio; j <= colFin; j++) {
                if (i == filaInicio || i == filaFin || j == colInicio || j == colFin) {
                    tablero[i][j] = "\u001B[43m*\u001B[0m"; // Amarillo
                }
            }
        }
    }

    public boolean jugarEnCuadrante(int cuadranteIndex, String posicion, String simbolo) {
        int fila, columna;

        // Mapeo correcto de posiciones A1, B2, etc.
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
            } // Posición inválida
        }

        // Cálculo correcto del desplazamiento dentro del tablero principal (19x19)
        int filaTablero = (cuadranteIndex / 3) * 6 + 1 + fila * 2;
        int columnaTablero = (cuadranteIndex % 3) * 6 + 1 + columna * 2;

        // Validar que la posición está libre antes de asignar el símbolo
        if (limpiarColor(tablero[filaTablero][columnaTablero]).equals(" ")) {
            tablero[filaTablero][columnaTablero]
                    = simbolo.equals("X") ? "\u001B[31mX\u001B[0m" : "\u001B[34mO\u001B[0m";
            return true;
        } else {
            return false;
        }
    }

    // Método para verificar si un mini cuadrante ha sido ganado por un jugador
    public boolean verificarMiniCuadranteGanado(int cuadranteIndex, String simbolo) {
        int filaBase = (cuadranteIndex / 3) * 3;
        int colBase = (cuadranteIndex % 3) * 3;

        for (int i = 0; i < 3; i++) {
            if (limpiarColor(tablero[filaBase + i][colBase]).equals(simbolo)
                    && limpiarColor(tablero[filaBase + i][colBase + 1]).equals(simbolo)
                    && limpiarColor(tablero[filaBase + i][colBase + 2]).equals(simbolo)) {
                return true;
            }
        }

        for (int j = 0; j < 3; j++) {
            if (limpiarColor(tablero[filaBase][colBase + j]).equals(simbolo)
                    && limpiarColor(tablero[filaBase + 1][colBase + j]).equals(simbolo)
                    && limpiarColor(tablero[filaBase + 2][colBase + j]).equals(simbolo)) {
                return true;
            }
        }

        if (limpiarColor(tablero[filaBase][colBase]).equals(simbolo)
                && limpiarColor(tablero[filaBase + 1][colBase + 1]).equals(simbolo)
                && limpiarColor(tablero[filaBase + 2][colBase + 2]).equals(simbolo)) {
            return true;
        }

        if (limpiarColor(tablero[filaBase][colBase + 2]).equals(simbolo)
                && limpiarColor(tablero[filaBase + 1][colBase + 1]).equals(simbolo)
                && limpiarColor(tablero[filaBase + 2][colBase]).equals(simbolo)) {
            return true;
        }

        return false;
    }

    // Método para marcar el cuadrante ganado y pintar las posiciones existentes
    public void marcarCuadranteGanado(int cuadranteIndex, String simbolo) {
        int filaBase = (cuadranteIndex / 3) * 3;
        int colBase = (cuadranteIndex % 3) * 3;
        String colorGanador = simbolo.equals("X") ? "\u001B[31m" : "\u001B[34m";

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[filaBase + i][colBase + j] = colorGanador + limpiarColor(tablero[filaBase + i][colBase + j]) + "\u001B[0m";
            }
        }

        // Guardar que el mini cuadrante ha sido ganado
        miniCuadrantesGanados[cuadranteIndex] = simbolo.equals("X") ? 1 : 2;
    }

    // Método para verificar si todo el tablero ha sido completado
    public boolean tableroCompleto() {
        for (int i = 0; i < 9; i++) {
            if (miniCuadrantesGanados[i] == 0) {
                return false;
            }
        }
        return true;
    }

    // Verificar si el jugador ha ganado el juego al obtener 3 mini cuadrantes en línea
    public boolean verificarJuegoGanado(String simbolo) {
        int valorSimbolo = simbolo.equals("X") ? 1 : 2;

        // Verificar filas de mini cuadrantes ganados
        for (int i = 0; i < 3; i++) {
            if (miniCuadrantesGanados[i * 3] == valorSimbolo
                    && miniCuadrantesGanados[i * 3 + 1] == valorSimbolo
                    && miniCuadrantesGanados[i * 3 + 2] == valorSimbolo) {
                return true; // Ganó en una fila de mini cuadrantes
            }
        }

        // Verificar columnas de mini cuadrantes ganados
        for (int j = 0; j < 3; j++) {
            if (miniCuadrantesGanados[j] == valorSimbolo
                    && miniCuadrantesGanados[j + 3] == valorSimbolo
                    && miniCuadrantesGanados[j + 6] == valorSimbolo) {
                return true; // Ganó en una columna de mini cuadrantes
            }
        }

        // Verificar diagonal principal de mini cuadrantes ganados
        if (miniCuadrantesGanados[0] == valorSimbolo
                && miniCuadrantesGanados[4] == valorSimbolo
                && miniCuadrantesGanados[8] == valorSimbolo) {
            return true; // Ganó en la diagonal principal de mini cuadrantes
        }

        // Verificar diagonal secundaria de mini cuadrantes ganados
        if (miniCuadrantesGanados[2] == valorSimbolo
                && miniCuadrantesGanados[4] == valorSimbolo
                && miniCuadrantesGanados[6] == valorSimbolo) {
            return true;
        }

        return false;
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

    // Función auxiliar para limpiar los colores del símbolo antes de compararlo
    private String limpiarColor(String valor) {
        return valor.replaceAll("\\u001B\\[[;\\d]*m", ""); // Elimina los códigos de colores ANSI
    }

    private void limpiarResaltado() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                // Cambiar los bordes amarillos de vuelta al verde, si están resaltados
                if (tablero[i][j].equals("\u001B[43m*\u001B[0m")) {
                    tablero[i][j] = "\u001B[42m*\u001B[0m"; // Volver al verde
                }
            }
        }
    }

}
