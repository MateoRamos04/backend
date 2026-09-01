package utnfc.backend.util.generics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ArrayList<E> - implementación propia basada en AbstractList")
class ArrayListTest {

    private ArrayList<String> lista;

    @BeforeEach
    void setUp() {
        lista = new ArrayList<>();
    }

    // ============================================================
    // 1. CONSTRUCCIÓN
    // ============================================================

    @Nested
    @DisplayName("1. Construcción")
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
            ArrayList<String> otra = new ArrayList<>(3);

            assertTrue(otra.add("Java"));

            assertAll(
                    () -> assertEquals(1, otra.size()),
                    () -> assertEquals("Java", otra.get(0))
            );
        }

        @Test
        @DisplayName("Una capacidad inicial inválida se reemplaza por la capacidad por defecto")
        void capacidadInicialInvalida() {
            ArrayList<String> otra = new ArrayList<>(0);

            for (int i = 0; i < 10; i++) {
                otra.add("Elemento " + i);
            }

            assertEquals(10, otra.size());
        }

        @Test
        @DisplayName("El constructor desde Collection copia los elementos respetando el orden")
        void constructorDesdeCollection() {
            Collection<String> origen =
                    java.util.List.of("Java", "Spring", "Maven");

            ArrayList<String> otra = new ArrayList<>(origen);

            assertAll(
                    () -> assertEquals(3, otra.size()),
                    () -> assertEquals("Java", otra.get(0)),
                    () -> assertEquals("Spring", otra.get(1)),
                    () -> assertEquals("Maven", otra.get(2))
            );
        }

        @Test
        @DisplayName("El constructor desde Collection acepta subtipos de E")
        void constructorCollectionConWildcardExtends() {
            Collection<Integer> enteros =
                    java.util.List.of(10, 20, 30);

            ArrayList<Number> numeros = new ArrayList<>(enteros);

            assertAll(
                    () -> assertEquals(3, numeros.size()),
                    () -> assertEquals(10, numeros.get(0)),
                    () -> assertEquals(20, numeros.get(1)),
                    () -> assertEquals(30, numeros.get(2))
            );
        }

        @Test
        @DisplayName("El constructor desde Collection null lanza NullPointerException")
        void constructorCollectionNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> new ArrayList<String>((Collection<String>) null)
            );
        }
    }

    // ============================================================
    // 2. ADD
    // ============================================================

    @Nested
    @DisplayName("2. Agregar elementos")
    class Agregar {

        @Test
        @DisplayName("add(E) agrega al final aunque nuestra clase no lo haya sobrescrito")
        void addHeredadoAgregaAlFinal() {
            /*
             * Este add(E) proviene del comportamiento de AbstractList,
             * que termina delegando en add(size(), e).
             */
            assertTrue(lista.add("Java"));
            assertTrue(lista.add("Spring"));
            assertTrue(lista.add("Maven"));

            assertAll(
                    () -> assertEquals(3, lista.size()),
                    () -> assertEquals("Java", lista.get(0)),
                    () -> assertEquals("Spring", lista.get(1)),
                    () -> assertEquals("Maven", lista.get(2))
            );
        }

        @Test
        @DisplayName("add(index, E) inserta al comienzo y desplaza los demás")
        void addEnIndiceCero() {
            lista.add("Spring");
            lista.add("Maven");

            lista.add(0, "Java");

            assertAll(
                    () -> assertEquals(3, lista.size()),
                    () -> assertEquals("Java", lista.get(0)),
                    () -> assertEquals("Spring", lista.get(1)),
                    () -> assertEquals("Maven", lista.get(2))
            );
        }

        @Test
        @DisplayName("add(index, E) permite insertar en size")
        void addEnSize() {
            lista.add("Java");

            lista.add(lista.size(), "Spring");

            assertEquals("Spring", lista.get(1));
        }

        @Test
        @DisplayName("add(index, null) no modifica la lista")
        void addNullNoModifica() {
            lista.add("Java");

            lista.add(1, null);

            assertAll(
                    () -> assertEquals(1, lista.size()),
                    () -> assertEquals("Java", lista.get(0))
            );
        }

        @Test
        @DisplayName("add con índice inválido lanza IndexOutOfBoundsException")
        void addIndiceInvalido() {
            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> lista.add(1, "Java")
            );
        }
    }

    // ============================================================
    // 3. GET Y SET
    // ============================================================

    @Nested
    @DisplayName("3. Acceso y reemplazo")
    class Acceso {

        @Test
        @DisplayName("get devuelve directamente E")
        void getDevuelveTipoGenerico() {
            lista.add("Java");

            String valor = lista.get(0);

            assertEquals("Java", valor);
        }

        @Test
        @DisplayName("set reemplaza el elemento y devuelve el anterior")
        void setReemplaza() {
            lista.add("Java");

            String anterior = lista.set(0, "Spring");

            assertAll(
                    () -> assertEquals("Java", anterior),
                    () -> assertEquals("Spring", lista.get(0))
            );
        }

        @Test
        @DisplayName("set permite almacenar null")
        void setPermiteNull() {
            lista.add("Java");

            String anterior = lista.set(0, null);

            assertAll(
                    () -> assertEquals("Java", anterior),
                    () -> assertNull(lista.get(0))
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
    // 4. REMOVE
    // ============================================================

    @Nested
    @DisplayName("4. Eliminar elementos")
    class Eliminar {

        @Test
        @DisplayName("remove(index) elimina y devuelve el elemento")
        void removePorIndice() {
            lista.add("Java");
            lista.add("Spring");
            lista.add("Maven");

            String eliminado = lista.remove(1);

            assertAll(
                    () -> assertEquals("Spring", eliminado),
                    () -> assertEquals(2, lista.size()),
                    () -> assertEquals("Java", lista.get(0)),
                    () -> assertEquals("Maven", lista.get(1))
            );
        }

        @Test
        @DisplayName("remove(Object) funciona aunque no esté implementado explícitamente en nuestra clase")
        void removePorObjetoHeredado() {
            lista.add("Java");
            lista.add("Spring");
            lista.add("Maven");

            boolean eliminado = lista.remove("Spring");

            assertAll(
                    () -> assertTrue(eliminado),
                    () -> assertEquals(2, lista.size()),
                    () -> assertFalse(lista.contains("Spring"))
            );
        }

        @Test
        @DisplayName("remove(Object) devuelve false cuando el elemento no existe")
        void removeObjetoInexistente() {
            lista.add("Java");

            assertFalse(lista.remove("Spring"));
        }
    }

    // ============================================================
    // 5. CONTAINS
    // ============================================================

    @Nested
    @DisplayName("5. Buscar elementos")
    class Buscar {

        @Test
        @DisplayName("contains encuentra elementos equivalentes usando equals")
        void containsUtilizaEquals() {
            lista.add(new String("Java"));

            assertTrue(lista.contains(new String("Java")));
        }

        @Test
        @DisplayName("contains(null) devuelve false")
        void containsNull() {
            assertFalse(lista.contains(null));
        }
    }

    // ============================================================
    // 6. CLEAR
    // ============================================================

    @Nested
    @DisplayName("6. Vaciar la lista")
    class Vaciar {

        @Test
        @DisplayName("clear elimina todos los elementos")
        void clearVaciaLista() {
            lista.add("Java");
            lista.add("Spring");

            lista.clear();

            assertAll(
                    () -> assertEquals(0, lista.size()),
                    () -> assertTrue(lista.isEmpty()),
                    () -> assertEquals("{}", lista.toString())
            );
        }

        @Test
        @DisplayName("Después de clear la lista puede reutilizarse")
        void clearPermiteReutilizar() {
            lista.add("Java");

            lista.clear();

            lista.add("Spring");

            assertAll(
                    () -> assertEquals(1, lista.size()),
                    () -> assertEquals("Spring", lista.get(0))
            );
        }
    }

    // ============================================================
    // 7. COMPORTAMIENTO DE LIST<E>
    // ============================================================

    @Nested
    @DisplayName("7. Comportamientos heredados de List y AbstractList")
    class ListYAbstractList {

        @Test
        @DisplayName("La clase puede utilizarse mediante una referencia List<E>")
        void puedeUsarseComoList() {
            List<String> coleccion = new ArrayList<>();

            coleccion.add("Java");
            coleccion.add("Spring");

            assertAll(
                    () -> assertEquals(2, coleccion.size()),
                    () -> assertEquals("Java", coleccion.get(0)),
                    () -> assertEquals("Spring", coleccion.get(1))
            );
        }

        @Test
        @DisplayName("indexOf funciona heredado desde AbstractList")
        void indexOfHeredado() {
            lista.add("Java");
            lista.add("Spring");
            lista.add("Maven");

            assertEquals(1, lista.indexOf("Spring"));
        }

        @Test
        @DisplayName("indexOf devuelve -1 para un elemento inexistente")
        void indexOfInexistente() {
            lista.add("Java");

            assertEquals(-1, lista.indexOf("Spring"));
        }

        @Test
        @DisplayName("lastIndexOf funciona heredado desde AbstractList")
        void lastIndexOfHeredado() {
            lista.add("Java");
            lista.add("Spring");
            lista.add("Java");

            assertEquals(2, lista.lastIndexOf("Java"));
        }

        @Test
        @DisplayName("addAll agrega una colección completa")
        void addAllHeredado() {
            lista.add("Java");

            boolean modificada = lista.addAll(
                    java.util.List.of("Spring", "Maven")
            );

            assertAll(
                    () -> assertTrue(modificada),
                    () -> assertEquals(3, lista.size()),
                    () -> assertEquals("Java", lista.get(0)),
                    () -> assertEquals("Spring", lista.get(1)),
                    () -> assertEquals("Maven", lista.get(2))
            );
        }

        @Test
        @DisplayName("containsAll permite comprobar una colección completa")
        void containsAllHeredado() {
            lista.add("Java");
            lista.add("Spring");
            lista.add("Maven");

            assertTrue(
                    lista.containsAll(
                            java.util.List.of("Java", "Maven")
                    )
            );
        }

        @Test
        @DisplayName("toArray devuelve los elementos en el mismo orden")
        void toArrayHeredado() {
            lista.add("Java");
            lista.add("Spring");
            lista.add("Maven");

            Object[] elementos = lista.toArray();

            assertArrayEquals(
                    new Object[]{"Java", "Spring", "Maven"},
                    elementos
            );
        }

        @Test
        @DisplayName("equals compara listas por contenido y orden")
        void equalsHeredado() {
            lista.add("Java");
            lista.add("Spring");

            java.util.List<String> otra =
                    java.util.List.of("Java", "Spring");

            assertEquals(otra, lista);
        }
    }

    // ============================================================
    // 8. ITERATOR
    // ============================================================

    @Nested
    @DisplayName("8. Iterator heredado desde AbstractList")
    class IteratorTests {

        @Test
        @DisplayName("La lista puede recorrerse con Iterator<E> sin implementarlo manualmente")
        void puedeRecorrerseConIterator() {
            lista.add("Java");
            lista.add("Spring");
            lista.add("Maven");

            Iterator<String> iterator = lista.iterator();

            assertAll(
                    () -> assertEquals("Java", iterator.next()),
                    () -> assertEquals("Spring", iterator.next()),
                    () -> assertEquals("Maven", iterator.next()),
                    () -> assertFalse(iterator.hasNext())
            );
        }

        @Test
        @DisplayName("Cada llamada a iterator crea un recorrido independiente")
        void iteradoresIndependientes() {
            lista.add("Java");
            lista.add("Spring");

            Iterator<String> it1 = lista.iterator();
            Iterator<String> it2 = lista.iterator();

            assertEquals("Java", it1.next());
            assertEquals("Spring", it1.next());

            assertEquals(
                    "Java",
                    it2.next(),
                    "El segundo iterador mantiene su propio estado"
            );
        }

        @Test
        @DisplayName("El iterador detecta una modificación estructural realizada fuera de él")
        void iteratorEsFailFast() {
            lista.add("Java");
            lista.add("Spring");
            lista.add("Maven");

            Iterator<String> iterator = lista.iterator();

            assertEquals("Java", iterator.next());

            /*
             * Modificamos la lista directamente después de haber
             * creado el iterador.
             */
            lista.add("Docker");

            assertThrows(
                    ConcurrentModificationException.class,
                    iterator::next
            );
        }

        @Test
        @DisplayName("remove del Iterator elimina el último elemento retornado por next")
        void iteratorRemove() {
            lista.add("Java");
            lista.add("Spring");
            lista.add("Maven");

            Iterator<String> iterator = lista.iterator();

            assertEquals("Java", iterator.next());

            iterator.remove();

            assertAll(
                    () -> assertEquals(2, lista.size()),
                    () -> assertEquals("Spring", lista.get(0)),
                    () -> assertEquals("Maven", lista.get(1))
            );
        }
    }

    // ============================================================
    // 9. FOR-EACH
    // ============================================================

    @Nested
    @DisplayName("9. Recorrido con for-each")
    class ForEach {

        @Test
        @DisplayName("La lista puede recorrerse con for-each")
        void recorridoConForeach() {
            lista.add("Java");
            lista.add("Spring");
            lista.add("Maven");

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
        @DisplayName("for-each recorre exactamente todos los elementos y en orden")
        void foreachRespetaOrden() {
            lista.add("Java");
            lista.add("Spring");
            lista.add("Maven");

            String[] esperados = {
                    "Java",
                    "Spring",
                    "Maven"
            };

            int indice = 0;

            for (String elemento : lista) {
                assertEquals(
                        esperados[indice],
                        elemento
                );
                indice++;
            }

            assertEquals(esperados.length, indice);
        }
    }

    // ============================================================
    // 10. RANDOM ACCESS
    // ============================================================

    @Nested
    @DisplayName("10. Interfaz marcadora RandomAccess")
    class RandomAccessTests {

        @Test
        @DisplayName("Nuestra lista declara soportar acceso aleatorio eficiente")
        void implementaRandomAccess() {
            assertTrue(
                    lista instanceof RandomAccess
            );
        }
    }

    // ============================================================
    // 11. CLONE
    // ============================================================

    @Nested
    @DisplayName("11. Clonación")
    class CloneTests {

        @Test
        @DisplayName("clone crea una nueva instancia con el mismo contenido")
        void cloneCreaNuevaLista() throws CloneNotSupportedException {
            lista.add("Java");
            lista.add("Spring");

            @SuppressWarnings("unchecked")
            ArrayList<String> clon =
                    (ArrayList<String>) lista.clone();

            assertAll(
                    () -> assertNotSame(lista, clon),
                    () -> assertEquals(lista, clon),
                    () -> assertEquals(2, clon.size())
            );
        }

        @Test
        @DisplayName("clone realiza una copia superficial de los elementos")
        void cloneEsSuperficial() throws CloneNotSupportedException {
            ArrayList<Object> original = new ArrayList<>();

            Object objeto = new Object();
            original.add(objeto);

            ArrayList<?> clon =
                    (ArrayList<?>) original.clone();

            assertSame(
                    objeto,
                    clon.get(0),
                    "Ambas listas contienen la misma referencia al objeto"
            );
        }

        @Test
        @DisplayName("Modificar estructuralmente el clon no modifica la lista original")
        void clonTieneEstructuraIndependiente() throws CloneNotSupportedException {
            lista.add("Java");
            lista.add("Spring");

            @SuppressWarnings("unchecked")
            ArrayList<String> clon =
                    (ArrayList<String>) lista.clone();

            clon.add("Maven");

            assertAll(
                    () -> assertEquals(2, lista.size()),
                    () -> assertEquals(3, clon.size())
            );
        }
    }

    // ============================================================
    // 12. CAPACIDAD
    // ============================================================

    @Nested
    @DisplayName("12. Administración de capacidad")
    class Capacidad {

        @Test
        @DisplayName("ensureCapacity permite ampliar la capacidad sin perder elementos")
        void ensureCapacityConservaElementos() {
            lista.add("Java");
            lista.add("Spring");

            lista.ensureCapacity(100);

            assertAll(
                    () -> assertEquals(2, lista.size()),
                    () -> assertEquals("Java", lista.get(0)),
                    () -> assertEquals("Spring", lista.get(1))
            );
        }

        @Test
        @DisplayName("trimToSize conserva exactamente los elementos almacenados")
        void trimToSizeConservaElementos() {
            ArrayList<String> otra = new ArrayList<>(100);

            otra.add("Java");
            otra.add("Spring");
            otra.add("Maven");

            otra.trimToSize();

            assertAll(
                    () -> assertEquals(3, otra.size()),
                    () -> assertEquals("Java", otra.get(0)),
                    () -> assertEquals("Spring", otra.get(1)),
                    () -> assertEquals("Maven", otra.get(2))
            );
        }

        @Test
        @DisplayName("Después de trimToSize la lista puede volver a crecer")
        void puedeCrecerDespuesDeTrimToSize() {
            ArrayList<String> otra = new ArrayList<>(100);

            otra.add("Java");
            otra.add("Spring");

            otra.trimToSize();

            otra.add("Maven");

            assertAll(
                    () -> assertEquals(3, otra.size()),
                    () -> assertEquals("Maven", otra.get(2))
            );
        }
    }

    // ============================================================
    // 13. POLIMORFISMO CON EL FRAMEWORK
    // ============================================================

    @Nested
    @DisplayName("13. Integración polimórfica con el Framework de Colecciones")
    class IntegracionFramework {

        @Test
        @DisplayName("Nuestra implementación puede asignarse a una referencia List<String>")
        void puedeUsarsePolimorficamenteComoList() {
            List<String> tecnologias = new ArrayList<>();

            tecnologias.add("Java");
            tecnologias.add("Spring");
            tecnologias.add("Maven");

            assertEquals(
                    java.util.List.of("Java", "Spring", "Maven"),
                    tecnologias
            );
        }

        @Test
        @DisplayName("Nuestra List puede interactuar con una implementación estándar de Java")
        void interactuaConListaJava() {
            List<String> propia = new ArrayList<>();
            propia.add("Java");
            propia.add("Spring");

            List<String> estandar =
                    new java.util.ArrayList<>();

            estandar.add("Java");
            estandar.add("Spring");

            assertEquals(
                    estandar,
                    propia,
                    "Ambas implementaciones cumplen el mismo contrato List"
            );
        }
    }

    // ============================================================
    // 14. TOSTRING
    // ============================================================

    @Nested
    @DisplayName("14. Representación textual propia")
    class ToStringTests {

        @Test
        @DisplayName("toString mantiene nuestra representación entre llaves")
        void toStringPropio() {
            lista.add("Java");
            lista.add("Spring");
            lista.add("Maven");

            assertEquals(
                    "{Java, Spring, Maven}",
                    lista.toString()
            );
        }
    }
}
