package com.mariajosegamez.psp_to03

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mariajosegamez.psp_to03.ejercicio3.Ejercicio3Activity
import com.mariajosegamez.psp_to03.ejercicio2.Ejercicio2Activity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Referencias a los elementos del layout
        val btnEj2: Button = findViewById(R.id.btnEj2)
        val btnEj3: Button = findViewById(R.id.btnEj3)


        // Eventos de los botones

        btnEj2.setOnClickListener {
            val intent = Intent(this, Ejercicio2Activity::class.java)
            startActivity(intent)
        }

        btnEj3.setOnClickListener {
            val intent = Intent(this, Ejercicio3Activity::class.java)
            startActivity(intent)
        }


    }
}