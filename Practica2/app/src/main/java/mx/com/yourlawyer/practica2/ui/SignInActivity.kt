package mx.com.yourlawyer.practica2.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import message
import mx.com.yourlawyer.practica2.R
import mx.com.yourlawyer.practica2.databinding.ActivitySignInBinding
import mx.com.yourlawyer.practica2.ui.fragments.LawyersListFragment

class SignInActivity : AppCompatActivity() {

        private lateinit var binding: ActivitySignInBinding

        private lateinit var firebaseAuth: FirebaseAuth

        // Propiedades para el usuario y contraseña.
        private var email = ""
        private var contrasenia = ""

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivitySignInBinding.inflate(layoutInflater)
            enableEdgeToEdge()
            setContentView(binding.root)

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            // instancia de FirebaseAuth para autenticación
            firebaseAuth = FirebaseAuth.getInstance()

            // Verificar si el usuario ya ha iniciado sesión
            if(firebaseAuth.currentUser != null){

                actionLoginSuccessful()
            }



            // btn login Sign In hola22
            binding.signInButton.setOnClickListener{
                if(!validateFields()) return@setOnClickListener
                binding.progressBar.visibility = View.VISIBLE
                authUser(email, contrasenia)
            }


            // btn login Registrarse Sign Up
            binding.signUpTextView.setOnClickListener{
                if (!validateFields()) return@setOnClickListener
                binding.progressBar.visibility = View.VISIBLE
                firebaseAuth.createUserWithEmailAndPassword(email,contrasenia).addOnCompleteListener { authResult ->
                    if(authResult.isSuccessful){
                        message(getString(R.string.user_succesfully_registred))

                        //Correo de verificación
                        firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener{
                            message("${getString(R.string.email_has_been_sent_to)}$email")
                        }?.addOnFailureListener{
                            message(getString(R.string.error_has_been_occurred_sending_the_mail))
                        }
                        actionLoginSuccessful()
                    }else{
                        binding.progressBar.visibility = View.GONE
                        handleErrors(authResult)
                    }

                }
            }


            // btn login Restablecer Password
            binding.forgotPasswordTextView.setOnClickListener {
                val resetMail = EditText(this)
                resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.reset_password))
                    .setMessage(getString(R.string.enter_your_email_to_receive_reset_link))
                    .setView(resetMail)
                    .setPositiveButton(getString(R.string.send)){ _, _ ->
                        val mail = resetMail.text.toString()
                        if(mail.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                            //Mandamos el enlace de restablecimiento
                            firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                                message(getString(R.string.the_email_has_been_sent_to_reset_your_password))
                            }.addOnFailureListener {
                                message(getString(R.string.the_link_to_reset_the_password_has_not_been_sent))
                            }
                        }else{
                            message(getString(R.string.please_enter_a_valid_email_address))
                        }
                    }
                    .setNegativeButton(getString(R.string.cancel)){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
        private fun validateFields(): Boolean{
            email = binding.emailEditText.text.toString().trim()  //Elimina los espacios en blanco
            contrasenia = binding.passwordEditText.text.toString().trim()

            //Verifica que el campo de correo no esté vacío
            if(email.isEmpty()){
                binding.emailEditText.error = getString(R.string.mail_is_needed)
                binding.emailEditText.requestFocus()
                return false
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.emailEditText.error = getString(R.string.mail_has_not_the_correct_format)
                binding.emailEditText.requestFocus()
                return false
            }

            //Verifica que el campo de la contraseña no esté vacía y tenga al menos 6 caracteres
            if(contrasenia.isEmpty()){
                binding.passwordEditText.error = getString(R.string.password_is_needed)
                binding.passwordEditText.requestFocus()
                return false
            }else if(contrasenia.length < 6){
                binding.passwordEditText.error =
                    getString(R.string.password_needs_at_least_6_characters)
                binding.passwordEditText.requestFocus()
                return false
            }
            return true
        }

        private fun handleErrors(task: Task<AuthResult>){
            var errorCode = ""

            try{
                errorCode = (task.exception as FirebaseAuthException).errorCode
            }catch(e: Exception){
                e.printStackTrace()
            }

            when(errorCode){
                getString(R.string.error_invalid_email) -> {
                    Toast.makeText(this,
                        getString(R.string.error_mail_has_not_the_correct_format), Toast.LENGTH_SHORT).show()
                    binding.emailEditText.error = getString(R.string.error_mail_has_not_the_correct_format)
                    binding.emailEditText.requestFocus()
                }
                getString(R.string.error_wrong_password) -> {
                    Toast.makeText(this,
                        getString(R.string.error_password_has_not_the_correct_format), Toast.LENGTH_SHORT).show()
                    binding.passwordEditText.error = getString(R.string.error_password_has_not_the_correct_format)
                    binding.passwordEditText.requestFocus()
                    binding.passwordEditText.setText("")

                }
                getString(R.string.error_account_exists_with_different_credential) -> {
                    //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                    Toast.makeText(this,
                        getString(R.string.error_an_account_already_exists_with_the_same_email_address_but_different_sign_in_credentials), Toast.LENGTH_SHORT).show()
                }
                getString(R.string.error_email_already_in_use) -> {
                    Toast.makeText(this,
                        getString(R.string.error_the_email_address_is_already_in_use_by_another_account), Toast.LENGTH_LONG).show()
                    binding.emailEditText.error = getString(R.string.error_the_email_address_is_already_in_use_by_another_account)
                    binding.emailEditText.requestFocus()
                }
                getString(R.string.error_user_token_expired) -> {
                    Toast.makeText(this,
                        getString(R.string.error_the_session_has_expired_please_log_in_again), Toast.LENGTH_LONG).show()
                }
                getString(R.string.error_user_not_found) -> {
                    Toast.makeText(this,
                        getString(R.string.error_the_user_does_not_exist), Toast.LENGTH_LONG).show()
                }
                getString(R.string.error_weak_password) -> {
                    Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_LONG).show()
                    binding.passwordEditText.error = getString(R.string.password_needs_at_least_6_characters)
                    binding.passwordEditText.requestFocus()
                }
                getString(R.string.no_network) -> {
                    Toast.makeText(this,
                        getString(R.string.no_network_connection), Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this,
                        getString(R.string.error_authentication_failed_please_check), Toast.LENGTH_SHORT).show()
                }
            }

        }

        private fun actionLoginSuccessful(){
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, LawyersListFragment()) // Replace with your fragment
                    .commit()

//            startActivity(Intent(this, LawyersListFragment::class.java))
//            finish()
        }

        private fun authUser(usr: String, pwd: String){
            firebaseAuth.signInWithEmailAndPassword(usr , pwd).addOnCompleteListener{ authResult ->
                // Se verifica el resultado de la autenticación
                if(authResult.isSuccessful){
                    message(getString(R.string.authentication_successful))
                    actionLoginSuccessful()
                }else{
                    binding.progressBar.visibility = View.GONE
//                message("Autenticación fallida")
                    handleErrors(authResult)
                }

            }
        }

    }