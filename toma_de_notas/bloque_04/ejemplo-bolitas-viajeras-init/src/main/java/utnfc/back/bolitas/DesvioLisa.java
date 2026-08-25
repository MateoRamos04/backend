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
public class DesvioLisa extends Desvio
{

    public DesvioLisa(String nombre)
    {
        super(nombre);
    }

    @Override
    public Tramo avanzar(Bolita b)
    {
        Tramo proximo = salida;
	    b.setTramoActual(this);

        if (isBienConfigurado())
        {
            if (!b.isLisa())
            {
                proximo = alternativa;
            }
        }
        else
        {
            proximo = this;
        }
        
        return proximo;
	
    }
    
    
    
}
