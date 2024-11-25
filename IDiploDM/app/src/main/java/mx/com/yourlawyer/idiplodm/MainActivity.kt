package mx.com.yourlawyer.idiplodm

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import message
import mx.com.yourlawyer.idiplodm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private var user : FirebaseUser? = null
    private var userId : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Bloquear la orientación de la pantalla
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //****************************************************
        // Inicializar Firebase
        //****************************************************
        // Instancia de FirebaseAuth para autenticación
        firebaseAuth = FirebaseAuth.getInstance()

        // Obtener el usuario actual
        user = firebaseAuth.currentUser
        // Obtener el id del usuario
        userId = user?.uid


        //****************************************************
        // Mostrar los datos del usuario
        //****************************************************
        binding.tvUsuario.text = user?.email

        // Revisar si el correo ha sido verificado
        if(user?.isEmailVerified == false){
            binding.tvCorreoNoVerificado.visibility = View.VISIBLE
            binding.btnReenviarVerificacion.visibility = View.GONE
            binding.btnReenviarVerificacion.setOnClickListener{
                user?.sendEmailVerification()?.addOnSuccessListener{
                    message("Se ha enviado un correo de verificación a ${user?.email}")
                }?.addOnFailureListener{
                    message("Error al enviar el correo de verificación")
                }
            }
        }

        //****************************************************
        // Botón Cerrar Sesión
        //****************************************************

        binding.btnCerrarSesion.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            message("Sesión cerrada Exitosamente")
            finish()
        }

    }
}