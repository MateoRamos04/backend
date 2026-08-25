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
public class DesvioPeso extends Desvio
{
    private int pesoMinimo;

    public DesvioPeso(int peso, String nombre)
    {
        super(nombre);
        this.pesoMinimo = peso;
    }
    
    public int getPeso()
    {
        return pesoMinimo;
    }

    public void setPeso(int peso)
    {
        this.pesoMinimo = peso;
    }

    @Override
    public Tramo avanzar(Bolita b)
    {
        Tramo proximo = salida;
	    b.setTramoActual(this);

        if (isBienConfigurado())
        {
            if (b.getPeso() > pesoMinimo)
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
