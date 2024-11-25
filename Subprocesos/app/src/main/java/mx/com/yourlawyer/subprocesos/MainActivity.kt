package mx.com.yourlawyer.subprocesos

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.com.yourlawyer.subprocesos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var counter = 0
    private var a = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ***************************************************
        // * 1. Corrutinas
        // ***************************************************
        binding.btnCounter.setOnClickListener {
            counter ++
            binding.tvCounter.text = counter.toString()
        }


        binding.btnDownload.setOnClickListener {
            binding.btnDownload.isEnabled = false
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    downloadData()
                }finally {
                    withContext(Dispatchers.Main){
                        binding.btnDownload.isEnabled = true
                    }
                }
            }
        }

        // ***************************************************
        // * 2. Corrutina con async y await para calcular
        // ***************************************************
        binding.btnCalculate.setOnClickListener{
            binding.btnCalculate.isEnabled = false

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    withContext(Dispatchers.Main) {
                        binding.tvCalculation.text = "Calculando..."
                    }
                    Log.d("APPLOGS", "Inicio corrutinas de calculo")
                    val initialTime = System.currentTimeMillis()
                    val result1 = async { calculateResult() }
                    val result2 = async { calculateResult2() }
                    val total = result1.await() + result2.await()
                    val finalTime = System.currentTimeMillis() - initialTime
                    Log.d("APPLOGS", "Tiempo total ${finalTime / 1000} segundos")
                    Log.d("APPLOGS", "Total $total")
                    withContext(Dispatchers.Main) {
                        binding.tvCalculation.text = total.toString()
                    }
                }finally {
                    withContext(Dispatchers.Main){
                        binding.btnCalculate.isEnabled = true
                    }
                }
            }

        }

        // ***************************************************
        // * 3. Corrutina con repeat y async y await para calcular
        // ***************************************************
        binding.btnStartMany.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                repeat(100000){
                    val number = async{
                        withContext(Dispatchers.Main){
                            binding.tvMany.text = a++.toString()
                        }
                        Log.d("APPLOGS", "${Thread.currentThread().name}")
                    }
                    number.await()
                }
            }
        }

    }




    // ***************************************************
    // downloadData - Descarga de datos en un hilo
    // diferente al principal para no saturar la UI
    // ***************************************************

    private suspend fun downloadData() {
        for( i in 1..200000 ){
            //Cambiamos de hilo
            withContext(Dispatchers.Main){
            binding.tvMessage.text = "Descargando $i bytes en el hilo ${Thread.currentThread().name}"
            }
//            Log.d("APPLOGS", "Descargando $i bytes en el hilo ${Thread.currentThread().name}")
        }
    }
    // ***************************************************
    // calculateResult - Calculo delay 8 segundos
    // un hilo diferente al principal
    // ***************************************************
    private suspend fun calculateResult(): Int {
        delay(8000)
        Log.d("APPLOGS", "Calculo en el hilo 1 ${Thread.currentThread().name}")
        return 2000
    }
    // ***************************************************
    // calculateResult2 - Calculo delay 4 segundos en un
    // hilo diferente al principal
    // ***************************************************
    private suspend fun calculateResult2(): Int {
        delay(4000)
        Log.d("APPLOGS", "Calculo en el hilo 2  Finalizado ${Thread.currentThread().name}")
        return 3000
    }
}