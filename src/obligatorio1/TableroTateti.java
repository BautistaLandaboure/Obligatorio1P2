
package obligatorio1;

public class TableroTateti {
    private String[][] tablero;

    public TableroTateti() {
        this.tablero = new String[9][9];
        inicializarTablero();
    }

    private void inicializarTablero() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tablero[i][j] = " ";
            }
        }
    }

    public void mostrarTablero() {
        for (int i = 0; i < 3; i++) {
            imprimirBordeDivisor();
            imprimirFilas(i);
        }
        imprimirBordeDivisor();
    }

    private void imprimirBordeDivisor() {
        System.out.println("\u001B[32m*******************\u001B[0m");
    }

   private void imprimirFilas(int superFila) {
    for (int fila = 0; fila < 3; fila++) {
        System.out.print("\u001B[32m*\u001B[0m");  // Asterisco en verde
        for (int superColumna = 0; superColumna < 3; superColumna++) {
            System.out.print(tablero[superFila * 3 + fila][superColumna * 3] + "|"
                    + tablero[superFila * 3 + fila][superColumna * 3 + 1] + "|"
                    + tablero[superFila * 3 + fila][superColumna * 3 + 2]);

            if (superColumna != 2) {
                System.out.print("\u001B[32m*\u001B[0m");  // Asterisco en verde
            }
        }
        System.out.println("\u001B[32m*\u001B[0m");  // Asterisco en verde

        if (fila != 2) {
            System.out.println("\u001B[32m*-+-+-*-+-+-*-+-+-*\u001B[0m");  // Solo los asteriscos en verde
        }
    }
}
   
 public boolean jugarEnCuadrante(int cuadranteIndex, String posicion, String simbolo) {
    // Convierte la posición (A1, A2, etc.) a coordenadas en el arreglo
    int fila, columna;
    switch (posicion) {
        case "A1":
            fila = 0; columna = 0; break;
        case "A2":
            fila = 0; columna = 1; break;
        case "A3":
            fila = 0; columna = 2; break;
        case "B1":
            fila = 1; columna = 0; break;
        case "B2":
            fila = 1; columna = 1; break;
        case "B3":
            fila = 1; columna = 2; break;
        case "C1":
            fila = 2; columna = 0; break;
        case "C2":
            fila = 2; columna = 1; break;
        case "C3":
            fila = 2; columna = 2; break;
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
}
