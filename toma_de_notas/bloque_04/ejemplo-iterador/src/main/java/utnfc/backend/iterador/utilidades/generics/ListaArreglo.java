package utnfc.backend.iterador.utilidades.generics;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaArreglo<T> implements Iterable<T>
{
    private T[] items;
    private int cantidad;

    @SuppressWarnings("unchecked")
    public ListaArreglo(int pTam)
    {
        cantidad = 0;
        items = (T[]) new Object[pTam];
    }

    public ListaArreglo()
    {
        this(10);
    }

    public T getItem(int idx)
    {
        if (idx >= 0 && idx < cantidad)
            return items[idx];
        return null;
    }

    public void setItem(T pDato, int idx)
    {
        if (idx >= 0 && idx < cantidad)
            items[idx] = pDato;
    }

    public int getCantidad()
    {
        return cantidad;
    }

    @SuppressWarnings("unchecked")
    public void agregar(T pDato)
    {
        if (cantidad == items.length)
        {
            int nuevaCapacidad = items.length + Math.max(1, items.length / 2);
            T[] aux = (T[]) new Object[nuevaCapacidad];
            for (int i = 0; i < items.length; i++)
                aux[i] = items[i];
            items = aux;
        }
        items[cantidad] = pDato;
        cantidad++;
    }

    public void quitar(int idx)
    {
        throw new UnsupportedOperationException("Haganlo uds.");
    }

    public void limpiar()
    {
        throw new UnsupportedOperationException("Haganlo uds.");
    }

    @Override
    public String toString()
    {
        StringBuilder buff = new StringBuilder("{");
        for (int i = 0; i < items.length; i++)
        {
            buff.append(items[i]);
            if (i < items.length - 1)
                buff.append(", ");
        }
        return buff.append('}').toString();
    }

    @Override
    public Iterator<T> iterator()
    {
        return new IteradorLineal();
    }

    private class IteradorLineal implements Iterator<T>
    {
        private int actual;

        @Override
        public boolean hasNext()
        {
            return actual < cantidad;
        }

        @Override
        public T next()
        {
            if (!hasNext())
                throw new NoSuchElementException();
            T resp = getItem(actual);
            actual++;
            return resp;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("No implementado.");
        }
    }
}
