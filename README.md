# Interfaces Funcionales en Java — Laboratorios

Hola En este repositorio resolví los **4 laboratorios** de la guía *"Interfaces Funcionales en Java"*.
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


