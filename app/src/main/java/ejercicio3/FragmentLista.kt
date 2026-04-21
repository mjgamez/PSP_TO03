package com.mariajosegamez.psp_to03.ejercicio3

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mariajosegamez.psp_to03.R
import com.mariajosegamez.psp_to03.databinding.FragmentListaBinding
// ESTAS SON LAS IMPORTANTES CORREGIDAS:

// Borra el que dice "import ejercicio3.data.RetrofitClient"
// Y pon este:
import com.mariajosegamez.psp_to03.ejercicio3.data.RetrofitClient
import com.mariajosegamez.psp_to03.ejercicio3.model.EstacionBizi
import ejercicio3.adapter.BiziAdapter
import kotlinx.coroutines.launch


class FragmentLista : Fragment(R.layout.fragment_lista) {

    private lateinit var adapter: BiziAdapter
    private var estacionList = mutableListOf<EstacionBizi>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentListaBinding.bind(view)

        val manager = LinearLayoutManager(context)
        adapter = BiziAdapter(estacionList) { estacion -> verDetalle(estacion) }

        binding.recyclerEstaciones.layoutManager = manager
        binding.recyclerEstaciones.adapter = adapter
        binding.recyclerEstaciones.addItemDecoration(DividerItemDecoration(context, manager.orientation))

        obtenerDatosBizi()
    }

    // viewLifecycleOwner.lifecycleScope en lugar de solo lifecycleScope porque el código está dentro de un Fragment, no de una Activity
    private fun obtenerDatosBizi() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.service.listadoEstacionesBici()
                if (response.isSuccessful) {
                    response.body()?.estaciones?.let {
                        estacionList.clear()
                        estacionList.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verDetalle(estacion: EstacionBizi) {
        val fragmentDetalle = FragmentDetalle()
        val bundle = Bundle()
        bundle.putSerializable("ESTACION", estacion)
        fragmentDetalle.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.list_fragment_container, fragmentDetalle)
            .addToBackStack(null)
            .commit()
    }
}