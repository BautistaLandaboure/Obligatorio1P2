package obligatorio1;

import java.util.ArrayList;
import java.util.Iterator;

public class ManejoRegistro {
    
    // Lista para almacenar los usuarios, es de tipo RegistroUsuarios y se llama usuarios
    private ArrayList<RegistroUsuarios> usuarios;

    // Constructor para la lista de usuarios, es necesario inicalizar a la lista usuarios, si no, no va a dejar agregarle nada
    public ManejoRegistro() {
        usuarios = new ArrayList<>();
    }

    // Método para verificar si el alias ya existe 
   public boolean aliasExiste(String alias) {
       
    Iterator<RegistroUsuarios> iterator = usuarios.iterator(); // Obtener un iterator para la lista de usuarios
    
    while (iterator.hasNext()) { 
        RegistroUsuarios usuario = iterator.next(); 
        
        if (usuario.getAlias().equals(alias)) {
            return true; 
        }
    }
    return false; 
}

    // Método para registrar un nuevo usuario
    public boolean registrarUsuario(String nombre, int edad, String alias) {
        if (aliasExiste(alias)) {
            
            System.out.println("El alias ya está en uso.");
            
            return false;
        }
        RegistroUsuarios nuevoUsuario = new RegistroUsuarios(nombre, edad, alias);
        
        usuarios.add(nuevoUsuario);
        
        System.out.println("Usuario registrado: " + nuevoUsuario);
        
        return true;
    }  
}
