package ar.edu.utnfc.backend;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws FileNotFoundException
    {
        File f = new File("datos.txt");
        Scanner miEscaner = new Scanner(f);

        while (miEscaner.hasNext()) {
            System.out.println(miEscaner.nextInt());
        }

    }
}
