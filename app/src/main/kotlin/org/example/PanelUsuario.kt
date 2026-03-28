package org.example

import java.awt.*
import javax.swing.*
import javax.swing.table.DefaultTableModel
import javax.swing.KeyStroke
import java.awt.event.KeyEvent

class PanelUsuarios(private val usuario: Usuario) : JPanel(BorderLayout()) {
    private lateinit var modeloTabla: DefaultTableModel
    private lateinit var tabla: JTable
    private val colorAzulOscuro = Color(0, 70, 160)
    private val colorAzulClaro = Color(0, 90, 180)

    init {
        inicializar()
    }

    private fun inicializar() {
        if (!usuario.esAdmin) {
            add(JLabel("Acceso denegado: Solo administradores pueden gestionar usuarios."), BorderLayout.CENTER)
            return
        }

        background = Color(245, 245, 245)

        val titulo = JLabel("👥 Gestión de Usuarios", JLabel.CENTER)
        titulo.font = Font("Segoe UI", Font.BOLD, 20)
        titulo.foreground = colorAzulOscuro
        add(titulo, BorderLayout.NORTH)

        modeloTabla = DefaultTableModel(arrayOf("Nombre", "Es Admin"), 0)
        tabla = JTable(modeloTabla)
        tabla.font = Font("Segoe UI", Font.PLAIN, 14)
        tabla.rowHeight = 25
        tabla.fillsViewportHeight = true

        add(JScrollPane(tabla), BorderLayout.CENTER)

        val panelBotones = JPanel(FlowLayout(FlowLayout.RIGHT))
        panelBotones.background = Color(245, 245, 245)

        val botonCrear = JButton("➕ Crear Usuario")
        botonCrear.background = colorAzulClaro
        botonCrear.foreground = Color.WHITE
        botonCrear.font = Font("Segoe UI", Font.BOLD, 14)
        botonCrear.isOpaque = true
        botonCrear.setBorderPainted(false)
        botonCrear.setFocusPainted(false)
        botonCrear.addActionListener {
            Registro().isVisible = true
        }

        val botonRefrescar = JButton("🔄 Refrescar")
        botonRefrescar.background = colorAzulClaro
        botonRefrescar.foreground = Color.WHITE
        botonRefrescar.font = Font("Segoe UI", Font.BOLD, 14)
        botonRefrescar.isOpaque = true
        botonRefrescar.setBorderPainted(false)
        botonRefrescar.setFocusPainted(false)
        botonRefrescar.addActionListener { cargarUsuarios() }

        val botonBorrar = JButton("🗑️ Borrar Usuario")
        botonBorrar.background = colorAzulClaro
        botonBorrar.foreground = Color.WHITE
        botonBorrar.font = Font("Segoe UI", Font.BOLD, 14)
        botonBorrar.isOpaque = true
        botonBorrar.setBorderPainted(false)
        botonBorrar.setFocusPainted(false)
        botonBorrar.addActionListener {
            val filaSeleccionada = tabla.selectedRow
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla para borrar.")
                return@addActionListener
            }
            val nombreUsuario = modeloTabla.getValueAt(filaSeleccionada, 0) as String
            if (nombreUsuario == usuario.nombre) {
                JOptionPane.showMessageDialog(this, "No puedes borrar tu propia cuenta desde aquí.")
                return@addActionListener
            }
            val confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que quieres borrar al usuario '$nombreUsuario'? Esta acción no se puede deshacer.",
                "Confirmar Borrado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            )
            if (confirmacion == JOptionPane.YES_OPTION) {
                val eliminado = RegistroUsuarios.eliminarUsuario(nombreUsuario)
                if (eliminado) {
                    cargarUsuarios()
                    JOptionPane.showMessageDialog(this, "Usuario '$nombreUsuario' borrado correctamente.")
                } else {
                    JOptionPane.showMessageDialog(this, "Error al borrar el usuario.")
                }
            }
        }

        panelBotones.add(botonCrear)
        panelBotones.add(botonRefrescar)
        panelBotones.add(botonBorrar)

        add(panelBotones, BorderLayout.SOUTH)

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "refrescar")
        actionMap.put("refrescar", object : javax.swing.AbstractAction() {
            override fun actionPerformed(e: java.awt.event.ActionEvent?) {
                cargarUsuarios()
            }
        })

        cargarUsuarios()
    }

    private fun cargarUsuarios() {
        modeloTabla.setRowCount(0)

        val usuarios = RegistroUsuarios.listarUsuarios()
        for (u in usuarios) {
            modeloTabla.addRow(arrayOf(u.nombre, if (u.esAdmin) "Sí" else "No"))
        }
    }
}



