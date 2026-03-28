package org.example

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import javax.swing.table.DefaultTableModel
import java.io.FileOutputStream
import javax.swing.JOptionPane
import java.io.File

object Exportar {

    fun exportarHistorialAExcel(modelo: DefaultTableModel?) {
        try {
            val workbook = XSSFWorkbook()
            val hoja = workbook.createSheet("Historial")

            val estiloEncabezado = workbook.createCellStyle()
            val fuente = workbook.createFont()
            fuente.bold = true
            fuente.color = IndexedColors.WHITE.index
            estiloEncabezado.setFont(fuente)
            estiloEncabezado.fillForegroundColor = IndexedColors.BLUE.index
            estiloEncabezado.fillPattern = FillPatternType.SOLID_FOREGROUND

            val filaEncabezado = hoja.createRow(0)
            val columnas = if (modelo != null) {
                for (i in 0 until modelo.columnCount) {
                    val celda = filaEncabezado.createCell(i)
                    celda.setCellValue(modelo.getColumnName(i))
                    celda.cellStyle = estiloEncabezado
                }
                modelo
            } else {
                // Historial general
                filaEncabezado.createCell(0).setCellValue("Historial")
                filaEncabezado.getCell(0).cellStyle = estiloEncabezado
                val historial = Inventario.obtenerHistorialGeneral()
                val modeloTemp = DefaultTableModel(arrayOf("Movimiento"), historial.size)
                historial.forEachIndexed { index, linea ->
                    modeloTemp.setValueAt(linea, index, 0)
                }
                modeloTemp
            }

            for (i in 0 until columnas.rowCount) {
                val fila = hoja.createRow(i + 1)
                for (j in 0 until columnas.columnCount) {
                    fila.createCell(j).setCellValue(columnas.getValueAt(i, j).toString())
                }
            }

            for (i in 0 until columnas.columnCount) {
                hoja.autoSizeColumn(i)
            }

            val archivo = File("historial_inventario.xlsx")
            FileOutputStream(archivo).use { fos ->
                workbook.write(fos)
            }

            workbook.close()

            JOptionPane.showMessageDialog(null, "Archivo Excel exportado correctamente en:\n${archivo.absolutePath}")

        } catch (e: Exception) {
            e.printStackTrace()
            JOptionPane.showMessageDialog(null, "Error al exportar el archivo: ${e.message}")
        }
    }

    fun exportarStockAExcel() {
        try {
            val workbook = XSSFWorkbook()
            val hoja = workbook.createSheet("Stock")

            val estiloEncabezado = workbook.createCellStyle()
            val fuente = workbook.createFont()
            fuente.bold = true
            fuente.color = IndexedColors.WHITE.index
            estiloEncabezado.setFont(fuente)
            estiloEncabezado.fillForegroundColor = IndexedColors.BLUE.index
            estiloEncabezado.fillPattern = FillPatternType.SOLID_FOREGROUND

            val filaEncabezado = hoja.createRow(0)
            val columnas = arrayOf("Producto", "Stock", "Tipo", "Precio Compra", "Precio Reventa")
            for (i in columnas.indices) {
                val celda = filaEncabezado.createCell(i)
                celda.setCellValue(columnas[i])
                celda.cellStyle = estiloEncabezado
            }

            val productos = Inventario.obtenerProductos()
            for (i in productos.indices) {
                val fila = hoja.createRow(i + 1)
                fila.createCell(0).setCellValue(productos[i].nombre)
                fila.createCell(1).setCellValue(productos[i].stock.toDouble())
                fila.createCell(2).setCellValue(productos[i].tipo)
                fila.createCell(3).setCellValue(productos[i].precioCompra)
                fila.createCell(4).setCellValue(productos[i].precioReventa)
            }

            for (i in columnas.indices) {
                hoja.autoSizeColumn(i)
            }

            val archivo = File("stock_inventario.xlsx")
            FileOutputStream(archivo).use { fos ->
                workbook.write(fos)
            }

            workbook.close()

            JOptionPane.showMessageDialog(null, "Archivo Excel exportado correctamente en:\n${archivo.absolutePath}")

        } catch (e: Exception) {
            e.printStackTrace()
            JOptionPane.showMessageDialog(null, "Error al exportar el archivo: ${e.message}")
        }
    }

    fun exportarAmbosAExcel() {
        try {
            val workbook = XSSFWorkbook()

            // Hoja de Stock
            val hojaStock = workbook.createSheet("Stock")
            val estiloEncabezado = workbook.createCellStyle()
            val fuente = workbook.createFont()
            fuente.bold = true
            fuente.color = IndexedColors.WHITE.index
            estiloEncabezado.setFont(fuente)
            estiloEncabezado.fillForegroundColor = IndexedColors.BLUE.index
            estiloEncabezado.fillPattern = FillPatternType.SOLID_FOREGROUND

            val filaEncabezadoStock = hojaStock.createRow(0)
            val columnasStock = arrayOf("Producto", "Stock", "Tipo", "Precio Compra", "Precio Reventa")
            for (i in columnasStock.indices) {
                val celda = filaEncabezadoStock.createCell(i)
                celda.setCellValue(columnasStock[i])
                celda.cellStyle = estiloEncabezado
            }

            val productos = Inventario.obtenerProductos()
            for (i in productos.indices) {
                val fila = hojaStock.createRow(i + 1)
                fila.createCell(0).setCellValue(productos[i].nombre)
                fila.createCell(1).setCellValue(productos[i].stock.toDouble())
                fila.createCell(2).setCellValue(productos[i].tipo)
                fila.createCell(3).setCellValue(productos[i].precioCompra)
                fila.createCell(4).setCellValue(productos[i].precioReventa)
            }

            for (i in columnasStock.indices) {
                hojaStock.autoSizeColumn(i)
            }

            // Hoja de Historial
            val hojaHistorial = workbook.createSheet("Historial")
            val filaEncabezadoHistorial = hojaHistorial.createRow(0)
            filaEncabezadoHistorial.createCell(0).setCellValue("Movimiento")
            filaEncabezadoHistorial.getCell(0).cellStyle = estiloEncabezado

            val historial = Inventario.obtenerHistorialGeneral()
            for (i in historial.indices) {
                val fila = hojaHistorial.createRow(i + 1)
                fila.createCell(0).setCellValue(historial[i])
            }

            hojaHistorial.autoSizeColumn(0)

            val archivo = File("inventario_completo.xlsx")
            FileOutputStream(archivo).use { fos ->
                workbook.write(fos)
            }

            workbook.close()

            JOptionPane.showMessageDialog(null, "Archivo Excel exportado correctamente en:\n${archivo.absolutePath}")

        } catch (e: Exception) {
            e.printStackTrace()
            JOptionPane.showMessageDialog(null, "Error al exportar el archivo: ${e.message}")
        }
    }
}






