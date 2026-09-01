package utnfc.backend.util.generics;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ListaArrayIterable<E>")
class ListaArrayIterableTest {

    private ListaArrayIterable<String> lista;

    @BeforeEach
    void setUp() {
        lista = new ListaArrayIterable<>();
    }

    // ============================================================
    // 1. CONSTRUCCIÓN
    // ============================================================

    @Nested
    @DisplayName("1. Construcción de la lista")
    class Construccion {

        @Test
        @DisplayName("Una lista nueva comienza vacía")
        void listaNuevaComienzaVacia() {
            assertAll(
                    () -> assertEquals(0, lista.size()),
                    () -> assertTrue(lista.isEmpty()),
                    () -> assertEquals("{}", lista.toString())
            );
        }

        @Test
        @DisplayName("El constructor con capacidad positiva crea una lista utilizable")
        void constructorConCapacidadPositiva() {
            ListaArrayIterable<String> otra = new ListaArrayIterable<>(3);

            otra.agregar("Java");

            assertAll(
                    () -> assertEquals(1, otra.size()),
                    () -> assertEquals("Java", otra.get(0))
            );
        }

        @Test
        @DisplayName("Una capacidad menor o igual a cero utiliza la capacidad por defecto")
        void capacidadInvalidaUsaCapacidadPorDefecto() {
            ListaArrayIterable<String> otra = new ListaArrayIterable<>(0);

            for (int i = 0; i < 10; i++) {
                otra.agregar("Elemento " + i);
            }

            assertEquals(10, otra.size());
        }
    }

    // ============================================================
    // 2. AGREGAR
    // ============================================================

    @Nested
    @DisplayName("2. Agregar elementos")
    class Agregar {

        @Test
        @DisplayName("Agregar incorpora el elemento al final")
        void agregarElemento() {
            assertTrue(lista.agregar("Java"));

            assertAll(
                    () -> assertEquals(1, lista.size()),
                    () -> assertEquals("Java", lista.get(0))
            );
        }

        @Test
        @DisplayName("Agregar varios elementos conserva el orden")
        void agregarVariosMantieneOrden() {
            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            assertAll(
                    () -> assertEquals("Java", lista.get(0)),
                    () -> assertEquals("Spring", lista.get(1)),
                    () -> assertEquals("Maven", lista.get(2))
            );
        }

        @Test
        @DisplayName("Agregar null es rechazado")
        void agregarNullEsRechazado() {
            assertFalse(lista.agregar(null));
            assertEquals(0, lista.size());
        }

        @Test
        @DisplayName("La lista crece cuando supera su capacidad inicial")
        void crecimientoAutomatico() {
            ListaArrayIterable<String> pequeña = new ListaArrayIterable<>(2);

            pequeña.agregar("Java");
            pequeña.agregar("Spring");
            pequeña.agregar("Maven");

            assertAll(
                    () -> assertEquals(3, pequeña.size()),
                    () -> assertEquals("Java", pequeña.get(0)),
                    () -> assertEquals("Spring", pequeña.get(1)),
                    () -> assertEquals("Maven", pequeña.get(2))
            );
        }
    }

    // ============================================================
    // 3. INSERTAR
    // ============================================================

    @Nested
    @DisplayName("3. Insertar elementos")
    class Insertar {

        @Test
        @DisplayName("Insertar al comienzo desplaza los elementos")
        void insertarAlComienzo() {
            lista.agregar("Spring");
            lista.agregar("Maven");

            lista.insertar(0, "Java");

            assertAll(
                    () -> assertEquals(3, lista.size()),
                    () -> assertEquals("Java", lista.get(0)),
                    () -> assertEquals("Spring", lista.get(1)),
                    () -> assertEquals("Maven", lista.get(2))
            );
        }

        @Test
        @DisplayName("Insertar en size agrega al final")
        void insertarAlFinal() {
            lista.agregar("Java");

            lista.insertar(lista.size(), "Spring");

            assertEquals("Spring", lista.get(1));
        }

        @Test
        @DisplayName("Insertar con índice inválido lanza IndexOutOfBoundsException")
        void insertarIndiceInvalido() {
            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.insertar(1, "Java")
            );
        }
    }

    // ============================================================
    // 4. GET Y SET
    // ============================================================

    @Nested
    @DisplayName("4. Acceso y reemplazo")
    class Acceso {

        @Test
        @DisplayName("get devuelve directamente el tipo parametrizado")
        void getDevuelveTipoParametrizado() {
            lista.agregar("Java");

            String valor = lista.get(0);

            assertEquals("Java", valor);
        }

        @Test
        @DisplayName("set reemplaza y devuelve el elemento anterior")
        void setReemplazaElemento() {
            lista.agregar("Java");

            String anterior = lista.set(0, "Spring");

            assertAll(
                    () -> assertEquals("Java", anterior),
                    () -> assertEquals("Spring", lista.get(0))
            );
        }

        @Test
        @DisplayName("get con índice inválido lanza IndexOutOfBoundsException")
        void getIndiceInvalido() {
            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.get(0)
            );
        }
    }

    // ============================================================
    // 5. QUITAR
    // ============================================================

    @Nested
    @DisplayName("5. Quitar elementos")
    class Quitar {

        @Test
        @DisplayName("Quitar devuelve el elemento eliminado y desplaza los restantes")
        void quitarElemento() {
            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            String eliminado = lista.quitar(1);

            assertAll(
                    () -> assertEquals("Spring", eliminado),
                    () -> assertEquals(2, lista.size()),
                    () -> assertEquals("Java", lista.get(0)),
                    () -> assertEquals("Maven", lista.get(1))
            );
        }
    }

    // ============================================================
    // 6. CONTAINS
    // ============================================================

    @Nested
    @DisplayName("6. Buscar elementos")
    class Contains {

        @Test
        @DisplayName("contains encuentra un elemento equivalente")
        void containsEncuentraElemento() {
            lista.agregar(new String("Java"));

            assertTrue(lista.contains(new String("Java")));
        }

        @Test
        @DisplayName("contains devuelve false para null")
        void containsNull() {
            assertFalse(lista.contains(null));
        }
    }

    // ============================================================
    // 7. CLEAR
    // ============================================================

    @Nested
    @DisplayName("7. Vaciar la lista")
    class Clear {

        @Test
        @DisplayName("clear elimina todos los elementos")
        void clearVaciaLista() {
            lista.agregar("Java");
            lista.agregar("Spring");

            lista.clear();

            assertAll(
                    () -> assertEquals(0, lista.size()),
                    () -> assertTrue(lista.isEmpty()),
                    () -> assertEquals("{}", lista.toString())
            );
        }
    }

    // ============================================================
    // 8. ITERABLE
    // ============================================================

    @Nested
    @DisplayName("8. Implementación de Iterable<E>")
    class IterableTests {

        @Test
        @DisplayName("iterator devuelve un Iterator<E>")
        void iteratorDevuelveIterator() {
            Iterator<String> iterator = lista.iterator();

            assertNotNull(iterator);
        }

        @Test
        @DisplayName("Cada invocación a iterator crea un iterador independiente")
        void iteradoresIndependientes() {
            lista.agregar("Java");
            lista.agregar("Spring");

            Iterator<String> it1 = lista.iterator();
            Iterator<String> it2 = lista.iterator();

            assertEquals("Java", it1.next());

            assertAll(
                    () -> assertEquals("Spring", it1.next()),
                    () -> assertEquals("Java", it2.next())
            );
        }
    }

    // ============================================================
    // 9. ITERATOR - hasNext()
    // ============================================================

    @Nested
    @DisplayName("9. Iterator<E> - hasNext()")
    class HasNext {

        @Test
        @DisplayName("hasNext devuelve false en una lista vacía")
        void hasNextListaVacia() {
            Iterator<String> iterator = lista.iterator();

            assertFalse(iterator.hasNext());
        }

        @Test
        @DisplayName("hasNext devuelve true si queda un elemento por recorrer")
        void hasNextConElementos() {
            lista.agregar("Java");

            Iterator<String> iterator = lista.iterator();

            assertTrue(iterator.hasNext());
        }

        @Test
        @DisplayName("Después de consumir el último elemento hasNext devuelve false")
        void hasNextLuegoDelUltimo() {
            lista.agregar("Java");

            Iterator<String> iterator = lista.iterator();

            iterator.next();

            assertFalse(iterator.hasNext());
        }
    }

    // ============================================================
    // 10. ITERATOR - next()
    // ============================================================

    @Nested
    @DisplayName("10. Iterator<E> - next()")
    class Next {

        @Test
        @DisplayName("next devuelve el primer elemento y avanza")
        void nextDevuelvePrimerElemento() {
            lista.agregar("Java");
            lista.agregar("Spring");

            Iterator<String> iterator = lista.iterator();

            assertAll(
                    () -> assertEquals("Java", iterator.next()),
                    () -> assertEquals("Spring", iterator.next())
            );
        }

        @Test
        @DisplayName("next permite recorrer también el último elemento")
        void nextRecorreUltimoElemento() {
            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            Iterator<String> iterator = lista.iterator();

            assertEquals("Java", iterator.next());
            assertEquals("Spring", iterator.next());
            assertEquals("Maven", iterator.next());

            assertFalse(iterator.hasNext());
        }

        @Test
        @DisplayName("next sobre una lista vacía lanza NoSuchElementException")
        void nextListaVacia() {
            Iterator<String> iterator = lista.iterator();

            assertThrows(
                    NoSuchElementException.class,
                    iterator::next
            );
        }

        @Test
        @DisplayName("next luego de consumir todos los elementos lanza NoSuchElementException")
        void nextLuegoDelFinal() {
            lista.agregar("Java");

            Iterator<String> iterator = lista.iterator();

            iterator.next();

            assertThrows(
                    NoSuchElementException.class,
                    iterator::next
            );
        }
    }

    // ============================================================
    // 11. ITERATOR - remove()
    // ============================================================

    @Nested
    @DisplayName("11. Iterator<E> - remove()")
    class Remove {

        @Test
        @DisplayName("remove elimina el último elemento devuelto por next")
        void removeEliminaUltimoDevuelto() {
            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            Iterator<String> iterator = lista.iterator();

            assertEquals("Java", iterator.next());

            iterator.remove();

            assertAll(
                    () -> assertEquals(2, lista.size()),
                    () -> assertEquals("Spring", lista.get(0)),
                    () -> assertEquals("Maven", lista.get(1))
            );
        }

        @Test
        @DisplayName("Después de remove el recorrido continúa correctamente")
        void recorridoContinuaDespuesDeRemove() {
            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            Iterator<String> iterator = lista.iterator();

            assertEquals("Java", iterator.next());

            iterator.remove();

            assertEquals(
                    "Spring",
                    iterator.next(),
                    "El iterador debe continuar con el elemento que quedó en la posición eliminada"
            );

            assertEquals("Maven", iterator.next());
        }

        @Test
        @DisplayName("remove antes de next lanza IllegalStateException")
        void removeAntesDeNext() {
            lista.agregar("Java");

            Iterator<String> iterator = lista.iterator();

            assertThrows(
                    IllegalStateException.class,
                    iterator::remove
            );
        }

        @Test
        @DisplayName("remove no puede invocarse dos veces después de un único next")
        void removeDosVeces() {
            lista.agregar("Java");
            lista.agregar("Spring");

            Iterator<String> iterator = lista.iterator();

            iterator.next();
            iterator.remove();

            assertThrows(
                    IllegalStateException.class,
                    iterator::remove
            );
        }

        @Test
        @DisplayName("Cada nueva llamada a next habilita nuevamente remove")
        void nextHabilitaNuevamenteRemove() {
            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            Iterator<String> iterator = lista.iterator();

            assertEquals("Java", iterator.next());
            iterator.remove();

            assertEquals("Spring", iterator.next());
            iterator.remove();

            assertAll(
                    () -> assertEquals(1, lista.size()),
                    () -> assertEquals("Maven", lista.get(0))
            );
        }
    }

    // ============================================================
    // 12. FOR-EACH
    // ============================================================

    @Nested
    @DisplayName("12. Recorrido mediante for-each")
    class ForEach {

        @Test
        @DisplayName("La lista puede recorrerse directamente con for-each")
        void puedeRecorrerseConForEach() {
            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            String resultado = "";

            for (String elemento : lista) {
                resultado += elemento + ";";
            }

            assertEquals(
                    "Java;Spring;Maven;",
                    resultado
            );
        }

        @Test
        @DisplayName("for-each recorre todos los elementos exactamente una vez y en orden")
        void foreachRecorreTodosEnOrden() {
            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");
            lista.agregar("JUnit");

            String[] esperados = {
                    "Java",
                    "Spring",
                    "Maven",
                    "JUnit"
            };

            int indice = 0;

            for (String elemento : lista) {
                assertEquals(
                        esperados[indice],
                        elemento,
                        "El elemento recorrido debe coincidir con la posición esperada"
                );

                indice++;
            }

            assertEquals(
                    esperados.length,
                    indice,
                    "for-each debe recorrer todos los elementos"
            );
        }

        @Test
        @DisplayName("for-each sobre una lista vacía no ejecuta el cuerpo del ciclo")
        void foreachListaVacia() {
            int cantidad = 0;

            for (String elemento : lista) {
                cantidad++;
            }

            assertEquals(0, cantidad);
        }

        @Test
        @DisplayName("for-each también respeta el tipo genérico de la lista")
        void foreachRespetaTipoGenerico() {
            ListaArrayIterable<Integer> numeros = new ListaArrayIterable<>();

            numeros.agregar(10);
            numeros.agregar(20);
            numeros.agregar(30);

            int suma = 0;

            for (Integer numero : numeros) {
                suma += numero;
            }

            assertEquals(60, suma);
        }
    }

    // ============================================================
    // 13. TOSTRING
    // ============================================================

    @Nested
    @DisplayName("13. Representación textual")
    class ToString {

        @Test
        @DisplayName("toString muestra los elementos en orden")
        void toStringMuestraContenido() {
            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            assertEquals(
                    "{Java, Spring, Maven}",
                    lista.toString()
            );
        }
    }
}
