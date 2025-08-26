package org.example
import java.util.UUID

data class Producto(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val descripcion: String,
    val costo: Double
)

fun main() {
    val producto1 = Producto(nombre = "PlayStation 5", descripcion = "Consola de última generación", costo = 999.99)
    val producto2 = Producto(nombre = "Notebook Lenovo", descripcion = "Notebook para uso profesional", costo = 1200.0)

    println(producto1)
    println(producto2)
}
**chau**
