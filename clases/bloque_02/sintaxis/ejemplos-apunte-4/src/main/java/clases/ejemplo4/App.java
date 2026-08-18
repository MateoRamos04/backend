package clases.ejemplo4;

/**
 * Ejemplo simple de programa en Java. Mostramos la forma de tomar parámetros en el método
 * main.
 *
 */

public class App
{
    public static void main (String[] args)
    {
        /*
         *  1.) El método main toma un parámetro general que no es otra cosa que un arreglo
         *  (vector) de cadenas. Al pedir la ejecución del método, se pueden enviar tantas
         *  cadenas (encerradas entre comillas y separadas por comas) como se quiera. La pimera
         *  cadena enviada será asignada en args[0], la segunda en args[1], y así sucesivamente.
         *
         *  2.) La longitud del vector args puede saberse consultando el atributo args.length
         *  (los arreglos son objetos en Java, y length es un atributo (o "campo") propio de
         *  los arreglos
         *
         *  3.) Si se desea convertir valores String a números int, se puede usar el método
         *  parseInt() de la clase Integer (que también está en java.lang). Veremos más adelante que
         *  el método parseInt() es un método estático, y que, por lo tanto, para invocarlo es suficiente
         *  con escribir el nombre de la clase que lo contiene, un punto, y luego el nombre del método
         *
         *  4.) Dentro de un string se pueden usar caracteres de control de la forma '\n', '\t' etc.
         *  Un caracter de control no se muestra en pantalla, sino que es interpretado como una orden
         *  para llevar a cabo alguna acción. El caracter '\n' fuerza a un salto de línea y retorno de carro,
         *  mientras que '\t' produce una sangría o tabulación
         *
         */

        if (args.length != 0)
        {
            String nombre = args[0];
            int edad      = Integer.parseInt(args[1]);
            int hijos     = Integer.parseInt(args[2]);
            System.out.println("Datos recibidos...");
            System.out.println("\nNombre: " + nombre + "\tEdad: " + edad + "\tHijos: " + hijos);
        }
        System.out.print("Terminado...");
    }
}
