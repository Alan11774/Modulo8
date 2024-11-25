package mx.com.yourlawyer.whipdm

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mx.com.yourlawyer.whipdm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var sensorEventListener: SensorEventListener? = null

    private var mediaPlayer: MediaPlayer? = null
    private var whip = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        mediaPlayer = MediaPlayer.create(this, R.raw.whip)

        if(accelerometer != null) {
            sensorEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    var accelValue = event?.values?.get(0)
                    Log.d("APPLOGS", "Accelerometer Value: $accelValue")
                    if(accelValue!! < -5 && whip == 0) { //El usuario movio el disp. a la izquierda
                        whip++
                    }else if(accelValue > 5 && whip == 1) { //Se movió a la derecha
                        whip++
                    }

                    if(whip == 2) {  //Hizo el movimiento completo
                        whip = 0
                        mediaPlayer?.start()
                    }
                }

                override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

                }

            }

        }else{
            Toast.makeText(this, "Dispositivo no Compatible con la app", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(sensorEventListener)

    }
    private fun blinkScreen(){
        val anim = ObjectAnimator.ofInt(binding.main.background, "alpha", 0, 255).apply {
            duration = 500L
            repeatCount = 0
            setEvaluator(ArgbEvaluator())
        }

        anim.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                binding.main.background = getDrawable(R.drawable.gradient_bg)
            }
        })

        anim.start()
    }

}