package mx.com.yourlawyer.whipdm

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

abstract class AndroidSensor(
    private val context: Context, //Para tener acceso a la instancia del SensorManager
    private val sensorFeature: String, //Para ver si una característica específica existe
    sensorType: Int
): MeasurableSensor(sensorType), SensorEventListener {
    override val doesSensorExist: Boolean
        get() = context.packageManager.hasSystemFeature(sensorFeature)

    private lateinit var sensorManager: SensorManager

    private var sensor: Sensor?= null

    override fun startListening() {
        //Si no existe el sensor, se regresa
        if(!doesSensorExist){
            return
        }
        //Si existe, pero no se ha inicializado, lo inicializamos
        if(!::sensorManager.isInitialized && sensor == null){
            sensorManager = context.getSystemService(SensorManager::class.java) as SensorManager
            sensor = sensorManager.getDefaultSensor(sensorType)
        }
        sensor.let{
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun stopListening() {
        //Si el sensor no existe
        if(!doesSensorExist && !::sensorManager.isInitialized){
            return
        }
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(!doesSensorExist){
            return
        }
        //Si existe, checamos que sea el sensor que queremos
        if(event?.sensor?.type == sensorType){
            onSensorValuesChanged?.invoke(event.values.toList())
        }
    }

    //Se agrega para que no marque error, pero por ahora no le agregamos alguna funcionalidad
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

}