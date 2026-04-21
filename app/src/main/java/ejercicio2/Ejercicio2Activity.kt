package com.mariajosegamez.psp_to03.ejercicio2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mariajosegamez.psp_to03.databinding.ActivityEjercicio2Binding
//import com.mariajosegamez.psp_to03.databinding.ActivityEjercicio3Binding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Ejercicio2Activity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityEjercicio2Binding
    private var ratio: Double = 0.0


    private val urlJson = "https://api.frankfurter.app/latest?from=EUR&to=USD"
    private val urlImagen = "https://whiletruedo.com/imagenes/conversor.jpg"

    private var inicio: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEjercicio2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar la imagen con Glide
        Glide.with(this)
            .load(urlImagen)
            .into(binding.ivConversor)

        binding.btnConvertir.setOnClickListener(this)

        // Listeners para limpiar campos al ganar foco
        val focusListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (v?.id == binding.euros.id) {
                    binding.dolares.text.clear()
                } else if (v?.id == binding.dolares.id) {
                    binding.euros.text.clear()
                }
            }
        }

        binding.euros.onFocusChangeListener = focusListener
        binding.dolares.onFocusChangeListener = focusListener

        // Descargamos el JSON al iniciar
        descargarRatioCambio()
    }

    override fun onClick(v: View?) {
        if (v?.id == binding.btnConvertir.id) {
            convertir()
        }
    }

    private fun descargarRatioCambio() {
        inicio = System.currentTimeMillis()
        val client = OkHttpClient()
        val request = Request.Builder().url(urlJson).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Si falla, avisamos al usuario
                mostrarRespuesta("Fallo: " + e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        mostrarRespuesta("Error en servidor: ${response.code}")
                    } else {
                        val jsonData = response.body?.string()
                        try {
                            // PARSEO DEL JSON
                            val jsonObject = JSONObject(jsonData)
                            val rates = jsonObject.getJSONObject("rates")
                            ratio = rates.getDouble("USD")

                            mostrarRespuesta("Cambio actualizado: $ratio")
                        } catch (e: Exception) {
                            Log.e("Error JSON", e.message ?: "")
                            mostrarRespuesta("Error al procesar los datos JSON")
                        }
                    }
                }
            }
        })
    }

    private fun mostrarRespuesta(message: String) {
        runOnUiThread {
            binding.tvCambio.text = message
        }
    }

    private fun convertir() {
        if (ratio <= 0.0) {
            Toast.makeText(this, "No se pudo convertir porque no se ha obtenido un cambio correcto", Toast.LENGTH_LONG).show()
            return
        }

        val textoEuros = binding.euros.text.toString()
        val textoDolares = binding.dolares.text.toString()

        try {
            if (textoEuros.isNotEmpty()) {
                val valorEuros = textoEuros.toDouble()
                val resultadoDolares = valorEuros * ratio
                binding.dolares.setText(String.format("%.2f", resultadoDolares))
                Toast.makeText(this, "Convertido a Dólares", Toast.LENGTH_SHORT).show()
            }
            else if (textoDolares.isNotEmpty()) {
                val valorDolares = textoDolares.toDouble()
                val resultadoEuros = valorDolares / ratio
                binding.euros.setText(String.format("%.2f", resultadoEuros))
                Toast.makeText(this, "Convertido a Euros", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Por favor, introduce una cantidad en algún campo", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Formato de número no válido", Toast.LENGTH_SHORT).show()
        }
    }
}
