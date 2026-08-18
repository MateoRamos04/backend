package ar.edu.utn.frc.back;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.utn.frc.back.ListaEnArray;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListaEnArrayTest {

    private ListaEnArray<Integer> lista;
    private final int[] numbers = new int[]{10, 9, 5, 1, 8, 4, 2, 7, 3, 6};

    @BeforeEach
    public void setup() {
        lista = new ListaEnArray<Integer>();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(lista.isEmpty());
    }

    @Test
    public void testIsEmptyWhenNotEmpty() {
        lista.add(1);
        assertFalse(lista.isEmpty());
    }

    @Test
    public void testGetWithNegativeIndex() {
        addNumbers();
        assertThrows(IndexOutOfBoundsException.class, () -> lista.get(-1));
    }

    @Test
    public void testGetWithLargeIndex() {
        addNumbers();
        assertThrows(IndexOutOfBoundsException.class, () -> lista.get(lista.size()));
    }

    @Test
    public void testGetWithEmptyList() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.get(0));
    }

    @Test
    public void testSizeEmptyList() {
        assertEquals(0, lista.size());
    }

    @Test
    public void testSize() {
        for (int i = 0; i < numbers.length; i++) {
            lista.add(numbers[i]);
            assertEquals(i + 1, lista.size());
        }
    }

    @Test
    public void testIncreaseCapacity() {
        for (int i = 0; i < 100; i++) {
            lista.add(i);
        }
        assertEquals(100, lista.size());
        assertEquals(99, lista.get(99));
    }

    @Test
    public void testGet() {
        addNumbers();
        for (int i = 0; i < numbers.length; i++) {
            assertEquals(numbers[i], lista.get(i));
        }
    }

    @Test
    public void testToString() {
        addNumbers();
        assertEquals("[10, 9, 5, 1, 8, 4, 2, 7, 3, 6, ]", lista.toString());
    }

    @Test
    public void testIterator() {
        addNumbers();
        Iterator<Integer> it = lista.iterator();
        for (int i = 0; i < numbers.length; i++) {
            assertTrue(it.hasNext());
            assertEquals(numbers[i], it.next());
        }
        assertFalse(it.hasNext());

    }

    private void addNumbers() {
        for (int i = 0; i < numbers.length; i++) {
            lista.add(numbers[i]);
        }
    }


}