package mx.com.yourlawyer.whipdm

//Se hace abstracta para poder reutilizar (como una clase general)
//Así se pueden obtener los datos directo en el viewModel
abstract class MeasurableSensor(
    //Se pasa el tipo de sensor en el constructor
    protected val sensorType: Int //se requiere protected para que se use en subclases de esta
) {

    //Ponemos una lambda para obtener los valores, puede ser uno o más, pero son Float
    //Algunos sensores devuelven más de un valor
    //Unit es que no regresa nada (como un void en Java)
    protected var onSensorValuesChanged: ((List<Float>) -> Unit)? = null

    abstract val doesSensorExist: Boolean
    abstract fun startListening()
    abstract fun stopListening()

    //Función pública
    fun setOnSensorValuesChangedListener(listener: (List<Float>) -> Unit){
        onSensorValuesChanged = listener
    }
}