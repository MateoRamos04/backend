package utnfc.backend.iterador;

import utnfc.backend.util.ListaArrayMejorada;
import utnfc.backend.util.ListaArrayMejorada.IteradorLineal;

public class App {
    public static void main(String[] args) {
        ListaArrayMejorada l = new ListaArrayMejorada();
        l.agregar("Elemento 1");
        l.agregar("Elemento 2");
        l.agregar("Elemento 3");
        l.agregar("Perro");
        l.agregar("WestRam FC");

        System.out.println("Lista: " + l);

        // Recorrer la lista utilizando el iterador
        IteradorLineal it1 = l.iterador();
        IteradorLineal it2 = l.iterador();

        it1.siguiente();
        it1.siguiente();
        System.out.println("Iterador 1 - Elemento actual: " + it1.getActual());

        it2.siguiente();
        it2.siguiente();
        it2.siguiente();
        System.out.println("Iterador 2 - Elemento actual: " + it2.getActual());
    }
}


