package clases.ejemplo1;

/**
 * Ejemplo simple de programa en Java. Solo contiene una clase que a su
 * vez incluye al método main para lanzar la aplicación.
 * 
 */

public class App
{
   public static void main (String[] args)
   {
       /*
        *  1.) En Java, toda función es un método de alguna clase... incluido el
        *  método main que sirve como punto de entrada de la aplicación. Es
        *  común declarar una clase cuyo único objetivo sea contener al método
        *  main.
        *  
        *  2.) La cabecera de main debe escribirse como se muestra. La única variante es
        *  el nombre del parámetro (que puede llamarse de cualquier forma en lugar de "args"),
        *  y la ubicación de los corchetes junto a ese parámetro: los mismos podrían ir después
        *  del nombre del mismo. La siguiente sería una declaración válida también:
        *                    public static void main (String x[])
        */
       
       
       /*
        * Mostramos un mensaje en la consola estandar.
        * El método print es invocado desde el objeto "out" incluido en la clase
        * System, que pertenece al núcleo de clases básicas de Java. Ese núcleo de
        * clases básicas viene en un "paquete" de clases llamado "java.lang", el cual
        * es automáticamente incluido en un programa: no es necesaria ninguna instrucción
        * de inclusión ni importación de paquetes o librerías para usar las clases de ese
        * "paquete".
        */ 
           
       System.out.print("Hola Mundo");
   }
}
