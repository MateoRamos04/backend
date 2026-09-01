package utnfc.backend.util;

import java.util.NoSuchElementException;

/**
 * @author philip.
 * @version 2023
 */
public class ListaArrayMejorada {
    // el arreglo que contendrá los elementos...
    private Object[] elementos;

    // el tamaño inicial del arreglo...
    private int capacidadInicial;

    // la cantidad de casillas realmente usadas...
    private int count;

    /**
     * Crea una lista con capacidad inicial de 10 casilleros, pero ninguno
     * ocupado realmente: la lista está vacía a todos los efectos prácticos.
     */
    public ListaArrayMejorada() {

        this(10);
    }

    /**
     * Crea una lista con capacidadInicial casilleros de capacidad, pero ninguno
     * ocupado realmente: la lista está vacía a todos los efectos prácticos. Si
     * el valor de capacidad es <= 0, el valor se ajusta a 10.
     *
     * @param capacidad la capacidad inicial de la lista.
     */
    public ListaArrayMejorada(int capacidad) {
        if (capacidad <= 0) {
            capacidad = 10;
        }
        elementos = new Object[capacidad];
        capacidadInicial = capacidad;
        count = 0;
    }

    /**
     * Añade al final de la lista el objeto e. La inserción será rechazada si la
     * referencia e es null.
     *
     * @param e el objeto a agregar en la lista.
     * @return true si la operación tuvo éxito.
     */
    public boolean agregar(Object e) {
        if (e == null) return false;

        if (count == elementos.length) this.asegurarCapacidad(elementos.length * 2);

        elementos[count] = e;
        count++;
        return true;
    }

    /**
     * Añade el objeto e en la posisicón index de la lista . La inserción será
     * rechazada si la referencia e es null (nen ese caso, el método sale sin
     * hacer nada). Si index coincide con el tamaño de la lista, el objeto e
     * será agregado exactamente al final de la lista, como si se hubiese
     * invocado a add(e).
     *
     * @param index el índice de la casilla donde debe quedar el objeto e.
     * @param e     el objeto a agregar en la lista.
     * @throws IndexOutOfBoundsException si index < 0 o index > size().
     */
    public void insertar(int index, Object e) {
        if (index > count || index < 0) {
            throw new IndexOutOfBoundsException("add(): índice fuera de rango...");
        }

        if (e == null) return;

        if (count == elementos.length) this.asegurarCapacidad(elementos.length * 2);

        int t = count - index;
        System.arraycopy(elementos, index, elementos, index + 1, t);
        elementos[index] = e;
        count++;
    }

    /**
     * Elimina todo el contenido de la lista, y reinicia su capacidad al valor
     * de la capacidad con que fue creada originalmente. La lista queda vacía
     * luego de invocar a clear().
     */
    public void clear() {
        elementos = new Object[capacidadInicial];
        count = 0;
    }

    /**
     * Devuelve true si la lista contiene al elemento e. Si e es null el método
     * retorna false. Puede lanzar una excepción de ClassCastException si la clase
     * de e no es compatible con el contenido de la lista.
     *
     * @param e el objeto a buscar en la lista.
     * @return true si la lista contiene al objeto e.
     * @throws ClassCastException si e no es compatible con los objetos de la lista.
     */
    public boolean contains(Object e) {
        if (e == null) return false;

        for (int i = 0; i < count; i++) {
            if (e.equals(elementos[i])) return true;
        }
        return false;
    }

    /**
     * Aumenta la capacidad del arreglo de soporte, si es necesario, para
     * asegurar que pueda contener al menos un número de elementos igual al
     * indicado por el parámetro capacidadMinima.
     *
     * @param capacidadMinima - la mínima capacidad requerida.
     */
    private void asegurarCapacidad(int capacidadMinima) {
        if (capacidadMinima == elementos.length) return;
        if (capacidadMinima < count) return;

        Object[] temp = new Object[capacidadMinima];
        System.arraycopy(elementos, 0, temp, 0, count);
        elementos = temp;
    }

    /**
     * Retorna el objeto contenido en la casilla index. Si el valor de index no
     * es válido, el método lanzará una excepción de la clase
     * IndexOutOfBoundsException.
     *
     * @param index índice de la casilla a acceder.
     * @return referencia al objeto contenido en la casilla index.
     * @throws IndexOutOfBoundsException si index < 0 o index >= size().
     */
    public Object get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("get(): índice fuera de rango...");
        }
        return elementos[index];
    }

    /**
     * Devuelve true si la lista no contiene elementos.
     *
     * @return true si la lista está vacía.
     */
    public boolean isEmpty() {
        return (count == 0);
    }

    /**
     * Remueve de la lista el elemento contenido en la posición index. Los
     * objetos ubicados a la derecha de este, se desplazan un casillero a la
     * izquierda. El objeto removido es retornado. La capacidad de la lista no
     * se altera. Si el valor de index no es válido, el método lanzará una
     * excepción de IndexOutOfBoundsException.
     *
     * @param index el índice de la casilla a remover.
     * @return el objeto removido de la lista.
     * @throws IndexOutOfBoundsException si index < 0 o index >= size().
     */
    public Object quitar(int index) {
        if (index >= count || index < 0) {
            throw new IndexOutOfBoundsException("remove(): índice fuera de rango...");
        }

        int t = elementos.length;
        if (count < t / 2) this.asegurarCapacidad(t / 2);

        Object old = elementos[index];
        int n = count;
        System.arraycopy(elementos, index + 1, elementos, index, n - index - 1);
        count--;
        elementos[count] = null;
        return old;
    }

    /**
     * Reemplaza el objeto en la posición index por el referido por element, y
     * retorna el objeto originalmente contenido en la posición index. Si el
     * valor de index no es válido, el método lanzará una excepción de la clase
     * IndexOutOfBoundsException.
     *
     * @param index   índice de la casilla a acceder.
     * @param element el objeto que será ubicado en la posición index.
     * @return el objeto originalmente contenido en la posición index.
     * @throws IndexOutOfBoundsException si index < 0 o index >= size().
     */
    public Object set(int index, Object element) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("set(): índice fuera de rango...");
        }
        Object old = elementos[index];
        elementos[index] = element;
        return old;
    }

    /**
     * Retorna el tamaño de la lista: la cantidad de elementos realmente
     * contenidos en ella.
     *
     * @return la cantidad de elementos que la lista contiene.
     */
    public int size() {
        return count;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append('{');
        for (int i = 0; i < count; i++) {
            buff.append(elementos[i]);
            if (i < count - 1) {
                buff.append(", ");
            }
        }
        buff.append('}');
        return buff.toString();
    }

    public IteradorLineal iterador() {
        return new IteradorLineal();
    }

    /**
     * Clase que implementa el iterador de la versión anterior pero de forma de poder tener tantos iteradores como haga
     * cada uno independiente de los demás.
     *
     */
    public class IteradorLineal {

        // índice de la posición actual del iterador
        private int actual;
        public IteradorLineal() {

            actual = 0;
        }

        public boolean hayMas() {
            if(isEmpty()) { return false; }
            if(actual >= size() - 1) { return false; }
            return true;
        }

        public Object getActual() {
            if (actual >= count) {
                throw new NoSuchElementException("next(): no quedan elementos por recorrer...");
            }
            return elementos[actual];
        }

        public void siguiente() {
            if (!hayMas()) {
                throw new NoSuchElementException("next(): no quedan elementos por recorrer...");
            }
            actual++;
        }

    }
}
