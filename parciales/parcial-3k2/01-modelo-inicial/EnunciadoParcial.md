# Parcial práctico: impresión doble faz

*Tiempo orientativo: 45 minutos.* Partir exactamente del proyecto preparcial. Se entregan este documento, pedidos-parcial.csv y la carpeta tests-nuevos. Mantener los tests existentes.

El CSV del parcial contiene 140 filas de datos más encabezado (141 líneas); es un conjunto distinto del preparcial de 68 filas. El volumen no agrega reglas ni tareas de programación.

La central de impresión incorpora la modalidad *DOBLE_FAZ* (impresión dúplex a ambas caras de la hoja) para reducir el consumo de papel. Se necesita procesar ambos tipos de pedido en una misma colección y obtener la demanda real de hojas físicas necesarias.

---

## Reglas nuevas

El nuevo encabezado es:

text
id,sector,paginas,copias,estado,modalidad


Conservar también el formato original de cinco columnas: todos los pedidos del formato anterior se consideran pedidos normales (NORMAL). Cada fila debe tener el ancho indicado por su encabezado; no aceptar filas de cinco campos bajo un encabezado de seis, ni a la inversa.

Para las filas CONFIRMADO del nuevo formato, el sexto campo admite exactamente NORMAL o DOBLE_FAZ, después de quitar espacios externos. Un valor vacío o desconocido hace inválida la fila.

### Comportamiento por modalidad

- *NORMAL:* Conserva todas las reglas anteriores y su cálculo: hojas = paginas * copias. Se representa con la clase base PedidoImpresion.
- *DOBLE_FAZ:* Se modela como una especialización de PedidoImpresion (clase hija PedidoDobleFaz o equivalente):
  - Respeta las validaciones generales (id y sector no vacíos).
  - Al imprimirse de ambos lados, cada hoja física rinde hasta 2 páginas del ejemplar. Las hojas necesarias por ejemplar son $\lceil \text{paginas} / 2 \rceil$ (es decir, (paginas + 1) / 2).
  - La demanda total de hojas físicas es:  
    $$\text{hojas} = \left\lceil \frac{\text{paginas}}{2} \right\rceil \times \text{copias}$$
  - *Restricciones específicas:* Requiere como mínimo *2 páginas* (un trabajo de 1 sola página no admite doble faz) y admite como máximo *50 copias inclusive* por limitaciones mecánicas del alimentador dúplex. Fuera de esos rangos, el pedido es inválido y debe lanzar IllegalArgumentException.

### Precedencia de parseo
Conservar la precedencia del preparcial:
1. Verificar ancho de columnas según el encabezado activo.
2. Descartar ANULADO sin validar números ni modalidad.
3. Para CONFIRMADO, validar modalidad (NORMAL o DOBLE_FAZ), páginas, copias y reglas de dominio. Las filas inválidas se registran con su número de línea física y motivo; la carga continúa.

### Ejemplos

- 10 páginas, 5 copias NORMAL $\rightarrow$ $10 \times 5 = 50$ hojas.
- 10 páginas, 5 copias DOBLE_FAZ $\rightarrow$ $5 \times 5 = 25$ hojas.
- 7 páginas, 4 copias DOBLE_FAZ $\rightarrow$ $\lceil 7/2 \rceil \times 4 = 4 \times 4 = 16$ hojas.
- 1 página, 10 copias DOBLE_FAZ $\rightarrow$ *Inválido* (mínimo 2 páginas).
- 20 páginas, 60 copias DOBLE_FAZ $\rightarrow$ *Inválido* (máximo 50 copias).

---

## Resultado requerido

1. *Lectura unificada:* Integrar la lectura de ambas versiones del CSV (5 y 6 columnas) en ParserPedidos sin duplicar el proceso de carga ni la lectura línea a línea.
2. *Modelo y cálculo polimórfico:* Crear la clase PedidoDobleFaz heredando de PedidoImpresion, redefiniendo el cálculo de hojas() y validando sus invariantes específicas.
3. *Actualización de CentralImpresion:*
   - Asegurar que demandaTotal(), demandaPorSector() y filtros reflejen polimórficamente el cálculo de hojas de ambos tipos de pedido.
   - Incorporar en CentralImpresion una operación de *conteo por modalidad* (por ejemplo, devolviendo un Map<String, Long> o similar con las cantidades de pedidos NORMAL y DOBLE_FAZ). Para una colección vacía debe dar resultado vacío o ceros.
4. *Actualización del Main:*
   - Hacer que Main use por defecto datos/pedidos-parcial.csv.
   - Mostrar por consola el resumen del procesamiento (leídas, procesadas, descartadas, inválidas), la demanda total de hojas, la demanda por sector y las cantidades de pedidos por modalidad. Seguir delegando los cálculos en el colector.
5. *Compatibilidad:* Conservar el comportamiento anterior y pasar tanto los tests existentes del preparcial como los tests nuevos.

---

## Preparación y entrega

Desde la raíz de su proyecto:

sh
cp -R tests-nuevos/utnfc src/test/java/
cp pedidos-parcial.csv datos/pedidos-parcial.csv
mvn test
java -cp target/classes utnfc.backend.parcial.Main datos/pedidos-parcial.csv


Los tests nuevos compilan sobre el proyecto inicial y fallan antes de realizar los cambios.

Entregar un único archivo ZIP del proyecto que contenga pom.xml, src/ y datos/; excluir target/, .git/ y carpetas de configuración del IDE. Incluir un archivo breve DECISIONES.md (3–5 líneas) describiendo la estrategia elegida para la jerarquía, el parser y si quedó algún punto pendiente.