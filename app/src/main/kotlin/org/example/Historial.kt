package org.example
import java.io.File
import java.time.LocalDateTime

class HistorialInventario {

    private val movimientos: MutableList<MovimientoInventario> = mutableListOf()

    fun registrarMovimiento(movimiento: MovimientoInventario) {
        movimientos.add(movimiento)
        guardar()
    }

    fun obtenerTodos(): List<MovimientoInventario> = movimientos.toList()

    fun obtenerHistorialPorProducto(producto: Producto): List<MovimientoInventario> =
        movimientos.filter { it.producto.nombre.equals(producto.nombre, ignoreCase = true) }

    fun borrarHistorial() {
        movimientos.clear()
        archivo.delete()
    }

    companion object {
        private val archivo = File("historial.csv")

        fun cargar(): HistorialInventario {
            val historial = HistorialInventario()
            if (archivo.exists()) {
                archivo.forEachLine { linea ->
                    val partes = linea.split(",")
                    if (partes.size == 5) {
                        val id = partes[0]
                        val nombre = partes[1]
                        val costo = partes[2].toDoubleOrNull() ?: 0.0
                        val cantidad = partes[3].toIntOrNull() ?: 0
                        val tipo = TipoMovimiento.valueOf(partes[4])
                        val producto = Producto(id = id, nombre = nombre, descripcion = "", costo = costo)
                        historial.movimientos.add(
                            MovimientoInventario(producto, cantidad, LocalDateTime.now(), tipo)
                        )
                    }
                }
            }
            return historial
        }
    }

    private fun guardar() {
        val builder = StringBuilder()
        movimientos.forEach { mov ->
            builder.append("${mov.producto.id},${mov.producto.nombre},${mov.producto.costo},${mov.cantidad},${mov.tipo}\n")
        }
        archivo.writeText(builder.toString())
    }
}




