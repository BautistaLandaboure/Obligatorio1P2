package obligatorio1;

import java.util.ArrayList;

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
        int filaCuadrante = cuadranteSeleccionado.charAt(0) - 'A'; // 'A' -> 0, 'B' -> 1, 'C' -> 2
        int colCuadrante = cuadranteSeleccionado.charAt(1) - '1'; // '1' -> 0, '2' -> 1, '3' -> 2

        int filaInicio = filaCuadrante * 6;
        int filaFin = Math.min(filaInicio + 6, 18); // Asegurar que no excede el límite

        int colInicio = colCuadrante * 6;
        int colFin = Math.min(colInicio + 6, 18); // Asegurar que no excede el límite

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
        int filaBase = (cuadranteIndex / 3) * 6 + 1;
        int colBase = (cuadranteIndex % 3) * 6 + 1;

        // Verificar filas
        for (int i = 0; i < 3; i++) {
            if (simboloIgual(tablero[filaBase + i * 2][colBase], simbolo)
                    && simboloIgual(tablero[filaBase + i * 2][colBase + 2], simbolo)
                    && simboloIgual(tablero[filaBase + i * 2][colBase + 4], simbolo)) {
                actualizarMiniCuadranteGanado(cuadranteIndex, simbolo);
                pintarCuadranteGanado(filaBase, colBase, simbolo);
                mostrarTablero(""); // Refrescar tablero
                return true;
            }
        }

        // Verificar columnas
        for (int j = 0; j < 3; j++) {
            if (simboloIgual(tablero[filaBase][colBase + j * 2], simbolo)
                    && simboloIgual(tablero[filaBase + 2][colBase + j * 2], simbolo)
                    && simboloIgual(tablero[filaBase + 4][colBase + j * 2], simbolo)) {
                actualizarMiniCuadranteGanado(cuadranteIndex, simbolo);
                pintarCuadranteGanado(filaBase, colBase, simbolo);
                mostrarTablero(""); // Refrescar tablero
                return true;
            }
        }

        // Verificar diagonales
        if (simboloIgual(tablero[filaBase][colBase], simbolo)
                && simboloIgual(tablero[filaBase + 2][colBase + 2], simbolo)
                && simboloIgual(tablero[filaBase + 4][colBase + 4], simbolo)) {
            actualizarMiniCuadranteGanado(cuadranteIndex, simbolo);
            pintarCuadranteGanado(filaBase, colBase, simbolo);
            mostrarTablero(""); // Refrescar tablero
            return true;
        }

        if (simboloIgual(tablero[filaBase][colBase + 4], simbolo)
                && simboloIgual(tablero[filaBase + 2][colBase + 2], simbolo)
                && simboloIgual(tablero[filaBase + 4][colBase], simbolo)) {
            actualizarMiniCuadranteGanado(cuadranteIndex, simbolo);
            pintarCuadranteGanado(filaBase, colBase, simbolo);
            mostrarTablero(""); // Refrescar tablero
            return true;
        }

        return false;
    }

    private void actualizarMiniCuadranteGanado(int cuadranteIndex, String simbolo) {
        miniCuadrantesGanados[cuadranteIndex] = simbolo.equals("X") ? 1 : 2;
    }

    // Método auxiliar para comparar símbolos ignorando el color
    private boolean simboloIgual(String valor, String simbolo) {
        return limpiarColor(valor).equals(simbolo);
    }

    // Método para pintar todo el minicuadrante con el color del ganador
    private void pintarCuadranteGanado(int filaBase, int colBase, String simbolo) {
        String color = simbolo.equals("X") ? "\u001B[31m" : "\u001B[34m"; // Rojo para X, Azul para O

        // Recorrer todas las posiciones del minicuadrante, excluyendo la última fila y columna de separación
        for (int i = 0; i < 5; i++) { // 5 filas (sin incluir la última fila de separación)
            for (int j = 0; j < 5; j++) { // 5 columnas (sin incluir la última columna de separación)
                // Aplicar color al contenido de la celda, limpiando cualquier color previo
                tablero[filaBase + i][colBase + j]
                        = color + limpiarColor(tablero[filaBase + i][colBase + j]) + "\u001B[0m";
            }
        }
    }

    
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
    public boolean estaCuadranteGanado(int cuadranteIndex) {
        // Revisa si el minicuadrante está ganado por X (1) o por O (2)
        return miniCuadrantesGanados[cuadranteIndex] != 0;
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
    
    
    public ArrayList<Integer> obtenerCuadrantesLibres() {
        ArrayList<Integer> cuadrantesLibres = new ArrayList<>();
       
        for (int i = 0; i < miniCuadrantesGanados.length; i++) {
            if (miniCuadrantesGanados[i] == 0 && !estaCuadranteCompleto(i)) {
                cuadrantesLibres.add(i);
            }
        }
        return cuadrantesLibres;
    }
    public ArrayList<String> obtenerPosicionesLibresEnCuadrante(int cuadranteIndex) {
        ArrayList<String> posicionesLibres = new ArrayList<>(); 
        // Coordenadas base del cuadrante (calcula su posición en el tablero principal)
        int filaBase = (cuadranteIndex / 3) * 6 + 1;
        int colBase = (cuadranteIndex % 3) * 6 + 1;

        // Revisar cada posición en el cuadrante 
        if (limpiarColor(tablero[filaBase][colBase]).equals(" ")) posicionesLibres.add("A1");
        if (limpiarColor(tablero[filaBase][colBase + 2]).equals(" ")) posicionesLibres.add("A2");
        if (limpiarColor(tablero[filaBase][colBase + 4]).equals(" ")) posicionesLibres.add("A3");
        if (limpiarColor(tablero[filaBase + 2][colBase]).equals(" ")) posicionesLibres.add("B1");
        if (limpiarColor(tablero[filaBase + 2][colBase + 2]).equals(" ")) posicionesLibres.add("B2");
        if (limpiarColor(tablero[filaBase + 2][colBase + 4]).equals(" ")) posicionesLibres.add("B3");
        if (limpiarColor(tablero[filaBase + 4][colBase]).equals(" ")) posicionesLibres.add("C1");
        if (limpiarColor(tablero[filaBase + 4][colBase + 2]).equals(" ")) posicionesLibres.add("C2");
        if (limpiarColor(tablero[filaBase + 4][colBase + 4]).equals(" ")) posicionesLibres.add("C3");
        return posicionesLibres;
    }
    
    
    
    
    
    

}
