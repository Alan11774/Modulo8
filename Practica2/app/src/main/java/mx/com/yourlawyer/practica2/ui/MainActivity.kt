package mx.com.yourlawyer.practica2.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import mx.com.yourlawyer.practica2.R
import mx.com.yourlawyer.practica2.databinding.ActivityMainBinding
import mx.com.yourlawyer.practica2.ui.fragments.LawyersListFragment
import mx.com.yourlawyer.practica2.ui.fragments.MapsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, SignInActivity())
////                .replace(R.id.fragment_container, MapsFragment())
//                .commit()
        }
    }
}