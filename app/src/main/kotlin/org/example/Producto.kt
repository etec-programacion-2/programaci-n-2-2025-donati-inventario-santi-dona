package org.example
import java.util.UUID
data class Producto(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val descripcion: String,
    val costo: Double
)
