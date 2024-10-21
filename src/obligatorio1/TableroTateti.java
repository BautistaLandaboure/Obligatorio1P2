package obligatorio1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// @author Valentina Giusti - 199747
// @author Bautista Landaboure 

public class TableroTateti {

    private final String[][] tablero;
    private final int[] miniCuadrantesGanados;
    private String ultimoCuadranteSeleccionado = "";
    private static final String COLOR_VERDE = "\u001B[42m";
    private static final String COLOR_AMARILLO = "\u001B[43m";
    private static final String COLOR_ROJO = "\u001B[31m";
    private static final String COLOR_AZUL = "\u001B[34m";
    private static final String RESET_COLOR = "\u001B[0m";

    // Etiquetas de los cuadrantes
    private static final List<String> cuadranteLabels = Arrays.asList(
            "A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3"
    );

    public TableroTateti() {
        this.tablero = new String[19][19];
        this.miniCuadrantesGanados = new int[9];
        inicializarTablero();
    }

    public void inicializarTablero() {
        for (int i = 0; i < 19; i++) {
            Arrays.fill(tablero[i], " ");
        }
        Arrays.fill(miniCuadrantesGanados, 0);
    }

    public void mostrarTablero(String cuadranteSeleccionado) {
        construirTablero();

        if (!cuadranteSeleccionado.isEmpty()) {
            resaltarCuadrante(cuadranteSeleccionado);
        }

        for (String[] fila : tablero) {
            for (String celda : fila) {
                System.out.print(celda);
            }
            System.out.println();
        }
    }

    private void construirTablero() {
        int tamano = 19;

        for (int i = 0; i < tamano; i++) {
            for (int j = 0; j < tamano; j++) {
                if (!limpiarColor(tablero[i][j]).equals(" "))
                    continue;

                if (i == 0 || i == tamano - 1 || j == 0 || j == tamano - 1) {
                    tablero[i][j] = COLOR_VERDE + "*" + RESET_COLOR;
                } else if (i % 6 == 0 || j % 6 == 0) {
                    tablero[i][j] = COLOR_VERDE + "*" + RESET_COLOR;
                } else if (i % 2 == 0 && j % 2 == 0) {
                    tablero[i][j] = "+";
                } else if (i % 2 == 0) {
                    tablero[i][j] = "-";
                } else if (j % 2 == 0) {
                    tablero[i][j] = "|";
                } else {
                    tablero[i][j] = " ";
                }
            }
        }
    }

    public void resaltarCuadrante(String cuadranteSeleccionado) {
        if (!ultimoCuadranteSeleccionado.isEmpty()) {
            limpiarResaltado(ultimoCuadranteSeleccionado);
        }

        ultimoCuadranteSeleccionado = cuadranteSeleccionado;
        int filaInicio = (cuadranteSeleccionado.charAt(0) - 'A') * 6;
        int colInicio = (cuadranteSeleccionado.charAt(1) - '1') * 6;

        for (int i = filaInicio; i <= filaInicio + 6; i++) {
            for (int j = colInicio; j <= colInicio + 6; j++) {
                if (i == filaInicio || i == filaInicio + 6 || j == colInicio || j == colInicio + 6) {
                    tablero[i][j] = COLOR_AMARILLO + "*" + RESET_COLOR;
                }
            }
        }
    }

    public boolean jugarEnCuadrante(int cuadranteIndex, String posicion, String simbolo) {
        int filaBase = (cuadranteIndex / 3) * 6 + 1;
        int colBase = (cuadranteIndex % 3) * 6 + 1;

        int[] coords = obtenerCoordenadasPosicion(posicion);
        if (coords == null) {
            return false;
        }

        int fila = filaBase + coords[0] * 2;
        int col = colBase + coords[1] * 2;

        if (limpiarColor(tablero[fila][col]).equals(" ")) {
            String colorSimbolo = simbolo.equals("X") ? COLOR_ROJO : COLOR_AZUL;
            tablero[fila][col] = colorSimbolo + simbolo + RESET_COLOR;

            if (verificarMiniCuadranteGanado(cuadranteIndex, simbolo)) {
                System.out.println("¡Mini cuadrante ganado por " + simbolo + "!");
            }

            return true;
        }
        return false;
    }


    private int[] obtenerCoordenadasPosicion(String posicion) {
        if (posicion.length() != 2) {
            return null;  // verifica que la posición tenga el formato correcto
        }
        char fila = posicion.charAt(0);
        char columna = posicion.charAt(1);

        if (fila < 'A' || fila > 'C' || columna < '1' || columna > '3') {
            return null;
        }

        // convertimos 'A'-'C' en 0-2 y '1'-'3' en 0-2
        int filaIndex = fila - 'A';
        int columnaIndex = columna - '1';

        return new int[]{filaIndex, columnaIndex};
    }

    public int obtenerIndiceCuadrante(String label) {
        int index = cuadranteLabels.indexOf(label);
        if (index == -1) {
            System.out.println("Error: Cuadrante '" + label + "' no encontrado.");
        } else {
            System.out.println("Cuadrante válido: " + label + " con índice " + index);
        }
        return index;
    }

    public String obtenerLabelCuadrante(int index) {
        return (index >= 0 && index < cuadranteLabels.size()) ? cuadranteLabels.get(index) : "Invalid";
    }

    public void limpiarTodoResaltado() {
        for (String label : cuadranteLabels) {
            limpiarResaltado(label);
        }
    }


    public boolean verificarMiniCuadranteGanado(int cuadranteIndex, String simbolo) {
        // coordenadas base del cuadrante
        int filaBase = (cuadranteIndex / 3) * 6 + 1;
        int colBase = (cuadranteIndex % 3) * 6 + 1;

        // definimos las posiciones relativas de filas, columnas y diagonales
        int[][] lineas = {
            {0, 0, 0, 2, 0, 4},
            {2, 0, 2, 2, 2, 4},
            {4, 0, 4, 2, 4, 4},
            {0, 0, 2, 0, 4, 0},
            {0, 2, 2, 2, 4, 2},
            {0, 4, 2, 4, 4, 4},
            {0, 0, 2, 2, 4, 4},
            {0, 4, 2, 2, 4, 0}
        };

        for (int[] linea : lineas) {
            if (esLineaGanadora(filaBase, colBase, linea, simbolo)) {
                marcarCuadranteGanado(cuadranteIndex, simbolo);
                return true;
            }
        }
        return false;
    }

    private boolean esLineaGanadora(int filaBase, int colBase, int[] linea, String simbolo) {
        return simboloIgual(tablero[filaBase + linea[0]][colBase + linea[1]], simbolo)
                && simboloIgual(tablero[filaBase + linea[2]][colBase + linea[3]], simbolo)
                && simboloIgual(tablero[filaBase + linea[4]][colBase + linea[5]], simbolo);
    }

    private void marcarCuadranteGanado(int cuadranteIndex, String simbolo) {
        actualizarMiniCuadranteGanado(cuadranteIndex, simbolo);
        int filaBase = (cuadranteIndex / 3) * 6 + 1;
        int colBase = (cuadranteIndex % 3) * 6 + 1;
        pintarCuadranteGanado(filaBase, colBase, simbolo);
    }

    private boolean simboloIgual(String valor, String simbolo) {
        return limpiarColor(valor).equals(simbolo);
    }

    private void pintarCuadranteGanado(int filaBase, int colBase, String simbolo) {
        String color = simbolo.equals("X") ? COLOR_ROJO : COLOR_AZUL;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                tablero[filaBase + i][colBase + j]
                        = color + limpiarColor(tablero[filaBase + i][colBase + j]) + RESET_COLOR;
            }
        }
    }

    private void actualizarMiniCuadranteGanado(int cuadranteIndex, String simbolo) {
        miniCuadrantesGanados[cuadranteIndex] = simbolo.equals("X") ? 1 : 2;
    }

    public boolean estaCuadranteGanado(int cuadranteIndex) {
        if (cuadranteIndex < 0 || cuadranteIndex >= miniCuadrantesGanados.length) {
            System.out.println("Error: Índice de cuadrante fuera de rango: " + cuadranteIndex);
            return false;
        }
        return miniCuadrantesGanados[cuadranteIndex] != 0;
    }


    public boolean verificarJuegoGanado(String simbolo) {
        int valorSimbolo = simbolo.equals("X") ? 1 : 2;

        int[][] lineasGanadoras = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
        };

        // Verificar cada combinación ganadora
        for (int[] linea : lineasGanadoras) {
            if (miniCuadrantesGanados[linea[0]] == valorSimbolo
                    && miniCuadrantesGanados[linea[1]] == valorSimbolo
                    && miniCuadrantesGanados[linea[2]] == valorSimbolo) {
                return true;
            }
        }

        return false;
    }


    private String limpiarColor(String valor) {
        return valor.replaceAll("\\u001B\\[[;\\d]*m", "");
    }

    public boolean tableroCompleto() {
        for (int estado : miniCuadrantesGanados) {
            if (estado == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean estaCuadranteCompleto(int cuadranteIndex) {
        if (cuadranteIndex < 0 || cuadranteIndex >= miniCuadrantesGanados.length) {
            System.out.println("Error: Índice inválido para cuadrante: " + cuadranteIndex);
            return false; // Evita continuar con un índice inválido.
        }

        int filaInicio = (cuadranteIndex / 3) * 6 + 1;
        int colInicio = (cuadranteIndex % 3) * 6 + 1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int filaTablero = filaInicio + i * 2;
                int colTablero = colInicio + j * 2;

                if (limpiarColor(tablero[filaTablero][colTablero]).equals(" ")) {
                    return false;
                }
            }
        }
        return true;
    }


    public void limpiarResaltado(String cuadrante) {
        int filaInicio = (cuadrante.charAt(0) - 'A') * 6;
        int colInicio = (cuadrante.charAt(1) - '1') * 6;

        for (int i = filaInicio; i <= filaInicio + 6; i++) {
            for (int j = colInicio; j <= colInicio + 6; j++) {
                if (i == filaInicio || i == filaInicio + 6 || j == colInicio || j == colInicio + 6) {
                    tablero[i][j] = COLOR_VERDE + "*" + RESET_COLOR;
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

        int filaBase = (cuadranteIndex / 3) * 6 + 1;
        int colBase = (cuadranteIndex % 3) * 6 + 1;

        char[] filas = {'A', 'B', 'C'};
        char[] columnas = {'1', '2', '3'};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int fila = filaBase + i * 2;
                int columna = colBase + j * 2;

                if (limpiarColor(tablero[fila][columna]).equals(" ")) {
                    String posicion = "" + filas[i] + columnas[j];  // Ej: "A1", "B2"
                    posicionesLibres.add(posicion);
                }
            }
        }

        return posicionesLibres;
    }


    public void limpiarJugadasCuadrante(int cuadranteIndex, String posicionPreservada) {
        if (cuadranteIndex < 0 || cuadranteIndex >= cuadranteLabels.size()) {
            System.out.println("Error: Índice de cuadrante inválido. No se puede limpiar.");
            return;
        }

        int filaBase = (cuadranteIndex / 3) * 6 + 1;
        int colBase = (cuadranteIndex % 3) * 6 + 1;

        // convertir la posición preservada a coordenadas dentro del cuadrante
        int[] coordenadasPreservada = obtenerCoordenadasPosicion(posicionPreservada);

        if (coordenadasPreservada == null) {
            System.out.println("Error: Posición preservada inválida.");
            return;
        }

        int filaPreservada = filaBase + coordenadasPreservada[0] * 2;
        int colPreservada = colBase + coordenadasPreservada[1] * 2;

        System.out.println("Limpiando jugadas del cuadrante: " + obtenerLabelCuadrante(cuadranteIndex));

        // iterar sobre cada posición en el mini cuadrante y limpiarla excepto la preservada
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int fila = filaBase + i * 2;
                int columna = colBase + j * 2;

                if (fila != filaPreservada || columna != colPreservada) {
                    tablero[fila][columna] = " ";  // Limpiar posición
                }
            }
        }

        mostrarTablero("");
    }
}
