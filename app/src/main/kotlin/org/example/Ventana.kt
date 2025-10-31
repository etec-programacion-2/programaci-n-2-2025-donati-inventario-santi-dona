package org.example

import java.awt.*
import javax.swing.*

class VentanaPrincipal(private val usuario: Usuario) : JFrame("Gestor de Inventario - Libertia") {
    private val panelPrincipal = JPanel(BorderLayout())
    private val sidebar = JPanel()
    private val colorAzulOscuro = Color(0, 70, 160)
    private val colorAzulClaro = Color(0, 90, 180)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(1000, 600)
        setLocationRelativeTo(null)
        background = Color(245, 245, 245)

        crearSidebar()
        add(sidebar, BorderLayout.WEST)
        add(panelPrincipal, BorderLayout.CENTER)
        mostrarPanelInventario()
    }

    private fun crearSidebar() {
        sidebar.layout = BoxLayout(sidebar, BoxLayout.Y_AXIS)
        sidebar.background = colorAzulOscuro
        sidebar.preferredSize = Dimension(200, height)

        val titulo = JLabel("Libertia", JLabel.CENTER)
        titulo.font = Font("Segoe UI", Font.BOLD, 18)
        titulo.foreground = Color.WHITE
        titulo.alignmentX = Component.CENTER_ALIGNMENT
        sidebar.add(Box.createVerticalStrut(20))
        sidebar.add(titulo)
        sidebar.add(Box.createVerticalStrut(30))

        val btnInventario = crearBotonSidebar("📦 Inventario")
        btnInventario.addActionListener { mostrarPanelInventario() }
        sidebar.add(btnInventario)
        sidebar.add(Box.createVerticalStrut(10))

        val btnHistorial = crearBotonSidebar("📊 Historial")
        btnHistorial.addActionListener { mostrarPanelHistorial() }
        sidebar.add(btnHistorial)
        sidebar.add(Box.createVerticalStrut(10))

        val btnBuscar = crearBotonSidebar("🔍 Buscar")
        btnBuscar.addActionListener { mostrarPanelBuscar() }
        sidebar.add(btnBuscar)
        sidebar.add(Box.createVerticalStrut(10))

        // Botón de Exportar con menú desplegable
        val btnExportar = crearBotonSidebar("📤 Exportar")
        val popupMenu = JPopupMenu()
        val itemExportarStock = JMenuItem("Exportar Stock")
        itemExportarStock.addActionListener { Exportar.exportarStockAExcel() }
        val itemExportarHistorial = JMenuItem("Exportar Historial")
        itemExportarHistorial.addActionListener { Exportar.exportarHistorialAExcel(null) }
        val itemExportarAmbos = JMenuItem("Exportar Ambos")
        itemExportarAmbos.addActionListener { Exportar.exportarAmbosAExcel() }
        popupMenu.add(itemExportarStock)
        popupMenu.add(itemExportarHistorial)
        popupMenu.add(itemExportarAmbos)
        btnExportar.addActionListener { popupMenu.show(btnExportar, 0, btnExportar.height) }
        sidebar.add(btnExportar)
        sidebar.add(Box.createVerticalStrut(10))

        if (usuario.esAdmin) {
            val btnGestionarUsuarios = crearBotonSidebar("👥 Gestionar Usuarios")
            btnGestionarUsuarios.addActionListener { mostrarPanelUsuarios() }
            sidebar.add(btnGestionarUsuarios)
            sidebar.add(Box.createVerticalStrut(10))

            val btnGanancias = crearBotonSidebar("💰 Ganancias")
            btnGanancias.addActionListener { mostrarPanelGanancias() }
            sidebar.add(btnGanancias)
            sidebar.add(Box.createVerticalStrut(10))
        }

        val btnMiCuenta = crearBotonSidebar("👤 Mi Cuenta")
        val popupMenuCuenta = JPopupMenu()
        val itemCerrarSesion = JMenuItem("Cerrar Sesión")
        itemCerrarSesion.addActionListener {
            dispose()
            Login().isVisible = true
        }
        val itemBorrarCuenta = JMenuItem("Borrar Cuenta")
        itemBorrarCuenta.addActionListener {
            val confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que quieres borrar tu cuenta? Todos tus datos se perderán y no podrán recuperarlos.",
                "Confirmar Borrado de Cuenta",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            )
            if (confirmacion == JOptionPane.YES_OPTION) {
                val eliminado = RegistroUsuarios.eliminarUsuario(usuario.nombre)
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Cuenta borrada correctamente. Volviendo al inicio de sesión.")
                    dispose()
                    Login().isVisible = true
                } else {
                    JOptionPane.showMessageDialog(this, "Error al borrar la cuenta.")
                }
            }
        }
        popupMenuCuenta.add(itemCerrarSesion)
        popupMenuCuenta.add(itemBorrarCuenta)
        btnMiCuenta.addActionListener { popupMenuCuenta.show(btnMiCuenta, 0, btnMiCuenta.height) }

        sidebar.add(Box.createVerticalGlue())
        sidebar.add(btnMiCuenta)
        sidebar.add(Box.createVerticalStrut(20))
    }

    private fun crearBotonSidebar(texto: String): JButton {
        val boton = JButton(texto)
        boton.font = Font("Segoe UI", Font.PLAIN, 14)
        boton.background = colorAzulClaro
        boton.foreground = Color.WHITE
        boton.isOpaque = true
        boton.setBorderPainted(false)
        boton.setFocusPainted(false)
        boton.maximumSize = Dimension(180, 40)
        boton.alignmentX = Component.CENTER_ALIGNMENT
        return boton
    }

    private fun mostrarPanelInventario() {
        panelPrincipal.removeAll()
        panelPrincipal.add(PanelInventario(usuario), BorderLayout.CENTER)
        panelPrincipal.revalidate()
        panelPrincipal.repaint()
    }

    private fun mostrarPanelHistorial() {
        panelPrincipal.removeAll()
        panelPrincipal.add(PanelHistorial(usuario), BorderLayout.CENTER)
        panelPrincipal.revalidate()
        panelPrincipal.repaint()
    }

    private fun mostrarPanelBuscar() {
        panelPrincipal.removeAll()
        panelPrincipal.add(PanelBuscar(), BorderLayout.CENTER)
        panelPrincipal.revalidate()
        panelPrincipal.repaint()
    }

    private fun mostrarPanelUsuarios() {
        panelPrincipal.removeAll()
        panelPrincipal.add(PanelUsuarios(usuario), BorderLayout.CENTER)
        panelPrincipal.revalidate()
        panelPrincipal.repaint()
    }

    private fun mostrarPanelGanancias() {
        panelPrincipal.removeAll()
        panelPrincipal.add(PanelGanancias(usuario), BorderLayout.CENTER)
        panelPrincipal.revalidate()
        panelPrincipal.repaint()
    }
}

























