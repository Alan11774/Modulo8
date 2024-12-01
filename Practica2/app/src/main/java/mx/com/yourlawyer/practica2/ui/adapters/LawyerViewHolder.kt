package mx.com.yourlawyer.practica2.ui.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mx.com.yourlawyer.practica2.R
import mx.com.yourlawyer.practica2.data.remote.model.LawyerDto
import mx.com.yourlawyer.practica2.databinding.LawyerElementBinding

class LawyerViewHolder (
    private val binding: LawyerElementBinding
): RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(lawyer: LawyerDto){
        val context = binding.root.context

        binding.tvTitle.text = lawyer.category

        // Con Glide
        Glide.with(context)
            .load(lawyer.image)
            .into(binding.ivThumbnail)

        binding.tvDeveloper.text = context.getString(R.string.subcategoria_menus, lawyer.subcategory?.joinToString(", "))
        binding.tvReleased.text = context.getString(R.string.abogados_activos_menus, lawyer.activeLawyers)
        binding.tvRating.text = context.getString(R.string.id_menus, lawyer.id)

    }
}