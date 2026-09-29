package lab4_promociones;

import java.util.function.DoubleUnaryOperator;
import java.util.function.Predicate;

record Promocion(String nombre, Predicate<Producto> aplicaA, DoubleUnaryOperator ajuste) {
    double precioCon(Producto p) {
        return aplicaA.test(p) ? ajuste.applyAsDouble(p.precio()) : p.precio();
    }
}
