package org.example
import java.io.*

class Inventario(private val historial: HistorialInventario) : Serializable {

    private val stock: MutableMap<Producto, Int> = mutableMapOf()

    companion object {
        private const val FILE_NAME = "inventario.dat"

        fun cargar(historial: HistorialInventario): Inventario {
            return try {
                ObjectInputStream(FileInputStream(FILE_NAME)).use { it.readObject() as Inventario }
            } catch (e: Exception) {
                Inventario(historial)
            }
        }
    }

    fun guardar() {
        ObjectOutputStream(FileOutputStream(FILE_NAME)).use { it.writeObject(this) }
    }

    fun registrarEntrada(producto: Producto, cantidad: Int) {
        require(cantidad > 0) { "La cantidad debe ser mayor a 0." }

        val productoExistente = stock.keys.find { it.nombre.equals(producto.nombre, ignoreCase = true) }
        if (productoExistente != null) {
            val stockActual = stock[productoExistente] ?: 0
            stock[productoExistente] = stockActual + cantidad
            historial.registrarMovimiento(MovimientoInventario(productoExistente, cantidad, tipo = TipoMovimiento.ENTRADA))
        } else {
            stock[producto] = cantidad
            historial.registrarMovimiento(MovimientoInventario(producto, cantidad, tipo = TipoMovimiento.ENTRADA))
        }
        guardar()
    }

    fun registrarSalida(producto: Producto, cantidad: Int) {
        require(cantidad > 0) { "La cantidad debe ser mayor a 0." }

        val productoExistente = stock.keys.find { it.nombre.equals(producto.nombre, ignoreCase = true) }
            ?: throw IllegalArgumentException("El producto no existe en el inventario.")

        val stockActual = stock[productoExistente] ?: 0
        require(stockActual >= cantidad) { "Stock insuficiente. Disponible: $stockActual" }

        val nuevoStock = stockActual - cantidad
        if (nuevoStock == 0) {
            stock.remove(productoExistente)
        } else {
            stock[productoExistente] = nuevoStock
        }

        historial.registrarMovimiento(MovimientoInventario(productoExistente, -cantidad, tipo = TipoMovimiento.SALIDA))
        guardar()
    }

    fun obtenerStock(producto: Producto): Int {
        return stock[producto] ?: 0
    }

    fun obtenerProductos(): List<Producto> {
        return stock.keys.toList()
    }

    fun obtenerCantidad(producto: Producto): Int {
        return stock[producto] ?: 0
    }
}




