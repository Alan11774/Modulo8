package mx.com.yourlawyer.whipdm

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor

class AccelerometerSensor(
    context: Context
): AndroidSensor(    //Hereda de AndroidSensor
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_ACCELEROMETER,
    sensorType = Sensor.TYPE_ACCELEROMETER
)