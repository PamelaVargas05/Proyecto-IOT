package com.example.evaluacion1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainMenuActivity : AppCompatActivity() {

    private lateinit var tvFechaHora: TextView
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private val dateFormat = SimpleDateFormat("'Fecha/Hora: 'dd-MM-yyyy, HH:mm:ss", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        tvFechaHora = findViewById(R.id.tvFechaHora)
        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenidaUsuario)
        val cardCrud = findViewById<MaterialCardView>(R.id.cardCrudUsuario)
        val cardSensor = findViewById<MaterialCardView>(R.id.cardDatosSensor)
        val cardDesarrollador = findViewById<MaterialCardView>(R.id.cardDesarrollador)
        val btnCerrarSesion = findViewById<MaterialButton>(R.id.btnCerrarSesion)

        // Obtener datos del usuario logueado
        val usuarioNombre = intent.getStringExtra("USUARIO_NOMBRE")
        if (!usuarioNombre.isNullOrBlank()) {
            tvBienvenida.text = "¡Hola, $usuarioNombre!"
        }

        // Tarea repetitiva para la fecha y hora en tiempo real
        runnable = object : Runnable {
            override fun run() {
                val now = Date()
                tvFechaHora.text = dateFormat.format(now)
                handler.postDelayed(this, 1000)
            }
        }

        // Navegación Card 1: CRUD USUARIO
        cardCrud.setOnClickListener {
            startActivity(Intent(this, GestionUsuariosActivity::class.java))
        }

        // Navegación Card 2: DATOS SENSOR
        cardSensor.setOnClickListener {
            startActivity(Intent(this, SensoresActivity::class.java))
        }

        // Navegación Card 3: DESARROLLADOR
        cardDesarrollador.setOnClickListener {
            startActivity(Intent(this, DesarrolladoresActivity::class.java))
        }

        // Cerrar sesión con confirmación
        btnCerrarSesion.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("¿Cerrar Sesión?")
                .setContentText("Volverás a la pantalla de acceso.")
                .setCancelText("Cancelar")
                .setConfirmText("Sí, Salir")
                .showCancelButton(true)
                .setConfirmClickListener { dialog ->
                    dialog.dismissWithAnimation()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        handler.post(runnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}
