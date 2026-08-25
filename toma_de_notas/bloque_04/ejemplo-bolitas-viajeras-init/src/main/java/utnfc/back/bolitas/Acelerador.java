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
public class Acelerador extends Tramo
{

    public Acelerador(String nombre)
    {
        super(nombre);
    }
    
    public Tramo avanzar(Bolita b)
    {
        b.setTramoActual(this);
	
        b.parar(10);
        b.acelerar(3);

        return salida;
    }
    
}
