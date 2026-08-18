package utnfc.isi.backend;

import java.util.Scanner;


public class PruebaVector
{
    public static void main(String[] args)
    {
        Scanner miEscaner = new Scanner(System.in);
        System.out.println("Calculador de distancias");
        
        //Declaro las referencias a los objetos
        Punto origen, destino;
        Vector varVector;
        double auxX, auxY;
        
        //Contruyo y cargo los valores del punto origen
        System.out.println("Ingrese el punto de origen");
        System.out.print("Ingrese x: ");
        auxX = miEscaner.nextDouble();
        System.out.print("Ingrese y: ");
        auxY = miEscaner.nextDouble();
        origen = new Punto(auxX, auxY);
        
        //Contruyo y cargo los valores del punto destino
        System.out.println("Ingrese el punto de destino");
        System.out.print("Ingrese x: ");
        auxX = miEscaner.nextDouble();
        System.out.print("Ingrese y: ");
        auxY = miEscaner.nextDouble();
        destino = new Punto(auxX, auxY);
        
        //Construyo el vector en base a los puntos cargados
        varVector = new Vector(origen, destino);
        
        System.out.println("El vector cargado es: " + varVector);
        double resultadoDistancia = varVector.modulo();
        System.out.println("\tSu distancia es: " + resultadoDistancia);
        System.out.println("\tEs paralelo al eje x: " + varVector.paraleloEjeX());
        System.out.println("\tEs paralelo al eje y: " + varVector.paraleloEjeY());
        System.out.println("Fin!");
        
    }
    
}
