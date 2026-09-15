1. Cómo interactuar con el proyecto

Abrí la carpeta actividad-RepoJPA en VS Code. Tiene que ser la carpeta donde está el pom.xml, así VS Code la reconoce como proyecto Maven.

Desde la terminal de VS Code, parado en esa carpeta:

Comando	Qué hace
mvn test	Compila todo y corre ArtistRepositoryTest. La primera vez baja las dependencias (Hibernate, H2, Lombok, JUnit), así que tarda un poco.
mvn compile exec:java	Ejecuta Main: imprime artistas, álbumes y tracks, y crea un álbum nuevo.

También podés usar los botones de VS Code: "Run" arriba del main en Main.java, o el ícono ▶ al lado de cada @Test. Así corrés un solo test.

Importante: la base es H2 en memoria. Se crea al arrancar el programa y desaparece al terminar. Cada ejecución empieza con Chinook limpia, así que podés romper cosas sin miedo.

2. El mapa general

Cuando corrés Main, pasa esto:

Main
 ├─1─► DataInitializer        → crea Chinook en H2 (scripts SQL)
 ├─2─► LocalEntityManagerProvider → abre JPA sobre esa misma base
 ├─3─► new JpaArtistRepository(em) ... → crea los repositorios
 ├─4─► new CatalogService(...)        → crea el servicio
 └─5─► usa repos y servicio → cierra todo

Y cada pedido baja por las capas:

Main → CatalogService → Repositorio → EntityManager (JPA/Hibernate) → H2

Cada capa solo conoce a la de abajo. Esa es la idea central del patrón.

3. Cada parte, una por una
pom.xml: las dependencias

Le dice a Maven qué librerías bajar:

- hibernate-core: la implementación de JPA. Traduce objetos a SQL.
- h2: la base de datos. Corre dentro del mismo proceso Java, no hay que instalar nada.
- lombok: genera getters, setters y builders al compilar.
- junit-jupiter: JUnit 5 para los tests.
- exec-maven-plugin: permite mvn exec:java para correr Main.
- resources/sql/: los 4 scripts de Chinook

Crean la base física, en este orden:

01_chinook_tables.sql: las tablas (Artist, Album, Track, Invoice, etc.), todavía sin claves foráneas.
02_chinook_data.sql: los datos (275 artistas, 347 álbumes, 3503 tracks…).
03_chinook_constraints_indexes.sql: agrega las claves foráneas y los índices. Van después de los datos para que el orden de los inserts no importe.
03_chinook_sequences.sql: ajusta los autoincrementales. Como los datos se cargaron con ids fijos (1..275), le dice a H2 que el próximo ArtistId sea 276. Sin esto, el primer save() chocaría con el id 1.

Hibernate no crea las tablas; las crean estos scripts. Por eso en persistence.xml está hbm2ddl.auto=none.

config/DataInitializer.java: ejecuta los scripts
Se conecta a H2 por JDBC puro, sin JPA, con la URL jdbc:h2:mem:chinook;....
Se fija si la tabla ARTIST ya existe. Si no existe, corre los 4 scripts con RUNSCRIPT FROM 'classpath:/sql/...'.
Tiene un flag initialized para que llamarlo dos veces no duplique nada.

Punto clave: su URL es idéntica a la de persistence.xml. Si difieren, los scripts llenan una base y JPA mira otra vacía. Es el error de "table not found" que menciona el md.

resources/META-INF/persistence.xml: la unidad de persistencia

Define chinookPU, la configuración que JPA necesita:

provider: Hibernate.
class: qué clases son entidades (Artist, Album, Track).
url, user, password: a qué base conectarse.
hbm2ddl.auto = none: Hibernate no toca el esquema.
show_sql = false: si lo ponés en true, ves en consola el SQL que genera Hibernate. Muy útil para estudiar.
config/LocalEntityManagerProvider.java: administra JPA

Hace a mano lo que después hará Spring:

emf (EntityManagerFactory): se crea una sola vez porque es costoso. Lee chinookPU y arma toda la configuración.
em(): te da un EntityManager. Si el hilo actual ya tiene uno abierto, te devuelve ese. Si no, crea uno nuevo.
ThreadLocal: cada hilo tiene su propio EntityManager. Nunca se comparte entre hilos.
closeCurrent(): cierra el EntityManager del hilo. Las entidades quedan detached.
shutdown(): cierra la fábrica al final del programa.

Resumen: la fábrica vive toda la aplicación; el EntityManager vive una operación.

domain/: las entidades

Clases Java que representan filas de tablas.

Artist es la más simple:

@Entity y @Table(name="Artist"): esta clase es la tabla Artist.
@Id y @GeneratedValue(IDENTITY): artistId es la clave primaria y la genera la base al insertar.
@Column(name="Name"): el atributo name es la columna Name.
Lombok: @Getter, @Setter, @Builder y los constructores se generan solos. @EqualsAndHashCode(of="artistId") compara artistas solo por id. No se usa @Data porque genera toString y equals sobre todo, y con relaciones eso puede entrar en loops.

Album y Track agregan una relación:

java
@ManyToOne(optional = false, fetch = FetchType.LAZY)
@JoinColumn(name = "ArtistId")
private Artist artist;

En la tabla hay un número (ArtistId), pero en Java tenés un objeto Artist. Por eso podés hacer album.getArtist().getName(). LAZY significa que el artista no se carga hasta que lo tocás.

La navegación es de un solo sentido: Track → Album → Artist. Artist no tiene lista de álbumes; es a propósito, para no sumar complejidad.

repository/base/: lo genérico

BaseRepository<T, ID> (interfaz) es el contrato: qué operaciones tiene cualquier repositorio.

save, findById, findAll, findAll(offset, limit), delete, count, existsById.
T es la entidad (Artist) e ID el tipo de su id (Long).
findById devuelve Optional<T>: te obliga a contemplar que el registro puede no existir, en vez de devolver null.

JpaBaseRepository<T, ID> (clase) es cómo se cumple ese contrato usando JPA. Se escribe una sola vez y sirve para las tres entidades:

Recibe Class<T> en el constructor porque Java no permite escribir T.class. Con eso arma "select e from " + entityClass.getSimpleName() + " e".
save() usa em.merge(): sirve tanto para entidades nuevas como existentes. Hay que usar el objeto que devuelve, no el original.
delete() primero verifica que la entidad esté administrada (em.contains). Si no lo está, la hace merge y después la borra.
findAll(offset, limit) usa setFirstResult y setMaxResults para paginar.
No abre transacciones. Eso lo decide quien lo usa.

PageRequest (record) agrupa offset y limit y valida que no sean negativos. PageRequest.ofPage(2, 10) calcula offset = 20.

repository/: los repositorios específicos

Para cada entidad hay una interfaz y una implementación JPA:

ArtistRepository (interfaz)      ← extiende BaseRepository<Artist, Long>
      ▲                            y agrega findByNameContainsIgnoreCase
JpaArtistRepository (clase)      ← extiende JpaBaseRepository (hereda el CRUD)
                                   e implementa solo la consulta nueva

Las consultas específicas usan JPQL, que habla de entidades y atributos, no de tablas:

Artist: where lower(a.name) like lower(:text). Busca "contiene", sin importar mayúsculas.
Album: where a.artist.artistId = :artistId. Navega la relación sin escribir el JOIN a mano.
Track: busca por álbum (t.album.albumId) y por nombre.

:text es un parámetro nombrado que se completa con setParameter. Nunca se concatena el valor dentro del string (eso evita SQL injection).

service/CatalogService.java: el caso de uso

createAlbumForArtist(artistId, title) es una operación de negocio con varios pasos:

java
tx.begin();
  buscar artista (si no existe → excepción)
  construir Album con builder
  albums.save(album)
tx.commit();
// si algo falla → rollback

Acá está la transacción, no en el repositorio. Si el repositorio abriera su propia transacción, no podrías agrupar varias operaciones en una unidad atómica (todo o nada).

El servicio depende de las interfaces (ArtistRepository), no de JpaArtistRepository. No sabe que abajo hay JPA.

app/Main.java: el que arma todo

Hace de "contenedor manual": inicializa la base, crea el EntityManager, construye los repos pasándoles ese em, construye el servicio, llama métodos e imprime. El finally cierra todo aunque algo falle.

Main no escribe JPQL ni usa em.createQuery: solo consume repositorios y servicios.

test/.../ArtistRepositoryTest.java: los tests
@BeforeAll: inicializa Chinook una vez para toda la clase.
@BeforeEach: EntityManager y repositorio nuevos para cada test.
@AfterEach: si quedó una transacción abierta, hace rollback, y cierra el em.
@AfterAll: cierra la fábrica.

Qué prueba cada test:

findAllReturnsArtists: que haya datos.
findByIdReturnsExistingArtist: que el artista 1 sea "AC/DC".
findByIdReturnsEmptyWhenMissing: que un id inexistente dé Optional vacío.
findAllWithPaginationRespectsLimit: que limit corte bien.
findAllRejectsInvalidPage: que offset o limit inválidos tiren excepción.
findByNameContainsIgnoreCaseAppliesCriteria: que "QUEEN" encuentre "Queen".
savePersistsArtist: que se genere un id. El test abre la transacción, porque save() no lo hace.
deleteRemovesArtist: crea, borra y verifica que count bajó en 1.
4. Experimentos para estudiarlo
Ver el SQL: poné hibernate.show_sql en true en persistence.xml, corré Main y compará cada JPQL con el SQL que genera Hibernate.
Romper la URL: cambiá la URL de persistence.xml a jdbc:h2:mem:otra;DB_CLOSE_DELAY=-1. Vas a ver el error de "tabla no encontrada".
Save sin transacción: en un test, sacá tx.begin() y tx.commit() de savePersistsArtist y fijate qué pasa.
Rollback: en CatalogService, tirá una excepción justo después de albums.save(album). Después verificá con albumRepository.count() que el álbum no quedó guardado.
LAZY: traé un álbum, llamá LocalEntityManagerProvider.closeCurrent() y después album.getArtist().getName(). Vas a ver una LazyInitializationException.
Dirty checking: dentro de una transacción, findById(1L), setName("Otro") y commit, sin llamar a save. Después buscalo de nuevo y fijate si cambió.
Tu propia consulta: agregá findByMillisecondsGreaterThan(int ms, int offset, int limit) a TrackRepository y su implementación. Es el mejor ejercicio para fijar el patrón.