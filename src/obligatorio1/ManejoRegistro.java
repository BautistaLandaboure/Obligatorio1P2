package obligatorio1;

import java.util.ArrayList;

// @author Valentina Giusti - 199747
// @author Bautista Landaboure - 316326

public class ManejoRegistro {

    private ArrayList<Usuario> usuarios;

    public ManejoRegistro() {
        usuarios = new ArrayList<>();
    }

    public boolean registrarUsuario(Usuario nuevoUsuario) {
        if (aliasExiste(nuevoUsuario.getAlias())) {
            return false;
        }

        usuarios.add(nuevoUsuario);
        return true;
    }

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
