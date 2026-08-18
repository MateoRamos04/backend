package utnfc.isi.backend;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main(String[] args)
    {
        // Primero con números enteros
        System.out.println("Primero con enteros");
        int acumuladorI = 0;
        for(int i = 0; i < 10; i++)
        {
            int aux = (int) (Math.random() * 10);
            System.out.println("Generado: " + aux);
            acumuladorI += aux; // acumuladorI = acumuladorI + aux;
        }

        double mediaI = acumuladorI / 10.0;
        System.out.println("\n\nLa media es: " + mediaI);

        System.out.println();

        // Ahora con Fracciones
        System.out.println("Ahora con fracciones");
        Fraccion acumuladorF = new Fraccion(0);
        for(int i = 0; i < 10; i++)
        {
//            int num = (int)(Math.random() * 7 + 1);
//            int den = (int)(Math.random() * 8 + 2);
//            Fraccion aux = new Fraccion(num, den);
            Fraccion aux = new Fraccion((int)(Math.random() * 7 + 1), (int)(Math.random() * 9 + 1));

            System.out.println("Generado: " + aux);
            acumuladorF = acumuladorF.sumarA(aux);
        }

        Fraccion mediaF = acumuladorF.dividirPor(10);
        System.out.println("\n\nLa media es: " + mediaF);

    }

}

