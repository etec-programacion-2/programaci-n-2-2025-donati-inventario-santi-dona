package org.example
import java.util.Scanner

fun gestionarInventarioPorConsola() {
    val servicio = ServicioInventario.cargar()
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

                servicio.crearNuevoProducto(nombre, precio, cantidad)
                println("Producto agregado correctamente.")
            }

            "2" -> {
                println("Agregar cantidad a producto existente:")
                print("Nombre del producto: ")
                val nombre = scanner.nextLine().trim()
                print("Cantidad a agregar: ")
                val cantidad = scanner.nextLine().trim().toIntOrNull()
                if (cantidad == null || cantidad <= 0) {
                    println("Cantidad inválida.")
                    continue
                }

                try {
                    servicio.agregarStock(nombre, cantidad)
                    println("Stock actualizado correctamente.")
                } catch (e: IllegalArgumentException) {
                    println(e.message)
                }
            }

            "3" -> {
                println("Vender producto:")
                print("Nombre del producto: ")
                val nombre = scanner.nextLine().trim()
                print("Cantidad a vender: ")
                val cantidad = scanner.nextLine().trim().toIntOrNull()
                if (cantidad == null || cantidad <= 0) {
                    println("Cantidad inválida.")
                    continue
                }

                try {
                    servicio.venderProducto(nombre, cantidad)
                    println("Venta realizada.")
                } catch (e: IllegalArgumentException) {
                    println(e.message)
                }
            }

            "4" -> {
                println("\nInventario actual:")
                println("------------------------------------------------------------------------------------------------------------")
                println(String.format("%-36s | %-20s | %-10s | %-10s", "ID", "Nombre", "Precio", "Cantidad"))
                println("------------------------------------------------------------------------------------------------------------")
                servicio.listarTodosLosProductosConStock().forEach { (producto, cantidad) ->
                    println(String.format("%-36s | %-20s | $%9.2f | %10d", producto.id, producto.nombre, producto.costo, cantidad))
                }
                println("------------------------------------------------------------------------------------------------------------")
            }

            "5" -> {
                println("\nSeleccione una opción:")
                println("1 - Ver historial completo")
                println("2 - Ver historial por producto")
                println("3 - Borrar historial")
                val subopcion = scanner.nextLine().trim()

                when (subopcion) {
                    "1" -> {
                        println("\nHistorial completo de movimientos:")
                        println("------------------------------------------------------------------------------------------------------")
                        println(String.format("%-30s | %-10s | %-20s | %-10s", "Fecha", "Tipo", "Producto", "Cantidad"))
                        println("------------------------------------------------------------------------------------------------------")
                        servicio.verHistorialCompleto().forEach { mov ->
                            println(String.format("%-30s | %-10s | %-20s | %10d", mov.fecha, mov.tipo, mov.producto.nombre, mov.cantidad))
                        }
                        println("------------------------------------------------------------------------------------------------------")
                    }

                    "2" -> {
                        print("Ingrese el nombre del producto: ")
                        val nombreProducto = scanner.nextLine().trim()
                        try {
                            val movimientos = servicio.verHistorialDeProducto(nombreProducto)
                            if (movimientos.isEmpty()) {
                                println("No hay movimientos registrados para $nombreProducto.")
                            } else {
                                println("\nHistorial de movimientos del producto: $nombreProducto")
                                println("------------------------------------------------------------")
                                println(String.format("%-30s | %-10s | %-10s", "Fecha", "Tipo", "Cantidad"))
                                println("------------------------------------------------------------")
                                movimientos.forEach { mov ->
                                    println(String.format("%-30s | %-10s | %10d", mov.fecha, mov.tipo, mov.cantidad))
                                }
                                println("------------------------------------------------------------")
                            }
                        } catch (e: IllegalArgumentException) {
                            println(e.message)
                        }
                    }

                    "3" -> {
                        print("⚠️ ¿Está seguro que desea borrar todo el historial? (s/n): ")
                        val confirmacion = scanner.nextLine().trim().lowercase()
                        if (confirmacion == "s") {
                            servicio.borrarHistorial()
                            println("Historial borrado correctamente.")
                        } else {
                            println("Operación cancelada.")
                        }
                    }

                    else -> println("Opción inválida.")
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











