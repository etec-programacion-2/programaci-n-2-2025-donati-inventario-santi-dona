package org.example

import java.io.*
import java.time.LocalDateTime

class HistorialInventario : Serializable {

    private val movimientos: MutableList<MovimientoInventario> = mutableListOf()

    companion object {
        private const val FILE_NAME = "historial.dat"

        fun cargar(): HistorialInventario {
            return try {
                ObjectInputStream(FileInputStream(FILE_NAME)).use { it.readObject() as HistorialInventario }
            } catch (e: Exception) {
                HistorialInventario()
            }
        }
    }

    fun guardar() {
        ObjectOutputStream(FileOutputStream(FILE_NAME)).use { it.writeObject(this) }
    }

    fun registrarMovimiento(movimiento: MovimientoInventario) {
        movimientos.add(movimiento)
        guardar()
    }

    /**
     * Devuelve todos los movimientos del producto pasado (orden cronológico ascendente).
     */
    fun obtenerHistorialPorProducto(producto: Producto): List<MovimientoInventario> {
        return movimientos
            .filter { it.producto.id == producto.id }
            .sortedBy { it.fecha }
    }

    /**
     * Devuelve todo el historial (orden cronológico ascendente).
     */
    fun obtenerTodos(): List<MovimientoInventario> {
        return movimientos.sortedBy { it.fecha }.toList()
    }
}

