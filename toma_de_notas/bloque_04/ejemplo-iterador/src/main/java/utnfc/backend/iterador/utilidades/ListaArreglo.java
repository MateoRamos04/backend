package utnfc.backend.iterador.utilidades;

public class ListaArreglo //implements Iterable
{
    private Object[] items;
    private int cantidad;

    public ListaArreglo(int pTam)
    {
        cantidad = 0;
        items = new Object[pTam];
    }

    public ListaArreglo()
    {
        this(10);
    }

    public Object getItem(int idx)
    {
        if (idx >= 0 && idx < cantidad)
            return items[idx];
        throw new IndexOutOfBoundsException("Indice fuera de rango: " + idx); // genero una excepcion si el indice es invalido.
    }

    public void setItem(Object pDato, int idx)
    {
        if (idx < 0 || idx >= cantidad)
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + idx); //
        items[idx] = pDato;
    }

    public int getCantidad()
    {
        return cantidad;
    }

    public void agregar(Object pDato)
    {
        if (cantidad == items.length)
        {
            int nuevaCapacidad = items.length + Math.max(1, items.length / 2);
            Object[] aux = new Object[nuevaCapacidad];
            System.err.println("Se agrando el vector a: " + aux.length);
            for (int i = 0; i < items.length; i++)
                aux[i] = items[i];
            items = aux;
        } // esta condicion es para agrandar el arreglo si ya no hay espacio.
        items[cantidad] = pDato;
        cantidad++;
    }

    public void quitar(int idx)
    {
        System.out.print("Haganlo uds.");
    }

    public void limpiar()
    {
        System.out.print("Haganlo uds.");
    }

    @Override
    public String toString()
    {
        StringBuilder buff = new StringBuilder("ListaArreglo {"); // creo un StringBuilder para ir armando el string de salida.
        for (int i = 0; i < items.length; i++) // recorro el arreglo de items y voy agregando cada item al StringBuilder.
        {
            buff.append(items[i]);
            if (i < items.length - 1)
                buff.append(", ");
        }
        return buff.append('}').toString(); // devuelvo el string armado.
    } 

    // @Override
    // public Iterator iterator()
    // {
    //     return new IteradorLineal();
    // }

    // private class IteradorLineal implements Iterator
    // {
    //     private int actual;

    //     @Override
    //     public boolean hasNext()
    //     {
    //         return actual < cantidad;
    //     }

    //     @Override
    //     public Object next()
    //     {
    //         if (!hasNext())
    //             throw new NoSuchElementException();
    //         Object resp = getItem(actual);
    //         actual++;
    //         return resp;
    //     }
    // }
}
