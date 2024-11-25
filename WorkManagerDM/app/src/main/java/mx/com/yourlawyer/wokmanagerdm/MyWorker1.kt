package mx.com.yourlawyer.wokmanagerdm

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class MyWorker1(
    private val context: Context,
    private val workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val suma = suma(5.0,3.0)
        return if(suma > 0)
            Result.success()
        else
            Result.failure()

    }
    private suspend fun suma(num1:Double,num2:Double):Double{
        delay(5000)
        return num1+num2

    }
}