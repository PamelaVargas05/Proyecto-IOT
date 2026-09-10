package com.example.evaluacion1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etContrasena = findViewById<EditText>(R.id.etContrasena)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegistrarme = findViewById<Button>(R.id.btnRegistrarme)
        val tvRecuperar = findViewById<TextView>(R.id.tvRecuperar)

        // Acción al presionar el botón de inicio de sesión
        btnLogin.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val pass = etContrasena.text.toString().trim()

            // 1. Validaciones: campos obligatorios
            if (correo.isEmpty() || pass.isEmpty()) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.WARNING_TYPE,
                    "Campos obligatorios",
                    "Por favor ingresa tanto tu correo electrónico como tu contraseña."
                )
                return@setOnClickListener
            }

            // 2. Validación de formato de email
            if (!Utils.esEmailValido(correo)) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.WARNING_TYPE,
                    "Formato de Correo Inválido",
                    "Por favor ingresa un correo con formato válido (ejemplo: usuario@dominio.cl)."
                )
                return@setOnClickListener
            }

            // 3. Comprobación de credenciales contra UsuarioRepository
            val usuario = UsuarioRepository.buscarUsuarioPorEmail(correo)

            if (usuario != null && usuario.clave == pass) {
                SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("¡Bienvenido!")
                    .setContentText("Acceso concedido al sistema: ${usuario.nombres} ${usuario.apellidos}")
                    .setConfirmText("Entrar")
                    .setConfirmClickListener { dialog ->
                        dialog.dismissWithAnimation()
                        val intent = Intent(this, MainMenuActivity::class.java).apply {
                            putExtra("USUARIO_EMAIL", usuario.email)
                            putExtra("USUARIO_NOMBRE", "${usuario.nombres} ${usuario.apellidos}")
                        }
                        startActivity(intent)
                        finish()
                    }
                    .show()
            } else {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.ERROR_TYPE,
                    "Credenciales Incorrectas",
                    "El correo o la contraseña ingresados no coinciden con nuestros registros."
                )
            }
        }

        // Navegación hacia Registro de Usuario
        btnRegistrarme.setOnClickListener {
            startActivity(Intent(this, Registro_usuario::class.java))
        }

        // Navegación hacia Recuperar Contraseña
        tvRecuperar.setOnClickListener {
            startActivity(Intent(this, RecuperarContra::class.java))
        }
    }
}