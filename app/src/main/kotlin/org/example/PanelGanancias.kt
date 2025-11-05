package org.example

import java.awt.*
import javax.swing.*
import javax.swing.table.DefaultTableModel
import javax.swing.KeyStroke
import java.awt.event.KeyEvent

class PanelGanancias(private val usuario: Usuario) : JPanel(BorderLayout()) {
    private lateinit var modeloTabla: DefaultTableModel
    private lateinit var tabla: JTable
    private val colorAzulOscuro = Color(0, 70, 160)
    private val colorAzulClaro = Color(0, 90, 180)

    init {
        inicializar()
    }

    private fun inicializar() {
        if (!usuario.esAdmin) {
            add(JLabel("Acceso denegado: Solo administradores pueden ver ganancias."), BorderLayout.CENTER)
            return
        }

        background = Color(245, 245, 245)

        var titulo = JLabel("💰 Ganancias del Negocio", JLabel.CENTER)
        titulo.font = Font("Segoe UI", Font.BOLD, 20)
        titulo.foreground = colorAzulOscuro
        add(titulo, BorderLayout.NORTH)

        modeloTabla = DefaultTableModel(arrayOf("Producto", "Ganancia Acumulada"), 0)
        tabla = JTable(modeloTabla)
        tabla.font = Font("Segoe UI", Font.PLAIN, 14)
        tabla.rowHeight = 25
        tabla.fillsViewportHeight = true

        add(JScrollPane(tabla), BorderLayout.CENTER)

        val panelBotones = JPanel(FlowLayout(FlowLayout.RIGHT))
        panelBotones.background = Color(245, 245, 245)

        var lblTotal = JLabel("Ganancia Total: ${Inventario.gananciaTotal}")
        lblTotal.font = Font("Segoe UI", Font.BOLD, 16)
        lblTotal.foreground = colorAzulOscuro

        val btnRefrescar = JButton("🔄 Refrescar")
        btnRefrescar.background = colorAzulClaro
        btnRefrescar.foreground = Color.WHITE
        btnRefrescar.font = Font("Segoe UI", Font.BOLD, 14)
        btnRefrescar.isOpaque = true
        btnRefrescar.setBorderPainted(false)
        btnRefrescar.setFocusPainted(false)
        btnRefrescar.addActionListener { cargarGanancias() }

        panelBotones.add(lblTotal)
        panelBotones.add(btnRefrescar)

        add(panelBotones, BorderLayout.SOUTH)

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "refrescar")
        actionMap.put("refrescar", object : javax.swing.AbstractAction() {
            override fun actionPerformed(e: java.awt.event.ActionEvent?) {
                cargarGanancias()
            }
        })

        cargarGanancias()
    }

    private fun cargarGanancias() {
        modeloTabla.setRowCount(0)
        Inventario.obtenerGananciasPorProducto().forEach { (producto: String, ganancia: Double) ->
            modeloTabla.addRow(arrayOf(producto, ganancia))
        }
    }
}

