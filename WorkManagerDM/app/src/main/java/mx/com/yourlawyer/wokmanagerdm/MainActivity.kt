package mx.com.yourlawyer.wokmanagerdm

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import mx.com.yourlawyer.wokmanagerdm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
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
        binding.tvWork1Status.movementMethod = ScrollingMovementMethod()
        binding.tvWork2Status.movementMethod = ScrollingMovementMethod()

        val workerManager = WorkManager.getInstance(this)
        val workRequest1 = OneTimeWorkRequestBuilder<MyWorker1>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            ).build()
        val workRequest2 = OneTimeWorkRequestBuilder<MyWorker2>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            ).build()

        binding.ivStartWork.setOnClickListener{
            workerManager.beginUniqueWork(
                "work1",
                ExistingWorkPolicy.KEEP,
                workRequest1
            ).then(workRequest2).enqueue()
        }

        workerManager.getWorkInfoByIdLiveData(workRequest1.id).observe(this){ work1Info ->
            if(work1Info != null){
                val workStatus = work1Info.state.name
                binding.tvWork1Status.append("$workStatus\n")
                if(work1Info.state.isFinished){
                    binding.tvWork1Status.append("\n")
                }
            }

        }
        workerManager.getWorkInfoByIdLiveData(workRequest2.id).observe(this){ work2Info ->
            if(work2Info != null){
                val workStatus = work2Info.state.name
                binding.tvWork2Status.append("$workStatus\n")
                if(work2Info.state.isFinished){
                    binding.tvWork2Status.append("\n")
                }
            }

        }
    }
}