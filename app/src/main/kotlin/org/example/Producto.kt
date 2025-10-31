package org.example

import java.io.Serializable

data class Producto(
    val nombre: String,
    var stock: Int,
    val historial: MutableList<String> = mutableListOf(),
    var precioCompra: Double = 0.0,
    var precioReventa: Double = 0.0,
    var tipo: String = "",
    var fechaVencimiento: String? = null,
    var gananciaAcumulada: Double = 0.0
) : Serializable




