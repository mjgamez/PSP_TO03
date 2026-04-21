package ejercicio3.adapter

import com.mariajosegamez.psp_to03.R
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mariajosegamez.psp_to03.databinding.ItemEstacionBinding
import com.mariajosegamez.psp_to03.ejercicio3.model.EstacionBizi

class BiziAdapter(
    private val estacionList: List<EstacionBizi>,
    private val onClickListener: (EstacionBizi) -> Unit
) : RecyclerView.Adapter<BiziAdapter.BiziViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiziViewHolder {
        val binding =
            ItemEstacionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BiziViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BiziViewHolder, position: Int) {
        val item = estacionList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int = estacionList.size

    class BiziViewHolder(private val binding: ItemEstacionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun render(estacion: EstacionBizi, onClickListener: (EstacionBizi) -> Unit) {
            binding.tvNombreEstacion.text = estacion.title
            binding.tvResumenBicis.text = "Bicis disponibles: ${estacion.bicisDisponibles}"

            // Colores según bicis disponibles (no es parte del enunciado del ejercicio)
            val colorRes = when {
                estacion.bicisDisponibles == 0 -> R.color.red
                estacion.bicisDisponibles in 1..5 -> R.color.orange
                else -> R.color.green
            }

            val colorReal = ContextCompat.getColor(binding.root.context, colorRes)

            binding.ivIconoEstacion.backgroundTintList = ColorStateList.valueOf(colorReal)

            itemView.setOnClickListener { onClickListener(estacion) }
        }
    }
}

