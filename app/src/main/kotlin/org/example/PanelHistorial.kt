package org.example

import java.awt.*
import javax.swing.*
import javax.swing.table.DefaultTableModel
import javax.swing.KeyStroke
import java.awt.event.KeyEvent

class PanelHistorial(private val usuario: Usuario) : JPanel(BorderLayout()) {
    private val tabla: JTable
    private val modeloTabla: DefaultTableModel
    private val colorAzulOscuro = Color(0, 70, 160)
    private val colorAzulClaro = Color(0, 90, 180)

    init {
        background = Color(245, 245, 245)

        val titulo = JLabel("📊 Historial de Movimientos", JLabel.CENTER)
        titulo.font = Font("Segoe UI", Font.BOLD, 20)
        titulo.foreground = colorAzulOscuro
        add(titulo, BorderLayout.NORTH)

        modeloTabla = DefaultTableModel(arrayOf("Producto", "Cantidad", "Tipo", "Fecha", "Usuario"), 0)
        tabla = JTable(modeloTabla)
        tabla.font = Font("Segoe UI", Font.PLAIN, 14)
        tabla.rowHeight = 25
        tabla.fillsViewportHeight = true

        add(JScrollPane(tabla), BorderLayout.CENTER)

        val panelBotones = JPanel(FlowLayout(FlowLayout.RIGHT))
        panelBotones.background = Color(245, 245, 245)

        val botonRefrescar = JButton("🔄 Refrescar")
        botonRefrescar.background = colorAzulClaro
        botonRefrescar.foreground = Color.WHITE
        botonRefrescar.font = Font("Segoe UI", Font.BOLD, 14)
        botonRefrescar.isOpaque = true
        botonRefrescar.setBorderPainted(false)
        botonRefrescar.setFocusPainted(false)
        botonRefrescar.addActionListener { cargarHistorial() }

        val botonBorrar = JButton("🗑️ Borrar Historial")
        botonBorrar.background = colorAzulClaro
        botonBorrar.foreground = Color.WHITE
        botonBorrar.font = Font("Segoe UI", Font.BOLD, 14)
        botonBorrar.isOpaque = true
        botonBorrar.setBorderPainted(false)
        botonBorrar.setFocusPainted(false)
        botonBorrar.addActionListener {
            val confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que quieres borrar todo el historial? Esta acción no se puede deshacer.",
                "Confirmar Borrado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            )
            if (confirmacion == JOptionPane.YES_OPTION) {
                Inventario.borrarHistorial()
                cargarHistorial()
                JOptionPane.showMessageDialog(this, "Historial borrado correctamente.")
            }
        }

        panelBotones.add(botonRefrescar)
        panelBotones.add(botonBorrar)

        add(panelBotones, BorderLayout.SOUTH)

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "refrescar")
        actionMap.put("refrescar", object : javax.swing.AbstractAction() {
            override fun actionPerformed(e: java.awt.event.ActionEvent?) {
                cargarHistorial()
            }
        })

        cargarHistorial()
    }

    private fun cargarHistorial() {
        modeloTabla.setRowCount(0)

        val historial = Inventario.obtenerHistorialGeneral()
        println("Historial cargado: ${historial.size} entradas")

        if (historial.isEmpty()) {
            modeloTabla.addRow(arrayOf("—", "—", "—", "—", "—"))
        } else {
            for (linea in historial) {
                val partes = linea.split(" → ")
                if (partes.size == 2) {
                    val fechaProducto = partes[0]
                    val descripcion = partes[1]

                    val fechaProductoParts = fechaProducto.split("] ")
                    if (fechaProductoParts.size == 2) {
                        val fecha = fechaProductoParts[0].removePrefix("[")
                        val producto = fechaProductoParts[1]

                        val tipo = when {
                            descripcion.contains("Entrada") -> "Entrada"
                            descripcion.contains("Salida") -> "Salida"
                            else -> "Otro"
                        }
                        val cantidadMatch = Regex("([+-]?\\d+)").find(descripcion)
                        val cantidad = cantidadMatch?.groupValues?.get(1)?.toIntOrNull() ?: 0

                        modeloTabla.addRow(arrayOf(producto, cantidad.toString(), tipo, fecha, usuario.nombre))
                    }
                }
            }
        }
    }
}



