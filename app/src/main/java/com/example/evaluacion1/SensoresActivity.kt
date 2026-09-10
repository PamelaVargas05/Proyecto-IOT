package com.example.evaluacion1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import kotlin.random.Random

class SensoresActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensores)

        val btnVolver = findViewById<ImageButton>(R.id.btnVolverSensores)
        val tvHumedad = findViewById<TextView>(R.id.tvValorHumedad)
        val tvTemperatura = findViewById<TextView>(R.id.tvValorTemperatura)
        val switchLuces = findViewById<MaterialSwitch>(R.id.switchLuces)
        val tvEstadoLuces = findViewById<TextView>(R.id.tvEstadoLuces)
        val cardIconLuces = findViewById<MaterialCardView>(R.id.cardIconLuces)
        val ivLuces = findViewById<ImageView>(R.id.ivLuces)

        val switchLinterna = findViewById<MaterialSwitch>(R.id.switchLinterna)
        val tvEstadoLinterna = findViewById<TextView>(R.id.tvEstadoLinterna)
        val cardIconLinterna = findViewById<MaterialCardView>(R.id.cardIconLinterna)
        val ivLinterna = findViewById<ImageView>(R.id.ivLinterna)

        val btnActualizar = findViewById<MaterialButton>(R.id.btnActualizarSensores)

        btnVolver.setOnClickListener {
            finish()
        }

        // Control de Iluminación
        switchLuces.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvEstadoLuces.text = "Encendida (80% Intensidad)"
                cardIconLuces.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primary_container))
                ivLuces.setColorFilter(ContextCompat.getColor(this, R.color.primary))
                SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Iluminación Activada")
                    .setContentText("Las luces principales han sido encendidas en el recinto IoT.")
                    .setConfirmText("OK")
                    .show()
            } else {
                tvEstadoLuces.text = "Apagada"
                cardIconLuces.setCardBackgroundColor(ContextCompat.getColor(this, R.color.surface_variant))
                ivLuces.setColorFilter(ContextCompat.getColor(this, R.color.text_muted))
            }
        }

        // Control de Linterna
        switchLinterna.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvEstadoLinterna.text = "Encendida (Modo Emergencia)"
                cardIconLinterna.setCardBackgroundColor(ContextCompat.getColor(this, R.color.secondary_container))
                ivLinterna.setColorFilter(ContextCompat.getColor(this, R.color.secondary))
                SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Linterna Activada")
                    .setContentText("El foco auxiliar IoT se encuentra transmitiendo luz.")
                    .setConfirmText("OK")
                    .show()
            } else {
                tvEstadoLinterna.text = "Apagada"
                cardIconLinterna.setCardBackgroundColor(ContextCompat.getColor(this, R.color.surface_variant))
                ivLinterna.setColorFilter(ContextCompat.getColor(this, R.color.text_muted))
            }
        }

        // Simulación de lectura de sensores con barra de progreso SweetAlert
        btnActualizar.setOnClickListener {
            val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog.titleText = "Leyendo telemetría IoT..."
            pDialog.setCancelable(false)
            pDialog.show()

            Handler(Looper.getMainLooper()).postDelayed({
                pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                pDialog.titleText = "Datos Actualizados"
                pDialog.contentText = "Sensores calibrados en tiempo real."
                pDialog.confirmText = "Aceptar"

                // Valores realistas alrededor de la maqueta
                val nuevaHum = Random.nextInt(40, 50)
                val nuevaTemp = Random.nextInt(17, 23)

                tvHumedad.text = "$nuevaHum%"
                tvTemperatura.text = "$nuevaTemp°"
            }, 1200)
        }
    }
}
