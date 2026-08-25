package utnfc.backend.iterador.utilidades;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListaArreglo sin Generics")
class ListaArregloTest
{
    @Test @DisplayName("el constructor por defecto crea una lista vacia")
    void constructorPorDefecto()
    {
        ListaArreglo lista = new ListaArreglo();
        assertEquals(0, lista.getCantidad());
    }

    @Test @DisplayName("el constructor con capacidad crea una lista vacia")
    void constructorConCapacidad()
    {
        ListaArreglo lista = new ListaArreglo(2);
        assertEquals(0, lista.getCantidad());
    }

    @Test @DisplayName("agregar un elemento incrementa cantidad")
    void agregarUno()
    {
        ListaArreglo lista = new ListaArreglo();
        lista.agregar("uno");
        assertEquals(1, lista.getCantidad());
        assertEquals("uno", lista.getItem(0));
    }

    @Test @DisplayName("agregar varios conserva el orden")
    void agregarVariosConservaOrden()
    {
        ListaArreglo lista = listaConTresElementos();
        assertAll(
                () -> assertEquals(3, lista.getCantidad()),
                () -> assertEquals("uno", lista.getItem(0)),
                () -> assertEquals("dos", lista.getItem(1)),
                () -> assertEquals("tres", lista.getItem(2)));
    }

    @Test @DisplayName("getItem recupera primero, intermedio y ultimo")
    void getItemEnIndicesValidos()
    {
        ListaArreglo lista = listaConTresElementos();
        assertAll(
                () -> assertEquals("uno", lista.getItem(0)),
                () -> assertEquals("dos", lista.getItem(1)),
                () -> assertEquals("tres", lista.getItem(2)));
    }

    @Test @DisplayName("getItem devuelve null para indices invalidos")
    void getItemEnIndicesInvalidos()
    {
        ListaArreglo lista = listaConTresElementos();
        assertAll(
                () -> assertNull(lista.getItem(-1)),
                () -> assertNull(lista.getItem(3)),
                () -> assertNull(lista.getItem(20)));
    }

    @Test @DisplayName("setItem reemplaza sin cambiar cantidad")
    void setItemValido()
    {
        ListaArreglo lista = listaConTresElementos();
        lista.setItem("nuevo", 1);
        assertEquals("nuevo", lista.getItem(1));
        assertEquals(3, lista.getCantidad());
    }

    @Test @DisplayName("setItem ignora indices invalidos")
    void setItemInvalido()
    {
        ListaArreglo lista = listaConTresElementos();
        lista.setItem("nuevo", -1);
        lista.setItem("nuevo", 3);
        assertEquals("uno", lista.getItem(0));
        assertEquals("tres", lista.getItem(2));
        assertEquals(3, lista.getCantidad());
    }

    @Test @DisplayName("crece sin perder elementos ni orden")
    void crecimiento()
    {
        ListaArreglo lista = new ListaArreglo(2);
        for (int i = 0; i < 8; i++)
            lista.agregar(i);
        assertEquals(8, lista.getCantidad());
        for (int i = 0; i < 8; i++)
            assertEquals(i, lista.getItem(i));
    }

    @Test @DisplayName("tambien puede crecer desde capacidad cero")
    void crecimientoDesdeCero()
    {
        ListaArreglo lista = new ListaArreglo(0);
        lista.agregar("primero");
        assertEquals("primero", lista.getItem(0));
    }

    @Test @DisplayName("toString contiene los elementos")
    void toStringContieneElementos()
    {
        ListaArreglo lista = listaConTresElementos();
        assertAll(
                () -> assertTrue(lista.toString().contains("uno")),
                () -> assertTrue(lista.toString().contains("dos")),
                () -> assertTrue(lista.toString().contains("tres")));
    }

    @Test @DisplayName("iterator devuelve un Iterator vacio para una lista vacia")
    void iteradorDeListaVacia()
    {
        Iterator it = new ListaArreglo().iterator();
        assertNotNull(it);
        assertFalse(it.hasNext());
    }

    @Test @DisplayName("iterator recorre un elemento")
    void iteradorConUnElemento()
    {
        ListaArreglo lista = new ListaArreglo();
        lista.agregar("uno");
        Iterator it = lista.iterator();
        assertTrue(it.hasNext());
        assertEquals("uno", it.next());
        assertFalse(it.hasNext());
    }

    @Test @DisplayName("iterator conserva el orden y actualiza hasNext")
    void iteradorConVariosElementos()
    {
        Iterator it = listaConTresElementos().iterator();
        assertTrue(it.hasNext());
        assertEquals("uno", it.next());
        assertTrue(it.hasNext());
        assertEquals("dos", it.next());
        assertTrue(it.hasNext());
        assertEquals("tres", it.next());
        assertFalse(it.hasNext());
    }

    @Test @DisplayName("next agotado cumple el contrato de Iterator")
    void nextAgotadoLanzaExcepcion()
    {
        Iterator it = new ListaArreglo().iterator();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test @DisplayName("dos iteradores de la misma lista mantienen recorridos independientes")
    void dosIteradoresMantienenRecorridosIndependientes()
    {
        ListaArreglo lista = listaConTresElementos();
        Iterator it1 = lista.iterator();
        Iterator it2 = lista.iterator();
        assertEquals("uno", it1.next());
        assertEquals("dos", it1.next());
        assertEquals("uno", it2.next());
    }

    @Test @DisplayName("foreach recorre cantidad y no capacidad")
    void foreachRecorreElementosAlmacenados()
    {
        ListaArreglo lista = listaConTresElementos();
        StringBuilder resultado = new StringBuilder();
        int cantidad = 0;
        for (Object item : lista)
        {
            resultado.append(item).append(' ');
            cantidad++;
        }
        assertEquals(3, cantidad);
        assertEquals("uno dos tres ", resultado.toString());
    }

    private ListaArreglo listaConTresElementos()
    {
        ListaArreglo lista = new ListaArreglo(10);
        lista.agregar("uno");
        lista.agregar("dos");
        lista.agregar("tres");
        return lista;
    }
}
