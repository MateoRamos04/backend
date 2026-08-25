package utnfc.backend.iterador;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utnfc.backend.iterador.utilidades.ListaArreglo;

@DisplayName("Fraccion almacenada sin Generics")
class ListaArregloConFraccionTest
{
    @Test @DisplayName("se recupera por indice mediante cast")
    void recuperarPorIndiceConCast()
    {
        ListaArreglo lista = listaDeFracciones();
        Fraccion fraccion = (Fraccion) lista.getItem(0);
        assertEquals(0.5, fraccion.valorReal(), 0.0001);
    }

    @Test @DisplayName("se recorre con Iterator mediante cast")
    void recorrerConIteratorYCast()
    {
        Iterator it = listaDeFracciones().iterator();
        Fraccion suma = new Fraccion(0);
        while (it.hasNext())
            suma = suma.sumarA((Fraccion) it.next());
        assertEquals(3.0, suma.valorReal(), 0.0001);
    }

    @Test @DisplayName("se recorre con foreach mediante cast")
    void recorrerConForeachYCast()
    {
        int cantidad = 0;
        for (Object item : listaDeFracciones())
        {
            Fraccion fraccion = (Fraccion) item;
            if (fraccion.valorReal() > 0)
                cantidad++;
        }
        assertEquals(3, cantidad);
    }

    @Test @DisplayName("cuenta fracciones mayores que una referencia")
    void contarMayoresQueReferencia()
    {
        Fraccion referencia = new Fraccion(1);
        int mayores = 0;
        for (Object item : listaDeFracciones())
            if (((Fraccion) item).compareTo(referencia) > 0)
                mayores++;
        assertEquals(1, mayores);
    }

    private ListaArreglo listaDeFracciones()
    {
        ListaArreglo lista = new ListaArreglo();
        lista.agregar(new Fraccion(1, 2));
        lista.agregar(new Fraccion(1));
        lista.agregar(new Fraccion(3, 2));
        return lista;
    }
}
