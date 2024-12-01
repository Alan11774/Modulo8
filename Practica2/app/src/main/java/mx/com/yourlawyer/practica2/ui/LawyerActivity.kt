package mx.com.yourlawyer.practica2.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.squareup.picasso.Picasso
import mx.com.yourlawyer.practica2.R
import mx.com.yourlawyer.practica2.data.remote.LawyerApi
import mx.com.yourlawyer.practica2.data.remote.model.LawyerDetailDto
import mx.com.yourlawyer.practica2.data.remote.model.LawyerDetailDtoApi
import mx.com.yourlawyer.practica2.databinding.ActivityLawyerBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LawyerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLawyerBinding

    companion object{
        const val BASE_URL = "https://private-56712-yourlawyer1.apiary-mock.com/"

        const val LOGTAG = "LOGSLAWYER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLawyerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Log.d(LOGTAG, "Hola")

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val lawyerApi = retrofit.create(LawyerApi::class.java)

        val call: Call<LawyerDetailDtoApi> = lawyerApi.getLawyerDetail("82")

        call.enqueue(object: Callback<LawyerDetailDtoApi> {
            override fun onResponse(p0: Call<LawyerDetailDtoApi>, response: Response<LawyerDetailDtoApi>) {
                Log.d(
                    LOGTAG,
                    getString(R.string.url_del_lawyer_normal, response.body()?.category)
                )

                Log.d(
                    LOGTAG,
                    getString(R.string.url_de_lawyer, response.body()?.subcategory)
                )

                response.body()?.description

                binding.tvPokemon.text = response.body()?.category

                Picasso.get()
                    .load(response.body()?.image)
                    .into(binding.ivPokemon)

            }



            override fun onFailure(p0: Call<LawyerDetailDtoApi>, p1: Throwable) {

            }

        })

    }
}