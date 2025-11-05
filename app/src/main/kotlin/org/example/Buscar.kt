package org.example

import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Color
import javax.swing.*

class PanelBuscar : JPanel(BorderLayout()) {
    private val txtBuscar = JTextField(20)
    private val area = JTextArea()
    private val colorAzulClaro = Color(0, 90, 180)

    init {
        background = Color(245, 245, 245)
        val top = JPanel(FlowLayout(FlowLayout.LEFT))
        top.background = Color(245, 245, 245)
        val btnBuscar = JButton("Buscar")
        btnBuscar.background = colorAzulClaro
        btnBuscar.foreground = Color.WHITE
        btnBuscar.isOpaque = true
        btnBuscar.setBorderPainted(false)
        btnBuscar.setFocusPainted(false)
        top.add(JLabel("Nombre del producto:"))
        top.add(txtBuscar)
        top.add(btnBuscar)
        add(top, BorderLayout.NORTH)

        area.isEditable = false
        add(JScrollPane(area), BorderLayout.CENTER)

        btnBuscar.addActionListener { buscarProducto() }
        txtBuscar.addActionListener { buscarProducto() }
    }

    private fun buscarProducto() {
        val nombre = txtBuscar.text.trim()
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre para buscar.")
            return
        }
        val p = Inventario.buscarProducto(nombre)
        area.text = if (p != null) {
            "Producto: ${p.nombre}\nStock: ${p.stock}\nTipo: ${p.tipo}\nPrecio Compra: ${p.precioCompra}\nPrecio Reventa: ${p.precioReventa}\nFecha Vencimiento: ${p.fechaVencimiento ?: "N/A"}\n\nHistorial:\n${p.historial.joinToString("\n")}"
        } else {
            "Producto no encontrado."
        }
    }
}





