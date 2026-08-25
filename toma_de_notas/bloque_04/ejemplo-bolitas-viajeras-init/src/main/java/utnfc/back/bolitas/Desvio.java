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
public abstract class Desvio extends Tramo
{
    protected Tramo alternativa;

    public Desvio(String nombre)
    {
        super(nombre);
        alternativa = null;
    }
    
    @Override
    public boolean isBienConfigurado()
    {
        return salida != null && alternativa != null; // Verifica que ambos tramos estén configurados
    }

    public Tramo getAlternativa()
    {
        return alternativa;
    }

    public void setAlternativa(Tramo alternativa)
    {
        this.alternativa = alternativa;
    }



}
