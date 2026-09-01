package utnfc.backend.util;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ListaArrayMejorada")
class ListaArrayMejoradaTest {

    private ListaArrayMejorada lista;

    @BeforeEach
    void setUp() {
        lista = new ListaArrayMejorada();
    }

    // ============================================================
    // CONSTRUCCIÓN
    // ============================================================

    @Nested
    @DisplayName("1. Construcción de la lista")
    class Construccion {

        @Test
        @DisplayName("Una lista nueva comienza vacía")
        void listaNuevaComienzaVacia() {

            assertAll(
                    () -> assertEquals(
                            0,
                            lista.size(),
                            "La cantidad inicial de elementos debe ser cero"
                    ),
                    () -> assertTrue(
                            lista.isEmpty(),
                            "Una lista recién creada debe informar que está vacía"
                    ),
                    () -> assertEquals(
                            "{}",
                            lista.toString(),
                            "La representación textual de una lista vacía debe ser {}"
                    )
            );
        }

        @Test
        @DisplayName("El constructor con capacidad positiva crea una lista vacía y utilizable")
        void constructorConCapacidadPositiva() {

            ListaArrayMejorada otra = new ListaArrayMejorada(3);

            assertAll(
                    () -> assertEquals(0, otra.size()),
                    () -> assertTrue(otra.isEmpty()),
                    () -> assertTrue(otra.agregar("Java")),
                    () -> assertEquals("Java", otra.get(0))
            );
        }

        @Test
        @DisplayName("Una capacidad inicial igual a cero se reemplaza por la capacidad por defecto")
        void capacidadCeroUsaCapacidadPorDefecto() {

            ListaArrayMejorada otra = new ListaArrayMejorada(0);

            for (int i = 0; i < 10; i++) {
                assertTrue(otra.agregar("Elemento " + i));
            }

            assertEquals(
                    10,
                    otra.size(),
                    "La lista debe poder almacenar al menos los diez elementos de la capacidad por defecto"
            );
        }

        @Test
        @DisplayName("Una capacidad inicial negativa se reemplaza por la capacidad por defecto")
        void capacidadNegativaUsaCapacidadPorDefecto() {

            ListaArrayMejorada otra = new ListaArrayMejorada(-5);

            otra.agregar("Java");

            assertAll(
                    () -> assertEquals(1, otra.size()),
                    () -> assertEquals("Java", otra.get(0))
            );
        }
    }


    // ============================================================
    // AGREGAR
    // ============================================================

    @Nested
    @DisplayName("2. Agregar elementos")
    class Agregar {

        @Test
        @DisplayName("Agregar un elemento lo incorpora al final de la lista")
        void agregarUnElemento() {

            boolean resultado = lista.agregar("Java");

            assertAll(
                    () -> assertTrue(
                            resultado,
                            "agregar debe informar que la operación fue exitosa"
                    ),
                    () -> assertEquals(
                            1,
                            lista.size(),
                            "La cantidad debe aumentar a uno"
                    ),
                    () -> assertFalse(
                            lista.isEmpty(),
                            "La lista ya no debe estar vacía"
                    ),
                    () -> assertEquals(
                            "Java",
                            lista.get(0),
                            "El elemento agregado debe quedar en la primera posición"
                    )
            );
        }

        @Test
        @DisplayName("Agregar varios elementos conserva el orden de inserción")
        void agregarVariosElementosMantieneOrden() {

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
                    () -> assertFalse(
                            resultado,
                            "La operación debe informar que null no fue agregado"
                    ),
                    () -> assertEquals(
                            0,
                            lista.size(),
                            "La cantidad debe continuar en cero"
                    ),
                    () -> assertTrue(lista.isEmpty())
            );
        }

        @Test
        @DisplayName("La lista almacena referencias Object de diferentes tipos")
        void permiteAlmacenarReferenciasDeDistintosTipos() {

            String texto = "Java";
            Integer numero = 21;
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
        @DisplayName("Al superar la capacidad inicial la lista crece sin perder elementos")
        void superarCapacidadInicialConservaElementos() {

            ListaArrayMejorada pequeña = new ListaArrayMejorada(2);

            pequeña.agregar("Java");
            pequeña.agregar("Spring");
            pequeña.agregar("Maven");
            pequeña.agregar("JUnit");
            pequeña.agregar("Docker");

            assertAll(
                    () -> assertEquals(5, pequeña.size()),
                    () -> assertEquals("Java", pequeña.get(0)),
                    () -> assertEquals("Spring", pequeña.get(1)),
                    () -> assertEquals("Maven", pequeña.get(2)),
                    () -> assertEquals("JUnit", pequeña.get(3)),
                    () -> assertEquals("Docker", pequeña.get(4))
            );
        }
    }


    // ============================================================
    // INSERTAR
    // ============================================================

    @Nested
    @DisplayName("3. Insertar elementos")
    class Insertar {

        @Test
        @DisplayName("Insertar al comienzo desplaza los elementos existentes")
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
        void insertarEnPosicionIntermedia() {

            lista.agregar("Java");
            lista.agregar("Maven");

            lista.insertar(1, "Spring");

            assertAll(
                    () -> assertEquals(3, lista.size()),
                    () -> assertEquals("Java", lista.get(0)),
                    () -> assertEquals("Spring", lista.get(1)),
                    () -> assertEquals("Maven", lista.get(2))
            );
        }

        @Test
        @DisplayName("Insertar en size agrega el elemento al final")
        void insertarEnSizeAgregaAlFinal() {

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
        void insertarConIndiceNegativoLanzaExcepcion() {

            IndexOutOfBoundsException ex = assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.insertar(-1, "Java")
            );

            assertEquals(
                    "add(): índice fuera de rango...",
                    ex.getMessage()
            );
        }

        @Test
        @DisplayName("Insertar más allá de size lanza IndexOutOfBoundsException")
        void insertarMasAllaDeSizeLanzaExcepcion() {

            lista.agregar("Java");

            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.insertar(2, "Spring")
            );
        }

        @Test
        @DisplayName("Insertar también provoca crecimiento cuando se agota la capacidad")
        void insertarPuedeProvocarCrecimiento() {

            ListaArrayMejorada pequeña = new ListaArrayMejorada(2);

            pequeña.agregar("Java");
            pequeña.agregar("Maven");

            pequeña.insertar(1, "Spring");

            assertAll(
                    () -> assertEquals(3, pequeña.size()),
                    () -> assertEquals("Java", pequeña.get(0)),
                    () -> assertEquals("Spring", pequeña.get(1)),
                    () -> assertEquals("Maven", pequeña.get(2))
            );
        }
    }


    // ============================================================
    // GET
    // ============================================================

    @Nested
    @DisplayName("4. Recuperar elementos")
    class Get {

        @Test
        @DisplayName("get recupera la misma referencia almacenada")
        void getRecuperaMismaReferencia() {

            Object objeto = new Object();

            lista.agregar(objeto);

            assertSame(
                    objeto,
                    lista.get(0),
                    "La lista debe devolver exactamente la misma referencia que fue almacenada"
            );
        }

        @Test
        @DisplayName("get permite acceder al primer, intermedio y último elemento")
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
        void getConIndiceNegativoLanzaExcepcion() {

            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.get(-1)
            );
        }

        @Test
        @DisplayName("get con índice igual a size lanza IndexOutOfBoundsException")
        void getConIndiceIgualASizeLanzaExcepcion() {

            lista.agregar("Java");

            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.get(lista.size())
            );
        }

        @Test
        @DisplayName("get sobre una lista vacía lanza IndexOutOfBoundsException")
        void getSobreListaVaciaLanzaExcepcion() {

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
    @DisplayName("5. Reemplazar elementos")
    class Set {

        @Test
        @DisplayName("set reemplaza un elemento y devuelve la referencia anterior")
        void setReemplazaYDevuelveAnterior() {

            Object anterior = new Object();
            Object nuevo = new Object();

            lista.agregar(anterior);

            Object resultado = lista.set(0, nuevo);

            assertAll(
                    () -> assertSame(
                            anterior,
                            resultado,
                            "set debe devolver la referencia reemplazada"
                    ),
                    () -> assertSame(
                            nuevo,
                            lista.get(0),
                            "La nueva referencia debe quedar almacenada"
                    ),
                    () -> assertEquals(
                            1,
                            lista.size(),
                            "Reemplazar no debe cambiar la cantidad de elementos"
                    )
            );
        }

        @Test
        @DisplayName("set permite reemplazar una referencia por null")
        void setPermiteNull() {

            lista.agregar("Java");

            Object anterior = lista.set(0, null);

            assertAll(
                    () -> assertEquals("Java", anterior),
                    () -> assertNull(lista.get(0)),
                    () -> assertEquals(1, lista.size())
            );
        }

        @Test
        @DisplayName("set con índice inválido lanza IndexOutOfBoundsException")
        void setConIndiceInvalidoLanzaExcepcion() {

            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.set(0, "Java")
            );
        }
    }


    // ============================================================
    // CONTAINS
    // ============================================================

    @Nested
    @DisplayName("6. Buscar elementos")
    class Contains {

        @Test
        @DisplayName("contains devuelve true cuando encuentra el elemento")
        void containsEncuentraElementoExistente() {

            lista.agregar("Java");
            lista.agregar("Spring");

            assertTrue(lista.contains("Spring"));
        }

        @Test
        @DisplayName("contains devuelve false cuando el elemento no existe")
        void containsNoEncuentraElementoInexistente() {

            lista.agregar("Java");

            assertFalse(lista.contains("Docker"));
        }

        @Test
        @DisplayName("contains de null devuelve false")
        void containsNullDevuelveFalse() {

            lista.agregar("Java");

            assertFalse(lista.contains(null));
        }

        @Test
        @DisplayName("contains compara objetos utilizando equals")
        void containsUtilizaEquals() {

            lista.agregar(new String("Java"));

            assertTrue(
                    lista.contains(new String("Java")),
                    "No es necesario buscar exactamente la misma referencia: contains utiliza equals"
            );
        }
    }


    // ============================================================
    // QUITAR
    // ============================================================

    @Nested
    @DisplayName("7. Quitar elementos")
    class Quitar {

        @Test
        @DisplayName("quitar devuelve la referencia eliminada")
        void quitarDevuelveReferenciaEliminada() {

            Object objeto = new Object();

            lista.agregar(objeto);

            Object eliminado = lista.quitar(0);

            assertSame(objeto, eliminado);
        }

        @Test
        @DisplayName("Quitar el primero desplaza los restantes hacia la izquierda")
        void quitarPrimerElementoDesplazaRestantes() {

            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            lista.quitar(0);

            assertAll(
                    () -> assertEquals(2, lista.size()),
                    () -> assertEquals("Spring", lista.get(0)),
                    () -> assertEquals("Maven", lista.get(1))
            );
        }

        @Test
        @DisplayName("Quitar un elemento intermedio mantiene el orden de los restantes")
        void quitarElementoIntermedio() {

            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            Object eliminado = lista.quitar(1);

            assertAll(
                    () -> assertEquals("Spring", eliminado),
                    () -> assertEquals(2, lista.size()),
                    () -> assertEquals("Java", lista.get(0)),
                    () -> assertEquals("Maven", lista.get(1))
            );
        }

        @Test
        @DisplayName("Quitar el último elemento reduce el tamaño")
        void quitarUltimoElemento() {

            lista.agregar("Java");
            lista.agregar("Spring");

            Object eliminado = lista.quitar(1);

            assertAll(
                    () -> assertEquals("Spring", eliminado),
                    () -> assertEquals(1, lista.size()),
                    () -> assertEquals("Java", lista.get(0))
            );
        }

        @Test
        @DisplayName("Quitar el único elemento deja la lista vacía")
        void quitarUnicoElementoDejaListaVacia() {

            lista.agregar("Java");

            lista.quitar(0);

            assertAll(
                    () -> assertEquals(0, lista.size()),
                    () -> assertTrue(lista.isEmpty()),
                    () -> assertEquals("{}", lista.toString())
            );
        }

        @Test
        @DisplayName("Quitar con índice negativo lanza IndexOutOfBoundsException")
        void quitarConIndiceNegativoLanzaExcepcion() {

            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.quitar(-1)
            );
        }

        @Test
        @DisplayName("Quitar con índice igual a size lanza IndexOutOfBoundsException")
        void quitarConIndiceIgualASizeLanzaExcepcion() {

            lista.agregar("Java");

            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.quitar(lista.size())
            );
        }

        @Test
        @DisplayName("Luego de varias eliminaciones la lista continúa funcionando correctamente")
        void quitarVariosYSeguirUtilizandoLista() {

            ListaArrayMejorada pequeña = new ListaArrayMejorada(4);

            pequeña.agregar("Java");
            pequeña.agregar("Spring");
            pequeña.agregar("Maven");
            pequeña.agregar("JUnit");
            pequeña.agregar("Docker");

            pequeña.quitar(0);
            pequeña.quitar(0);
            pequeña.quitar(0);

            pequeña.agregar("Kafka");

            assertAll(
                    () -> assertEquals(3, pequeña.size()),
                    () -> assertEquals("JUnit", pequeña.get(0)),
                    () -> assertEquals("Docker", pequeña.get(1)),
                    () -> assertEquals("Kafka", pequeña.get(2))
            );
        }
    }


    // ============================================================
    // CLEAR
    // ============================================================

    @Nested
    @DisplayName("8. Vaciar la lista")
    class Clear {

        @Test
        @DisplayName("clear elimina todos los elementos")
        void clearEliminaTodosLosElementos() {

            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            lista.clear();

            assertAll(
                    () -> assertEquals(0, lista.size()),
                    () -> assertTrue(lista.isEmpty()),
                    () -> assertEquals("{}", lista.toString())
            );
        }

        @Test
        @DisplayName("Después de clear la lista puede volver a utilizarse")
        void listaPuedeReutilizarseDespuesDeClear() {

            lista.agregar("Java");
            lista.agregar("Spring");

            lista.clear();

            lista.agregar("Docker");

            assertAll(
                    () -> assertEquals(1, lista.size()),
                    () -> assertEquals("Docker", lista.get(0))
            );
        }
    }


    // ============================================================
    // TOSTRING
    // ============================================================

    @Nested
    @DisplayName("9. Representación textual")
    class ToString {

        @Test
        @DisplayName("toString de una lista vacía devuelve {}")
        void toStringListaVacia() {

            assertEquals("{}", lista.toString());
        }

        @Test
        @DisplayName("toString muestra únicamente los elementos ocupados y en orden")
        void toStringListaConElementos() {

            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            assertEquals(
                    "{Java, Spring, Maven}",
                    lista.toString()
            );
        }
    }


    // ============================================================
    // CREACIÓN DE ITERADORES
    // ============================================================

    @Nested
    @DisplayName("10. Creación de IteradorLineal")
    class CreacionIterador {

        @Test
        @DisplayName("iterador devuelve un nuevo objeto IteradorLineal")
        void iteradorDevuelveNuevoIterador() {

            ListaArrayMejorada.IteradorLineal iterador = lista.iterador();

            assertNotNull(iterador);
        }

        @Test
        @DisplayName("Cada invocación a iterador devuelve una instancia diferente")
        void cadaInvocacionCreaUnIteradorDiferente() {

            ListaArrayMejorada.IteradorLineal iterador1 = lista.iterador();
            ListaArrayMejorada.IteradorLineal iterador2 = lista.iterador();

            assertNotSame(
                    iterador1,
                    iterador2,
                    "Cada recorrido debe disponer de su propio objeto iterador"
            );
        }
    }


    // ============================================================
    // ITERADOR LINEAL
    // ============================================================

    @Nested
    @DisplayName("11. Recorrido mediante IteradorLineal")
    class RecorridoIterador {

        @Test
        @DisplayName("Un iterador nuevo comienza ubicado en el primer elemento")
        void iteradorNuevoComienzaEnPrimerElemento() {

            lista.agregar("Java");
            lista.agregar("Spring");

            ListaArrayMejorada.IteradorLineal iterador = lista.iterador();

            assertEquals(
                    "Java",
                    iterador.getActual()
            );
        }

        @Test
        @DisplayName("getActual devuelve la misma referencia almacenada en la lista")
        void getActualDevuelveMismaReferencia() {

            Object objeto = new Object();

            lista.agregar(objeto);

            ListaArrayMejorada.IteradorLineal iterador = lista.iterador();

            assertSame(
                    objeto,
                    iterador.getActual()
            );
        }

        @Test
        @DisplayName("En una lista vacía hayMas devuelve false")
        void listaVaciaNoTieneMas() {

            ListaArrayMejorada.IteradorLineal iterador = lista.iterador();

            assertFalse(iterador.hayMas());
        }

        @Test
        @DisplayName("getActual sobre una lista vacía lanza NoSuchElementException")
        void getActualSobreListaVaciaLanzaExcepcion() {

            ListaArrayMejorada.IteradorLineal iterador = lista.iterador();

            NoSuchElementException ex = assertThrows(
                    NoSuchElementException.class,
                    iterador::getActual
            );

            assertEquals(
                    "next(): no quedan elementos por recorrer...",
                    ex.getMessage()
            );
        }

        @Test
        @DisplayName("siguiente avanza exactamente una posición")
        void siguienteAvanzaUnaPosicion() {

            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            ListaArrayMejorada.IteradorLineal iterador = lista.iterador();

            assertEquals("Java", iterador.getActual());

            iterador.siguiente();

            assertEquals("Spring", iterador.getActual());

            iterador.siguiente();

            assertEquals("Maven", iterador.getActual());
        }

        @Test
        @DisplayName("hayMas indica si existe una posición posterior a la actual")
        void hayMasIndicaSiExistePosicionPosterior() {

            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            ListaArrayMejorada.IteradorLineal iterador = lista.iterador();

            assertTrue(
                    iterador.hayMas(),
                    "Desde Java todavía podemos avanzar a Spring"
            );

            iterador.siguiente();

            assertTrue(
                    iterador.hayMas(),
                    "Desde Spring todavía podemos avanzar a Maven"
            );

            iterador.siguiente();

            assertFalse(
                    iterador.hayMas(),
                    "Estando en Maven ya no existe una posición posterior"
            );
        }

        @Test
        @DisplayName("Con un único elemento getActual lo recupera pero hayMas devuelve false")
        void unElementoExisteAunqueNoHayaPosicionSiguiente() {

            lista.agregar("Java");

            ListaArrayMejorada.IteradorLineal iterador = lista.iterador();

            assertAll(
                    () -> assertEquals(
                            "Java",
                            iterador.getActual(),
                            "El elemento actual existe"
                    ),
                    () -> assertFalse(
                            iterador.hayMas(),
                            "No existe una posición posterior a la actual"
                    )
            );
        }

        @Test
        @DisplayName("siguiente desde el último elemento lanza NoSuchElementException")
        void siguienteDesdeUltimoElementoLanzaExcepcion() {

            lista.agregar("Java");

            ListaArrayMejorada.IteradorLineal iterador = lista.iterador();

            NoSuchElementException ex = assertThrows(
                    NoSuchElementException.class,
                    iterador::siguiente
            );

            assertEquals(
                    "next(): no quedan elementos por recorrer...",
                    ex.getMessage()
            );
        }
    }


    // ============================================================
    // INDEPENDENCIA ENTRE ITERADORES
    // ============================================================

    @Nested
    @DisplayName("12. Independencia entre múltiples iteradores")
    class MultiplesIteradores {

        @Test
        @DisplayName("Dos iteradores sobre la misma lista comienzan en el primer elemento")
        void dosIteradoresComienzanIndependientemente() {

            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            ListaArrayMejorada.IteradorLineal iterador1 = lista.iterador();
            ListaArrayMejorada.IteradorLineal iterador2 = lista.iterador();

            assertAll(
                    () -> assertEquals("Java", iterador1.getActual()),
                    () -> assertEquals("Java", iterador2.getActual())
            );
        }

        @Test
        @DisplayName("Avanzar un iterador no modifica la posición del otro")
        void avanzarUnIteradorNoAfectaAlOtro() {

            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            ListaArrayMejorada.IteradorLineal iterador1 = lista.iterador();
            ListaArrayMejorada.IteradorLineal iterador2 = lista.iterador();

            iterador1.siguiente();

            assertAll(
                    () -> assertEquals(
                            "Spring",
                            iterador1.getActual(),
                            "El primer iterador avanzó hasta Spring"
                    ),
                    () -> assertEquals(
                            "Java",
                            iterador2.getActual(),
                            "El segundo iterador debe continuar en Java"
                    )
            );
        }

        @Test
        @DisplayName("Dos iteradores pueden avanzar a ritmos diferentes sobre la misma lista")
        void dosIteradoresAvanzanARitmosDiferentes() {

            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");
            lista.agregar("JUnit");

            ListaArrayMejorada.IteradorLineal iterador1 = lista.iterador();
            ListaArrayMejorada.IteradorLineal iterador2 = lista.iterador();

            iterador1.siguiente();
            iterador1.siguiente();

            iterador2.siguiente();

            assertAll(
                    () -> assertEquals(
                            "Maven",
                            iterador1.getActual(),
                            "El primer iterador debe encontrarse en la tercera posición"
                    ),
                    () -> assertEquals(
                            "Spring",
                            iterador2.getActual(),
                            "El segundo iterador debe encontrarse en la segunda posición"
                    )
            );
        }

        @Test
        @DisplayName("Crear un nuevo iterador permite iniciar otro recorrido desde el comienzo")
        void nuevoIteradorComienzaNuevoRecorrido() {

            lista.agregar("Java");
            lista.agregar("Spring");
            lista.agregar("Maven");

            ListaArrayMejorada.IteradorLineal primero = lista.iterador();

            primero.siguiente();
            primero.siguiente();

            assertEquals("Maven", primero.getActual());

            ListaArrayMejorada.IteradorLineal segundo = lista.iterador();

            assertEquals(
                    "Java",
                    segundo.getActual(),
                    "Un iterador nuevo comienza nuevamente desde el primer elemento"
            );
        }
    }
}
