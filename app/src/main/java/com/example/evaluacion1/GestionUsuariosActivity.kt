package com.example.evaluacion1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class GestionUsuariosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_usuarios)

        val btnVolver = findViewById<ImageButton>(R.id.btnVolverMenuGestion)
        val cardIngresar = findViewById<MaterialCardView>(R.id.cardIngresarUsuarios)
        val cardListar = findViewById<MaterialCardView>(R.id.cardListarUsuarios)

        btnVolver.setOnClickListener {
            finish()
        }

        // 1. Botón INGRESAR USUARIOS -> Abre Registro_usuario
        cardIngresar.setOnClickListener {
            startActivity(Intent(this, Registro_usuario::class.java))
        }

        // 2. Botón LISTAR USUARIOS -> Abre ListarUsuariosActivity
        cardListar.setOnClickListener {
            startActivity(Intent(this, ListarUsuariosActivity::class.java))
        }
    }
}
