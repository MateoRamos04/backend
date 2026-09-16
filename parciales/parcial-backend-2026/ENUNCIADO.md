# Parcial Backend de Aplicaciones 2026

## Introducción

El contexto de este parcial es el de generación de liquidaciones de tarjetas de crédito.

Se entrega, junto a este enunciado, los scripts de creación de una base de datos reducida para este fin, como así también un script con datos iniciales:

* `schema.sql` → estructura
* `data.sql` → datos

El parcial consta de dos partes:

1. Una parte de preparación, desarrollada libremente por los alumnos.
2. Una parte presencial, realizada en la Universidad y con acceso únicamente a las Fichas de Clase.

---

# Estructura de la base de datos

La base de datos se compone de las siguientes tablas:

---

## COTIZACIONES

Contiene una fila por cada tasa de cambio entre el Peso Argentino y otra moneda.

Ejemplo:

| MONEDA | TASA_CAMBIO |
| ------ | ----------- |
| USD    | 1400        |

Significa que 1 USD vale ARS 1400.

---

## TARJETAS

Contiene una fila por cada tarjeta de crédito.

### Columnas

| Columna        | Descripción                   |
| -------------- | ----------------------------- |
| ID             | Identificador autoincremental |
| NUMERO         | Número de tarjeta             |
| TITULAR        | Nombre del titular            |
| LIMITE_CREDITO | Límite de crédito mensual     |

---

## CONSUMOS

Contiene una fila por cada consumo realizado.

Cada consumo está asociado a una tarjeta de crédito.

### Columnas

| Columna    | Descripción                            |
| ---------- | -------------------------------------- |
| ID         | Identificador autoincremental          |
| ID_TARJETA | ID de la tarjeta asociada              |
| MONTO      | Monto del consumo                      |
| DIA        | Día del consumo                        |
| MES        | Mes del consumo                        |
| ANIO       | Año del consumo                        |
| RUBRO      | Rubro del consumo                      |
| MONEDA     | Código de moneda (ARS, USD, EUR, etc.) |

---

## LIQUIDACIONES

Contiene una fila por cada liquidación realizada.

Una liquidación representa el resumen mensual de una tarjeta de crédito.

Para este parcial se considera mes completo:

* desde el primer día
* hasta el último día del mes

---

# Etapa de Preparación

Los alumnos deben generar un proyecto Maven con las dependencias necesarias para trabajar con:

* JPA
* Hibernate
* JUnit
* Lombok (opcional)

## Restricciones

* **NO debe utilizarse Spring Boot**
* Puede utilizarse:

    * Java 17
    * Hibernate 6.4.4
    * JUnit 6

---

## Requerimientos

Preparar todo el código y configuraciones necesarias para:

### 1. Entidades

Trabajar con las entidades:

* Tarjeta
* Consumo
* Moneda
* Liquidacion

Mapeando correctamente:

* entidades
* relaciones
* JPA

---

### 2. Inicialización de base de datos

Configurar el proyecto para que al iniciar:

* se cree la estructura de base de datos
* se carguen los datos iniciales

---

### 3. Acceso a datos

Implementar operaciones CRUD para todas las entidades.

Además, implementar las siguientes consultas:

#### Consumos de una tarjeta por año y mes

Obtener todos los consumos de una tarjeta para:

* un año específico
* un mes específico

---

#### Tarjetas sin liquidación

Obtener las tarjetas que no tienen liquidación para:

* un año específico
* un mes específico

---

#### Liquidación de una tarjeta

Obtener la liquidación de una tarjeta buscando por:

* número de tarjeta
* año
* mes

---

# Requisitos de la base de datos

La base de datos debe recrearse completamente en cada inicio de la aplicación.

Cada ejecución debe:

1. Crear nuevamente la estructura
2. Cargar únicamente los datos de `data.sql`

Durante la etapa presencial:

* `data.sql` podría reemplazarse por otro archivo distinto

La aplicación debe soportar ese cambio automáticamente.

---

# Configuración sugerida

Se puede utilizar una base:

* en memoria
* o en archivo

Una forma sugerida de configurar JPA es mediante las siguientes propiedades en `persistence.xml`:

```xml
<property name="jakarta.persistence.schema-generation.database.action"
          value="drop-and-create"/>

<property name="jakarta.persistence.schema-generation.create-source"
          value="script"/>

<property name="jakarta.persistence.schema-generation.create-script-source"
          value="schema.sql"/>

<property name="jakarta.persistence.sql-load-script-source"
          value="data.sql"/>
```

Los archivos `.sql` deben ubicarse en `resources`.

No es obligatorio utilizar este mecanismo.
Los alumnos pueden optar por cualquier estrategia equivalente.

---

# Etapa Presencial

Durante la etapa presencial se solicitarán:

* agregados
* cambios
* nuevas funcionalidades

sobre el proyecto realizado previamente.

Los alumnos deben tener disponible su proyecto base al momento de rendir:

* repositorio personal
* pendrive
* o cualquier otro medio

Debe contemplarse la posibilidad de rendir en computadoras de la Universidad.
