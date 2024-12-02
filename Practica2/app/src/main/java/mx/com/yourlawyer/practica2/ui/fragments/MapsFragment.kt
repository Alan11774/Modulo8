package mx.com.yourlawyer.practica2.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mx.com.yourlawyer.practica2.R
import mx.com.yourlawyer.practica2.application.LawyersRfApp
import mx.com.yourlawyer.practica2.data.LawyerRepository
import mx.com.yourlawyer.practica2.databinding.FragmentMapsBinding
import mx.com.yourlawyer.practica2.utils.Constants

private var name:String = ""
private var latitude:String = ""
private var longitude:String = ""

class MapsFragment : Fragment(R.layout.fragment_maps), OnMapReadyCallback, LocationListener {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private var fineLocationPermissionGranted = false

    private lateinit var repository: LawyerRepository
    private var lawyerId: String? = null


    private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            actionPermissionGranted()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.permission_required))
                    .setMessage(getString(R.string.is_needed_user_permission_for_location_access))
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        updateOrRequestPermissions()
                    }
                    .setNegativeButton(getString(R.string.close)) { dialog, _ ->
                        dialog.dismiss()
                        requireActivity().finish()
                    }
                    .create()
                    .show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.the_permission_has_been_denied),
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {

        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = (requireActivity().application as LawyersRfApp).repository
        repository
            .getLawyersApiary()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingPermission")
    private fun actionPermissionGranted() {
        map.isMyLocationEnabled = true
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            1f,
            this
        )
    }

    private fun updateOrRequestPermissions() {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        fineLocationPermissionGranted = hasFineLocationPermission

        if (!fineLocationPermissionGranted) {
            permissionsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            actionPermissionGranted()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!::map.isInitialized) return
        if (!fineLocationPermissionGranted) {
            updateOrRequestPermissions()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
    }

    private fun createMarker() {
        name = requireArguments().getString("NAME").toString()
        latitude = requireArguments().getString("LATITUDE").toString()
        longitude = requireArguments().getString("LONGITUDE").toString()
//        val coordinates = LatLng(19.322326, -99.184592)
        val coordinates = LatLng(latitude.toDouble(), longitude.toDouble())
        val marker = MarkerOptions()
            .position(coordinates)
            .title(name)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(api_name: String, api_latitude: String, api_longitude: String) =
            MapsFragment().apply {
                arguments = Bundle().apply {
                    putString("NAME", api_name)
                    putString("LATITUDE", api_latitude)
                    putString("LONGITUDE", api_longitude)
                }
            }
    }
}