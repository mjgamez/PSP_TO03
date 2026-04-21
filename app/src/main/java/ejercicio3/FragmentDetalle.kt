package com.mariajosegamez.psp_to03.ejercicio3

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mariajosegamez.psp_to03.R
import com.mariajosegamez.psp_to03.databinding.FragmentDetalleBinding
import com.mariajosegamez.psp_to03.ejercicio3.model.EstacionBizi

class FragmentDetalle : Fragment(R.layout.fragment_detalle) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetalleBinding.bind(view)


        val estacion = arguments?.getSerializable("ESTACION") as? EstacionBizi

        estacion?.let {
            binding.tvDetalleTitulo.text = it.title
            binding.tvDetalleBicis.text = "Bicicletas disponibles: ${it.bicisDisponibles}"
            binding.tvDetalleAnclajes.text = "Anclajes disponibles: ${it.anclajesDisponibles}"
            binding.tvDetalleEstado.text = "Estado actual: ${it.estado}"
            binding.tvDetalleActualizacion.text = "Última actualización: ${it.lastUpdated}"
        }
    }
}