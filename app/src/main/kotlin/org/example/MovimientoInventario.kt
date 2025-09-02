package org.example

import java.time.LocalDateTime

data class MovimientoInventario(
    val producto: Producto,
    val cantidad: Int,
    val fecha: LocalDateTime = LocalDateTime.now(),
    val tipo: TipoMovimiento
)
