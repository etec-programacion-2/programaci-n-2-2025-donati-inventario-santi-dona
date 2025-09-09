package org.example
import java.io.*

class Inventario : Serializable {

    private val stock: MutableMap<Producto, Int> = mutableMapOf()

    companion object {
        private const val FILE_NAME = "inventario.dat"

        fun cargar(): Inventario {
            return try {
                ObjectInputStream(FileInputStream(FILE_NAME)).use { it.readObject() as Inventario }
            } catch (e: Exception) {
                Inventario() // Si no existe archivo, se crea uno nuevo
            }
        }
    }

    fun guardar() {
        ObjectOutputStream(FileOutputStream(FILE_NAME)).use { it.writeObject(this) }
    }

    fun agregarOActualizarProducto(nuevoProducto: Producto, cantidad: Int) {
        val productoExistente = stock.keys.find { it.nombre.equals(nuevoProducto.nombre, ignoreCase = true) }

        if (productoExistente != null) {
            val stockActual = stock[productoExistente] ?: 0
            val nuevoStock = stockActual + cantidad
            require(nuevoStock >= 0) { "No hay suficiente stock para realizar la operación." }

            if (nuevoStock == 0) {
                stock.remove(productoExistente) // eliminar producto cuando no queda stock
            } else {
                stock[productoExistente] = nuevoStock
            }
        } else {
            require(cantidad >= 0) { "No se puede iniciar un producto con stock negativo." }
            if (cantidad > 0) {
                stock[nuevoProducto] = cantidad
            }
        }
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




