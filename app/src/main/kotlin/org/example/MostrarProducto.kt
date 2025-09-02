package org.example

import java.time.format.DateTimeFormatter

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
        descripcion = "Teléfono inteligente",
        costo = 300.0
    )
    val producto6 = Producto(
        nombre = "Auriculares Sony",
        descripcion = "Auriculares inalámbricos",
        costo = 150.0
    )
    val producto7 = Producto(
        nombre = "Cámara Canon",
        descripcion = "Cámara réflex digital",
        costo = 800.0
    )
    val producto8 = Producto(
        nombre = "Impresora HP",
        descripcion = "Impresora multifunción",
        costo = 200.0
    )
    val producto9 = Producto(
        nombre = "Monitor Dell",
        descripcion = "Monitor LED 24 pulgadas",
        costo = 250.0
    )
    val producto10 = Producto(
        nombre = "Teclado Snapdragon",
        descripcion = "Teclado mecánico para gaming",
        costo = 400.0
    )    

    val movimientos = listOf(
        MovimientoInventario(producto1, 15, tipo = TipoMovimiento.ENTRADA),
        MovimientoInventario(producto1, 5, tipo = TipoMovimiento.SALIDA),
        MovimientoInventario(producto2, 20, tipo = TipoMovimiento.ENTRADA),
        MovimientoInventario(producto2, 7, tipo = TipoMovimiento.SALIDA),
        MovimientoInventario(producto3, 10, tipo = TipoMovimiento.ENTRADA),
        MovimientoInventario(producto3, 3, tipo = TipoMovimiento.SALIDA),
        MovimientoInventario(producto4, 25, tipo = TipoMovimiento.ENTRADA),
        MovimientoInventario(producto4, 10, tipo = TipoMovimiento.SALIDA),
        MovimientoInventario(producto5, 30, tipo = TipoMovimiento.ENTRADA),
        MovimientoInventario(producto5, 12, tipo = TipoMovimiento.SALIDA),
        MovimientoInventario(producto6, 18, tipo = TipoMovimiento.ENTRADA),
        MovimientoInventario(producto6, 6, tipo = TipoMovimiento.SALIDA),
        MovimientoInventario(producto7, 8, tipo = TipoMovimiento.ENTRADA),
        MovimientoInventario(producto7, 2, tipo = TipoMovimiento.SALIDA),
        MovimientoInventario(producto8, 22, tipo = TipoMovimiento.ENTRADA),
        MovimientoInventario(producto8, 9, tipo = TipoMovimiento.SALIDA),
        MovimientoInventario(producto9, 14, tipo = TipoMovimiento.ENTRADA),
        MovimientoInventario(producto9, 4, tipo = TipoMovimiento.SALIDA),
        MovimientoInventario(producto10, 16, tipo = TipoMovimiento.ENTRADA),
        MovimientoInventario(producto10, 5, tipo = TipoMovimiento.SALIDA)
    )

    println("Productos:")
    println("--------------------------------------------------------------------------------")
    println(String.format("%-20s | %-30s | %-10s", "Nombre", "Descripción", "Costo"))
    println("--------------------------------------------------------------------------------")
    listOf(producto1, producto2, producto3, producto4, producto5, producto6, producto7, producto8, producto9, producto10).forEach {
        println(String.format("%-20s | %-30s | $%9.2f", it.nombre, it.descripcion, it.costo))
    }
    println()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    println("Movimientos de Inventario:")
    println("---------------------------------------------------------------------------------------------")
    println(String.format("%-20s | %-8s | %-10s | %-19s", "Producto", "Tipo", "Cantidad", "Fecha y Hora"))
    println("---------------------------------------------------------------------------------------------")
    movimientos.forEach { mov ->
        val tipoStr = if (mov.tipo == TipoMovimiento.ENTRADA) "Entrada" else "Salida"
        println(
            String.format(
                "%-20s | %-8s | %10d | %-19s",
                mov.producto.nombre,
                tipoStr,
                mov.cantidad,
                mov.fecha.format(formatter)
            )
        )
    }
    println("---------------------------------------------------------------------------------------------")
}



