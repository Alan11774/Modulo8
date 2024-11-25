package mx.com.yourlawyer.practica2.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.com.yourlawyer.practica2.data.remote.model.LawyerDto
import mx.com.yourlawyer.practica2.databinding.LawyerElementBinding

class LawyersAdapter(
    private val lawyers: MutableList<LawyerDto>,
    private val onLawyerClicked: (LawyerDto) -> Unit
): RecyclerView.Adapter<LawyerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyerViewHolder {
        val binding = LawyerElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LawyerViewHolder(binding)
    }

    override fun getItemCount(): Int = lawyers.size

    override fun onBindViewHolder(holder: LawyerViewHolder, position: Int) {

        val game = lawyers[position]

        holder.bind(game)

        holder.itemView.setOnClickListener {
            //Para los clicks a los juegos
            onLawyerClicked(game)
        }

    }


}