package org.example
import java.util.UUID
// Clase Producto
data class Producto(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val descripcion: String,
    val costo: Double
)
