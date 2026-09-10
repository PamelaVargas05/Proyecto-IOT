package com.example.evaluacion1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog

class Registro_usuario : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)

        val etNombres = findViewById<EditText>(R.id.etNombresRegistro)
        val etApellidos = findViewById<EditText>(R.id.etApellidosRegistro)
        val etCorreo = findViewById<EditText>(R.id.etCorreoRegistro)
        val etPass = findViewById<EditText>(R.id.etContrasenaRegistro)
        val etConfirmPass = findViewById<EditText>(R.id.etConfirmarContrasenaRegistro)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val tvVolverLogin = findViewById<TextView>(R.id.tvVolverLogin)

        btnRegistrar.setOnClickListener {
            val nombres = etNombres.text.toString().trim()
            val apellidos = etApellidos.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val pass = etPass.text.toString().trim()
            val confirmPass = etConfirmPass.text.toString().trim()

            // 1. Validar ningún campo vacío
            if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.WARNING_TYPE,
                    "Campos Incompletos",
                    "Por favor completa todos los campos del formulario de registro."
                )
                return@setOnClickListener
            }

            // 2. Validar que Nombres y Apellidos contengan solo letras y espacios
            if (!Utils.esSoloLetras(nombres)) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.WARNING_TYPE,
                    "Nombres Inválidos",
                    "El campo nombres solo debe contener letras y espacios."
                )
                return@setOnClickListener
            }

            if (!Utils.esSoloLetras(apellidos)) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.WARNING_TYPE,
                    "Apellidos Inválidos",
                    "El campo apellidos solo debe contener letras y espacios."
                )
                return@setOnClickListener
            }

            // 3. Validar formato de correo
            if (!Utils.esEmailValido(correo)) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.WARNING_TYPE,
                    "Correo Inválido",
                    "Ingresa una dirección de correo electrónico con formato válido."
                )
                return@setOnClickListener
            }

            // 4. Validar unicidad del correo en UsuarioRepository
            if (UsuarioRepository.buscarUsuarioPorEmail(correo) != null) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.ERROR_TYPE,
                    "Usuario Ya Existente",
                    "El correo electrónico ingresado ya se encuentra registrado."
                )
                return@setOnClickListener
            }

            // 5. Validar robustez de contraseña mediante Regex
            if (!Utils.esClaveFuerte(pass)) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.ERROR_TYPE,
                    "Contraseña Débil",
                    "La contraseña debe tener al menos 8 caracteres, al menos 1 letra mayúscula, 1 minúscula, 1 número y 1 carácter especial (ej. @#$%^&+=!)."
                )
                return@setOnClickListener
            }

            // 6. Validar que ambas contraseñas coincidan
            if (pass != confirmPass) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.ERROR_TYPE,
                    "Error de Coincidencia",
                    "Las contraseñas no coinciden. Por favor verifícalas."
                )
                return@setOnClickListener
            }

            // 7. Guardar nuevo usuario en UsuarioRepository
            val nuevoUsuario = Usuario(
                id = 0,
                nombres = nombres,
                apellidos = apellidos,
                email = correo,
                clave = pass
            )
            UsuarioRepository.agregarUsuario(nuevoUsuario)

            // Confirmación de éxito con "Entendido"
            SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("¡Registro Exitoso!")
                .setContentText("El usuario ha sido registrado satisfactoriamente en el sistema.")
                .setConfirmText("Entendido")
                .setConfirmClickListener { dialog ->
                    dialog.dismissWithAnimation()
                    finish() // Regresa a la actividad anterior
                }
                .show()
        }

        tvVolverLogin.setOnClickListener {
            finish()
        }
    }
}