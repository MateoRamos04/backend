package utnfc.backend.iterador;

import java.util.Iterator;
import utnfc.backend.iterador.utilidades.generics.ListaArreglo;

/** Demostracion equivalente con Generics y sin casts. */
public class ProgramaConGenerics
{
    public static void main(String[] args)
    {
        System.out.println("\nFracciones generadas");
        ListaArreglo<Fraccion> lista = new ListaArreglo<>();
        Fraccion acumulador = new Fraccion(0);
        for (int i = 0; i < 20; i++)
        {
            int num = (int) (Math.random() * 7 + 1);
            int den = (int) (Math.random() * 8 + 2);
            Fraccion fraccion = new Fraccion(num, den);
            System.out.println("Generando: " + fraccion);
            acumulador = acumulador.sumarA(fraccion);
            lista.agregar(fraccion);
            // lista.agregar("texto"); // No compila: String no es Fraccion.
        }

        Fraccion media = acumulador.dividirPor(lista.getCantidad());
        System.out.println("\nLa media es: " + media);

        int contador = 0;
        System.out.println("Mayores: con for por indice");
        for (int i = 0; i < lista.getCantidad(); i++)
        {
            Fraccion aux = lista.getItem(i);
            if (aux.compareTo(media) > 0)
            {
                contador++;
                System.out.println(aux);
            }
        }
        System.out.println("Contador: " + contador);

        contador = 0;
        Iterator<Fraccion> it = lista.iterator();
        System.out.println("Mayores: con Iterator<Fraccion>");
        while (it.hasNext())
        {
            Fraccion aux = it.next();
            if (aux.compareTo(media) > 0)
            {
                contador++;
                System.out.println(aux);
            }
        }
        System.out.println("Contador: " + contador);

        contador = 0;
        System.out.println("Mayores: con foreach");
        for (Fraccion item : lista)
        {
            if (item.compareTo(media) > 0)
            {
                contador++;
                System.out.println(item);
            }
        }
        System.out.println("Fracciones mayores que el promedio: " + contador);
    }
}
