package utnfc.backend.iterador;

import utnfc.backend.iterador.utilidades.ListaArreglo;

/** Demostracion de los recorridos sin Generics y de los casts necesarios. */
public class Programa
{
    public static void main(String[] args)
    {
        System.out.println("\nFracciones generadas");
        ListaArreglo lista = new ListaArreglo();
        Fraccion acumulador = new Fraccion(0);
        for (int i = 0; i < 20; i++)
        {
            int num = (int) (Math.random() * 7 + 1);
            int den = (int) (Math.random() * 8 + 2);
            Fraccion fraccion = new Fraccion(num, den);
            System.out.println("Generando: " + fraccion);
            acumulador = acumulador.sumarA(fraccion);
            lista.agregar(fraccion);
        }

        Fraccion media = acumulador.dividirPor(lista.getCantidad());
        System.out.println("\nLa media es: " + media);

        int contador = 0;
        System.out.println("Mayores: con for por indice");
        for (int i = 0; i < lista.getCantidad(); i++)
        {
            Fraccion aux = (Fraccion) lista.getItem(i);
            if (aux.compareTo(media) > 0)
            {
                contador++;
                System.out.println(aux);
            }
        }
        System.out.println("Contador: " + contador);

        contador = 0;




        
        // Iterator it = lista.iterator();
        // System.out.println("Mayores: con Iterator");
        // while (it.hasNext())
        // {
        //     Fraccion aux = (Fraccion) it.next();
        //     if (aux.compareTo(media) > 0)
        //     {
        //         contador++;
        //         System.out.println(aux);
        //     }
        // }
        // System.out.println("Contador: " + contador);

        // contador = 0;
        // System.out.println("Mayores: con foreach");
        // for (Object item : lista)
        // {
        //     Fraccion aux = (Fraccion) item;
        //     if (aux.compareTo(media) > 0)
        //     {
        //         contador++;
        //         System.out.println(aux);
        //     }
        // }
        // System.out.println("Fracciones mayores que el promedio: " + contador);
    }
}
