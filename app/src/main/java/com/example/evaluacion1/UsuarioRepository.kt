package com.example.evaluacion1

import java.text.Normalizer

data class Usuario(
    var id: Int,
    var nombres: String,
    var apellidos: String,
    var email: String,
    var clave: String
)

object UsuarioRepository {
    private var currentId = 1
    val listaUsuarios = mutableListOf<Usuario>()

    init {
        // Población inicial de usuarios por defecto
        agregarUsuario(
            Usuario(
                id = currentId++,
                nombres = "Administrador",
                apellidos = "Sistema IoT",
                email = "admin@iot.cl",
                clave = "Admin123!"
            )
        )
        agregarUsuario(
            Usuario(
                id = currentId++,
                nombres = "Pamela",
                apellidos = "Valenzuela",
                email = "pame@iot.cl",
                clave = "Pame2026!"
            )
        )
        agregarUsuario(
            Usuario(
                id = currentId++,
                nombres = "Carlos",
                apellidos = "González",
                email = "carlos.g@iot.cl",
                clave = "Carlos2026!"
            )
        )
    }

    fun agregarUsuario(usuario: Usuario): Boolean {
        if (buscarUsuarioPorEmail(usuario.email) != null) {
            return false
        }
        if (usuario.id == 0) {
            usuario.id = currentId++
        } else if (usuario.id >= currentId) {
            currentId = usuario.id + 1
        }
        listaUsuarios.add(usuario)
        return true
    }

    fun obtenerUsuarios(): List<Usuario> {
        return listaUsuarios.toList()
    }

    fun buscarUsuarioPorEmail(email: String): Usuario? {
        return listaUsuarios.find { it.email.trim().equals(email.trim(), ignoreCase = true) }
    }

    fun buscarUsuarioPorId(id: Int): Usuario? {
        return listaUsuarios.find { it.id == id }
    }

    fun actualizarUsuario(usuarioActualizado: Usuario): Boolean {
        val index = listaUsuarios.indexOfFirst { it.id == usuarioActualizado.id }
        if (index != -1) {
            listaUsuarios[index] = usuarioActualizado
            return true
        }
        return false
    }

    fun eliminarUsuario(id: Int): Boolean {
        return listaUsuarios.removeIf { it.id == id }
    }

    fun filtrarUsuarios(query: String): List<Usuario> {
        if (query.isBlank()) return obtenerUsuarios()
        val queryNormalizado = removerAcentos(query.trim().lowercase())
        return listaUsuarios.filter { usuario ->
            val nombreCompleto = "${usuario.nombres} ${usuario.apellidos}"
            val normalizado = removerAcentos(nombreCompleto.lowercase())
            normalizado.contains(queryNormalizado) ||
                    removerAcentos(usuario.email.lowercase()).contains(queryNormalizado)
        }
    }

    private fun removerAcentos(texto: String): String {
        val normalized = Normalizer.normalize(texto, Normalizer.Form.NFD)
        return normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    }
}