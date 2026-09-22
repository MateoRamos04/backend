# Material previo — 3K2 — Central de impresión

Este proyecto Maven está completo y funciona. Se entrega 24 horas antes para estudiar el modelo, ejecutar la aplicación y preparar el entorno. No hay que agregar funcionalidades en esta instancia.

## Dominio

Una central recibe pedidos de impresión de distintos sectores. Un pedido indica páginas por ejemplar y cantidad de copias. Se imprime a una cara: **hojas necesarias = páginas × copias**. Por ahora el sistema informa demanda; no asigna papel ni modifica pedidos.

## Ejecutar

Requisitos: JDK 21 y Maven 3.9.x. Desde esta carpeta:

```sh
mvn clean test
java -cp target/classes utnfc.backend.parcial.Main
java -cp target/classes utnfc.backend.parcial.Main datos/pedidos-previo.csv
```

La primera resolución de dependencias puede requerir Internet. Antes del examen, comprobar `mvn -o clean test` en la computadora que se usará. No hay dependencias de ejecución; JUnit 5 se usa en tests. No se requiere IDE particular ni Lombok.

## Clases provistas

| Clase | Responsabilidad |
|---|---|
| PedidoImpresion | Datos inmutables, invariantes y cantidad de hojas. |
| ParserPedidos | Lee el CSV y clasifica filas. |
| ResultadoCarga | Pedidos aceptados, contadores y errores con línea física. |
| CentralImpresion | Demanda total, demanda por sector y filtro tipado. |
| Main | Carga un archivo y muestra información delegando los cálculos. |

Leer primero `PedidoImpresion`, después el parser y sus tests, y finalmente el colector y el main. Observar dónde se valida, qué ocurre ante un error y qué listas se copian.

## Contrato del CSV

UTF-8. Encabezado exacto:

```text
id,sector,paginas,copias,estado
```

Cinco campos separados por coma. No hay comas, comillas ni saltos de línea dentro de campos. Se quitan espacios externos; `split(",", -1)` conserva vacíos finales. Es un formato controlado, no CSV universal.

- ID y sector no vacíos. Los sectores se comparan distinguiendo mayúsculas.
- Páginas: entero de 1 a 1000 inclusive. Copias: entero de 1 a 100 inclusive.
- No se exige unicidad de ID: cada fila aceptada representa un pedido independiente.
- Primero se controla el ancho. Después `ANULADO` se descarta sin validar el resto.
- `CONFIRMADO` se convierte y valida. Otro estado es inválido.
- Una fila inválida, incluida una línea vacía, se registra y no interrumpe la carga.
- Un encabezado incorrecto, incluido archivo vacío, lanza `IllegalArgumentException`.
- Un problema de lectura propaga `IOException`.

El constructor protege las invariantes numéricas y de texto para cualquier vía de creación. `desdeCampos` convierte texto; el estado y su precedencia son responsabilidad del parser.

El encabezado no cuenta como fila leída. Se cumple `leídas = procesadas + descartadas + inválidas`. Los pedidos y errores conservan su orden. Las listas expuestas por los objetos provistos no admiten modificaciones.

## Dataset y pruebas

El archivo tiene **60 pedidos válidos, 2 anulados y 6 inválidos**: 68 filas de datos + encabezado, 69 líneas físicas. Hay 15 pedidos válidos por sector.

Resultados previos: demanda total **103475 hojas**. Por sector: Administracion=1598, Biblioteca=725, Docencia=100269, Extension=883.

Casos testigo: I001 pide 80 hojas; I005 pide 1; I006 pide 100000 (límite de páginas y copias). La presencia de un trabajo grande no lo vuelve inválido si respeta los rangos.

Los 11 tests comprueban construcción, límites, errores, lectura, agregados, copias y dataset completo. El fixture `src/test/resources/previo.properties` contiene valores del material previo verificados independientemente. Se mantiene en la entrega final.
