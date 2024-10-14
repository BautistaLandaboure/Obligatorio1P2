package obligatorio1;

public class Usuario {

    private String nombre;
    private int edad;
    private String alias;

// Constructor para crear una instancia de usuario
    public Usuario(String nombre, int edad, String alias) {
        this.nombre = nombre;
        this.edad = edad;
        this.alias = alias;
    }

// Método para mostrar la información del usuario
    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Edad: " + edad + ", Alias: " + alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}