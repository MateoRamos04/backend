import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class App {
    public static void main(String[] args) throws FileNotFoundException{
        /*// declaramos e inicializamos variables
        int a, b;
        a = 3;
        b = a + 1;
        // mostramos el resultado por pantalla
        System.out.println(b);

        Scanner miEscaner = new Scanner(System.in);
        int c, d;

        System.out.print("Ingrese el valor de a: "); 
        c = miEscaner.nextInt(); 
        System.out.print("Ingrese el valor de b: "); 
        d = miEscaner.nextInt();

        System.out.println("El valor de la suma es:" + (c + d));
        

        System.out.print("Ingrese su edad: ");
        int edad = miEscaner.nextInt(); 
        String mensaje; 
        if (edad >= 18) { 
                mensaje = "Mayor de edad"; 
        } 
        else { 
                mensaje = "Menor de edad"; 
        } 
        System.out.println(mensaje);
        

        for (int i = 0 ; i < 10; i++) {
            System.out.println(i);
        }
        

        System.out.print('{'); 
        for (int i = 1; i <= 5; i++) { 
                    System.out.print(i); 
        if (i < 5) 
                System.out.print(','); 
        } 
        System.out.println('}');


        int cont = 0;
        while (cont < 100000) { 
            cont++; 
            System.out.println("Vuelta número: " + cont); 
        } */

        File f = new File("datos.txt");
        Scanner miScanner = new Scanner(f);
        while (miScanner.hasNextLine()) {
            System.out.println(miScanner.nextInt());
        } // fin del while
    } // fin del main
} // fin del class
