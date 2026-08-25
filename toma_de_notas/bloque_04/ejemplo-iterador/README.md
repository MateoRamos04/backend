# ListaArreglo: del array al Iterator de Java

## Objetivo del ejemplo

Este proyecto no intenta construir una colección lista para producción. Lo usamos para observar, paso a paso, cómo podemos separar una estructura que almacena objetos del mecanismo que permite recorrerla.

La evolución que seguimos es deliberadamente visible:

1. guardamos elementos en un array;
2. recorremos mediante índices;
3. trasladamos el estado del recorrido a un `Iterator`;
4. hacemos que la lista implemente `Iterable`;
5. llegamos al `foreach`;
6. identificamos los límites de trabajar con `Object`;
7. incorporamos Generics;
8. obtenemos `ListaArreglo<T>`, `Iterator<T>` e `Iterable<T>` sin casts.

Conservamos dos implementaciones diferentes. `utnfc.backend.iterador.utilidades.ListaArreglo` es la versión sin Generics; `utnfc.backend.iterador.utilidades.generics.ListaArreglo` es la evolución genérica. La comparación entre ambas es el centro de la actividad.

## 1. Nuestro punto de partida: una lista basada en un array

La primera lista contiene estos atributos:

```java
private Object[] items;
private int cantidad;
```

`items` es el array donde existen posiciones disponibles. `cantidad` indica cuántas de esas posiciones contienen elementos que agregamos. Por eso no significan lo mismo:

- `items.length` es la **capacidad** reservada;
- `cantidad` es el número de **elementos efectivamente almacenados**.

Si construimos un array con capacidad 10 y agregamos tres objetos, `items.length` vale 10 y `cantidad` vale 3. Las posiciones 3 a 9 no forman parte lógica de la lista aunque físicamente existan. Esta diferencia es esencial: un recorrido debe terminar en `cantidad`, nunca en `items.length`.

El constructor por defecto elige una capacidad inicial de 10. El constructor que recibe un entero permite observar el crecimiento usando capacidades pequeñas.

## 2. Agregando elementos

`agregar(Object dato)` almacena el objeto en `items[cantidad]` y después incrementa `cantidad`. Mientras queda capacidad, esa operación es directa.

Cuando `cantidad == items.length`, no existe una siguiente posición libre. Como un array no puede cambiar de tamaño, creamos otro de mayor capacidad, copiamos los elementos en el mismo orden y reemplazamos la referencia interna:

```java
int nuevaCapacidad = items.length + Math.max(1, items.length / 2);
Object[] aux = new Object[nuevaCapacidad];
for (int i = 0; i < items.length; i++) {
    aux[i] = items[i];
}
items = aux;
```

El `Math.max(1, ...)` asegura que también podamos crecer desde capacidades 0 y 1. No cambia la idea didáctica: seguimos creando un array y copiando manualmente. Después del crecimiento, `cantidad` conserva el número real de elementos y `items.length` representa la nueva capacidad.

`quitar()` y `limpiar()` se mantienen deliberadamente pendientes. Los mensajes “Haganlo uds.” señalan ejercicios posibles y no fueron completados de manera silenciosa.

## 3. Recuperando elementos mediante índice

`getItem(int idx)` entrega la referencia guardada cuando `idx` está entre cero y `cantidad - 1`. El contrato histórico de este ejemplo devuelve `null` para índices inválidos; no lanza `IndexOutOfBoundsException`. `setItem()` reemplaza una posición válida sin modificar `cantidad` e ignora índices inválidos.

Estos contratos son sencillos para la actividad, aunque una colección de producción podría tomar otras decisiones. Los tests los dejan explícitos.

## 4. Primer mecanismo de recorrido: for por índice

Podemos comenzar recorriendo la lista así:

```java
for (int i = 0; i < lista.getCantidad(); i++) {
    Fraccion f = (Fraccion) lista.getItem(i);
    System.out.println(f);
}
```

Este código funciona, pero quien usa la lista conoce y controla varios detalles: sabe que hay posiciones numéricas, mantiene el índice, consulta el límite y solicita cada elemento. El algoritmo de recorrido queda repetido y acoplado al código cliente.

Además, `getItem()` devuelve `Object`. Java solamente sabe que recibió algún objeto, aunque nosotros hayamos agregado una `Fraccion`. Necesitamos el cast `(Fraccion)` para recuperar el tipo específico y usar sus operaciones. Un cast incorrecto puede compilar y fallar después con `ClassCastException`.

## 5. ¿Qué problema queremos resolver?

Necesitamos separar dos responsabilidades:

- `ListaArreglo` sabe dónde y cuántos elementos almacena;
- otro objeto sabe en qué punto de un recorrido se encuentra y cuál es el siguiente elemento.

Queremos que el código cliente pueda pedir elementos sucesivos sin conocer el array, `cantidad` ni una posición. El objeto que encapsula ese algoritmo y su estado es un `Iterator`.

## 6. Construyendo nuestro Iterator

La lista declara una clase interna:

```java
private class IteradorLineal implements Iterator {
    private int actual;
    // ...
}
```

`actual` representa la posición del próximo elemento que ese iterador entregará. Comienza en cero. Después de devolver un elemento se incrementa.

El atributo pertenece al iterador, no a la lista. Cada iterador mantiene su propio estado. Si una posición de recorrido fuera atributo de `ListaArreglo`, dos recorridos simultáneos se interferirían.

## 7. hasNext()

`hasNext()` responde si queda al menos un elemento lógico por recorrer:

```java
public boolean hasNext() {
    return actual < cantidad;
}
```

La comparación usa `cantidad`, porque el iterador recorre elementos almacenados. Usar `items.length` haría que también visitáramos las posiciones libres que solamente representan capacidad.

`hasNext()` no avanza el recorrido. Podemos consultarlo varias veces y `actual` permanece igual.

## 8. next()

Cuando existe un siguiente elemento, `next()` realiza tres acciones:

```java
Object resp = getItem(actual);
actual++;
return resp;
```

Primero obtiene el elemento de la posición actual, luego avanza y finalmente devuelve la referencia obtenida.

El código antiguo devolvía `null` si se invocaba `next()` después del final. Eso no cumple el contrato estándar de `java.util.Iterator`, que espera una `NoSuchElementException`. Como la meta es llegar al Iterator que Java espera, ambas implementaciones ahora hacen explícitamente:

```java
if (!hasNext()) {
    throw new NoSuchElementException();
}
```

Esta corrección es diferente del contrato histórico de `getItem()`: `getItem()` conserva su `null` para un índice inválido, mientras `Iterator.next()` cumple el contrato propio de la interfaz Java.

## 9. Clases internas

`IteradorLineal` es una clase interna no estática. Cada instancia está vinculada a la instancia exterior de `ListaArreglo` que la creó. Por eso puede acceder directamente a `cantidad` e invocar `getItem()` sin recibirlos como parámetros.

La lista conserva el conocimiento sobre su representación y el iterador conserva el estado `actual`. La clase interna permite expresar esta colaboración sin convertir el iterador en parte de la API pública.

## 10. Implementando Iterable

La declaración:

```java
public class ListaArreglo implements Iterable
```

obliga a implementar `iterator()`:

```java
@Override
public Iterator iterator() {
    return new IteradorLineal();
}
```

`iterator()` **no recorre** la lista. Su responsabilidad es crear y devolver el objeto que realizará el recorrido. Cada llamada construye uno nuevo:

```java
Iterator it1 = lista.iterator();
Iterator it2 = lista.iterator();
```

Aunque ambos miran la misma lista, cada uno comienza con su propio `actual` en cero. Avanzar `it1` no mueve `it2`. Un test específico verifica esta propiedad porque muestra por qué el estado pertenece al Iterator.

## 11. Recorriendo con while + Iterator

Ahora podemos escribir:

```java
Iterator it = lista.iterator();
while (it.hasNext()) {
    Fraccion aux = (Fraccion) it.next();
    System.out.println(aux);
}
```

El código cliente ya no controla posiciones, no conoce el array, no consulta `cantidad` y no necesita saber cómo está implementada la estructura. Solamente pregunta si queda algo y solicita lo siguiente. El Iterator encapsula el algoritmo lineal de recorrido.

Seguimos necesitando un cast porque este primer `Iterator` devuelve `Object`. La independencia del recorrido y la seguridad de tipos son problemas diferentes que resolvemos en etapas distintas.

## 12. Llegando al foreach

Una clase puede aparecer a la derecha de `:` en un `foreach` porque implementa `Iterable`:

```java
for (Object item : lista) {
    Fraccion aux = (Fraccion) item;
}
```

`foreach` no es un mecanismo independiente. Conceptualmente, Java sigue esta cadena:

```text
foreach
   ↓
Iterable
   ↓
iterator()
   ↓
Iterator
   ↓
hasNext() / next()
```

La sintaxis oculta la administración repetitiva del Iterator, pero utiliza el mismo contrato que acabamos de construir.

## 13. Probando con objetos: Fraccion

`Fraccion` nos permite comprobar que la lista guarda referencias a objetos de dominio, no solamente `String` o `Integer`. Podemos crear fracciones, sumarlas, dividir una suma para obtener el promedio, compararlas y mostrarlas:

- `sumarA()` produce una nueva suma simplificada;
- `dividirPor()` permite calcular la media;
- `compareTo()` ordena conceptualmente por valor;
- `valorReal()` obtiene la representación decimal;
- `toString()` muestra numerador y denominador.

Las demos generan 20 fracciones, acumulan su suma y dividen por `lista.getCantidad()`. Luego cuentan cuántas son mayores que esa media mediante índice, Iterator y foreach. `Math.random()` es aceptable en estas demostraciones visuales; los tests usan siempre datos conocidos.

`Fraccion` conserva `Comparable` sin parámetro para no anticipar la evolución didáctica hacia Generics dentro de la etapa raw.

## 14. El problema de Object

La lista sin Generics permite agregar cualquier referencia:

```java
ListaArreglo lista = new ListaArreglo();
lista.agregar(new Fraccion(1, 2));
lista.agregar("texto"); // tambien compila
```

El compilador no puede garantizar que todo sea `Fraccion`. Los tres recorridos necesitan casts:

```java
Fraccion a = (Fraccion) lista.getItem(i);
Fraccion b = (Fraccion) it.next();
Fraccion c = (Fraccion) item;
```

Si aparece un `String`, el problema se detectará durante la ejecución. Queremos expresar el tipo esperado una sola vez y propagarlo a todas las operaciones.

## 15. Incorporando Generics

Comparamos estas declaraciones:

```java
ListaArreglo lista = new ListaArreglo();
ListaArreglo<Fraccion> lista = new ListaArreglo<>();
```

En la segunda, `Fraccion` es el argumento de tipo. Indica al compilador qué clase de elementos puede almacenar esa instancia. La letra `T` en la implementación es un parámetro que será sustituido conceptualmente por el tipo elegido.

## 16. ListaArreglo<T>

La evolución genérica se declara:

```java
public class ListaArreglo<T> implements Iterable<T>
```

El mismo `T` aparece en todo el contrato:

```java
private T[] items;
public void agregar(T dato)
public T getItem(int idx)
public void setItem(T dato, int idx)
public Iterator<T> iterator()
```

Cuando construimos `ListaArreglo<Fraccion>`, `agregar()` admite fracciones, `getItem()` devuelve una `Fraccion` e `iterator()` produce un `Iterator<Fraccion>`. El tipo se propaga desde la declaración de la lista hasta cada forma de recorrido.

### La particularidad del array genérico

Java no permite escribir directamente `new T[10]`. En tiempo de ejecución no dispone de `T` como un tipo concreto con el cual crear ese array, debido a las restricciones vinculadas al borrado de tipos.

Para mantener el array visible en esta actividad, la implementación crea un `Object[]` y realiza un cast controlado:

```java
items = (T[]) new Object[pTam];
```

El compilador no puede comprobar ese cast y por eso el constructor marca localmente `@SuppressWarnings("unchecked")`. La clase protege el uso del array haciendo que sus métodos públicos reciban y devuelvan `T`. El crecimiento repite la misma estrategia. No utilizamos reflexión ni APIs internas del JDK.

## 17. Iterator<T> e Iterable<T>

La clase interna genérica implementa `Iterator<T>` y su `next()` devuelve `T`. Para una lista de fracciones podemos escribir:

```java
Iterator<Fraccion> it = lista.iterator();
while (it.hasNext()) {
    Fraccion aux = it.next();
}
```

También podemos usar:

```java
for (Fraccion item : lista) {
    System.out.println(item);
}
```

No hay casts. Además, `lista.agregar("texto")` no compila cuando `lista` es `ListaArreglo<Fraccion>`. No intentamos convertir esta restricción en un test unitario: los tests ejecutan código que ya compiló, mientras esta garantía actúa precisamente durante la compilación.

## 18. Comparación lado a lado

| Aspecto | Sin Generics | Con Generics |
|---|---|---|
| Declaración | `ListaArreglo lista` | `ListaArreglo<Fraccion> lista` |
| Agregar | `agregar(Object)` | `agregar(Fraccion)` para esa instancia |
| `getItem` | devuelve `Object` | devuelve `Fraccion` |
| Iterator | `Iterator` | `Iterator<Fraccion>` |
| foreach | `for (Object item : lista)` | `for (Fraccion item : lista)` |
| Casts | necesarios al recuperar | no son necesarios |
| Error de tipo | puede aparecer en ejecución | se detecta al compilar |
| Recorrido | independiente mediante Iterator | igual mecanismo, ahora tipado |

## 19. Pruebas unitarias

Usamos JUnit Jupiter. Las pruebas separan claramente ambos modelos y verifican:

- construcción, cantidad, agregado, orden y crecimiento;
- contratos reales de `getItem()` y `setItem()`;
- recorrido vacío, de uno y de varios elementos;
- cambios de `hasNext()` y orden de `next()`;
- `NoSuchElementException` al agotar el Iterator;
- independencia entre dos iteradores de la misma lista;
- recorrido mediante foreach hasta `cantidad`, no hasta capacidad;
- almacenamiento y recuperación de `Fraccion`, con casts y sin ellos;
- suma, promedio y conteo deterministas;
- operaciones mínimas de `Fraccion` que sustentan esos escenarios.

Los tests no usan valores aleatorios ni dependen de la salida de consola. Verifican estado y comportamiento repetibles. No prueban directamente que código inválido no compile; esa propiedad se explica y puede observarse descomentando la línea correspondiente de la demo genérica.

Podemos ejecutarlos con:

```bash
mvn clean test
```

## 20. Conclusiones

Comenzamos con un `Object[]` y un contador. El recorrido por índice nos obligó a conocer posiciones y detalles de la estructura. Luego creamos un Iterator cuyo atributo `actual` conserva el estado de un recorrido independiente. Al implementar `Iterable`, la lista pudo entregar nuevos iteradores y Java pudo usarla con foreach.

Finalmente observamos que `Object` obliga a hacer casts y posterga algunos errores hasta la ejecución. Al incorporar `T`, el tipo elegido en `ListaArreglo<Fraccion>` atraviesa `agregar`, `getItem`, `Iterator<T>`, `Iterable<T>` y foreach. Así eliminamos casts y obtenemos seguridad de tipos en compilación sin cambiar la idea fundamental del recorrido.

## Próximos pasos

Este ejemplo prepara el camino para estudiar con más detalle:

- `Collection`;
- `List`;
- `AbstractList`;
- `ArrayList`;
- `LinkedList`;
- `Set`;
- `Queue`;
- Generics con mayor profundidad.
