package org.example

class ServicioInventario(
    private val inventario: Inventario,
    private val historial: HistorialInventario
) {

    companion object {
        fun cargar(): ServicioInventario {
            val historial = HistorialInventario.cargar()
            val inventario = Inventario.cargar(historial)
            return ServicioInventario(inventario, historial)
        }
    }

    fun crearNuevoProducto(nombre: String, costo: Double, cantidadInicial: Int) {
        val producto = Producto(nombre = nombre, descripcion = "", costo = costo)
        inventario.registrarEntrada(producto, cantidadInicial)
    }

    fun agregarStock(nombreProducto: String, cantidad: Int) {
        val producto = inventario.obtenerProductos().find { it.nombre.equals(nombreProducto, ignoreCase = true) }
            ?: throw IllegalArgumentException("Producto no encontrado.")
        inventario.registrarEntrada(producto, cantidad)
    }

    fun venderProducto(nombreProducto: String, cantidad: Int) {
        val producto = inventario.obtenerProductos().find { it.nombre.equals(nombreProducto, ignoreCase = true) }
            ?: throw IllegalArgumentException("Producto no encontrado.")
        inventario.registrarSalida(producto, cantidad)
    }

    fun listarTodosLosProductosConStock(): Map<Producto, Int> =
        inventario.obtenerProductos().associateWith { inventario.obtenerCantidad(it) }

    fun verHistorialDeProducto(nombreProducto: String): List<MovimientoInventario> {
        val producto = inventario.obtenerProductos().find { it.nombre.equals(nombreProducto, ignoreCase = true) }
            ?: throw IllegalArgumentException("Producto no encontrado en inventario.")
        return historial.obtenerHistorialPorProducto(producto)
    }

    fun verHistorialCompleto(): List<MovimientoInventario> = historial.obtenerTodos()

    fun borrarHistorial() = historial.borrarHistorial()

    fun guardar() {
 
    }
}

