package obligatorio1;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ManejoRegistro {

    private ArrayList<RegistroUsuarios> usuarios;

    public ManejoRegistro() {
        usuarios = new ArrayList<>();
    }

    // Método para registrar un nuevo usuario
    public boolean registrarUsuario(String nombre, String alias) {
        if (aliasExiste(alias)) {
            System.out.println("El alias ya está en uso.");
            return false;
        }

        int edad = pedirEdad();  // Aquí llamamos al método que controla la entrada de la edad

        RegistroUsuarios nuevoUsuario = new RegistroUsuarios(nombre, edad, alias);
        usuarios.add(nuevoUsuario);
        System.out.println("Usuario registrado: " + nuevoUsuario);
        return true;
    }

    // Método para pedir la edad al usuario y manejar posibles errores de entrada
    public int pedirEdad() {
        Scanner scanner = new Scanner(System.in);
        int edad = 0;
        boolean entradaValida = false;

        // Bucle que se repite hasta que se ingrese una edad válida
        while (!entradaValida) {
            System.out.print("Por favor, ingrese la edad: ");
            try {
                edad = scanner.nextInt();  // Intentar leer la edad
                entradaValida = true;  // Si es correcta, marcarla como válida
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Debe ingresar un número.");
                scanner.next();  // Limpiar la entrada inválida del buffer
            }
        }

        return edad;
    }

    // Método para verificar si el alias ya existe
    public boolean aliasExiste(String alias) {
        for (RegistroUsuarios usuario : usuarios) {
            if (usuario.getAlias().equals(alias)) {
                return true;
            }
        }
        return false;
    }
}
