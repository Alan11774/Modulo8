package mx.com.yourlawyer.practica2.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import mx.com.yourlawyer.practica2.R
import mx.com.yourlawyer.practica2.databinding.ActivityMainBinding
import mx.com.yourlawyer.practica2.ui.fragments.LawyersListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LawyersListFragment())
                .commit()
        }
    }
}