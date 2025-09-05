package org.example

import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

data class Producto(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val descripcion: String,
    val costo: Double
) : Serializable

enum class TipoMovimiento {
    ENTRADA,
    SALIDA
}

data class MovimientoInventario(
    val producto: Producto,
    val cantidad: Int,
    val fecha: LocalDateTime = LocalDateTime.now(),
    val tipo: TipoMovimiento
) : Serializable


