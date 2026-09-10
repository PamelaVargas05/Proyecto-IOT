package com.example.evaluacion1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.button.MaterialButton

class DesarrolladoresActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desarrolladores)

        val btnVolver = findViewById<ImageButton>(R.id.btnVolverDesarrollador)
        val btnGithubPamela = findViewById<MaterialButton>(R.id.btnGithubPamela)
        val btnLinkedinPamela = findViewById<MaterialButton>(R.id.btnLinkedinPamela)
        val btnGithubCarlos = findViewById<MaterialButton>(R.id.btnGithubCarlos)
        val btnLinkedinCarlos = findViewById<MaterialButton>(R.id.btnLinkedinCarlos)

        btnVolver.setOnClickListener {
            finish()
        }

        btnGithubPamela.setOnClickListener {
            abrirEnlace("https://github.com")
        }

        btnLinkedinPamela.setOnClickListener {
            abrirEnlace("https://linkedin.com")
        }

        btnGithubCarlos.setOnClickListener {
            abrirEnlace("https://github.com")
        }

        btnLinkedinCarlos.setOnClickListener {
            abrirEnlace("https://linkedin.com")
        }
    }

    private fun abrirEnlace(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Enlace Externo")
                .setContentText("Redirigiendo a: $url")
                .setConfirmText("Aceptar")
                .show()
        }
    }
}
