package utnfc.isi.backend;

import java.util.Scanner;

public class PruebaPunto
{

    public static void main(String[] args)
    {
        Scanner miEscaner = new Scanner(System.in);
        //Punto p1;
        
        System.out.println("Prueba clase Punto");
        System.out.print("Ingrese x: ");
        double valX = miEscaner.nextDouble();
        System.out.print("Ingrese y: ");
        double valY = miEscaner.nextDouble();
        Punto p1 = new Punto(valX, valY);
        System.out.println("El punto cargado es: " + p1);
        System.out.println("\tSu distancia al origen: " + p1.distanciaOrigen());
        System.out.println("\tEstá en el eje de las X: " + p1.estaEjeX());
        System.out.println("\tEstá en el eje de las Y: " + p1.estaEjeY());
        System.out.println("\tEstá en el origen: " + p1.estaOrigen());
        
        System.out.println("Fin!");
        
    }
    
}
