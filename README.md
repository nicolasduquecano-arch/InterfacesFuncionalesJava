# Interfaces Funcionales en Java — Laboratorios

Hola 👋 En este repositorio resolví los **4 laboratorios** de la guía *"Interfaces Funcionales en Java"*.
Los hice en IntelliJ IDEA con **Java 21 LTS** (también funciona con Java 25) y sin librerías externas:
solo uso lo que ya trae el JDK (`java.util.function`, `java.text.Normalizer`, `java.util`).

La idea de la guía es aprender a pasar **comportamiento como si fuera un dato**: guardar una lambda en una
variable, mandarla como parámetro y combinar varias funciones pequeñas para hacer algo más grande.

## ¿Qué hay en cada laboratorio?

| Lab | Paquete | Lo que practiqué |
|-----|---------|------------------|
| 1 · Generador de slugs | `lab1_slugs` | `UnaryOperator`, `Function.identity()`, `andThen` |
| 2 · Motor de validación | `lab2_validacion` | Crear mi propia `@FunctionalInterface` genérica con `default` y `static` |
| 3 · Calculadora con estrategias | `lab3_calculadora` | `Map<String, DoubleBinaryOperator>` y referencias a métodos |
| 4 · Motor de promociones | `lab4_promociones` | `Predicate`, `DoubleUnaryOperator`, `Supplier`, `BinaryOperator.minBy`, `BiConsumer`, `ToDoubleBiFunction` |

### Lab 1 · Generador de slugs
Un *slug* es el texto que va en una URL, por ejemplo `"Silla Ergonómica"` → `silla-ergonomica`.
Definí 5 pasos pequeños, cada uno es un `UnaryOperator<String>`:

1. `RECORTAR`: quita los espacios de los lados (`String::strip`).
2. `MINUSCULAS`: pasa todo a minúsculas.
3. `SIN_TILDES`: con `Normalizer` separo la letra de la tilde y luego borro las tildes con `\\p{M}`.
4. `SOLO_VALIDOS`: deja solo letras, números, espacios y guiones.
5. `GUIONES`: cambia los espacios y guiones repetidos por un solo `-`.

El método `tuberia(...)` empieza con `Function.identity()` (una función que no cambia nada) y le va pegando
cada paso con `andThen`.

**Lo que aprendí:** el orden importa. Si pongo `SOLO_VALIDOS` antes de `SIN_TILDES`, la `ó` se borra completa
en vez de quedar como `o`, porque todavía tiene la tilde pegada.

### Lab 2 · Motor de validación
Hice una interfaz propia `Regla<T>` que devuelve una lista de errores (vacía si todo está bien):

- `y(otra)`: método `default` que ejecuta dos reglas y junta sus errores.
- `exigir(condicion, mensaje)`: fábrica que convierte un `Predicate` en una regla.
- `campo(extractor, regla)`: aplica una regla solo a un campo del objeto (por ejemplo, al usuario).

Con eso validar un `Registro` queda así: `usuarioValido.y(correoValido).y(edadValida)`.

### Lab 3 · Calculadora con estrategias
En vez de un `switch` gigante, guardé cada operación en un `LinkedHashMap` (así se conserva el orden en que
las agregué). Usé referencias a métodos cuando se podía: `Double::sum`, `Math::pow`, `Math::max`.

- Dividir por cero lanza `ArithmeticException("división por cero")`.
- Un operador que no existe lanza `IllegalArgumentException`.
- En el `main` atrapo los errores y muestro su mensaje (no los silencio).

**Lo que aprendí:** para agregar el operador `%` solo tendría que añadir una línea `OPERACIONES.put("%", (a, b) -> a % b);`.
No hay que tocar `evaluar`.

### Lab 4 · Motor de promociones (integrador)
Es el más completo. Cada `Promocion` es un `record` con un nombre, un `Predicate<Producto>` (a qué productos
aplica) y un `DoubleUnaryOperator` (cómo cambia el precio).

- `porcentaje(pct)` y `montoFijo(monto)` son fábricas que devuelven lambdas (`montoFijo` nunca baja de 0).
- "Sin promoción" sale de un `Supplier<Promocion>` con `DoubleUnaryOperator.identity()`.
- Para cada producto disponible elijo la promoción más barata para el cliente con `BinaryOperator.minBy`.
- Imprimo cada fila con un `BiConsumer` y sumo el ahorro con una `ToDoubleBiFunction`.

**Lo que aprendí:** el código que calcula no conoce ninguna promoción en particular. Si mañana hay una nueva,
solo se agrega a la lista.

## Estructura del proyecto

```
InterfacesFuncionalesJava/
├── .idea/                    # Configuración de IntelliJ (JDK 21, UTF-8 y 4 run configs)
├── InterfacesFuncionalesJava.iml
├── README.md
└── src/
    ├── lab1_slugs/
    │   └── Main.java
    ├── lab2_validacion/
    │   ├── Regla.java
    │   ├── Registro.java
    │   └── Main.java
    ├── lab3_calculadora/
    │   └── Main.java
    └── lab4_promociones/
        ├── Producto.java     # modelo del Listado 1 de la guía
        ├── Catalogo.java     # productos de ejemplo del Listado 1
        ├── Promocion.java
        └── Main.java
```

Cada laboratorio tiene su propio paquete y su propio `Main`, para que no se mezclen.

## Cómo ejecutarlo

### En IntelliJ IDEA
1. `File > Open…` y seleccionar la carpeta `InterfacesFuncionalesJava`.
2. Si lo pide, elegir el JDK 21 en `File > Project Structure > Project > SDK`.
3. Arriba a la derecha elegir **Lab1 - lab1_slugs**, **Lab2 - lab2_validacion**, **Lab3 - lab3_calculadora**
   o **Lab4 - lab4_promociones** y darle ▶ Run.

### Por consola (en la carpeta del proyecto)
```bash
javac -encoding UTF-8 -d out src/lab1_slugs/*.java src/lab2_validacion/*.java src/lab3_calculadora/*.java src/lab4_promociones/*.java

java -Duser.language=es -Duser.country=CO -Dstdout.encoding=UTF-8 -cp out lab1_slugs.Main
java -Duser.language=es -Duser.country=CO -Dstdout.encoding=UTF-8 -cp out lab2_validacion.Main
java -Duser.language=es -Duser.country=CO -Dstdout.encoding=UTF-8 -cp out lab3_calculadora.Main
java -Duser.language=es -Duser.country=CO -Dstdout.encoding=UTF-8 -cp out lab4_promociones.Main
```

> Pongo `-Duser.language=es -Duser.country=CO` porque el Lab 4 usa `%,` y así los miles salen con punto
> (`3.200.000`) como en la guía. Si en la consola de Windows salen `?` en vez de tildes, primero hay que ejecutar `chcp 65001`.

## Salida que obtuve

**Lab 1**
```
" Silla Ergonómica "             → silla-ergonomica
"Audífonos Bluetooth 5.3"        → audifonos-bluetooth-53
"¡Oferta! Portátil i7 -- 16GB"   → oferta-portatil-i7-16gb
```

**Lab 2**
```
VÁLIDO    camila_r   []
INVÁLIDO  (vacío)    [usuario: obligatorio, usuario: mínimo 4 caracteres, correo: formato inválido, edad: mínimo 14 años]
INVÁLIDO  ana        [usuario: mínimo 4 caracteres, correo: formato inválido]
```

**Lab 3**
```
12 + 30  = 42.0
2 ^ 10   = 1024.0
7 / 2    = 3.5
9 max 4  = 9.0
5 / 0    → error: división por cero
3 % 2    → error: operador desconocido: %
Operadores disponibles: [+, -, *, /, ^, max]
```

**Lab 4**
```
Portátil          Tecno 10 %     $  3.200.000 → $  2.880.000
Mouse             Tecno 10 %     $     85.000 → $     76.500
Audífonos         Bono $50.000   $    240.000 → $    190.000
Lápiz             Liquidación    $      2.500 → $      1.750
Silla ergonómica  Bono $50.000   $    890.000 → $    840.000
Ahorro total para el cliente: $429.250
```

El cuaderno no aparece en el Lab 4 porque tiene stock 0 (`disponible()` devuelve `false`).

## Tecnologías
- Java 21 LTS
- IntelliJ IDEA
- Sin librerías externas
