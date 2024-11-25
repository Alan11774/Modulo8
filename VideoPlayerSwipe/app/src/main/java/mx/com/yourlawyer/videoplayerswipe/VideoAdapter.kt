package mx.com.yourlawyer.videoplayerswipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.com.yourlawyer.videoplayerswipe.databinding.ItemVideoBinding
import mx.com.yourlawyer.videoplayerswipe.model.Video

class VideoAdapter(
    private val videos: MutableList<Video>
):RecyclerView.Adapter<VideosViewHolder>() {

    var isFav = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        holder.bind(videos[position])
        holder.icFav.setOnClickListener {
            isFav = !isFav
            val color = if(isFav) R.color.RED else R.color.white
            holder.icFav.setColorFilter(color)

        }
    }

    override fun getItemCount(): Int = videos.size
}