package org.example

import java.io.*
import java.text.SimpleDateFormat
import java.util.*

data class Movimiento(
    val producto: String,
    val cantidad: Int,
    val tipo: String, // "Entrada" o "Salida"
    val fecha: String,
    val usuario: String
) : Serializable

object HistorialProductos {
    private val archivo = File("historial_mov.dat")  // Cambié a "historial_mov.dat" para evitar conflicto con "historial.dat" de Inventario

    fun agregarMovimiento(producto: String, cantidad: Int, tipo: String, usuario: String) {
        val historial = obtenerHistorial().toMutableList()
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())
        historial.add(Movimiento(producto, cantidad, tipo, fecha, usuario))
        guardarHistorial(historial)
    }

    fun obtenerHistorial(): List<Movimiento> {
        return if (archivo.exists()) {
            try {
                ObjectInputStream(FileInputStream(archivo)).use { ois ->
                    @Suppress("UNCHECKED_CAST")
                    ois.readObject() as? List<Movimiento> ?: emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        } else emptyList()
    }

    private fun guardarHistorial(historial: List<Movimiento>) {
        ObjectOutputStream(FileOutputStream(archivo)).use { oos ->
            oos.writeObject(historial)
        }
    }
}

