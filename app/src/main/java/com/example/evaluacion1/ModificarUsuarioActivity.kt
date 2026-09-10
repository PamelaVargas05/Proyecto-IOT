package com.example.evaluacion1

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.button.MaterialButton

class ModificarUsuarioActivity : AppCompatActivity() {

    private var usuarioActual: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar_usuario)

        val btnVolver = findViewById<ImageButton>(R.id.btnVolverModificar)
        val tvIdBadge = findViewById<TextView>(R.id.tvIdUsuarioBadge)
        val etNombres = findViewById<EditText>(R.id.etModificarNombres)
        val etApellidos = findViewById<EditText>(R.id.etModificarApellidos)
        val etEmail = findViewById<EditText>(R.id.etModificarEmail)
        val btnModificar = findViewById<MaterialButton>(R.id.btnModificarUsuario)
        val btnEliminar = findViewById<MaterialButton>(R.id.btnEliminarUsuario)

        btnVolver.setOnClickListener {
            finish()
        }

        // Obtener usuario por ID o Email pasado en el Intent
        val usuarioId = intent.getIntExtra("USUARIO_ID", -1)
        val usuarioEmail = intent.getStringExtra("USUARIO_EMAIL")

        usuarioActual = when {
            usuarioId != -1 -> UsuarioRepository.buscarUsuarioPorId(usuarioId)
            !usuarioEmail.isNullOrBlank() -> UsuarioRepository.buscarUsuarioPorEmail(usuarioEmail)
            else -> null
        }

        if (usuarioActual == null) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText("No se pudo cargar la información del usuario.")
                .setConfirmText("Aceptar")
                .setConfirmClickListener { dialog ->
                    dialog.dismissWithAnimation()
                    finish()
                }
                .show()
            return
        }

        // Cargar datos en los campos
        usuarioActual?.let { u ->
            tvIdBadge.text = "ID de Usuario: #${u.id}"
            etNombres.setText(u.nombres)
            etApellidos.setText(u.apellidos)
            etEmail.setText(u.email)
        }

        // Botón Modificar: Actualiza los datos en UsuarioRepository
        btnModificar.setOnClickListener {
            val nombres = etNombres.text.toString().trim()
            val apellidos = etApellidos.text.toString().trim()
            val email = etEmail.text.toString().trim()

            // 1. Validar campos no vacíos
            if (nombres.isEmpty() || apellidos.isEmpty() || email.isEmpty()) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.WARNING_TYPE,
                    "Campos Incompletos",
                    "Por favor completa todos los campos para actualizar el usuario."
                )
                return@setOnClickListener
            }

            // 2. Validar solo letras en nombres y apellidos
            if (!Utils.esSoloLetras(nombres)) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.WARNING_TYPE,
                    "Nombres Inválidos",
                    "El campo nombres solo puede contener letras y espacios."
                )
                return@setOnClickListener
            }

            if (!Utils.esSoloLetras(apellidos)) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.WARNING_TYPE,
                    "Apellidos Inválidos",
                    "El campo apellidos solo puede contener letras y espacios."
                )
                return@setOnClickListener
            }

            // 3. Validar formato de email
            if (!Utils.esEmailValido(email)) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.WARNING_TYPE,
                    "Correo Inválido",
                    "Ingresa un correo electrónico con formato válido."
                )
                return@setOnClickListener
            }

            // 4. Validar unicidad de email (excepto si es el mismo usuario)
            val existente = UsuarioRepository.buscarUsuarioPorEmail(email)
            if (existente != null && existente.id != usuarioActual?.id) {
                Utils.mostrarAlerta(
                    this,
                    SweetAlertDialog.ERROR_TYPE,
                    "Correo en Uso",
                    "El correo electrónico ya pertenece a otro usuario registrado."
                )
                return@setOnClickListener
            }

            // Actualizar datos
            usuarioActual?.let { u ->
                u.nombres = nombres
                u.apellidos = apellidos
                u.email = email
                UsuarioRepository.actualizarUsuario(u)

                SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("¡Usuario Modificado!")
                    .setContentText("Los datos del usuario han sido actualizados exitosamente.")
                    .setConfirmText("Entendido")
                    .setConfirmClickListener { dialog ->
                        dialog.dismissWithAnimation()
                        finish() // Regresa al listado
                    }
                    .show()
            }
        }

        // Botón Eliminar: Diálogo WARNING de confirmación
        btnEliminar.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("¿Eliminar Usuario?")
                .setContentText("¿Está seguro de eliminar este usuario? Esta acción no se puede deshacer.")
                .setCancelText("Cancelar")
                .setConfirmText("Sí, Eliminar")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    usuarioActual?.let { u ->
                        UsuarioRepository.eliminarUsuario(u.id)
                        sDialog
                            .setTitleText("¡Eliminado!")
                            .setContentText("El registro de usuario ha sido eliminado del sistema.")
                            .setConfirmText("Aceptar")
                            .showCancelButton(false)
                            .setConfirmClickListener { finishDialog ->
                                finishDialog.dismissWithAnimation()
                                finish() // Regresa al listado
                            }
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                    }
                }
                .show()
        }
    }
}
