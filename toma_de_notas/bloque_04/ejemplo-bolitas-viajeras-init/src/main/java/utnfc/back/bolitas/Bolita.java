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
public class Bolita
{
    private boolean lisa;
    private int longitudRecorrida;
    private int peso; 
    private double tiempoTranscurrido; 
    private double velocidad; 
    private Tramo tramoActual;

    public Bolita(boolean lisa, int peso, double velocidad)
    {
        this.lisa = lisa;
        this.peso = peso;
        this.velocidad = velocidad;
        tramoActual = null;
        longitudRecorrida = 0;
        tiempoTranscurrido = 0;
    }

    public boolean isLisa()
    {
        return lisa;
    }

    public int getLongitudRecorrida()
    {
        return longitudRecorrida;
    }

    public int getPeso()
    {
        return peso;
    }

    public double getTiempoTranscurrido()
    {
        return tiempoTranscurrido;
    }

    public double getVelocidad()
    {
        return velocidad;
    }

    public Tramo getTramoActual()
    {
        return tramoActual;
    }

    public void setTramoActual(Tramo tramoActual)
    {
        this.tramoActual = tramoActual;
    }

    @Override
    public String toString()
    {
        return "Bolita{" + "lisa=" + lisa + ", longitudRecorrida=" + longitudRecorrida + ", peso=" + peso + ", tiempoTranscurrido=" + tiempoTranscurrido + ", velocidad=" + velocidad + '}';
    }
    
    public void parar(int segundos)
    {
        tiempoTranscurrido += segundos;
    }
    
    public void acelerar(int metrosPorSegundo)
    {
        velocidad += metrosPorSegundo;
    }
    
    public void recorrer(int centimetros)
    {
        double tiempoEnRecorrer;
        tiempoEnRecorrer = centimetros / 100.0 / velocidad;
        tiempoTranscurrido += tiempoEnRecorrer;
        longitudRecorrida += centimetros;
    }
    
}
