package com.example.evaluacion1

import android.content.Context
import android.util.Patterns
import cn.pedant.SweetAlert.SweetAlertDialog

object Utils {

    // Regex: Mínimo 8 caracteres, al menos 1 mayúscula, 1 minúscula, 1 número y 1 carácter especial
    private val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!\\-_.*,;:]).{8,}$".toRegex()
    private val ONLY_LETTERS_REGEX = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$".toRegex()

    fun esEmailValido(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }

    fun esClaveFuerte(clave: String): Boolean {
        return clave.matches(PASSWORD_REGEX)
    }

    fun esSoloLetras(texto: String): Boolean {
        return texto.isNotBlank() && texto.matches(ONLY_LETTERS_REGEX)
    }

    fun mostrarAlerta(context: Context, tipo: Int, titulo: String, mensaje: String, onConfirm: (() -> Unit)? = null) {
        val dialog = SweetAlertDialog(context, tipo)
            .setTitleText(titulo)
            .setContentText(mensaje)
            .setConfirmText("Aceptar")

        if (onConfirm != null) {
            dialog.setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
                onConfirm()
            }
        }
        dialog.show()
    }
}
