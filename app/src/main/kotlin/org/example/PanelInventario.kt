package org.example

import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Color
import java.awt.Font
import javax.swing.*
import javax.swing.table.DefaultTableModel
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileInputStream
import javax.swing.KeyStroke
import java.awt.event.KeyEvent

class PanelInventario(private val usuario: Usuario) : JPanel(BorderLayout()) {
    private val modelo = DefaultTableModel(arrayOf("Producto", "Stock", "Tipo", "Precio Compra", "Precio Reventa"), 0)
    private val tabla = JTable(modelo)
    private val colorAzulClaro = Color(0, 90, 180)

    init {
        background = Color(245, 245, 245)

        val panelTop = JPanel(FlowLayout(FlowLayout.LEFT, 8, 8))
        panelTop.background = Color(245, 245, 245)
        val btnAgregar = crearBoton("Agregar")
        val btnEntrada = crearBoton("Entrada")
        val btnSalida = crearBoton("Salida")
        val btnRefrescar = crearBoton("Refrescar")
        val btnImportar = crearBoton("📥 Importar Excel")
        val btnMarcarVencido = crearBoton("⚠️ Marcar Vencido")

        panelTop.add(btnAgregar)
        panelTop.add(btnEntrada)
        panelTop.add(btnSalida)
        panelTop.add(btnRefrescar)
        panelTop.add(btnImportar)
        panelTop.add(btnMarcarVencido)

        add(panelTop, BorderLayout.NORTH)
        add(JScrollPane(tabla), BorderLayout.CENTER)

        btnAgregar.addActionListener {
            val nombre = JOptionPane.showInputDialog(this, "Nombre del producto:")
            if (nombre.isNullOrBlank()) return@addActionListener
            val cant = JOptionPane.showInputDialog(this, "Cantidad inicial:")?.toIntOrNull() ?: 0
            if (cant <= 0) return@addActionListener
            val precioCompra = JOptionPane.showInputDialog(this, "Precio de compra:")?.toDoubleOrNull() ?: 0.0
            if (precioCompra <= 0) return@addActionListener
            var precioReventa: Double
            do {
                precioReventa = JOptionPane.showInputDialog(this, "Precio de reventa (debe ser >= $precioCompra):")?.toDoubleOrNull() ?: 0.0
                if (precioReventa < precioCompra) JOptionPane.showMessageDialog(this, "El precio de reventa no puede ser menor al de compra.")
            } while (precioReventa < precioCompra)
            val tipos = arrayOf("Tecnología", "Comida", "Limpieza", "Otro")
            val tipo = JOptionPane.showInputDialog(this, "Tipo de producto:", "Seleccionar", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]) as String
            var fechaVencimiento: String? = null
            if (tipo == "Comida") {
                fechaVencimiento = JOptionPane.showInputDialog(this, "Fecha de vencimiento (dd/MM/yyyy):")
            }
            Inventario.agregarProducto(nombre, cant, precioCompra, precioReventa, tipo, fechaVencimiento)
            actualizarTabla()
            JOptionPane.showMessageDialog(this, "Producto agregado.")
        }

        btnEntrada.addActionListener {
            val nombre = JOptionPane.showInputDialog(this, "Producto (entrada):")
            val cant = JOptionPane.showInputDialog(this, "Cantidad a ingresar:")?.toIntOrNull() ?: 0
            if (!nombre.isNullOrBlank() && cant > 0) {
                Inventario.registrarEntrada(nombre, cant)
                actualizarTabla()
            }
        }

        btnSalida.addActionListener {
            val nombre = JOptionPane.showInputDialog(this, "Producto (salida):")
            val cant = JOptionPane.showInputDialog(this, "Cantidad a retirar:")?.toIntOrNull() ?: 0
            if (!nombre.isNullOrBlank() && cant > 0) {
                Inventario.registrarSalida(nombre, cant)
                actualizarTabla()
            }
        }

        btnRefrescar.addActionListener { actualizarTabla() }

        btnImportar.addActionListener {
            val fileChooser = JFileChooser()
            fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY
            fileChooser.fileFilter = object : javax.swing.filechooser.FileFilter() {
                override fun accept(f: File): Boolean = f.isDirectory || f.name.endsWith(".xlsx")
                override fun getDescription(): String = "Archivos Excel (.xlsx)"
            }
            val result = fileChooser.showOpenDialog(this)
            if (result == JFileChooser.APPROVE_OPTION) {
                val archivo = fileChooser.selectedFile
                importarDesdeExcel(archivo)
            }
        }

        btnMarcarVencido.addActionListener {
            val comidas = Inventario.obtenerProductos().filter { it.tipo == "Comida" && it.fechaVencimiento != null }
            if (comidas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay productos de comida con fecha de vencimiento.")
                return@addActionListener
            }
            val opciones = comidas.map { it.nombre }.toTypedArray()
            val seleccionado = JOptionPane.showInputDialog(this, "Selecciona producto vencido:", "Marcar Vencido", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]) as String?
            if (seleccionado != null) {
                Inventario.marcarVencido(seleccionado)
                actualizarTabla()
                JOptionPane.showMessageDialog(this, "Producto marcado como vencido y descontado de ganancias.")
            }
        }

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "refrescar")
        actionMap.put("refrescar", object : javax.swing.AbstractAction() {
            override fun actionPerformed(e: java.awt.event.ActionEvent?) {
                actualizarTabla()
            }
        })

        actualizarTabla()
    }

    private fun crearBoton(texto: String): JButton {
        val boton = JButton(texto)
        boton.background = colorAzulClaro
        boton.foreground = Color.WHITE
        boton.isOpaque = true
        boton.setBorderPainted(false)
        boton.setFocusPainted(false)
        return boton
    }

    private fun importarDesdeExcel(archivo: File) {
        try {
            val workbook = WorkbookFactory.create(FileInputStream(archivo))
            val hoja = workbook.getSheetAt(0)
            val productosActuales = Inventario.obtenerProductos().associateBy { it.nombre }
            val diferenciales = mutableListOf<Triple<String, Int, String>>()

            for (i in 1 until hoja.lastRowNum + 1) {
                val fila = hoja.getRow(i) ?: continue
                val nombre = fila.getCell(0)?.stringCellValue?.trim() ?: continue
                val stockImportado = fila.getCell(1)?.numericCellValue?.toInt() ?: 0

                val productoExistente = productosActuales[nombre]
                if (productoExistente == null) {
                    diferenciales.add(Triple(nombre, stockImportado, "Nuevo"))
                } else {
                    val diferencia = stockImportado - productoExistente.stock
                    val tipo = when {
                        diferencia > 0 -> "Entrada (+$diferencia)"
                        diferencia < 0 -> "Salida (${diferencia})"
                        else -> "Sin cambio"
                    }
                    diferenciales.add(Triple(nombre, diferencia, tipo))
                }
            }

            workbook.close()

            if (diferenciales.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron datos válidos en el archivo.")
                return
            }

            mostrarDiferencial(diferenciales)

        } catch (e: Exception) {
            JOptionPane.showMessageDialog(this, "Error al importar: ${e.message}")
        }
    }

    private fun mostrarDiferencial(diferenciales: List<Triple<String, Int, String>>) {
        val dialog = JDialog(SwingUtilities.getWindowAncestor(this) as JFrame, "Diferencial de Importación", true)
        dialog.setSize(600, 400)
        dialog.setLocationRelativeTo(this)
        dialog.layout = BorderLayout()

        val modeloDialog = DefaultTableModel(arrayOf("Producto", "Diferencia", "Tipo"), 0)
        val tablaDialog = JTable(modeloDialog)
        tablaDialog.font = Font("Segoe UI", Font.PLAIN, 14)
        tablaDialog.rowHeight = 25

        for ((producto, diferencia, tipo) in diferenciales) {
            modeloDialog.addRow(arrayOf(producto, diferencia.toString(), tipo))
        }

        dialog.add(JScrollPane(tablaDialog), BorderLayout.CENTER)

        val panelBotones = JPanel(FlowLayout())
        val btnAplicar = JButton("✅ Aplicar Cambios")
        btnAplicar.background = colorAzulClaro
        btnAplicar.foreground = Color.WHITE
        btnAplicar.isOpaque = true
        btnAplicar.setBorderPainted(false)
        btnAplicar.setFocusPainted(false)
        btnAplicar.addActionListener {
            aplicarCambios(diferenciales)
            actualizarTabla()
            dialog.dispose()
            JOptionPane.showMessageDialog(this, "Cambios aplicados al inventario.")
        }

        val btnCancelar = JButton("❌ Cancelar")
        btnCancelar.addActionListener { dialog.dispose() }

        panelBotones.add(btnAplicar)
        panelBotones.add(btnCancelar)
        dialog.add(panelBotones, BorderLayout.SOUTH)

        dialog.isVisible = true
    }

    private fun aplicarCambios(diferenciales: List<Triple<String, Int, String>>) {
        for ((producto, diferencia, tipo) in diferenciales) {
            when {
                tipo == "Nuevo" -> Inventario.agregarProducto(producto, diferencia)
                tipo.contains("Entrada") -> Inventario.registrarEntrada(producto, diferencia)
                tipo.contains("Salida") -> Inventario.registrarSalida(producto, -diferencia)
            }
        }
    }

    fun actualizarTabla() {
        modelo.rowCount = 0
        Inventario.obtenerProductos().forEach {
            modelo.addRow(arrayOf(it.nombre, it.stock, it.tipo, it.precioCompra, it.precioReventa))
        }
    }
}





