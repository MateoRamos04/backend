package ar.edu.utn.frc.back;

import java.util.Iterator;

public class ListaEnArray<E> implements Iterable<E> {

    private int size;
    private E[] v;

    public ListaEnArray() {
        size = 0;
        v = (E[]) new Object[10];
    }

    public void add(E nro) {
        if (size == v.length) {
            enlargeCapacity();
        }
        v[size] = nro;
        size++;
    }

    private void enlargeCapacity() {
        int newSize = (int) (v.length * 1.5);
        E[] aux = (E[]) new Object[newSize];
        System.arraycopy(v, 0, aux, 0, v.length);
        v = aux;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public E get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Indice inválido");
        }
        return v[i];
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(v[i]);
            sb.append(", ");
        }
        sb.append("]");
        return sb.toString();

    }

    @Override
    public Iterator<E> iterator() {
        return new IteradorListaArray<>();
    }

    private class IteradorListaArray<E> implements Iterator<E> {
        private int actual;

        public boolean hasNext() {
            return actual < size;
        }

        public E next() {
            E res = (E) v[actual];
            actual++;
            return res;
        }
    }
}
