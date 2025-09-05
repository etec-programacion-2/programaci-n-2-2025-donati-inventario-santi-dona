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
        require(cantidad >= 0) { "La cantidad no puede ser negativa" }
        val productoExistente = stock.keys.find { it.nombre.equals(nuevoProducto.nombre, ignoreCase = true) }
        if (productoExistente != null) {
            val stockActual = stock[productoExistente] ?: 0
            stock[productoExistente] = stockActual + cantidad
        } else {
            stock[nuevoProducto] = cantidad
        }
        guardar() // Guardar cada vez que se actualiza
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


