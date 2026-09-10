package com.example.evaluacion1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListarUsuariosActivity : AppCompatActivity() {

    private lateinit var rvUsuarios: RecyclerView
    private lateinit var adapter: UsuarioAdapter
    private lateinit var etBuscador: EditText
    private lateinit var tvContador: TextView
    private lateinit var tvListaVacia: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_usuarios)

        val btnVolver = findViewById<ImageButton>(R.id.btnVolverListar)
        etBuscador = findViewById(R.id.etBuscadorUsuarios)
        rvUsuarios = findViewById(R.id.rvUsuarios)
        tvContador = findViewById(R.id.tvContadorUsuarios)
        tvListaVacia = findViewById(R.id.tvListaVacia)

        btnVolver.setOnClickListener {
            finish()
        }

        // Configuración de RecyclerView
        rvUsuarios.layoutManager = LinearLayoutManager(this)
        adapter = UsuarioAdapter(UsuarioRepository.obtenerUsuarios()) { usuarioSeleccionado ->
            // Al hacer clic, abre ModificarUsuarioActivity pasando el ID
            val intent = Intent(this, ModificarUsuarioActivity::class.java).apply {
                putExtra("USUARIO_ID", usuarioSeleccionado.id)
            }
            startActivity(intent)
        }
        rvUsuarios.adapter = adapter

        // Filtrado dinámico en tiempo real insensible a mayúsculas, minúsculas y acentos
        etBuscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrar(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        actualizarVista()
    }

    override fun onResume() {
        super.onResume()
        // Actualizar datos al volver desde ModificarUsuarioActivity
        filtrar(etBuscador.text.toString())
    }

    private fun filtrar(query: String) {
        val listaFiltrada = UsuarioRepository.filtrarUsuarios(query)
        adapter.actualizarLista(listaFiltrada)

        tvContador.text = "${listaFiltrada.size} usuario(s) encontrado(s)"

        if (listaFiltrada.isEmpty()) {
            tvListaVacia.visibility = View.VISIBLE
            rvUsuarios.visibility = View.GONE
        } else {
            tvListaVacia.visibility = View.GONE
            rvUsuarios.visibility = View.VISIBLE
        }
    }

    private fun actualizarVista() {
        filtrar(etBuscador.text.toString())
    }
}
