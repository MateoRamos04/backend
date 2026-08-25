package utnfc.backend.iterador;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utnfc.backend.iterador.utilidades.generics.ListaArreglo;

@DisplayName("Fraccion almacenada con Generics")
class ListaArregloGenericsConFraccionTest
{
    @Test @DisplayName("getItem devuelve Fraccion directamente")
    void recuperarSinCast()
    {
        Fraccion fraccion = listaDeFracciones().getItem(0);
        assertEquals(0.5, fraccion.valorReal(), 0.0001);
    }

    @Test @DisplayName("Iterator<Fraccion> recorre sin cast")
    void iteratorSinCast()
    {
        Iterator<Fraccion> it = listaDeFracciones().iterator();
        Fraccion suma = new Fraccion(0);
        while (it.hasNext())
            suma = suma.sumarA(it.next());
        assertEquals(3.0, suma.valorReal(), 0.0001);
    }

    @Test @DisplayName("foreach recibe Fraccion sin cast")
    void foreachSinCast()
    {
        int cantidad = 0;
        for (Fraccion fraccion : listaDeFracciones())
            if (fraccion.valorReal() > 0)
                cantidad++;
        assertEquals(3, cantidad);
    }

    @Test @DisplayName("calcula suma, promedio y cantidad de mayores")
    void escenarioCompletoDeterminista()
    {
        ListaArreglo<Fraccion> lista = listaDeFracciones();
        Fraccion suma = new Fraccion(0);
        for (Fraccion fraccion : lista)
            suma = suma.sumarA(fraccion);
        Fraccion promedio = suma.dividirPor(lista.getCantidad());

        int mayores = 0;
        for (Fraccion fraccion : lista)
            if (fraccion.compareTo(promedio) > 0)
                mayores++;

        assertEquals(1.0, promedio.valorReal(), 0.0001);
        assertEquals(1, mayores);
    }

    private ListaArreglo<Fraccion> listaDeFracciones()
    {
        ListaArreglo<Fraccion> lista = new ListaArreglo<>();
        lista.agregar(new Fraccion(1, 2));
        lista.agregar(new Fraccion(1));
        lista.agregar(new Fraccion(3, 2));
        return lista;
    }
}
