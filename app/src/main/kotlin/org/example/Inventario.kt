package org.example

class Inventario(private val historial: HistorialInventario) {

    private val stock: MutableMap<Producto, Int> = mutableMapOf()

    fun registrarEntrada(producto: Producto, cantidad: Int) {
        require(cantidad > 0) { "La cantidad debe ser mayor a cero." }
        val productoExistente = stock.keys.find { it.nombre.equals(producto.nombre, ignoreCase = true) }
        if (productoExistente != null) {
            stock[productoExistente] = (stock[productoExistente] ?: 0) + cantidad
        } else {
            stock[producto] = cantidad
        }
        historial.registrarMovimiento(MovimientoInventario(producto, cantidad, tipo = TipoMovimiento.ENTRADA))
    }

    fun registrarSalida(producto: Producto, cantidad: Int) {
        val cantidadActual = stock[producto] ?: 0
        require(cantidad > 0) { "La cantidad debe ser mayor a cero." }
        require(cantidadActual >= cantidad) { "No hay suficiente stock para la venta." }

        stock[producto] = cantidadActual - cantidad
        historial.registrarMovimiento(MovimientoInventario(producto, cantidad, tipo = TipoMovimiento.SALIDA))
    }

    fun obtenerProductos(): List<Producto> = stock.keys.filter { stock[it]!! > 0 }

    fun obtenerCantidad(producto: Producto): Int = stock[producto] ?: 0

    companion object {
        fun cargar(historial: HistorialInventario): Inventario {
            return Inventario(historial)
        }
    }
}







