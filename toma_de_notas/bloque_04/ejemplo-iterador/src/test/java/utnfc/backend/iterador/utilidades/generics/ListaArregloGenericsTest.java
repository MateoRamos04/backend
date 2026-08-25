package utnfc.backend.iterador.utilidades.generics;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListaArreglo con Generics")
class ListaArregloGenericsTest
{
    @Test @DisplayName("ambos constructores crean listas vacias")
    void construccion()
    {
        assertEquals(0, new ListaArreglo<String>().getCantidad());
        assertEquals(0, new ListaArreglo<String>(2).getCantidad());
    }

    @Test @DisplayName("agregar y getItem conservan tipo, cantidad y orden")
    void agregarYRecuperar()
    {
        ListaArreglo<String> lista = listaConTresElementos();
        String intermedio = lista.getItem(1);
        assertEquals(3, lista.getCantidad());
        assertEquals("dos", intermedio);
    }

    @Test @DisplayName("getItem devuelve null para indices invalidos")
    void indicesInvalidos()
    {
        ListaArreglo<String> lista = listaConTresElementos();
        assertAll(
                () -> assertNull(lista.getItem(-1)),
                () -> assertNull(lista.getItem(3)),
                () -> assertNull(lista.getItem(8)));
    }

    @Test @DisplayName("setItem reemplaza sin cambiar cantidad")
    void setItem()
    {
        ListaArreglo<String> lista = listaConTresElementos();
        lista.setItem("nuevo", 1);
        lista.setItem("ignorado", -1);
        lista.setItem("ignorado", 3);
        assertEquals("nuevo", lista.getItem(1));
        assertEquals(3, lista.getCantidad());
    }

    @Test @DisplayName("crece varias veces sin perder tipo, elementos ni orden")
    void crecimiento()
    {
        ListaArreglo<Integer> lista = new ListaArreglo<>(1);
        for (int i = 0; i < 8; i++)
            lista.agregar(i);
        assertEquals(8, lista.getCantidad());
        for (int i = 0; i < 8; i++)
            assertEquals(i, lista.getItem(i));
    }

    @Test @DisplayName("crece desde capacidad cero")
    void crecimientoDesdeCero()
    {
        ListaArreglo<String> lista = new ListaArreglo<>(0);
        lista.agregar("uno");
        assertEquals("uno", lista.getItem(0));
    }

    @Test @DisplayName("Iterator<T> recorre en orden sin casts")
    void iteratorGenerico()
    {
        Iterator<String> it = listaConTresElementos().iterator();
        assertEquals("uno", it.next());
        assertEquals("dos", it.next());
        assertEquals("tres", it.next());
        assertFalse(it.hasNext());
    }

    @Test @DisplayName("next agotado lanza NoSuchElementException")
    void nextAgotado()
    {
        Iterator<String> it = new ListaArreglo<String>().iterator();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test @DisplayName("dos iteradores genericos son independientes")
    void iteradoresIndependientes()
    {
        ListaArreglo<String> lista = listaConTresElementos();
        Iterator<String> primero = lista.iterator();
        Iterator<String> segundo = lista.iterator();
        primero.next();
        primero.next();
        assertEquals("uno", segundo.next());
    }

    @Test @DisplayName("foreach recibe directamente el tipo declarado")
    void foreachGenerico()
    {
        StringBuilder resultado = new StringBuilder();
        for (String item : listaConTresElementos())
            resultado.append(item).append(' ');
        assertEquals("uno dos tres ", resultado.toString());
    }

    private ListaArreglo<String> listaConTresElementos()
    {
        ListaArreglo<String> lista = new ListaArreglo<>(3);
        lista.agregar("uno");
        lista.agregar("dos");
        lista.agregar("tres");
        return lista;
    }
}
