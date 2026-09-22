# Parcial práctico: impresión en cuadernillo

*Tiempo orientativo: 45 minutos.* Partir exactamente del proyecto preparcial (`01-modelo-inicial` tal como se entregó: CSV de cinco columnas, una sola clase de pedido). Se entregan este documento, `pedidos-parcial2.csv` y la carpeta `tests-nuevos`. Mantener los tests existentes.

El CSV del parcial contiene 140 filas de datos más encabezado (141 líneas); es un conjunto distinto del preparcial de 68 filas. El volumen no agrega reglas ni tareas de programación.

La central de impresión incorpora el acabado *CUADERNILLO* (impresión a doble faz con dos páginas por cara, para folletos y apuntes anillados) además del acabado *SIMPLE* actual. Se necesita procesar ambos tipos de pedido en una misma colección, obtener la demanda real de hojas físicas y poder informar cuánto papel se ahorra respecto de imprimir todo en simple.

---

## Reglas nuevas

El nuevo encabezado es:

```text
id,sector,paginas,copias,estado,acabado
```

Conservar también el formato original de cinco columnas: todos los pedidos del formato anterior se consideran de acabado SIMPLE. Cada fila debe tener el ancho indicado por su encabezado; no aceptar filas de cinco campos bajo un encabezado de seis, ni a la inversa.

Para las filas `CONFIRMADO` del nuevo formato, el sexto campo admite exactamente `SIMPLE` o `CUADERNILLO`, después de quitar espacios externos. Un valor vacío, en otra combinación de mayúsculas (`simple`) o desconocido (`ANILLADO`) hace inválida la fila.

### Comportamiento por acabado

- *SIMPLE:* Conserva todas las reglas anteriores y su cálculo: `hojas = paginas * copias`. Se representa con la clase base `PedidoImpresion`.
- *CUADERNILLO:* Se modela como una especialización de `PedidoImpresion` (clase hija `PedidoCuadernillo` o equivalente):
  - Respeta las validaciones generales (id y sector no vacíos, páginas 1..1000, copias 1..100).
  - Al imprimirse a doble faz y con dos páginas por cara, cada hoja física rinde hasta **4 páginas** del ejemplar. Las hojas necesarias por ejemplar son ⌈paginas / 4⌉, es decir `(paginas + 3) / 4`.
  - La demanda total de hojas físicas es:

    ```text
    hojas = ceil(paginas / 4) * copias
    ```

  - Debe exponer además `paginasEnBlanco()`: las páginas que sobran para completar la última hoja de **un** ejemplar, es decir `ceil(paginas / 4) * 4 - paginas` (siempre entre 0 y 3).
  - *Restricciones específicas:* requiere como mínimo **4 páginas** (con menos no se arma un pliego), admite como máximo **400 páginas** (más allá el lomo no cierra) y como máximo **25 copias inclusive** (limitación de la plegadora). Fuera de esos rangos, el pedido es inválido y debe lanzar `IllegalArgumentException`.

### Reglas de negocio revisadas

Estas dos reglas cambian respecto del material previo y también valen para el formato de cinco columnas:

1. *Estados descartados:* además de `ANULADO`, ahora también se descarta `RECHAZADO`, sin validar números ni acabado. Cualquier otro estado distinto de `CONFIRMADO` sigue siendo inválido.
2. *Unicidad de id:* dentro de un mismo archivo, un id ya aceptado hace inválida la fila que lo repita (motivo que contenga la palabra `duplicado`). La comparación distingue mayúsculas y se hace después de quitar espacios externos, de modo que `A` y ` A ` son el mismo id. Las filas descartadas y las inválidas **no** reservan su id: si un `ANULADO` usa el id `P200`, un `CONFIRMADO` posterior con `P200` es válido.

### Precedencia de parseo

Conservar la precedencia del preparcial, con el agregado de la unicidad al final:

1. Verificar ancho de columnas según el encabezado activo.
2. Descartar `ANULADO` y `RECHAZADO` sin validar números ni acabado.
3. Para `CONFIRMADO`, validar acabado (`SIMPLE` o `CUADERNILLO`), páginas, copias y reglas de dominio.
4. Recién sobre un pedido que quedaría aceptado, controlar que su id no se haya usado antes.

Las filas inválidas se registran con su número de línea física y motivo (`Linea N: motivo`); la carga continúa. Se sigue cumpliendo `leídas = procesadas + descartadas + inválidas`.

### Ejemplos

- 10 páginas, 5 copias SIMPLE → `10 * 5 = 50` hojas.
- 40 páginas, 2 copias CUADERNILLO → `10 * 2 = 20` hojas, 0 páginas en blanco.
- 7 páginas, 3 copias CUADERNILLO → `⌈7/4⌉ * 3 = 2 * 3 = 6` hojas, 1 página en blanco por ejemplar.
- 5 páginas, 4 copias CUADERNILLO → `2 * 4 = 8` hojas, 3 páginas en blanco por ejemplar.
- 3 páginas, 2 copias CUADERNILLO → *Inválido* (mínimo 4 páginas). En SIMPLE, 3 páginas sigue siendo válido.
- 40 páginas, 30 copias CUADERNILLO → *Inválido* (máximo 25 copias).
- 401 páginas, 1 copia CUADERNILLO → *Inválido* (máximo 400 páginas), aunque la clase base acepte hasta 1000.

---

## Resultado requerido

1. *Lectura unificada:* Integrar la lectura de ambas versiones del CSV (5 y 6 columnas) en `ParserPedidos` sin duplicar el proceso de carga ni la lectura línea a línea.
2. *Modelo y cálculo polimórfico:* Crear la clase `PedidoCuadernillo` heredando de `PedidoImpresion`, redefiniendo el cálculo de `hojasNecesarias()`, agregando `paginasEnBlanco()` y validando sus invariantes específicas.
3. *Identificación del acabado:* Incorporar `getAcabado()` en la clase base devolviendo `"SIMPLE"` y redefinirlo en la hija con `"CUADERNILLO"`, para no resolver el tipo con cadenas de `instanceof` en el colector.
4. *Actualización de `CentralImpresion`:*
   - Asegurar que `hojasSolicitadas()`, `demandaPorSector()` y `filtrar()` reflejen polimórficamente el cálculo de hojas de ambos tipos de pedido.
   - `conteoPorAcabado()`: `Map<String, Long>` con las cantidades de pedidos SIMPLE y CUADERNILLO. Para una colección vacía debe dar resultado vacío.
   - `hojasAhorradas()`: `long` con la diferencia entre lo que se habría consumido imprimiendo todo en simple (`paginas * copias`) y la demanda real. Los pedidos SIMPLE aportan 0.
   - `sectorConMayorDemanda()`: `Optional<String>` con el sector de mayor demanda; vacío si no hay pedidos y, ante un empate, el menor alfabéticamente.
5. *Actualización del `Main`:*
   - Hacer que `Main` use por defecto `datos/pedidos-parcial2.csv`.
   - Mostrar por consola el resumen del procesamiento (leídas, procesadas, descartadas, inválidas), la demanda total de hojas, la demanda por sector, las cantidades por acabado, las hojas ahorradas y el sector con mayor demanda. Seguir delegando los cálculos en el colector.
6. *Compatibilidad:* Conservar el comportamiento anterior y pasar tanto los tests existentes del preparcial como los tests nuevos.

---

## Preparación y entrega

Desde la raíz de su proyecto:

```sh
cp -R tests-nuevos/utnfc src/test/java/
cp tests-nuevos/parcial2.properties src/test/resources/
cp pedidos-parcial2.csv datos/pedidos-parcial2.csv
mvn test
java -cp target/classes utnfc.backend.parcial.Main datos/pedidos-parcial2.csv
```

Los tests nuevos no compilan hasta que existan la clase hija y los métodos pedidos: los primeros errores de compilación (`cannot find symbol: PedidoCuadernillo`, `getAcabado`, `conteoPorAcabado`) indican exactamente qué falta. Recién cuando todo compila empiezan a verse fallas de aserción.

Entregar un único archivo ZIP del proyecto que contenga `pom.xml`, `src/` y `datos/`; excluir `target/`, `.git/` y carpetas de configuración del IDE. Incluir un archivo breve `DECISIONES.md` (3–5 líneas) describiendo la estrategia elegida para la jerarquía, el parser y si quedó algún punto pendiente.

---

## Verificación del dataset (mirar recién al terminar)

Salida esperada de `Main` sobre `datos/pedidos-parcial2.csv`:

```text
Leidas=140; procesadas=126; descartadas=2; invalidas=12
Hojas solicitadas=296657
Demanda por sector={Administracion=30180, Biblioteca=45415, Docencia=166571, Extension=54491}
Cantidad por acabado={CUADERNILLO=55, SIMPLE=71}
Hojas ahorradas=60279
Sector con mayor demanda=Docencia
```

Líneas físicas inválidas: 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 31, 32. Las dos descartadas son un `ANULADO` (línea 10) y un `RECHAZADO` (línea 12).

Casos testigo del archivo: `P001` pide 20 hojas (40 páginas en cuadernillo, 2 copias); `P003` pide 100000 (topes de la clase base en simple); `P006` usa los dos topes del cuadernillo (400 páginas, 25 copias) y pide 2500; `P007` pide 1 hoja con el mínimo de 4 páginas. `P200` y `P300` aparecen dos veces cada uno y las dos veces la segunda fila es válida, porque la primera fue descartada o inválida.
