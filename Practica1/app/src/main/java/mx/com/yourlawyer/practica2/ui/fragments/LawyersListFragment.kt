package mx.com.yourlawyer.practica2.ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import mx.com.yourlawyer.practica2.R
import mx.com.yourlawyer.practica2.application.LawyersRfApp
import mx.com.yourlawyer.practica2.data.LawyerRepository
import mx.com.yourlawyer.practica2.data.remote.model.LawyerDto
import mx.com.yourlawyer.practica2.databinding.FragmentLawyersListBinding
import mx.com.yourlawyer.practica2.ui.adapters.LawyersAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LawyersListFragment : Fragment() {

    private var _binding: FragmentLawyersListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: LawyerRepository
    private lateinit var mediaPlayer: MediaPlayer
    private var playing = false
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLawyersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load the background image using Glide
        Glide.with(this)
            .load(R.drawable.background)
            .override(Target.SIZE_ORIGINAL)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.backgroundList)
        // Music Player
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.oxxo)
        mediaPlayer.start()
        //Obteniendo la instancia al repositorio
        repository = (requireActivity().application as LawyersRfApp).repository

        //Para apiary
        val call: Call<MutableList<LawyerDto>> = repository.getLawyersApiary()

        call.enqueue(object: Callback<MutableList<LawyerDto>> {
            override fun onResponse(
                p0: Call<MutableList<LawyerDto>>,
                response: Response<MutableList<LawyerDto>>
            ) {
                binding.pbLoading.visibility = View.GONE

                if (response.isSuccessful) {
                    response.body()?.let { lawyers ->
                        binding.rvLawyers.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = LawyersAdapter(lawyers) { lawyer ->
                                lawyer.id?.let { id ->
                                    requireActivity().supportFragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, LawyerDetailFragment.newInstance(id))
                                        .addToBackStack(null)
                                        .commit()
                                }
                            }
                        }
                    } ?: run {
                        Toast.makeText(requireContext(),
                            getString(R.string.no_lawyers_found), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(),
                        getString(R.string.failed_to_retrieve_lawyers), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(p0: Call<MutableList<LawyerDto>>, p1: Throwable) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_no_conexion_disponible),
                    Toast.LENGTH_SHORT
                ).show()
                binding.pbLoading.visibility = View.GONE
            }
        })
    }

    override fun onPause() {
        super.onPause()
        playing = mediaPlayer.isPlaying
        currentPosition = mediaPlayer.currentPosition
        mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        if (playing) {
            mediaPlayer.seekTo(currentPosition)
            mediaPlayer.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mediaPlayer.release()
    }
}