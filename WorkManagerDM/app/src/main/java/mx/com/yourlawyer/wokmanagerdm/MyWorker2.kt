package mx.com.yourlawyer.wokmanagerdm

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import kotlin.math.pow

class MyWorker2(
    private val context: Context,
    private val workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val suma = cubo(5.0)
        return if(suma > 0)
            Result.success()
        else
            Result.failure()

    }
    private suspend fun cubo(num1:Double):Double{
        delay(3000)
        return num1.pow(3.0)

    }
}