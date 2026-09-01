package utnfc.backend.util;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ListaArrayBasica")
class ListaArrayBasicaTest {

    private ListaArrayBasica lista;

    @BeforeEach
    void setUp() {
        lista = new ListaArrayBasica();
    }

    // ============================================================
    // CONSTRUCCIÓN
    // ============================================================

    @Nested
    @DisplayName("Construcción de la lista")
    class Construccion {

        @Test
        @DisplayName("Una lista nueva comienza vacía")
        void listaNuevaComienzaVacia() {
            assertAll(
                    () -> assertEquals(0, lista.size(),
                            "La cantidad inicial de elementos debe ser cero"),
                    () -> assertTrue(lista.isEmpty(),
                            "Una lista recién creada debe informar que está vacía"),
                    () -> assertEquals("{}", lista.toString(),
                            "La representación textual de una lista vacía debe ser {}")
            );
        }

        @Test
        @DisplayName("El constructor con capacidad positiva crea una lista vacía y utilizable")
        void constructorConCapacidadPositiva() {
            ListaArrayBasica otra = new ListaArrayBasica(3);

            assertAll(
                    () -> assertEquals(0, otra.size()),
                    () -> assertTrue(otra.isEmpty()),
                    () -> assertTrue(otra.agregar("A")),
                    () -> assertEquals("A", otra.get(0))
            );
        }

        @Test
        @DisplayName("Una capacidad inicial cero se reemplaza por la capacidad por defecto")
        void capacidadCeroSeAjusta() {
            ListaArrayBasica otra = new ListaArrayBasica(0);

            for (int i = 0; i < 10; i++) {
                assertTrue(otra.agregar(i));
            }

            assertEquals(10, otra.size(),
                    "La lista debe poder almacenar los diez elementos de la capacidad por defecto");
        }

        @Test
        @DisplayName("Una capacidad inicial negativa se reemplaza por la capacidad por defecto")
        void capacidadNegativaSeAjusta() {
            ListaArrayBasica otra = new ListaArrayBasica(-5);

            assertTrue(otra.agregar("elemento"));
            assertEquals("elemento", otra.get(0));
        }
    }

    // ============================================================
    // AGREGAR
    // ============================================================

    @Nested
    @DisplayName("Agregar elementos")
    class Agregar {

        @Test
        @DisplayName("Agregar un elemento lo incorpora al final y aumenta el tamaño")
        void agregarUnElemento() {
            boolean agregado = lista.agregar("A");

            assertAll(
                    () -> assertTrue(agregado),
                    () -> assertEquals(1, lista.size()),
                    () -> assertFalse(lista.isEmpty()),
                    () -> assertEquals("A", lista.get(0))
            );
        }

        @Test
        @DisplayName("Los elementos se conservan en el mismo orden en que fueron agregados")
        void agregarVariosMantieneOrden() {
            lista.agregar("A");
            lista.agregar("B");
            lista.agregar("C");

            assertAll(
                    () -> assertEquals(3, lista.size()),
                    () -> assertEquals("A", lista.get(0)),
                    () -> assertEquals("B", lista.get(1)),
                    () -> assertEquals("C", lista.get(2))
            );
        }

        @Test
        @DisplayName("La lista basada en Object puede almacenar referencias de distintos tipos")
        void puedeAlmacenarReferenciasDeDistintosTipos() {
            String texto = "UTN";
            Integer numero = 42;
            Object objeto = new Object();

            lista.agregar(texto);
            lista.agregar(numero);
            lista.agregar(objeto);

            assertAll(
                    () -> assertSame(texto, lista.get(0)),
                    () -> assertSame(numero, lista.get(1)),
                    () -> assertSame(objeto, lista.get(2))
            );
        }

        @Test
        @DisplayName("Agregar null es rechazado y no modifica la lista")
        void agregarNullEsRechazado() {
            boolean agregado = lista.agregar(null);

            assertAll(
                    () -> assertFalse(agregado),
                    () -> assertEquals(0, lista.size()),
                    () -> assertTrue(lista.isEmpty())
            );
        }

        @Test
        @DisplayName("Al superar la capacidad inicial la lista crece sin perder elementos")
        void crecimientoAutomatico() {
            ListaArrayBasica pequeña = new ListaArrayBasica(2);

            pequeña.agregar("A");
            pequeña.agregar("B");
            pequeña.agregar("C");
            pequeña.agregar("D");

            assertAll(
                    () -> assertEquals(4, pequeña.size()),
                    () -> assertEquals("A", pequeña.get(0)),
                    () -> assertEquals("B", pequeña.get(1)),
                    () -> assertEquals("C", pequeña.get(2)),
                    () -> assertEquals("D", pequeña.get(3))
            );
        }
    }

    // ============================================================
    // INSERTAR
    // ============================================================

    @Nested
    @DisplayName("Insertar elementos")
    class Insertar {

        @Test
        @DisplayName("Insertar al comienzo desplaza los elementos existentes hacia la derecha")
        void insertarAlComienzo() {
            lista.agregar("B");
            lista.agregar("C");

            lista.insertar(0, "A");

            assertAll(
                    () -> assertEquals(3, lista.size()),
                    () -> assertEquals("A", lista.get(0)),
                    () -> assertEquals("B", lista.get(1)),
                    () -> assertEquals("C", lista.get(2))
            );
        }

        @Test
        @DisplayName("Insertar en una posición intermedia conserva el orden relativo")
        void insertarEnElMedio() {
            lista.agregar("A");
            lista.agregar("C");

            lista.insertar(1, "B");

            assertAll(
                    () -> assertEquals("A", lista.get(0)),
                    () -> assertEquals("B", lista.get(1)),
                    () -> assertEquals("C", lista.get(2))
            );
        }

        @Test
        @DisplayName("Insertar en size equivale a agregar al final")
        void insertarAlFinal() {
            lista.agregar("A");
            lista.agregar("B");

            lista.insertar(lista.size(), "C");

            assertAll(
                    () -> assertEquals(3, lista.size()),
                    () -> assertEquals("C", lista.get(2))
            );
        }

        @Test
        @DisplayName("Insertar null no modifica la lista")
        void insertarNullNoHaceNada() {
            lista.agregar("A");

            lista.insertar(1, null);

            assertAll(
                    () -> assertEquals(1, lista.size()),
                    () -> assertEquals("A", lista.get(0))
            );
        }

        @Test
        @DisplayName("Insertar con índice negativo lanza IndexOutOfBoundsException")
        void insertarIndiceNegativo() {
            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.insertar(-1, "A")
            );
        }

        @Test
        @DisplayName("Insertar después de size lanza IndexOutOfBoundsException")
        void insertarDespuesDelFinal() {
            lista.agregar("A");

            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.insertar(2, "B")
            );
        }
    }

    // ============================================================
    // GET
    // ============================================================

    @Nested
    @DisplayName("Recuperar elementos")
    class Get {

        @Test
        @DisplayName("get recupera la misma referencia almacenada")
        void getRecuperaMismaReferencia() {
            Object objeto = new Object();
            lista.agregar(objeto);

            assertSame(objeto, lista.get(0));
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
            lista.agregar("A");

            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.get(lista.size())
            );
        }

        @Test
        @DisplayName("get sobre una lista vacía lanza IndexOutOfBoundsException")
        void getListaVacia() {
            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.get(0)
            );
        }
    }

    // ============================================================
    // SET
    // ============================================================

    @Nested
    @DisplayName("Reemplazar elementos")
    class Set {

        @Test
        @DisplayName("set reemplaza un elemento y devuelve la referencia anterior")
        void setReemplazaYDevuelveAnterior() {
            Object anterior = new Object();
            Object nuevo = new Object();

            lista.agregar(anterior);

            Object resultado = lista.set(0, nuevo);

            assertAll(
                    () -> assertSame(anterior, resultado),
                    () -> assertSame(nuevo, lista.get(0)),
                    () -> assertEquals(1, lista.size())
            );
        }

        @Test
        @DisplayName("set permite almacenar null porque su contrato no lo rechaza")
        void setPermiteNull() {
            lista.agregar("A");

            Object anterior = lista.set(0, null);

            assertAll(
                    () -> assertEquals("A", anterior),
                    () -> assertNull(lista.get(0)),
                    () -> assertEquals(1, lista.size())
            );
        }

        @Test
        @DisplayName("set con índice inválido lanza IndexOutOfBoundsException")
        void setIndiceInvalido() {
            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.set(0, "A")
            );
        }
    }

    // ============================================================
    // CONTAINS
    // ============================================================

    @Nested
    @DisplayName("Buscar elementos")
    class Contains {

        @Test
        @DisplayName("contains devuelve true cuando el elemento está presente")
        void contieneElementoExistente() {
            lista.agregar("A");
            lista.agregar("B");

            assertTrue(lista.contains("B"));
        }

        @Test
        @DisplayName("contains devuelve false cuando el elemento no está presente")
        void noContieneElementoInexistente() {
            lista.agregar("A");

            assertFalse(lista.contains("B"));
        }

        @Test
        @DisplayName("contains de null siempre devuelve false")
        void containsNull() {
            lista.agregar("A");

            assertFalse(lista.contains(null));
        }

        @Test
        @DisplayName("contains utiliza equals para comparar objetos")
        void containsUtilizaEquals() {
            lista.agregar(new String("UTN"));

            assertTrue(lista.contains(new String("UTN")),
                    "Dos referencias distintas consideradas iguales por equals deben ser encontradas");
        }
    }

    // ============================================================
    // QUITAR
    // ============================================================

    @Nested
    @DisplayName("Quitar elementos")
    class Quitar {

        @Test
        @DisplayName("Quitar devuelve el elemento removido")
        void quitarDevuelveElemento() {
            Object objeto = new Object();
            lista.agregar(objeto);

            Object removido = lista.quitar(0);

            assertSame(objeto, removido);
        }

        @Test
        @DisplayName("Quitar el primer elemento desplaza los restantes hacia la izquierda")
        void quitarPrimeroDesplazaRestantes() {
            lista.agregar("A");
            lista.agregar("B");
            lista.agregar("C");

            lista.quitar(0);

            assertAll(
                    () -> assertEquals(2, lista.size()),
                    () -> assertEquals("B", lista.get(0)),
                    () -> assertEquals("C", lista.get(1))
            );
        }

        @Test
        @DisplayName("Quitar un elemento intermedio conserva el orden de los restantes")
        void quitarElementoIntermedio() {
            lista.agregar("A");
            lista.agregar("B");
            lista.agregar("C");

            lista.quitar(1);

            assertAll(
                    () -> assertEquals(2, lista.size()),
                    () -> assertEquals("A", lista.get(0)),
                    () -> assertEquals("C", lista.get(1))
            );
        }

        @Test
        @DisplayName("Quitar el último elemento reduce el tamaño")
        void quitarUltimo() {
            lista.agregar("A");
            lista.agregar("B");

            Object eliminado = lista.quitar(1);

            assertAll(
                    () -> assertEquals("B", eliminado),
                    () -> assertEquals(1, lista.size()),
                    () -> assertEquals("A", lista.get(0))
            );
        }

        @Test
        @DisplayName("Quitar con índice negativo lanza IndexOutOfBoundsException")
        void quitarIndiceNegativo() {
            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.quitar(-1)
            );
        }

        @Test
        @DisplayName("Quitar con índice igual a size lanza IndexOutOfBoundsException")
        void quitarIndiceIgualASize() {
            lista.agregar("A");

            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.quitar(lista.size())
            );
        }
    }

    // ============================================================
    // CLEAR
    // ============================================================

    @Nested
    @DisplayName("Vaciar la lista")
    class Clear {

        @Test
        @DisplayName("clear elimina todos los elementos y deja la lista vacía")
        void clearVaciaLista() {
            lista.agregar("A");
            lista.agregar("B");
            lista.agregar("C");

            lista.clear();

            assertAll(
                    () -> assertEquals(0, lista.size()),
                    () -> assertTrue(lista.isEmpty()),
                    () -> assertEquals("{}", lista.toString())
            );
        }

        @Test
        @DisplayName("Después de clear la lista puede volver a utilizarse")
        void clearPermiteReutilizarLista() {
            lista.agregar("A");
            lista.clear();

            lista.agregar("B");

            assertAll(
                    () -> assertEquals(1, lista.size()),
                    () -> assertEquals("B", lista.get(0))
            );
        }
    }

    // ============================================================
    // TOSTRING
    // ============================================================

    @Nested
    @DisplayName("Representación textual")
    class ToString {

        @Test
        @DisplayName("toString de una lista vacía devuelve {}")
        void toStringListaVacia() {
            assertEquals("{}", lista.toString());
        }

        @Test
        @DisplayName("toString muestra los elementos ocupados en orden")
        void toStringConElementos() {
            lista.agregar("A");
            lista.agregar("B");
            lista.agregar("C");

            assertEquals("{A, B, C}", lista.toString());
        }
    }

    // ============================================================
    // ITERADOR ARTESANAL
    // ============================================================

    @Nested
    @DisplayName("Iterador artesanal")
    class IteradorArtesanal {

        @Test
        @DisplayName("Al iniciar el iterador, el elemento actual es el primero")
        void iniciarIteradorPosicionaEnElPrimerElemento() {
            lista.agregar("A");
            lista.agregar("B");

            lista.iniciarIterador();

            assertEquals("A", lista.getActual());
        }

        @Test
        @DisplayName("En una lista vacía hayMas devuelve false")
        void listaVaciaNoTieneMasElementos() {
            lista.iniciarIterador();

            assertFalse(lista.hayMas());
        }

        @Test
        @DisplayName("En una lista con un solo elemento hayMas devuelve false porque no existe una posición siguiente")
        void unSoloElementoNoTieneSiguientePosicion() {
            lista.agregar("A");
            lista.iniciarIterador();

            assertAll(
                    () -> assertEquals("A", lista.getActual(),
                            "El único elemento sí puede obtenerse como actual"),
                    () -> assertFalse(lista.hayMas(),
                            "hayMas indica si podemos avanzar a otra posición")
            );
        }

        @Test
        @DisplayName("Con varios elementos hayMas devuelve true mientras exista una posición siguiente")
        void hayMasMientrasExistaSiguiente() {
            lista.agregar("A");
            lista.agregar("B");
            lista.agregar("C");
            lista.iniciarIterador();

            assertTrue(lista.hayMas());

            lista.siguiente();
            assertTrue(lista.hayMas());

            lista.siguiente();
            assertFalse(lista.hayMas());
        }

        @Test
        @DisplayName("siguiente avanza exactamente una posición")
        void siguienteAvanzaUnaPosicion() {
            lista.agregar("A");
            lista.agregar("B");

            lista.iniciarIterador();
            assertEquals("A", lista.getActual());

            lista.siguiente();

            assertEquals("B", lista.getActual());
        }

        @Test
        @DisplayName("El iterador artesanal permite recorrer todos los elementos en orden")
        void recorreTodosLosElementosEnOrden() {
            lista.agregar("A");
            lista.agregar("B");
            lista.agregar("C");

            lista.iniciarIterador();

            assertEquals("A", lista.getActual());

            lista.siguiente();
            assertEquals("B", lista.getActual());

            lista.siguiente();
            assertEquals("C", lista.getActual());

            assertFalse(lista.hayMas());
        }

        @Test
        @DisplayName("siguiente desde el último elemento lanza NoSuchElementException")
        void siguienteDesdeUltimoElementoLanzaExcepcion() {
            lista.agregar("A");
            lista.iniciarIterador();

            NoSuchElementException ex = assertThrows(
                    NoSuchElementException.class,
                    lista::siguiente
            );

            assertEquals(
                    "next(): no quedan elementos por recorrer...",
                    ex.getMessage()
            );
        }

        @Test
        @DisplayName("getActual sobre una lista vacía lanza NoSuchElementException")
        void getActualListaVaciaLanzaExcepcion() {
            lista.iniciarIterador();

            NoSuchElementException ex = assertThrows(
                    NoSuchElementException.class,
                    lista::getActual
            );

            assertEquals(
                    "next(): no quedan elementos por recorrer...",
                    ex.getMessage()
            );
        }

        @Test
        @DisplayName("iniciarIterador permite volver a recorrer desde el comienzo")
        void reiniciarRecorrido() {
            lista.agregar("A");
            lista.agregar("B");
            lista.agregar("C");

            lista.iniciarIterador();
            lista.siguiente();
            lista.siguiente();

            assertEquals("C", lista.getActual());

            lista.iniciarIterador();

            assertEquals("A", lista.getActual());
        }
    }
}
