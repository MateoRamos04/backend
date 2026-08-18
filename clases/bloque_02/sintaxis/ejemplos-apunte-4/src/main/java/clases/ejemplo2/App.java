package clases.ejemplo2;

/**
 * Ejemplo simple de programa en Java. El método main contiene una serie de variables
 * de tipos primitivos para mostrar el uso de los mismos.
 * 
 */

public class App
{
   public static void main (String[] args)
   {
       String nombre = "Juan";
       int edad = 24;
       
       float sueldo = 500;  // esto compila, pues 500 es int y puede ir a un float...
       float peso = 70.45f;
       
       System.out.println("Datos de la persona:");
       System.out.println("Nombre: " + nombre);
       System.out.println("Edad:   " + edad);
       System.out.println("Sueldo: " + sueldo);
       System.out.println("Peso:   " + peso);
       
       /* No obstante, cuidado con el orden en el que entran los parámetros: analicemos
        * el código comentado que sigue antes de intentar ejecutarlo, y saquemos 
        * conlusiones....
        */
       
       //*
          int v1, v2;
          v1 = 10;
          v2 = 3;
          System.out.println("Salida: " + v1 + v2);
          System.out.println(v1 + v2 + " fue la salida");
        //*/
   }
}
