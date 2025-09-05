package org.example

import java.util.Scanner

fun gestionarInventarioPorConsola() {
    val inventario = Inventario.cargar() // Cargar inventario desde archivo
    val scanner = Scanner(System.`in`)

    println("Bienvenido al sistema de inventario.")

    while (true) {
        println("\nSeleccione una opción:")
        println("1 - Agregar producto nuevo al stock")
        println("2 - Agregar cantidad de producto existente")
        println("3 - Vender algún producto")
        println("4 - Mostrar inventario actual")
        println("5 - Salir")
        print("Opción: ")

        if (!scanner.hasNextLine()) {
            println("\nNo se detectó más entrada. Saliendo...")
            break
        }
        val opcion = scanner.nextLine().trim()

        when (opcion) {
            "1" -> { // Agregar producto nuevo
                println("Agregar producto nuevo:")
                print("Nombre: ")
                if (!scanner.hasNextLine()) break
                val nombre = scanner.nextLine().trim()

                print("Descripción: ")
                if (!scanner.hasNextLine()) break
                val descripcion = scanner.nextLine().trim()

                print("Precio: ")
                if (!scanner.hasNextLine()) break
                val precioStr = scanner.nextLine().trim()
                val precio = precioStr.toDoubleOrNull()
                if (precio == null || precio < 0) {
                    println("Precio inválido. Intente de nuevo.")
                    continue
                }

                print("Cantidad inicial: ")
                if (!scanner.hasNextLine()) break
                val cantidadStr = scanner.nextLine().trim()
                val cantidad = cantidadStr.toIntOrNull()
                if (cantidad == null || cantidad < 0) {
                    println("Cantidad inválida. Intente de nuevo.")
                    continue
                }

                val producto = Producto(nombre = nombre, descripcion = descripcion, costo = precio)
                inventario.agregarOActualizarProducto(producto, cantidad)
                println("Producto agregado correctamente.")
            }

            "2" -> { // Agregar cantidad a producto existente
                println("Agregar cantidad a producto existente:")
                print("Nombre del producto: ")
                if (!scanner.hasNextLine()) break
                val nombre = scanner.nextLine().trim()

                val productoExistente = inventario.obtenerProductos()
                    .find { it.nombre.equals(nombre, ignoreCase = true) }

                if (productoExistente == null) {
                    println("Producto no encontrado en el inventario.")
                    continue
                }

                print("Cantidad a agregar: ")
                if (!scanner.hasNextLine()) break
                val cantidadStr = scanner.nextLine().trim()
                val cantidad = cantidadStr.toIntOrNull()
                if (cantidad == null || cantidad <= 0) {
                    println("Cantidad inválida. Intente de nuevo.")
                    continue
                }

                inventario.agregarOActualizarProducto(productoExistente, cantidad)
                println("Stock actualizado correctamente.")
            }

            "3" -> { // Vender producto
                println("Vender producto:")
                print("Nombre del producto: ")
                if (!scanner.hasNextLine()) break
                val nombre = scanner.nextLine().trim()

                val productoExistente = inventario.obtenerProductos()
                    .find { it.nombre.equals(nombre, ignoreCase = true) }

                if (productoExistente == null) {
                    println("Producto no encontrado en el inventario.")
                    continue
                }

                print("Cantidad a vender: ")
                if (!scanner.hasNextLine()) break
                val cantidadStr = scanner.nextLine().trim()
                val cantidad = cantidadStr.toIntOrNull()
                if (cantidad == null || cantidad <= 0) {
                    println("Cantidad inválida. Intente de nuevo.")
                    continue
                }

                val stockActual = inventario.obtenerCantidad(productoExistente)
                if (cantidad > stockActual) {
                    println("No hay suficiente stock. Stock disponible: $stockActual")
                    continue
                }

                // Reducir stock
                inventario.agregarOActualizarProducto(productoExistente, -cantidad)
                println("Venta realizada. Stock restante: ${inventario.obtenerCantidad(productoExistente)}")
            }

            "4" -> { // Mostrar inventario
                println("\nInventario actual:")
                println("------------------------------------------------------------------------------------------------------------")
                println(String.format("%-36s | %-20s | %-30s | %-10s | %-10s", "ID", "Nombre", "Descripción", "Precio", "Cantidad"))
                println("------------------------------------------------------------------------------------------------------------")
                inventario.obtenerProductos().forEach { producto ->
                    val cantidad = inventario.obtenerCantidad(producto)
                    println(
                        String.format(
                            "%-36s | %-20s | %-30s | $%9.2f | %10d",
                            producto.id,
                            producto.nombre,
                            producto.descripcion,
                            producto.costo,
                            cantidad
                        )
                    )
                }
                println("------------------------------------------------------------------------------------------------------------")
            }

            "5" -> { // Salir
                println("Saliendo del programa...")
                break
            }

            else -> {
                println("Opción inválida. Intente de nuevo.")
            }
        }
    }
}







