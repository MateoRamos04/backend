/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utnfc.back.bolitas;

/**
 *
 * @author philip
 */
public abstract class Tramo
{
    private boolean log;

    public boolean isLog() {
        return log;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    private String nombre;
    protected Tramo salida; // referencia al siguiente tramo

    public Tramo(String nombre)
    {
        this.nombre = nombre;
        salida = null;
        this.log = true;
    }
    
    public void encastrarSalida(Tramo salida)
    {
        this.salida = salida;
    }

    public Tramo getSalida()
    {
        return salida;
    }
    
    public boolean isBienConfigurado()
    {
        return true;
    }

    public void transportar(Bolita b)
    { 
        Tramo proximo;
        
        proximo = avanzar(b);
        if (proximo != null && !(proximo == this))
        {
            proximo.transportar(b);
            if (log)
            {
                System.err.println(b);
            }
        }
                
    }

    public abstract Tramo avanzar(Bolita b);

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    @Override
    public String toString()
    {
        return "Tramo {" + "Clase: " + this.getClass().getName() + " | Nombre=" + nombre + '}';
    }
    
    
    
}
