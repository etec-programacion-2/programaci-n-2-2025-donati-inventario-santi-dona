package org.example

import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Inventario : Serializable {
    private const val archivoProductos = "productos.dat"
    private const val archivoHistorial = "historial.dat"
    private const val archivoGanancias = "ganancias.dat"

    private var productos = mutableListOf<Producto>()
    private var historialGeneral = mutableListOf<String>()
    var gananciaTotal: Double = 0.0

    init {
        cargarDatos()
    }

    private val fechaFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    fun agregarProducto(nombre: String, stockInicial: Int, precioCompra: Double = 0.0, precioReventa: Double = 0.0, tipo: String = "", fechaVencimiento: String? = null) {
        if (nombre.isBlank() || stockInicial <= 0 || precioCompra <= 0 || precioReventa < precioCompra) return
        val ext = productos.find { it.nombre.equals(nombre, ignoreCase = true) }
        if (ext != null) {
            ext.stock += stockInicial
            registrarMovimiento(nombre, "Aumento stock +$stockInicial")
        } else {
            val p = Producto(nombre.trim(), stockInicial, precioCompra = precioCompra, precioReventa = precioReventa, tipo = tipo, fechaVencimiento = fechaVencimiento)
            productos.add(p)
            registrarMovimiento(nombre, "Producto agregado con $stockInicial unidades")
        }
        guardarDatos()
    }

    fun registrarEntrada(nombre: String, cantidad: Int) {
        if (nombre.isBlank() || cantidad <= 0) return
        val p = productos.find { it.nombre.equals(nombre, ignoreCase = true) }
        if (p != null) {
            p.stock += cantidad
            registrarMovimiento(nombre, "Entrada +$cantidad")
        } else {
            val nuevo = Producto(nombre.trim(), cantidad)
            productos.add(nuevo)
            registrarMovimiento(nombre, "Producto agregado (entrada) +$cantidad")
        }
        guardarDatos()
    }

    fun registrarSalida(nombre: String, cantidad: Int) {
        if (nombre.isBlank() || cantidad <= 0) return
        val p = productos.find { it.nombre.equals(nombre, ignoreCase = true) }
        if (p == null) return
        if (p.stock < cantidad) return
        p.stock -= cantidad
        val ganancia = (p.precioReventa - p.precioCompra) * cantidad
        p.gananciaAcumulada += ganancia
        gananciaTotal += ganancia
        registrarMovimiento(nombre, "Salida -$cantidad (Ganancia: $ganancia)")
        if (p.stock == 0) {
            productos.remove(p)
            registrarMovimiento(nombre, "Producto quedó sin stock y fue removido del inventario")
        }
        guardarDatos()
    }

    fun marcarVencido(nombre: String) {
        val p = productos.find { it.nombre.equals(nombre, ignoreCase = true) && it.tipo == "Comida" }
        if (p != null && p.fechaVencimiento != null) {
            val perdida = p.precioCompra * p.stock
            gananciaTotal -= perdida
            productos.remove(p)
            registrarMovimiento(nombre, "Producto vencido marcado como pérdida (Pérdida: $perdida)")
            guardarDatos()
        }
    }

    private fun registrarMovimiento(nombre: String, descripcion: String) {
        val ts = LocalDateTime.now().format(fechaFmt)
        val linea = "[$ts] $nombre → $descripcion"
        historialGeneral.add(linea)
        productos.find { it.nombre.equals(nombre, ignoreCase = true) }?.historial?.add(linea)
    }

    fun obtenerHistorialGeneral(): List<String> = historialGeneral.toList()
    fun obtenerHistorialProducto(nombre: String): List<String> =
        productos.find { it.nombre.equals(nombre, ignoreCase = true) }?.historial ?: emptyList()

    fun borrarHistorial() {
        historialGeneral.clear()
        productos.forEach { it.historial.clear() }
        guardarDatos()
    }

    fun buscarProducto(nombre: String): Producto? =
        productos.find { it.nombre.equals(nombre, ignoreCase = true) }

    fun obtenerProductos(): List<Producto> = productos.filter { it.stock > 0 }.sortedBy { it.nombre }

    fun obtenerGananciasPorProducto(): List<Pair<String, Double>> =
        productos.map { it.nombre to it.gananciaAcumulada }

    private fun guardarDatos() {
        try {
            ObjectOutputStream(FileOutputStream(archivoProductos)).use { it.writeObject(productos) }
            ObjectOutputStream(FileOutputStream(archivoHistorial)).use { it.writeObject(historialGeneral) }
            ObjectOutputStream(FileOutputStream(archivoGanancias)).use { it.writeObject(gananciaTotal) }
        } catch (e: Exception) {
            println("Error guardando datos: ${e.message}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun cargarDatos() {
        try {
            val fProd = File(archivoProductos)
            val fHist = File(archivoHistorial)
            val fGan = File(archivoGanancias)
            if (fProd.exists()) {
                ObjectInputStream(FileInputStream(fProd)).use {
                    productos = it.readObject() as MutableList<Producto>
                }
            }
            if (fHist.exists()) {
                ObjectInputStream(FileInputStream(fHist)).use {
                    historialGeneral = it.readObject() as MutableList<String>
                }
            }
            if (fGan.exists()) {
                ObjectInputStream(FileInputStream(fGan)).use {
                    gananciaTotal = it.readObject() as Double
                }
            }
        } catch (e: Exception) {
            println("Error cargando datos: ${e.message}")
        }
    }
}














