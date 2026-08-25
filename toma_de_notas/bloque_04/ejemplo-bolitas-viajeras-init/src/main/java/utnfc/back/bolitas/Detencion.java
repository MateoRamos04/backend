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
public class Detencion extends Tramo
{
    private int segundos;

    public Detencion(int segundos, String nombre)
    {
        super(nombre);
        this.segundos = segundos;
    }

    public int getSegundos()
    {
        return segundos;
    }

    public void setSegundos(int segundos)
    {
        this.segundos = segundos;
    }

    @Override
    public Tramo avanzar(Bolita b)
    {
        b.setTramoActual(this);
	
        b.parar(segundos);

        return salida;
    }
    
}
