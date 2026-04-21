package com.mariajosegamez.psp_to03.ejercicio3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mariajosegamez.psp_to03.R
import com.mariajosegamez.psp_to03.databinding.ActivityEjercicio3Binding
import com.mariajosegamez.psp_to03.ejercicio3.FragmentLista

class Ejercicio3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityEjercicio3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEjercicio3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.list_fragment_container, FragmentLista())
                .commit()
        }
    }
}