package com.example.evaluacion1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog

class RecuperarContra : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contra)

        val etCorreo = findViewById<EditText>(R.id.etCorreoRecuperar)
        val btnVerificar = findViewById<Button>(R.id.btnVerificarCorreo)
        val tvVolver = findViewById<TextView>(R.id.tvVolverLoginRecuperar)

        btnVerificar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()

            // 1. Validación de campo obligatorio
            if (correo.isEmpty()) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.WARNING_TYPE,
                    "Campo Obligatorio",
                    "Por favor ingresa tu correo electrónico."
                )
                return@setOnClickListener
            }

            // 2. Validación de formato de email
            if (!Utils.esEmailValido(correo)) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.WARNING_TYPE,
                    "Formato Inválido",
                    "Por favor ingresa un correo con formato válido."
                )
                return@setOnClickListener
            }

            // 3. Validar que el correo exista en el repositorio
            val usuarioEncontrado = UsuarioRepository.buscarUsuarioPorEmail(correo)

            if (usuarioEncontrado != null) {
                SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Usuario Encontrado")
                    .setContentText("Se validó el correo correctamente. Procede a crear tu nueva contraseña.")
                    .setConfirmText("Continuar")
                    .setConfirmClickListener { dialog ->
                        dialog.dismissWithAnimation()
                        val intent = Intent(this, Crear_Contra::class.java).apply {
                            putExtra("CORREO_USUARIO", usuarioEncontrado.email)
                        }
                        startActivity(intent)
                        finish()
                    }
                    .show()
            } else {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.ERROR_TYPE,
                    "No Encontrado",
                    "El correo ingresado no se encuentra registrado en el sistema."
                )
            }
        }

        tvVolver.setOnClickListener {
            finish()
        }
    }
}