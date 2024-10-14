package obligatorio1;

import java.util.ArrayList;

public class ManejoRegistro {

    private ArrayList<Usuario> usuarios;

    public ManejoRegistro() {
        usuarios = new ArrayList<>();

        Usuario jugador1 = new Usuario("Jugador1", 25, "XMaster");
        Usuario jugador2 = new Usuario("Jugador2", 30, "OMaster");
        usuarios.add(jugador1);
        usuarios.add(jugador2);
    }

    public boolean registrarUsuario(Usuario nuevoUsuario) {
        if (aliasExiste(nuevoUsuario.getAlias())) {
            return false;  // Alias ya existe
        }

        usuarios.add(nuevoUsuario);
        return true;  // Usuario registrado con éxito
    }
    // Método para verificar si el alias ya existe
    public boolean aliasExiste(String alias) {
        for (Usuario usuario : usuarios) {
            if (usuario.getAlias().equals(alias)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
}
