package utnfc.backend.util.generics;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ListaArrayMejorada<E> - versión genérica")
class ListaArrayMejoradaTest {

    private ListaArrayMejorada<String> lista;

    @BeforeEach
    void setUp() {
        lista = new ListaArrayMejorada<>();
    }

    // ============================================================
    // 1. CONSTRUCCIÓN
    // ============================================================

    @Nested
    @DisplayName("1. Construcción de la lista")
    class Construccion {

        @Test
        @DisplayName("Una lista genérica nueva comienza vacía")
        void listaNuevaComienzaVacia() {
            assertAll(
                    () -> assertEquals(
                            0,
                            lista.size(),
                            "La cantidad inicial debe ser cero"
                    ),
                    () -> assertTrue(
                            lista.isEmpty(),
                            "Una lista recién creada debe estar vacía"
                    ),
                    () -> assertEquals(
                            "{}",
                            lista.toString(),
                            "La representación textual inicial debe ser {}"
                    )
            );
        }

        @Test
        @DisplayName("El constructor con capacidad positiva crea una lista vacía y utilizable")
        void constructorConCapacidadPositiva() {
            ListaArrayMejorada<String> otra = new ListaArrayMejorada<>(3);

            assertAll(
                    () -> assertEquals(0, otra.size()),
                    () -> assertTrue(otra.isEmpty()),
                    () -> assertTrue(otra.agregar("Java")),
                    () -> assertEquals("Java", otra.get(0))
            );
        }

        @Test
        @DisplayName("Una capacidad cero utiliza la capacidad inicial por defecto")
        void capacidadCeroUsaValorPorDefecto() {
            ListaArrayMejorada<String> otra = new ListaArrayMejorada<>(0);

            for (int i = 0; i < 10; i++) {
                assertTrue(otra.agregar("Elemento " + i));
            }

            assertEquals(
                    10,
                    otra.size(),
                    "La lista debe poder almacenar los diez elementos"
            );
        }

        @Test
        @DisplayName("Una capacidad negativa utiliza la capacidad inicial por defecto")
        void capacidadNegativaUsaValorPorDefecto() {
            ListaArrayMejorada<String> otra = new ListaArrayMejorada<>(-5);

            otra.agregar("Java");

            assertAll(
                    () -> assertEquals(1, otra.size()),
                    () -> assertEquals("Java", otra.get(0))
            );
        }
    }

    // ============================================================
    // 2. AGREGAR
    // ============================================================

    @Nested
    @DisplayName("2. Agregar elementos")
    class Agregar {

        @Test
        @DisplayName("Agregar un elemento incrementa el tamaño y conserva su valor")
        void agregarUnElemento() {
            boolean resultado = lista.agregar("Java");

            assertAll(
                    () -> assertTrue(resultado),
                    () -> assertEquals(1, lista.size()),
                    () -> assertFalse(lista.isEmpty()),
                    () -> assertEquals("Java", lista.get(0))
            );
        }

        @Test
        @DisplayName("Agregar varios elementos conserva el orden de inserción")
        void agregarVariosMantieneOrden() {
            lista.agregar("Java");
            lista.agregar("Spring Boot");
            lista.agregar("Maven");

            assertAll(
                    () -> assertEquals(3, lista.size()),
                    () -> assertEquals("Java", lista.get(0)),
                    () -> assertEquals("Spring Boot", lista.get(1)),
                    () -> assertEquals("Maven", lista.get(2))
            );
        }

        @Test
        @DisplayName("Agregar null es rechazado y no modifica la lista")
        void agregarNullEsRechazado() {
            boolean resultado = lista.agregar(null);

            assertAll(
                    () -> assertFalse(resultado),
                    () -> assertEquals(0, lista.size()),
                    () -> assertTrue(lista.isEmpty())
            );
        }

        @Test
        @DisplayName("Al superar la capacidad inicial la lista crece sin perder elementos")
        void crecimientoAutomatico() {
            ListaArrayMejorada<String> pequeña = new ListaArrayMejorada<>(2);

            pequeña.agregar("Java");
            pequeña.agregar("Spring");
            pequeña.agregar("Maven");
            pequeña.agregar("JUnit");

            assertAll(
                    () -> assertEquals(4, pequeña.size()),
                    () -> assertEquals("Java", pequeña.get(0)),
                    () -> assertEquals("Spring", pequeña.get(1)),
                    () -> assertEquals("Maven", pequeña.get(2)),
                    () -> assertEquals("JUnit", pequeña.get(3))
            );
        }
    }

    // ============================================================
    // 3. TIPADO GENÉRICO
    // ============================================================

    @Nested
    @DisplayName("3. Seguridad de tipos mediante Generics")
    class Generics {

        @Test
        @DisplayName("get devuelve directamente el tipo parametrizado sin necesidad de cast")
        void getDevuelveTipoParametrizado() {
            lista.agregar("Java");

            String tecnologia = lista.get(0);

            assertEquals("Java", tecnologia);
        }

        @Test
        @DisplayName("La lista conserva la misma referencia del tipo parametrizado")
        void conservaLaMismaReferencia() {
            String tecnologia = new String("Java");

            lista.agregar(tecnologia);

            String recuperada = lista.get(0);

            assertSame(
                    tecnologia,
                    recuperada,
                    "La lista debe conservar exactamente la misma referencia almacenada"
            );
        }

        @Test
        @DisplayName("Una lista parametrizada con Integer devuelve Integer directamente")
        void listaPuedeParametrizarseConOtroTipo() {
            ListaArrayMejorada<Integer> numeros = new ListaArrayMejorada<>();

            numeros.agregar(10);
            numeros.agregar(20);

            Integer primero = numeros.get(0);
            Integer segundo = numeros.get(1);

            assertAll(
                    () -> assertEquals(10, primero),
                    () -> assertEquals(20, segundo)
            );
        }
    }

    // ============================================================
    // 4. INSERTAR
    // ============================================================

    @Nested
    @DisplayName("4. Insertar elementos")
    class Insertar {

        @Test
        @DisplayName("Insertar al comienzo desplaza los elementos hacia la derecha")
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
        @DisplayName("Insertar en una posición intermedia conserva el orden")
        void insertarEnElMedio() {
            lista.agregar("Java");
            lista.agregar("Maven");

            lista.insertar(1, "Spring");

            assertAll(
                    () -> assertEquals("Java", lista.get(0)),
                    () -> assertEquals("Spring", lista.get(1)),
                    () -> assertEquals("Maven", lista.get(2))
            );
        }

        @Test
        @DisplayName("Insertar en size equivale a agregar al final")
        void insertarAlFinal() {
            lista.agregar("Java");
            lista.agregar("Spring");

            lista.insertar(lista.size(), "Maven");

            assertAll(
                    () -> assertEquals(3, lista.size()),
                    () -> assertEquals("Maven", lista.get(2))
            );
        }

        @Test
        @DisplayName("Insertar null no modifica la lista")
        void insertarNullNoModificaLista() {
            lista.agregar("Java");

            lista.insertar(1, null);

            assertAll(
                    () -> assertEquals(1, lista.size()),
                    () -> assertEquals("Java", lista.get(0))
            );
        }

        @Test
        @DisplayName("Insertar con índice negativo lanza IndexOutOfBoundsException")
        void insertarIndiceNegativo() {
            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.insertar(-1, "Java")
            );
        }

        @Test
        @DisplayName("Insertar más allá de size lanza IndexOutOfBoundsException")
        void insertarMasAllaDelFinal() {
            lista.agregar("Java");

            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.insertar(2, "Spring")
            );
        }
    }

    // ============================================================
    // 5. GET
    // ============================================================

    @Nested
    @DisplayName("5. Recuperar elementos")
    class Get {

        @Test
        @DisplayName("get recupera primer, intermedio y último elemento")
        void getRecuperaPosicionesValidas() {
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
        @DisplayName("get con índice negativo lanza IndexOutOfBoundsException")
        void getIndiceNegativo() {
            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.get(-1)
            );
        }

        @Test
        @DisplayName("get con índice igual a size lanza IndexOutOfBoundsException")
        void getIndiceIgualASize() {
            lista.agregar("Java");

            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.get(lista.size())
            );
        }
    }

    // ============================================================
    // 6. SET
    // ============================================================

    @Nested
    @DisplayName("6. Reemplazar elementos")
    class Set {

        @Test
        @DisplayName("set reemplaza el elemento y devuelve el anterior con el mismo tipo E")
        void setReemplazaYDevuelveAnterior() {
            lista.agregar("Java");

            String anterior = lista.set(0, "Spring");

            assertAll(
                    () -> assertEquals("Java", anterior),
                    () -> assertEquals("Spring", lista.get(0)),
                    () -> assertEquals(1, lista.size())
            );
        }

        @Test
        @DisplayName("set permite almacenar null en una posición existente")
        void setPermiteNull() {
            lista.agregar("Java");

            String anterior = lista.set(0, null);

            assertAll(
                    () -> assertEquals("Java", anterior),
                    () -> assertNull(lista.get(0)),
                    () -> assertEquals(1, lista.size())
            );
        }
    }

    // ============================================================
    // 7. CONTAINS
    // ============================================================

    @Nested
    @DisplayName("7. Buscar elementos")
    class Contains {

        @Test
        @DisplayName("contains devuelve true cuando el elemento está presente")
        void containsEncuentraElemento() {
            lista.agregar("Java");
            lista.agregar("Spring");

            assertTrue(lista.contains("Spring"));
        }

        @Test
        @DisplayName("contains devuelve false cuando el elemento no existe")
        void containsNoEncuentraElemento() {
            lista.agregar("Java");

            assertFalse(lista.contains("Docker"));
        }

        @Test
        @DisplayName("contains utiliza equals para comparar")
        void containsUtilizaEquals() {
            lista.agregar(new String("Java"));

            assertTrue(
                    lista.contains(new String("Java"))
            );
        }
    }

    // ============================================================
    // 8. QUITAR
    // ============================================================

    @Nested
    @DisplayName("8. Quitar elementos")
    class Quitar {

        @Test
        @DisplayName("quitar devuelve el elemento eliminado con el tipo parametrizado")
        void quitarDevuelveElementoEliminado() {
            lista.agregar("Java");
            lista.agregar("Spring");

            String eliminado = lista.quitar(0);

            assertAll(
                    () -> assertEquals("Java", eliminado),
                    () -> assertEquals(1, lista.size()),
                    () -> assertEquals("Spring", lista.get(0))
            );
        }

        @Test
        @DisplayName("Quitar un elemento intermedio desplaza los restantes")
        void quitarIntermedio() {
            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            lista.quitar(1);

            assertAll(
                    () -> assertEquals(2, lista.size()),
                    () -> assertEquals("Java", lista.get(0)),
                    () -> assertEquals("Maven", lista.get(1))
            );
        }

        @Test
        @DisplayName("Quitar con índice inválido lanza IndexOutOfBoundsException")
        void quitarIndiceInvalido() {
            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.quitar(0)
            );
        }

        @Test
        @DisplayName("La lista puede reducir su capacidad internamente sin perder información")
        void puedeReducirCapacidadSinPerderDatos() {
            ListaArrayMejorada<String> otra = new ListaArrayMejorada<>(4);

            otra.agregar("A");
            otra.agregar("B");
            otra.agregar("C");
            otra.agregar("D");
            otra.agregar("E");

            otra.quitar(4);
            otra.quitar(3);
            otra.quitar(2);
            otra.quitar(1);

            assertAll(
                    () -> assertEquals(1, otra.size()),
                    () -> assertEquals("A", otra.get(0))
            );
        }
    }

    // ============================================================
    // 9. CLEAR
    // ============================================================

    @Nested
    @DisplayName("9. Vaciar la lista")
    class Clear {

        @Test
        @DisplayName("clear deja la lista vacía")
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

        @Test
        @DisplayName("Después de clear la lista puede reutilizarse")
        void listaPuedeReutilizarse() {
            lista.agregar("Java");
            lista.clear();
            lista.agregar("Spring");

            assertAll(
                    () -> assertEquals(1, lista.size()),
                    () -> assertEquals("Spring", lista.get(0))
            );
        }
    }

    // ============================================================
    // 10. ITERADOR
    // ============================================================

    @Nested
    @DisplayName("10. IteradorLineal genérico")
    class Iterador {

        @Test
        @DisplayName("iterador devuelve un objeto IteradorLineal")
        void iteradorDevuelveObjeto() {
            ListaArrayMejorada<String>.IteradorLineal iterador = lista.iterador();

            assertNotNull(iterador);
        }

        @Test
        @DisplayName("getActual devuelve directamente E sin necesidad de cast")
        void getActualDevuelveTipoParametrizado() {
            lista.agregar("Java");

            ListaArrayMejorada<String>.IteradorLineal iterador = lista.iterador();

            String tecnologia = iterador.getActual();

            assertEquals("Java", tecnologia);
        }

        @Test
        @DisplayName("siguiente avanza una posición")
        void siguienteAvanza() {
            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            ListaArrayMejorada<String>.IteradorLineal iterador = lista.iterador();

            assertEquals("Java", iterador.getActual());

            iterador.siguiente();

            assertEquals("Spring", iterador.getActual());
        }

        @Test
        @DisplayName("Dos iteradores mantienen estados independientes")
        void dosIteradoresSonIndependientes() {
            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            ListaArrayMejorada<String>.IteradorLineal iterador1 = lista.iterador();
            ListaArrayMejorada<String>.IteradorLineal iterador2 = lista.iterador();

            iterador1.siguiente();
            iterador1.siguiente();

            iterador2.siguiente();

            assertAll(
                    () -> assertEquals("Maven", iterador1.getActual()),
                    () -> assertEquals("Spring", iterador2.getActual())
            );
        }

        @Test
        @DisplayName("En una lista vacía hayMas devuelve false")
        void listaVaciaNoTieneMas() {
            ListaArrayMejorada<String>.IteradorLineal iterador = lista.iterador();

            assertFalse(iterador.hayMas());
        }

        @Test
        @DisplayName("getActual sobre lista vacía lanza NoSuchElementException")
        void getActualListaVaciaLanzaExcepcion() {
            ListaArrayMejorada<String>.IteradorLineal iterador = lista.iterador();

            assertThrows(
                    NoSuchElementException.class,
                    iterador::getActual
            );
        }

        @Test
        @DisplayName("siguiente desde el último elemento lanza NoSuchElementException")
        void siguienteDesdeUltimoLanzaExcepcion() {
            lista.agregar("Java");

            ListaArrayMejorada<String>.IteradorLineal iterador = lista.iterador();

            assertThrows(
                    NoSuchElementException.class,
                    iterador::siguiente
            );
        }
    }

    // ============================================================
    // 11. TOSTRING
    // ============================================================

    @Nested
    @DisplayName("11. Representación textual")
    class ToString {

        @Test
        @DisplayName("toString muestra los elementos ocupados en orden")
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
