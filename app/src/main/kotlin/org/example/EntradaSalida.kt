package org.example

enum class TipoMovimiento {
    ENTRADA, // Movimiento de entrada (compra)
    SALIDA   // Movimiento de salida (venta o ajuste)
}

/** Clase que representa un movimiento en el inventario. Contiene el producto afectado, el tipo de movimiento (entrada o salida) y la cantidad de unidades involucradas.
 */
data class MovimientoStock(
    val producto: Producto,
    val tipo: TipoMovimiento,
    val cantidad: Int
)

/** Función de producto que crea productos y movimientos de stock, y muestra en consola el detalle de cada movimiento con su cantidad.
 */
fun mostrarProducto() {
    val producto1 = Producto(
        nombre = "PlayStation 5",
        descripcion = "Consola de última generación",
        costo = 999.99
    )
    val producto2 = Producto(
        nombre = "Notebook Lenovo",
        descripcion = "Notebook para uso profesional",
        costo = 1200.0
    )
    val producto3 = Producto(
        nombre = "Smart TV Samsung",
        descripcion = "Televisor inteligente 4K",
        costo = 1800.0
    )
    val producto4 = Producto(
        nombre = "Tablet Apple",
        descripcion = "Tablet de alta gama",
        costo = 400.0
    )
    val producto5 = Producto(
        nombre = "Smartphone Motorola",
        descripcion = "Teléfono inteligente con buena relación calidad-precio",
        costo = 300.0
    )

    // Movimientos de stock con cantidades
    val movimiento1 = MovimientoStock(producto1, TipoMovimiento.ENTRADA, 15)  
    val movimiento2 = MovimientoStock(producto1, TipoMovimiento.SALIDA, 5)    
    val movimiento3 = MovimientoStock(producto2, TipoMovimiento.ENTRADA, 20)  
    val movimiento4 = MovimientoStock(producto2, TipoMovimiento.SALIDA, 7)    
    val movimiento5 = MovimientoStock(producto3, TipoMovimiento.ENTRADA, 10) 
    val movimiento6 = MovimientoStock(producto3, TipoMovimiento.SALIDA, 3)    
    val movimiento7 = MovimientoStock(producto4, TipoMovimiento.ENTRADA, 25)  
    val movimiento8 = MovimientoStock(producto4, TipoMovimiento.SALIDA, 10)  
    val movimiento9 = MovimientoStock(producto5, TipoMovimiento.ENTRADA, 30)  
    val movimiento10 = MovimientoStock(producto5, TipoMovimiento.SALIDA, 12)  

    // Mostrar productos
    println(producto1)
    println(producto2)
    println(producto3)
    println(producto4)
    println(producto5)

    // Mostrar movimientos con cantidades y tipo
    val movimientos = listOf(movimiento1, movimiento2, movimiento3, movimiento4, 
                             movimiento5, movimiento6, movimiento7, movimiento8, 
                             movimiento9, movimiento10)
    for (mov in movimientos) {
        val accion = if (mov.tipo == TipoMovimiento.ENTRADA) "Entraron" else "Salieron"
        println("$accion ${mov.cantidad} unidades de ${mov.producto.nombre}")
    }
}



