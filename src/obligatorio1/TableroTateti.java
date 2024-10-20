package obligatorio1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableroTateti {

    private final String[][] tablero;
    private final int[] miniCuadrantesGanados;
    private String ultimoCuadranteSeleccionado = "";  // Para rastrear el último cuadrante seleccionado

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
                    tablero[i][j] = "\u001B[42m*\u001B[0m";
                } else if (i % 6 == 0 || j % 6 == 0) {
                    tablero[i][j] = "\u001B[42m*\u001B[0m";
                } else if (i % 2 == 0 && j % 2 == 0) {
                    tablero[i][j] = "+";  // Intersecciones dentro del cuadrante
                } else if (i % 2 == 0) {
                    tablero[i][j] = "-";  // Relleno horizontal
                } else if (j % 2 == 0) {
                    tablero[i][j] = "|";  // Relleno vertical
                } else {
                    tablero[i][j] = " ";  // Espacio vacío
                }
            }
        }
    }

    public void resaltarCuadrante(String cuadranteSeleccionado) {
        // Limpia el resalte anterior si existe
        if (!ultimoCuadranteSeleccionado.isEmpty()) {
            limpiarResaltado(ultimoCuadranteSeleccionado);
        }

        // Guardamos el cuadrante seleccionado como el último
        ultimoCuadranteSeleccionado = cuadranteSeleccionado;

        // Cálculo del inicio del cuadrante
        int filaInicio = (cuadranteSeleccionado.charAt(0) - 'A') * 6;
        int colInicio = (cuadranteSeleccionado.charAt(1) - '1') * 6;

        // Ajustamos el área de resalte para que no sobresalga
        for (int i = filaInicio; i <= filaInicio + 6; i++) {
            for (int j = colInicio; j <= colInicio + 6; j++) {
                // Resaltar solo el borde exterior
                if (i == filaInicio || i == filaInicio + 6 || j == colInicio || j == colInicio + 6) {
                    tablero[i][j] = "\u001B[43m*\u001B[0m";  // Amarillo
                }
            }
        }
    }



    public boolean jugarEnCuadrante(int cuadranteIndex, String posicion, String simbolo) {
        int filaBase = (cuadranteIndex / 3) * 6 + 1;
        int colBase = (cuadranteIndex % 3) * 6 + 1;

        int[] coords = obtenerCoordenadasPosicion(posicion);
        if (coords == null)
            return false;

        int fila = filaBase + coords[0] * 2;
        int col = colBase + coords[1] * 2;

        if (limpiarColor(tablero[fila][col]).equals(" ")) {
            tablero[fila][col] = simbolo.equals("X") ? "\u001B[31mX\u001B[0m" : "\u001B[34mO\u001B[0m";
            return true;
        }
        return false;
    }

    private int[] obtenerCoordenadasPosicion(String posicion) {
        return switch (posicion) {
            case "A1" ->
                new int[]{0, 0};
            case "A2" ->
                new int[]{0, 1};
            case "A3" ->
                new int[]{0, 2};
            case "B1" ->
                new int[]{1, 0};
            case "B2" ->
                new int[]{1, 1};
            case "B3" ->
                new int[]{1, 2};
            case "C1" ->
                new int[]{2, 0};
            case "C2" ->
                new int[]{2, 1};
            case "C3" ->
                new int[]{2, 2};
            default ->
                null;
        };
    }

    public int obtenerIndiceCuadrante(String label) {
        return cuadranteLabels.indexOf(label);
    }

    public String obtenerLabelCuadrante(int index) {
        return (index >= 0 && index < cuadranteLabels.size()) ? cuadranteLabels.get(index) : "Invalid";
    }

    public boolean verificarMiniCuadranteGanado(int cuadranteIndex, String simbolo) {
        // Coordenadas base del cuadrante
        int filaBase = (cuadranteIndex / 3) * 6 + 1;
        int colBase = (cuadranteIndex % 3) * 6 + 1;

        boolean filaGanada, columnaGanada;
        boolean diagPrincipalGanada = true, diagSecundariaGanada = true;

        // Usamos un solo bucle para verificar filas, columnas y diagonales
        for (int i = 0; i < 3; i++) {
            // Verificar fila i
            filaGanada = simboloIgual(tablero[filaBase + i * 2][colBase], simbolo)
                    && simboloIgual(tablero[filaBase + i * 2][colBase + 2], simbolo)
                    && simboloIgual(tablero[filaBase + i * 2][colBase + 4], simbolo);

            // Verificar columna i
            columnaGanada = simboloIgual(tablero[filaBase][colBase + i * 2], simbolo)
                    && simboloIgual(tablero[filaBase + 2][colBase + i * 2], simbolo)
                    && simboloIgual(tablero[filaBase + 4][colBase + i * 2], simbolo);

            // Si encontramos una fila o columna ganada, terminamos
            if (filaGanada || columnaGanada) {
                actualizarMiniCuadranteGanado(cuadranteIndex, simbolo);
                pintarCuadranteGanado(filaBase, colBase, simbolo);
                return true;
            }

            // Verificar las diagonales (solo en la primera iteración)
            if (i == 0) {
                diagPrincipalGanada = simboloIgual(tablero[filaBase][colBase], simbolo)
                        && simboloIgual(tablero[filaBase + 2][colBase + 2], simbolo)
                        && simboloIgual(tablero[filaBase + 4][colBase + 4], simbolo);

                diagSecundariaGanada = simboloIgual(tablero[filaBase][colBase + 4], simbolo)
                        && simboloIgual(tablero[filaBase + 2][colBase + 2], simbolo)
                        && simboloIgual(tablero[filaBase + 4][colBase], simbolo);
            }
        }

        // Si se ganó alguna diagonal, marcamos el cuadrante como ganado
        if (diagPrincipalGanada || diagSecundariaGanada) {
            actualizarMiniCuadranteGanado(cuadranteIndex, simbolo);
            pintarCuadranteGanado(filaBase, colBase, simbolo);
            return true;
        }

        return false;  // No se ganó el mini cuadrante
    }


    private boolean simboloIgual(String valor, String simbolo) {
        return limpiarColor(valor).equals(simbolo);
    }

    private void pintarCuadranteGanado(int filaBase, int colBase, String simbolo) {
        String color = simbolo.equals("X") ? "\u001B[31m" : "\u001B[34m";  // Rojo para X, Azul para O

        for (int i = 0; i < 5; i++) {  // Excluye la última fila de separación
            for (int j = 0; j < 5; j++) {  // Excluye la última columna de separación
                tablero[filaBase + i][colBase + j]
                        = color + limpiarColor(tablero[filaBase + i][colBase + j]) + "\u001B[0m";
            }
        }
    }
    
    private void actualizarMiniCuadranteGanado(int cuadranteIndex, String simbolo) {
        miniCuadrantesGanados[cuadranteIndex] = simbolo.equals("X") ? 1 : 2;
    }

    public boolean estaCuadranteGanado(int cuadranteIndex) {
        // Si el valor es distinto de 0, significa que el cuadrante fue ganado.
        return miniCuadrantesGanados[cuadranteIndex] != 0;
    }


    public boolean verificarJuegoGanado(String simbolo) {
        int valorSimbolo = simbolo.equals("X") ? 1 : 2;

        for (int i = 0; i < 3; i++) {
            if (miniCuadrantesGanados[i * 3] == valorSimbolo
                    && miniCuadrantesGanados[i * 3 + 1] == valorSimbolo
                    && miniCuadrantesGanados[i * 3 + 2] == valorSimbolo) {
                return true;
            }
        }

        for (int j = 0; j < 3; j++) {
            if (miniCuadrantesGanados[j] == valorSimbolo
                    && miniCuadrantesGanados[j + 3] == valorSimbolo
                    && miniCuadrantesGanados[j + 6] == valorSimbolo) {
                return true;
            }
        }

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
        // Calcular la fila y columna de inicio del cuadrante en el tablero principal
        int filaInicio = (cuadranteIndex / 3) * 6 + 1;
        int colInicio = (cuadranteIndex % 3) * 6 + 1;

        // Revisar cada posición dentro del cuadrante
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int filaTablero = filaInicio + i * 2;
                int colTablero = colInicio + j * 2;

                // Si hay una posición vacía, el cuadrante no está completo
                if (limpiarColor(tablero[filaTablero][colTablero]).equals(" ")) {
                    return false;
                }
            }
        }
        // Si no hay posiciones vacías, el cuadrante está completo
        return true;
    }

    public void limpiarResaltado(String cuadrante) {
        int filaInicio = (cuadrante.charAt(0) - 'A') * 6;
        int colInicio = (cuadrante.charAt(1) - '1') * 6;

        // Restaurar el borde original a verde
        for (int i = filaInicio; i <= filaInicio + 6; i++) {
            for (int j = colInicio; j <= colInicio + 6; j++) {
                if (i == filaInicio || i == filaInicio + 6 || j == colInicio || j == colInicio + 6) {
                    tablero[i][j] = "\u001B[42m*\u001B[0m";  // Verde
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

        // Verificar cada posición en el cuadrante (A1, A2, ..., C3)
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

    public void limpiarMiniCuadrante(String cuadrante) {
        int filaBase = (cuadrante.charAt(0) - 'A') * 6 + 1;
        int colBase = (cuadrante.charAt(1) - '1') * 6 + 1;
        for (int i = 0; i < 5; i++) {  // Excluye la fila de separación
            for (int j = 0; j < 5; j++) {  // Excluye la columna de separación
                tablero[filaBase + i][colBase + j] = " ";  // Limpia la celda
            }
        }
        System.out.println("Mini cuadrante " + cuadrante + " ha sido limpiado.");
    }

}
