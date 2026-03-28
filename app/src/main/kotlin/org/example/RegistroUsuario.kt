package org.example

import java.io.*
import java.io.Serializable

object RegistroUsuarios : Serializable {
    private const val archivo = "usuarios.dat"
    private val usuarios = mutableListOf<Usuario>()

    init {
        cargarUsuarios()
    }

    fun registrarUsuario(nombre: String, contrasena: String, esAdmin: Boolean = false): Boolean {
        if (usuarios.any { it.nombre.equals(nombre, ignoreCase = true) }) {
            return false
        }
        val nuevoUsuario = Usuario(nombre, contrasena, esAdmin)
        usuarios.add(nuevoUsuario)
        guardarUsuarios()
        return true
    }

    fun iniciarSesion(nombre: String, contrasena: String): Usuario? {
        return usuarios.find { it.nombre == nombre && it.contrasena == contrasena }
    }

    fun eliminarUsuario(nombre: String): Boolean {
        val eliminado = usuarios.removeIf { it.nombre == nombre }
        if (eliminado) guardarUsuarios()
        return eliminado
    }

    fun listarUsuarios(): List<Usuario> = usuarios.toList()

    private fun guardarUsuarios() {
        try {
            ObjectOutputStream(FileOutputStream(archivo)).use { it.writeObject(usuarios) }
        } catch (e: Exception) {
            println("Error guardando usuarios: ${e.message}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun cargarUsuarios() {
        try {
            val f = File(archivo)
            if (f.exists()) {
                ObjectInputStream(FileInputStream(f)).use {
                    usuarios.clear()
                    usuarios.addAll(it.readObject() as List<Usuario>)
                }
            }
        } catch (e: Exception) {
            println("Error cargando usuarios: ${e.message}")
        }
    }
}