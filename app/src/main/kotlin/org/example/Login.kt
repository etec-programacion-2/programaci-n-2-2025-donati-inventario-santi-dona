package org.example

import java.awt.*
import javax.swing.*

class Login : JFrame("Inicio de Sesión") {
    private val campoUsuario = JTextField(15)
    private val campoContraseña = JPasswordField(15)
    private val botonLogin = JButton("Iniciar Sesión")

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(400, 200)
        setLocationRelativeTo(null)
        layout = GridLayout(4, 1, 5, 5)
        background = Color(245, 245, 245)

        add(JLabel("Usuario:"))
        add(campoUsuario)
        add(JLabel("Contraseña:"))
        add(campoContraseña)
        add(botonLogin)

        // Estilo azul al botón
        botonLogin.background = Color(0, 90, 180)
        botonLogin.foreground = Color.WHITE
        botonLogin.isOpaque = true
        botonLogin.setBorderPainted(false)
        botonLogin.setFocusPainted(false)

        // Acción: Iniciar sesión
        val actionLogin = {
            val nombre = campoUsuario.text.trim()
            val contraseña = String(campoContraseña.password)

            val usuario = RegistroUsuarios.iniciarSesion(nombre, contraseña)
            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "Bienvenido ${usuario.nombre}")
                dispose()
                VentanaPrincipal(usuario).isVisible = true
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.")
            }
        }

        botonLogin.addActionListener { actionLogin() }

        // Atajos con Enter:
        // - En usuario: pasa foco a contraseña
        campoUsuario.addActionListener { campoContraseña.requestFocus() }
        // - En contraseña: inicia sesión
        campoContraseña.addActionListener { actionLogin() }
    }
}









