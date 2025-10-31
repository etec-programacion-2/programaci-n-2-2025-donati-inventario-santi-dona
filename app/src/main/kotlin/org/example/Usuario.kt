package org.example

import java.io.Serializable

data class Usuario(
    val nombre: String,
    val contrasena: String,
    val esAdmin: Boolean = false
) : Serializable

