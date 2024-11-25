package mx.com.yourlawyer.locationdiplogps

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.GoogleMap
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {
    // Propiedad Maps
    private lateinit var map : GoogleMap

    // Para el permiso
    private var fineLocationPermissionGranted = false


    private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted){
            //Se concedió el permiso
            actionPermissionGranted()
        }else{
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                    .setTitle("Permiso requerido")
                    .setMessage("Se necesita el permiso para poder ubicar la posición del usuario en el mapa")
                    .setPositiveButton("Entendido"){ _, _ ->
                        updateOrRequestPermissions()
                    }
                    .setNegativeButton("Salir"){ dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    .create()
                    .show()
            } else {
                Toast.makeText(
                    this,
                    "El permiso de ubicación se ha negado permanentemente",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    //***********************************************
    // Método para activar la ubicación en el mapa
    // Se usa @SuppressLint("MissingPermission") para
    // indicar que se está consciente de que no se
    // está solicitando el permiso
    //***********************************************
    @SuppressLint("MissingPermission")
    private fun actionPermissionGranted() {
        map.isMyLocationEnabled = true
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            1f,
            this
        )
    }


    //***********************************************
    // Método para actualizar o solicitar permisos
    //***********************************************
    private fun updateOrRequestPermissions() {
        //Revisando el permiso
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        fineLocationPermissionGranted = hasFineLocationPermission

        if (!fineLocationPermissionGranted) {
            //Pedimos el permiso
            permissionsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }else{
            //Tenemos los permisos
            actionPermissionGranted()
        }

    }

    override fun onRestart() {
        super.onRestart()
        if(!::map.isInitialized)return
        if(!fineLocationPermissionGranted) {
            updateOrRequestPermissions()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        //Obtenemos un objeto de tipo GoogleMap que representa el mapa
        //y lo asignamos a la propiedad map
        map = googleMap
        createMarker()
    }

    //****************************************************
    // Método para crear un marcador en el mapa
    //****************************************************
    private fun createMarker(){
        val coordinates = LatLng(19.322326,-99.184592)
        val marker = MarkerOptions()
            .position(coordinates)
            .title("DGTIC UNAM")
            .snippet("Cursos Y Diplomados en TIC")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.school))
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 15f),
            4000,
            null
        )
    }

    override fun onLocationChanged(location: Location) {
        map.clear()
        val coordinates = LatLng(location.latitude, location.longitude)
        val marker = MarkerOptions()
            .position(coordinates)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.person))

        map.addMarker(marker)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 18f))
    }

}