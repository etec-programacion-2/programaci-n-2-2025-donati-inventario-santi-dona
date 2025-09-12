package org.example
import java.util.Scanner

fun gestionarInventarioPorConsola() {
    val historial = HistorialInventario.cargar()
    val inventario = Inventario.cargar(historial)
    val scanner = Scanner(System.`in`)

    println("Bienvenido al sistema de inventario.")

    while (true) {
        println("\nSeleccione una opción:")
        println("1 - Agregar producto nuevo al stock")
        println("2 - Agregar cantidad de producto existente")
        println("3 - Vender algún producto")
        println("4 - Mostrar inventario actual")
        println("5 - Mostrar historial de movimientos")
        println("6 - Salir")
        print("Opción: ")

        if (!scanner.hasNextLine()) break
        val opcion = scanner.nextLine().trim()

        when (opcion) {
            "1" -> {
                println("Agregar producto nuevo:")
                print("Nombre: ")
                val nombre = scanner.nextLine().trim()
                print("Descripción: ")
                val descripcion = scanner.nextLine().trim()
                print("Precio: ")
                val precio = scanner.nextLine().trim().toDoubleOrNull()
                if (precio == null || precio < 0) {
                    println("Precio inválido.")
                    continue
                }
                print("Cantidad inicial: ")
                val cantidad = scanner.nextLine().trim().toIntOrNull()
                if (cantidad == null || cantidad < 0) {
                    println("Cantidad inválida.")
                    continue
                }

                val producto = Producto(nombre = nombre, descripcion = descripcion, costo = precio)
                inventario.registrarEntrada(producto, cantidad)
                println("Producto agregado correctamente.")
            }

            "2" -> {
                println("Agregar cantidad a producto existente:")
                print("Nombre del producto: ")
                val nombre = scanner.nextLine().trim()
                val productoExistente = inventario.obtenerProductos()
                    .find { it.nombre.equals(nombre, ignoreCase = true) }
                if (productoExistente == null) {
                    println("Producto no encontrado.")
                    continue
                }

                print("Cantidad a agregar: ")
                val cantidad = scanner.nextLine().trim().toIntOrNull()
                if (cantidad == null || cantidad <= 0) {
                    println("Cantidad inválida.")
                    continue
                }

                inventario.registrarEntrada(productoExistente, cantidad)
                println("Stock actualizado correctamente.")
            }

            "3" -> {
                println("Vender producto:")
                print("Nombre del producto: ")
                val nombre = scanner.nextLine().trim()
                val productoExistente = inventario.obtenerProductos()
                    .find { it.nombre.equals(nombre, ignoreCase = true) }
                if (productoExistente == null) {
                    println("Producto no encontrado.")
                    continue
                }

                print("Cantidad a vender: ")
                val cantidad = scanner.nextLine().trim().toIntOrNull()
                if (cantidad == null || cantidad <= 0) {
                    println("Cantidad inválida.")
                    continue
                }

                try {
                    inventario.registrarSalida(productoExistente, cantidad)
                    println("Venta realizada.")
                } catch (e: IllegalArgumentException) {
                    println(e.message)
                }
            }

            "4" -> {
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

            "5" -> {
                println("\nSeleccione una opción:")
                println("1 - Ver historial completo")
                println("2 - Ver historial por producto")
                val subopcion = scanner.nextLine().trim()

                if (subopcion == "1") {
                    println("\nHistorial completo de movimientos:")
                    println("----------------------------------------------------------------------------------------------------------------")
                    println(String.format("%-20s | %-10s | %-20s | %-10s | %-10s", "Fecha", "Tipo", "Producto", "Cantidad", "Stock Final"))
                    println("----------------------------------------------------------------------------------------------------------------")
                    historial.obtenerTodos().forEach { mov ->
                        val stockDespues = inventario.obtenerCantidad(mov.producto)
                        println(
                            String.format(
                                "%-20s | %-10s | %-20s | %10d | %10d",
                                mov.fecha,
                                mov.tipo,
                                mov.producto.nombre,
                                mov.cantidad,
                                stockDespues
                            )
                        )
                    }
                    println("----------------------------------------------------------------------------------------------------------------")
                } else if (subopcion == "2") {
                    print("Ingrese el nombre del producto: ")
                    val nombreProducto = scanner.nextLine().trim()
                    val producto = inventario.obtenerProductos()
                        .find { it.nombre.equals(nombreProducto, ignoreCase = true) }

                    if (producto == null) {
                        println("Producto no encontrado en el inventario o en el historial.")
                    } else {
                        val movimientos = historial.obtenerHistorialPorProducto(producto)
                        if (movimientos.isEmpty()) {
                            println("No hay movimientos registrados para ${producto.nombre}.")
                        } else {
                            println("\nHistorial de movimientos del producto: ${producto.nombre}")
                            println("------------------------------------------------------------------------------------------------------")
                            println(String.format("%-30s | %-20s | %-20s | %-10s", "Fecha", "Tipo", "Cantidad", "Stock Final"))
                            println("------------------------------------------------------------------------------------------------------")

                            var stockAcumulado = 0
                            movimientos.forEach { mov ->
                                stockAcumulado += mov.cantidad
                                println(
                                    String.format(
                                        "%-30s | %-20s | %20d | %10d",
                                        mov.fecha,
                                        mov.tipo,
                                        mov.cantidad,
                                        stockAcumulado
                                    )
                                )
                            }
                            println("------------------------------------------------------------------------------------------------------")
                        }
                    }
                } else {
                    println("Opción inválida.")
                }
            }

            "6" -> {
                println("Saliendo del programa...")
                break
            }

            else -> println("Opción inválida.")
        }
    }
}








