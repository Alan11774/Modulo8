import android.widget.Toast
import android.app.Activity

fun Activity.message(message: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(
          this,
        message,
        duration
    ).show()
}