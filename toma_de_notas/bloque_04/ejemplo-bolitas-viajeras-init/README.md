    # Las Bolitas Viajeras

Este proyecto es un ejemplo didáctico de la asignatura Backend de Aplicaciones de Ingeniería en Sistemas de Información. Nuestro objetivo no es construir una aplicación productiva: usamos un circuito de bolitas para hacer visibles la herencia, las clases abstractas, la redefinición de métodos, el despacho dinámico y el polimorfismo.

Todo el modelo pertenece al package `utnfc.back.bolitas` y utiliza Java 21 y Maven.

## Paso 1: construcción del modelo base

### 1. El problema

Imaginemos una bolita que avanza por un circuito armado con piezas encastrables. Algunas piezas representan tubos, otras detienen la bolita, otras la aceleran y otras deciden entre dos caminos. Queremos poder conectar esas piezas y transportar distintas bolitas por el circuito.

Podemos reconocer dos grupos de objetos:

- la `Bolita`, que posee un estado que cambia durante el viaje;
- los `Tramo`, que forman el circuito y producen esos cambios.

La pregunta central no es solamente “¿qué datos guardamos?”, sino “¿cómo conseguimos que cada pieza responda de una manera diferente sin llenar el recorrido general de decisiones sobre clases concretas?”.

### 2. La bolita y su estado

Una `Bolita` conoce:

- si es lisa (`true`) o texturada (`false`);
- su peso;
- su velocidad en metros por segundo;
- el tiempo transcurrido en segundos;
- la longitud total recorrida en centímetros;
- el tramo actual.

Al crearla indicamos las tres características iniciales:

```java
Bolita bolita = new Bolita(true, 20, 2);
```

En este ejemplo la bolita es lisa, pesa 20 unidades y comienza a 2 m/s. Su longitud, su tiempo y su tramo actual comienzan en cero, cero y `null`, respectivamente.

La bolita sabe realizar operaciones sencillas sobre su propio estado:

- `parar(segundos)` acumula tiempo;
- `acelerar(metrosPorSegundo)` modifica la velocidad (un valor negativo la desacelera);
- `recorrer(centimetros)` acumula longitud y calcula el tiempo como metros recorridos dividido velocidad;
- `setTramoActual(tramo)` registra dónde se encuentra.

Por ejemplo, recorrer 250 cm a 2 m/s requiere `2.5 / 2 = 1.25` segundos. Si luego recorremos otro tubo, tanto la longitud como el tiempo se suman a lo que ya llevaba la bolita.

### 3. Los tramos y la construcción del circuito

Cada pieza necesita conocer la siguiente. Si utilizáramos un tipo diferente para cada posible salida, una pieza debería anticipar cuál clase concreta vendrá después. En cambio, todos los elementos del circuito comparten el concepto general `Tramo`:

```java
Tramo primero = new Tubo(100, "primer tubo");
Tramo segundo = new Detencion(5, "pausa");

primero.encastrarSalida(segundo);
```

La salida está declarada como `Tramo`, por lo que puede contener un `Tubo`, una `Detencion`, un `Acelerador`, un desvío u otra especialización. También puede ser `null`: en un tramo normal eso representa el final del circuito y es una configuración válida.

### 4. El problema de preguntar por cada clase

Una primera solución podría recorrer el circuito preguntando qué objeto encontramos:

```java
if (tramo instanceof Tubo) {
    // lógica del tubo
} else if (tramo instanceof Detencion) {
    // lógica de la detención
} else if (tramo instanceof Acelerador) {
    // lógica del acelerador
}
```

Esta estructura coloca en el algoritmo general conocimientos que pertenecen a cada tramo. Cada vez que aparece una nueva clase debemos volver allí, agregar otro caso y correr el riesgo de afectar lo que ya funcionaba. Además, el objeto queda reducido a un dato que otro código interpreta, en vez de ser responsable de su comportamiento.

Queremos enviar el mismo mensaje a todos los tramos y dejar que cada objeto concreto decida cómo responder.

### 5. `Tramo` como clase abstracta

`Tramo` expresa todo lo común:

- un nombre;
- una salida normal;
- la posibilidad de encastrar y consultar esa salida;
- la validación de configuración;
- la operación general de transporte.

Sin embargo, no existe una manera general de avanzar aplicable a cualquier tramo. Por eso `Tramo` es abstracta y declara:

```java
public abstract Tramo avanzar(Bolita b);
```

El parámetro es la bolita afectada. El resultado es el próximo tramo. Un resultado `null` indica que no queda otra pieza por recorrer.

Al ser abstracta, `Tramo` no puede instanciarse directamente. Nos sirve como concepto común, contrato y tipo de referencia. Toda subclase concreta debe implementar —o redefinir— `avanzar()`.

### 6. Redefinición y despacho dinámico

Redefinir significa que una subclase proporciona su propia implementación de un método declarado por su superclase. La firma compartida permite escribir:

```java
Tramo tramo = new Tubo(100, "tubo");
tramo.avanzar(bolita);

tramo = new Acelerador("acelerador");
tramo.avanzar(bolita);
```

El tipo declarado de la variable sigue siendo `Tramo`, pero primero referencia un `Tubo` y luego un `Acelerador`. En la primera llamada Java ejecuta `Tubo.avanzar()`; en la segunda ejecuta `Acelerador.avanzar()`.

Esa selección en tiempo de ejecución se denomina **despacho dinámico**. Java considera la clase concreta del objeto referenciado, no solamente el tipo escrito a la izquierda de la variable. Esta propiedad permite la sustitución: distintas subclases pueden ocupar el lugar esperado para el tipo base.

En nuestro modelo, este punto es fundamental. El recorrido puede conocer sólo `Tramo`, mientras el mensaje `avanzar(bolita)` puede terminar ejecutando:

- `Tubo.avanzar(...)`;
- `Acelerador.avanzar(...)`;
- `Detencion.avanzar(...)`;
- `DesvioLisa.avanzar(...)`;
- `DesvioPeso.avanzar(...)`;
- y, en la extensión, `TuboCurvo.avanzar(...)` o `DesvioVelocidad.avanzar(...)`.

### 7. Primeras especializaciones

#### Tubo

Un `Tubo` agrega una longitud en centímetros. Al avanzar:

1. se registra como tramo actual;
2. pide a la bolita que recorra su longitud;
3. la bolita acumula esa longitud y el tiempo correspondiente;
4. retorna la salida normal.

La esencia de su implementación es:

```java
public Tramo avanzar(Bolita b) {
    b.setTramoActual(this);
    b.recorrer(longitud);
    return salida;
}
```

Si `salida` es `null`, el propio retorno señala el final.

#### Detencion

Una `Detencion` guarda una cantidad configurable de segundos. Registra el tramo, ejecuta `b.parar(segundos)` y retorna la salida. No modifica longitud ni velocidad.

#### Acelerador

Un `Acelerador` registra el tramo, demora siempre 10 segundos, aumenta la velocidad en 3 m/s y retorna la salida:

```java
b.setTramoActual(this);
b.parar(10);
b.acelerar(3);
return salida;
```

El recorrido general no contiene ninguna de estas reglas. Sólo llama a `avanzar()`.

### 8. Cuando una pieza tiene dos salidas

Un desvío comparte nombre y salida normal con cualquier tramo, pero además necesita una salida `alternativa`. Esa característica es común a varias decisiones distintas. Por eso `Desvio` es una abstracción intermedia:

```text
Tramo
├── Tubo
├── Detencion
├── Acelerador
└── Desvio (abstracta)
    ├── DesvioLisa
    └── DesvioPeso
```

`Desvio` también puede ser abstracta porque saber que existen dos salidas no alcanza para decidir cuál tomar. Las subclases concretas aportan ese criterio mediante `avanzar()`.

Un tramo normal acepta `salida == null` como final. En cambio, un desvío sólo está listo para decidir si posee ambos caminos:

```java
public boolean isBienConfigurado() {
    return salida != null && alternativa != null;
}
```

Si falta cualquiera, los desvíos del modelo registran el tramo actual y retornan `this`. De ese modo señalan que la bolita no pudo abandonarlos. `transportar()` reconoce ese resultado y se detiene, evitando una recursión infinita.

#### DesvioLisa

`DesvioLisa` preserva exactamente este criterio:

- bolita lisa: salida normal;
- bolita texturada (`!b.isLisa()`): alternativa.

Podemos cambiar entre ambas bolitas sin tocar el algoritmo de recorrido.

#### DesvioPeso

`DesvioPeso` posee un peso mínimo configurable. Su comparación exacta es:

- peso menor o igual al límite: salida normal;
- peso estrictamente mayor al límite: alternativa.

El caso de igualdad es importante: no toma la alternativa.

### 9. `avanzar()` y `transportar()` no hacen lo mismo

`avanzar()` procesa **un solo tramo** y devuelve cuál viene después. Esto permite observar o controlar el viaje paso a paso:

```java
Tramo actual = tramoInicial;

while (actual != null) {
    actual = actual.avanzar(bolita);
}
```

En cada iteración la variable `actual` sigue siendo de tipo `Tramo`. No preguntamos qué clase contiene. El despacho dinámico selecciona la implementación correcta y el valor retornado conduce a la siguiente pieza.

`transportar()` pertenece a `Tramo` y recorre el circuito completo de forma recursiva:

```java
public void transportar(Bolita b) {
    Tramo proximo = avanzar(b);
    if (proximo != null && proximo != this) {
        proximo.transportar(b);
    }
}
```

Conceptualmente ocurre lo siguiente:

1. el tramo actual recibe `avanzar(b)`;
2. su clase concreta modifica la bolita y retorna el próximo objeto;
3. si existe un próximo tramo diferente del actual, recibe `transportar(b)`;
4. ese nuevo objeto vuelve a llamar polimórficamente a su propio `avanzar()`;
5. la cadena termina ante una salida `null` o un desvío incompleto que se retorna a sí mismo.

Aunque `transportar()` se implementa una sola vez en `Tramo`, cada paso produce comportamientos diferentes. La recursión organiza el recorrido; `avanzar()` expresa la regla particular de la pieza.

### 10. Un circuito de referencia

La clase `App` construye un circuito con varios objetos declarados como `Tramo`. Su estructura principal puede leerse así:

```text
Tubo tb1 → DesvioLisa dl
              ├── salida → Tubo tb2 → Acelerador ac → Detencion dt → fin
              └── alternativa → Tubo tb3 → TuboCurvo tc → DesvioVelocidad dv
                                                        ├── salida → Tubo tb4 → fin
                                                        └── alternativa → tc
```

La última alternativa forma un ciclo. No se agregó un contador artificial: al atravesar `TuboCurvo`, la velocidad disminuye. Cuando deja de ser mayor que el límite de `DesvioVelocidad`, la bolita toma la salida normal y abandona el ciclo. Naturalmente, las condiciones iniciales deben permitir que la velocidad siga siendo válida para calcular los tiempos.

`App` conserva las dos maneras de ejecutar el modelo:

```java
tramoInicial.transportar(bolita);
```

y:

```java
Tramo tramo = tramoInicial;
while (tramo != null) {
    tramo = tramo.avanzar(bolita);
}
```

También crea objetos que no necesariamente forman parte del mismo camino. En un ejemplo didáctico pueden servir para ensayar variantes sin alterar el circuito principal.

## Paso 2: extensión del modelo

Ahora podemos pensar como si el cliente presentara nuevas necesidades. Ya tenemos un modelo funcionando: queremos incorporar variantes sin destruirlo ni reescribir el recorrido general.

### A. Un desvío según la velocidad

`DesvioVelocidad` hereda de `Desvio`, por lo que ya posee nombre, salida normal, alternativa y validación. Sólo agrega un límite configurable y redefine la decisión:

```java
if (b.getVelocidad() > velocidadMin) {
    proximo = alternativa;
}
```

Por lo tanto:

- `velocidad > límite`: alternativa;
- `velocidad <= límite`: salida normal.

La igualdad toma la salida normal. Si faltan salidas, al igual que los demás desvíos, retorna `this`.

Para agregar esta clase no necesitamos modificar `Tramo`, `DesvioLisa`, `DesvioPeso`, `Tubo`, `Detencion`, `Acelerador` ni el algoritmo general. Donde se acepta un `Tramo`, también podemos encastrar un `DesvioVelocidad`. Esta extensión muestra el valor práctico del polimorfismo: agregamos una respuesta nueva al mismo mensaje sin enseñar esa clase al recorrido.

### B. Un tubo curvo

`TuboCurvo` es una especialización de `Tubo`. Sigue siendo una pieza con longitud, recorre esa longitud y retorna la salida, pero antes desacelera una cantidad configurable:

```java
public Tramo avanzar(Bolita b) {
    b.setTramoActual(this);
    b.acelerar(-n);
    b.recorrer(getLongitud());
    return salida;
}
```

Tiene sentido especializar `Tubo` porque el concepto y la estructura siguen siendo los de un tubo. Heredamos la longitud y `getLongitud()` en vez de duplicarlos. Redefinimos `avanzar()` porque el orden importa: la velocidad baja antes de calcular el tiempo requerido por la longitud.

La implementación hace visible la redefinición completa. Sigue respetando el contrato: recibe una `Bolita` y retorna un `Tramo`.

### C. Una bolita con historia

El cliente también necesita mostrar por dónde pasó una bolita. `BolitaConHistoria` especializa `Bolita` y mantiene una lista ordenada de tramos. Aprovecha una colaboración que ya existía: todo tramo notifica su presencia mediante `setTramoActual(this)`.

Podemos redefinir esa operación:

```java
public void setTramoActual(Tramo tramoActual) {
    super.setTramoActual(tramoActual);
    tramosPasados.add(tramoActual);
}
```

Primero conservamos el comportamiento original con `super`: el tramo actual sigue actualizado. Después agregamos el mismo objeto a la historia. `mostrarRecorrido()` presenta la lista y `toString()` combina el estado heredado con ese recorrido.

Los tramos no fueron modificados y continúan declarando:

```java
public Tramo avanzar(Bolita b)
```

Sin embargo, podemos entregarles una instancia de `BolitaConHistoria` porque también **es una** `Bolita`:

```java
Bolita bolita = new BolitaConHistoria(false, 22, 2);
tramoInicial.transportar(bolita);
```

Cuando el tramo ejecuta `b.setTramoActual(this)`, el despacho dinámico llama a la redefinición de `BolitaConHistoria`. Éste es un segundo eje de sustitución: antes sustituíamos distintos `Tramo`; ahora sustituimos una `Bolita` por una especialización sin que los tramos necesiten conocerla.

## Pruebas unitarias

Los tests también son parte del material didáctico. Se encuentran en `src/test/java/utnfc/back/bolitas` y usan JUnit 5/Jupiter con objetos reales; el modelo no necesita Mockito ni otro framework.

### Dos niveles de prueba

Primero probamos objetos individuales. Clases como `BolitaTest`, `TuboTest`, `DetencionTest`, `AceleradorTest` y los tests de cada desvío aíslan una regla: podemos identificar con precisión si falló el cálculo, la selección de una salida o el registro del tramo actual. Los casos de frontera —por ejemplo, peso o velocidad exactamente iguales al límite— quedan explícitos.

Después probamos circuitos completos en `CircuitoTest`. Una implementación individual correcta no garantiza por sí sola que los objetos estén bien conectados o colaboren correctamente. Estos escenarios recorren distintas ramas, llegan a salidas `null`, ejercitan el ciclo acotado, comparan consecuencias de pesos y velocidades, y transportan una `BolitaConHistoria` por varios tipos de tramo.

En ambos niveles observamos resultados del dominio:

- longitud total;
- tiempo acumulado;
- velocidad final;
- último tramo;
- salida seleccionada;
- orden de la historia.

No necesitamos verificar “qué método fue llamado”. Cuando una referencia `Tramo` atraviesa un circuito y la bolita termina con el estado esperado, comprobamos indirectamente que el despacho polimórfico eligió los comportamientos concretos correctos.

También se prueban las dos estrategias usadas por `App`: `transportar()` y el ciclo que llama a `avanzar()` hasta obtener `null`.

### Ejecución

Desde la raíz del proyecto podemos ejecutar:

```bash
mvn test
```

Para borrar resultados anteriores, compilar desde cero y ejecutar toda la batería:

```bash
mvn clean test
```

Si un test falla, Maven informa la clase, el método y la diferencia entre valor esperado y real. El `@DisplayName` explica la regla conceptual. Conviene leer primero ese nombre, luego revisar el circuito preparado, la acción ejecutada y las aserciones. Así podemos distinguir un error aislado de una colaboración incorrecta entre piezas.

## Conclusiones

No utilizamos herencia y polimorfismo porque el ejercicio nos obligue artificialmente. Los utilizamos porque tenemos objetos distintos que representan el mismo concepto general de `Tramo`, pero responden de manera diferente al mismo mensaje:

```java
avanzar(bolita)
```

Una referencia de tipo `Tramo` puede contener cualquiera de sus especializaciones. El algoritmo general permanece estable y Java selecciona el comportamiento según el objeto concreto.

Después de agregar `TuboCurvo` y `DesvioVelocidad`, el recorrido continúa trabajando con `Tramo`. No agregamos preguntas con `instanceof` ni casos especiales. Después de agregar `BolitaConHistoria`, todos los tramos continúan trabajando con `Bolita`; la nueva especialización suma historia al redefinir un mensaje que ya recibía.

Nuestro resultado conceptual más importante es doble: podemos sustituir diferentes piezas a través de `Tramo` y podemos sustituir una bolita especializada a través de `Bolita`. En ambos casos, el código que coordina el circuito conoce el concepto general, mientras cada objeto aporta su respuesta particular.
