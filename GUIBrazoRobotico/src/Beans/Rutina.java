/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

/**
 *
 * @author zdm
 */
public class Rutina {
    private String rutina ="222,";
    private String descripcion;
    
    public Rutina(){ //constructor 
        
        
    }

    public String getRutina() { // Método para obtener datos de la rutina
        return rutina;
    }

    public void setRutina(String rutina) { // Método para definir datos en la rutina
        this.rutina += rutina;
    }


    public void setDescripcion(String descripcion) {  // Método para definir la descripción y será usada en el método toString
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {  // Método sobreescrito para que el JComboBox muestre la descripción
        return descripcion;
    }
    
    
}
