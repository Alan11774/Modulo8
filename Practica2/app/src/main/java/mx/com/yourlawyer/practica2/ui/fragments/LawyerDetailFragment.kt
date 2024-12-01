package mx.com.yourlawyer.practica2.ui.fragments

import android.annotation.SuppressLint
import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import mx.com.yourlawyer.practica2.R
import mx.com.yourlawyer.practica2.application.LawyersRfApp
import mx.com.yourlawyer.practica2.data.LawyerRepository
import mx.com.yourlawyer.practica2.data.remote.model.LawyerDetailDto
import mx.com.yourlawyer.practica2.databinding.FragmentLawyerDetailBinding
import mx.com.yourlawyer.practica2.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val LAWYER_ID = "lawyer_id"

class LawyerDetailFragment : Fragment() {

    private var lawyerId: String? = null

    private var _binding: FragmentLawyerDetailBinding? = null
    private val binding get()  = _binding!!

    private lateinit var repository: LawyerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            lawyerId = args.getString(LAWYER_ID)
            Log.d(Constants.LOGTAG, getString(R.string.id_recibido, lawyerId))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLawyerDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as LawyersRfApp).repository

        lawyerId?.let{ id ->
            val call: Call<LawyerDetailDto> = repository.getLawyerDetailApiary(id)

            call.enqueue(object: Callback<LawyerDetailDto> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(p0: Call<LawyerDetailDto>, response: Response<LawyerDetailDto>) {

                    binding.apply {
                        pbLoading.visibility = View.GONE

                        //Aquí utilizamos la respuesta exitosa y asignamos los valores a las vistas
                        tvTitle.text = response.body()?.category

                        Glide.with(requireActivity())
                            .load(response.body()?.image)
                            .into(ivImage)

                        /*Picasso.get()
                            .load(response.body()?.image)
                            .into(ivImage)*/

                        tvLongDesc.text =
                            getString(R.string.descripcion_menus, response.body()?.description)
                        tvSubcategory.text = getString(
                            R.string.subcategoria_menus,
                            response.body()?.subcategory?.joinToString(", ")
                        )
                        tvExamples.text = getString(
                            R.string.ejemplos_menus,
                            response.body()?.examples?.joinToString(", ")
                        )
                        // Sección del video
                        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                                override fun onReady(youTubePlayer: YouTubePlayer) {
//                                    youTubePlayer.loadVideo("y-wJo11dzPc", 0f)
                                    youTubePlayer.loadVideo( response.body()?.video.toString()
                                        , 0f)
                                }
                            })
                        lifecycle.addObserver(youtubePlayerView)

                        //Para justificar el texto de un textview
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) //Q corresponde a Android 10
                            tvLongDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
                    }



                }

                override fun onFailure(p0: Call<LawyerDetailDto>, p1: Throwable) {
                    //Manejo del error de conexión
                }

            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(lawyerId: String) =
            LawyerDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(LAWYER_ID, lawyerId)
                }
            }
    }
}