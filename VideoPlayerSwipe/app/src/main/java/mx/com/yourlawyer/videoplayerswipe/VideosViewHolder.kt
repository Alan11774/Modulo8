package mx.com.yourlawyer.videoplayerswipe

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import mx.com.yourlawyer.videoplayerswipe.databinding.ItemVideoBinding
import mx.com.yourlawyer.videoplayerswipe.model.Video

class VideosViewHolder(
    private val binding: ItemVideoBinding
) : RecyclerView.ViewHolder(binding.root) {


    val icFav = binding.icFav
    fun bind(video: Video){
        binding.apply {
            tvVideoTitle.text = video.title
            tvVideoDescription.text = video.description
            vvVideo.setVideoPath(video.url)

            vvVideo.setOnPreparedListener { mp ->
                pbVideo.visibility = View.GONE
                mp.start()
                val mediaRatio = mp.videoWidth / mp.videoHeight.toFloat()
                val screenRatio = vvVideo.width / vvVideo.height.toFloat()
                val scale = mediaRatio / screenRatio

                if(scale >= 1f) {
                    vvVideo.scaleX = scale
                } else {
                    vvVideo.scaleY = 1f / scale
                }


            }

            vvVideo.setOnCompletionListener { mp ->
                mp.start()
            }


        }
    }
}