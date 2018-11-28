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
public class Brazo { 
//atributos que definen a un brazo
    private int base;  
    private int hombro;
    private int codo;
    private int muneca;
    private int pinza;
    private String descripcion;
 //constructores sobrecargados
    public Brazo() {

    }

    public Brazo(int base, int hombro, int codo, int muneca, int pinza, String descripcion) { 
        this.base = base;
        this.hombro = hombro;
        this.codo = codo;
        this.muneca = muneca;
        this.pinza = pinza;
        this.descripcion = descripcion;
    }

    public int getBase() { // Método para obtener valor de la base
        return base;
    }

    public void setBase(int base) { // Método para definir el valor de la base
        this.base = base;
    }

    public int getHombro() {// Método para obtener valor del hombro
        return hombro;
    }

    public void setHombro(int hombro) { // Método para definir el valor del hombro
        this.hombro = hombro;
    }

    public int getCodo() { // Método para obtener valor del codo
        return codo;
    }

    public void setCodo(int codo) { // Método para definir el valor del codo
        this.codo = codo;
    }

    public int getMuneca() { // Método para obtener valor de la muñena
        return muneca;
    }

    public void setMuneca(int muneca) { // Método para definir el valor de la muñena
        this.muneca = muneca;
    }

    public int getPinza() { // Método para obtener valor de la pinza
        return pinza;
    }

    public void setPinza(int pinza) { // Método para definir el valor de la pinza
        this.pinza = pinza;
    }


    public void setDescripcion(String descripcion) { // Método para definir la descripción y será usada en el método toString
        this.descripcion = descripcion;
    }

    @Override
    public String toString() { // Método sobreescrito para que el JComboBox muestre la descripción
        return descripcion;
    }

}
