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
public class Tubo extends Tramo
{
    private int longitud;

    public Tubo(int longitud, String nombre)
    {
        super(nombre);
        this.longitud = longitud;
    }

    public int getLongitud()
    {
        return longitud;
    }

    @Override
    public Tramo avanzar(Bolita b)
    {
        b.setTramoActual(this);
	
	    b.recorrer(longitud);
	
	    return salida;
    }
}
