package clases.ejemplo6;
/*
 * Ejemplo simple de programa en Java. Muestra la forma elemental de permitir entradas desde
 * teclado en Java.
 *
 */
import java.io.*;
import java.util.Scanner;

public class App
{
    public static void main(String[] args) throws IOException
    {
        /*
         * 1.) Java provee muchas clases predefinidas que permiten obtener
         * entradas de valores de usuarios y la visualización o impresión de los
         * resultados generados. Sin embargo, por razones de mantener
         * compatibilidad entre plataformas, Java no provee métodos directos o
         * simples para facilitar la carga desde teclado en una aplicación. Las
         * clases y métodos existentes requieren algo de trabajo para lograr la
         * carga por teclado de valores que no sean estrictamente de tipo char.
         *
         * 2.) Así como la visualización por consola se puede lograr a partir
         * del objeto "out" definido en la clase System, la misma clase System
         * incluye un objeto "in" que puede usarse para acceder a métodos de
         * carga básicos.
         *
         * 3.) En Java, el uso de cualquier método que permita carga desde
         * dispositivos externos puede provocar situaciones de error (designados
         * como "excepciones" en Java), que el lenguaje obliga a tener en
         * consideración, ya sea manejando adecuadamente esas excepciones (lo
         * que oportunamente veremos) o simplemente indicando que la aplicación
         * puede llegar a disparar una excepción. Esto último es lo que hicimos
         * con la expresión "throws Exception" al declarar la cabecera del
         * método main.
         *
         * 4.) Finalmente, como expusimos en el material a partir de la versión 7
         * Java agrega la clase java.util.Scanner para simplificar el acceso a los flujos
         * incluido System.in y es lo que vamos a usar en primera instancia.
         */

        // Caso 1: carga de un caracter simple
        char entrada;
        System.out.print("Ingrese un caracter: ");
        entrada = (char) System.in.read(); // el método read() retorna el valor
        // int UNICODE de la tecla...
        System.in.read(); // eliminamos del buffer de teclado el caracter
        // <Enter>...
        System.out.println("Usted ingresó el caracter: " + entrada);

        // Caso 2: elegir una opción entre varias
        char opcion;
        do
        {
            System.in.read(); // limpiar el buffer de teclado en cada vuelta del
            // ciclo
            System.out
                    .println("Opciones disponibles: 'a', 'b', 'c' - Y con 'x' termina");
            System.out.print("Ingrese la opción elegida: ");
            opcion = (char) System.in.read();
            System.in.read(); // eliminamos del buffer de teclado el caracter
            // <Enter>...
            if (opcion != 'x')
            {
                if (opcion == 'a' || opcion == 'b' || opcion == 'c')
                {
                    System.out.println("Correcto...");
                }
                else
                {
                    System.out.println("Incorrecto...");
                }
            }
        } while (opcion != 'x');
        System.in.read(); // eliminamos del buffer de teclado el último caracter
        // <Enter>...

        // Un ejemplo de uso de la clase Scanner que mencionamos en el material simplifica
        // lo anterior
        System.out.println("Ejemplos de uso de la clase Scanner");

        // Declaración de la referencia
        Scanner miEscaner;
        // Creación de la instancia referida a System.in
        miEscaner = new Scanner(System.in);

        int x;
        System.out.print("Cargue un valor entero: ");
        x = miEscaner.nextInt();

        float y;
        System.out.print("Cargue un valor flotante: ");
        y = miEscaner.nextFloat();

        String z;
        System.out.print("Cargue una cadena: ");
        z = miEscaner.next();

        System.out.println("X: " + x + " Y: " + y + " Z: " + z);

        System.out.println("Terminado...");
    }
}

