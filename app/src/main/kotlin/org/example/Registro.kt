package org.example

import java.awt.*
import javax.swing.*

class Registro : JFrame("Registro de Usuario") {
    private val campoUsuario = JTextField(15)
    private val campoContraseña = JPasswordField(15)
    private val checkAdmin = JCheckBox("Registrar como administrador")
    private val botonRegistrar = JButton("Registrar")

    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        setSize(400, 200)
        setLocationRelativeTo(null)
        layout = GridLayout(4, 1, 5, 5)
        background = Color(245, 245, 245)

        add(JLabel("Nombre de usuario:"))
        add(campoUsuario)
        add(JLabel("Contraseña:"))
        add(campoContraseña)
        add(checkAdmin)
        add(botonRegistrar)

        // Estilo al botón
        botonRegistrar.background = Color(0, 90, 180)
        botonRegistrar.foreground = Color.WHITE
        botonRegistrar.isOpaque = true
        botonRegistrar.setBorderPainted(false)
        botonRegistrar.setFocusPainted(false)

        botonRegistrar.addActionListener {
            val nombre = campoUsuario.text.trim()
            val contraseña = String(campoContraseña.password)
            val esAdmin = checkAdmin.isSelected

            if (nombre.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.")
            } else {
                val registrado = RegistroUsuarios.registrarUsuario(nombre, contraseña, esAdmin)
                if (registrado) {
                    JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.")
                    dispose()
                } else {
                    JOptionPane.showMessageDialog(this, "El usuario ya existe.")
                }
            }
        }
    }
}