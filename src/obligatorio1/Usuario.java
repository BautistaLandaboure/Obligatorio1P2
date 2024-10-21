package obligatorio1;

// @author Valentina Giusti - 199747
// @author Bautista Landaboure - 316326

public class Usuario {

    private String nombre;
    private int edad;
    private String alias;
    private int victorias;
    private boolean usoJugadaMagica;


    public Usuario(String nombre, int edad, String alias) {
        this.nombre = nombre;
        this.edad = edad;
        this.alias = alias;
        this.victorias = 0;
        this.usoJugadaMagica = false;
    }

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

    public void setUsoJugadaMagica(boolean usoJugadaMagica) {
        this.usoJugadaMagica = usoJugadaMagica;
    }

    public boolean getUsoJugadaMagica() {
        return usoJugadaMagica;
    }

    public int getVictorias(){
        return victorias;
    }

     public void incrementarVictorias() {
        this.victorias++;
    }
}
