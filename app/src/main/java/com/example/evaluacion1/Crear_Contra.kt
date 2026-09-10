package com.example.evaluacion1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog

class Crear_Contra : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_contra)

        val etNuevaPass = findViewById<EditText>(R.id.etNuevaContrasena)
        val etConfirmarPass = findViewById<EditText>(R.id.etConfirmarNuevaContrasena)
        val btnCambiar = findViewById<Button>(R.id.btnCambiarContrasena)

        // Obtener el correo recibido desde la pantalla anterior
        val correoUsuario = intent.getStringExtra("CORREO_USUARIO")

        btnCambiar.setOnClickListener {
            val nuevaPass = etNuevaPass.text.toString().trim()
            val confirmPass = etConfirmarPass.text.toString().trim()

            // 1. Validaciones: campos obligatorios
            if (nuevaPass.isEmpty() || confirmPass.isEmpty()) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.WARNING_TYPE,
                    "Campos Obligatorios",
                    "Por favor llena ambos campos de contraseña."
                )
                return@setOnClickListener
            }

            // 2. Validación de coincidencia
            if (nuevaPass != confirmPass) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.ERROR_TYPE,
                    "Error de Coincidencia",
                    "Las contraseñas ingresadas no coinciden. Inténtalo nuevamente."
                )
                return@setOnClickListener
            }

            // 3. Validación de robustez de contraseña mediante Regex
            if (!Utils.esClaveFuerte(nuevaPass)) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.ERROR_TYPE,
                    "Contraseña Débil",
                    "La contraseña debe tener al menos 8 caracteres, al menos 1 letra mayúscula, 1 minúscula, 1 número y 1 carácter especial (ej. @#$%^&+=!)."
                )
                return@setOnClickListener
            }

            // 4. Actualizar la contraseña en UsuarioRepository
            if (correoUsuario != null) {
                val usuario = UsuarioRepository.buscarUsuarioPorEmail(correoUsuario)
                if (usuario != null) {
                    usuario.clave = nuevaPass
                    UsuarioRepository.actualizarUsuario(usuario)

                    SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("¡Contraseña Actualizada!")
                        .setContentText("Tu clave de acceso ha sido restablecida con éxito. Inicia sesión.")
                        .setConfirmText("Ir al Login")
                        .setConfirmClickListener { dialog ->
                            dialog.dismissWithAnimation()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        .show()
                } else {
                    Utils.mostrarAlerta(
                        this,
                        SweetAlertDialog.ERROR_TYPE,
                        "Error",
                        "No se encontró el usuario en la sesión."
                    )
                }
            } else {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.ERROR_TYPE,
                    "Error",
                    "Identificador de usuario inválido."
                )
            }
        }
    }
}