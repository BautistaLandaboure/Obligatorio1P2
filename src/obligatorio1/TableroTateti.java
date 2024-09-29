/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        System.out.println("*******************");
    }

    private void imprimirFilas(int superFila) {
        for (int fila = 0; fila < 3; fila++) {
            System.out.print("*");
            for (int superColumna = 0; superColumna < 3; superColumna++) {
                System.out.print(tablero[superFila * 3 + fila][superColumna * 3] + "|"
                        + tablero[superFila * 3 + fila][superColumna * 3 + 1] + "|"
                        + tablero[superFila * 3 + fila][superColumna * 3 + 2]);

                if (superColumna != 2) {
                    System.out.print("*");
                }
            }
            System.out.println("*");

            if (fila != 2) {
                System.out.println("*-+-+-*-+-+-*-+-+-*");
            }
        }
    }
}