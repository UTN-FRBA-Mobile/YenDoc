package ar.yendoc.ui

import android.Manifest
import androidx.fragment.app.Fragment
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import ar.yendoc.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import ar.yendoc.R
import ar.yendoc.network.ApiServices
import ar.yendoc.network.Paciente
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var myContext: FragmentActivity
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var mMap: GoogleMap
    private val defaultLocation = LatLng(-34.628744, -58.447452)
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null
    private lateinit var sharedPref: SharedPreferences
    private var paciente : Paciente? = null

    companion object {
        val TAG: String = MapFragment::class.java.simpleName
        var PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        var mapFragment : SupportMapFragment?=null
        fun newInstance() = MapFragment()
        private const val DEFAULT_ZOOM = 15
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    locationPermissionGranted = true
                }
            }

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        var idPaciente = sharedPref.getInt(getString(R.string.id_paciente),0)
        getPaciente(idPaciente)

        mapFragment = childFragmentManager.findFragmentById(ar.yendoc.R.id.mapGoogle) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.activity)
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            locationPermissionGranted = granted
        }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap!!

        activity?.let {
            if (hasPermissions(activity as Context, MapFragment.PERMISSIONS)) {
                locationPermissionGranted = true
            } else {
                permReqLauncher.launch(
                    MapFragment.PERMISSIONS
                )
            }
        }
        updateLocationUI()
        getDeviceLocation()
    }

    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                mMap?.isMyLocationEnabled = true
                mMap?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                mMap?.isMyLocationEnabled = false
                mMap?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
            }
        } catch (e: SecurityException) {
            Log.e(getString(R.string.error), e.message, e)
        }
    }

    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationClient?.lastLocation
                locationResult?.addOnCompleteListener(myContext) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.e(TAG, getString(R.string.error), task.exception)
                        mMap?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        mMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e(getString(R.string.error), e.message, e)
        }
    }

    private fun getPaciente(idPaciente: Int){
        val apiInterface = ApiServices.create().getPacienteById(idPaciente)
        apiInterface.enqueue( object : Callback<Paciente> {
            override fun onResponse(call: Call<Paciente>, response: Response<Paciente>
            ) {
                if(response?.body() != null){
                    paciente = response.body()!!
                    setPacienteLocation()
                }
            }

            override fun onFailure(call: Call<Paciente>, t: Throwable) {
                Log.d(getString(R.string.error), t.message.toString())
            }
        })
    }

    private fun setPacienteLocation(){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(paciente?.direccion_latitud!!.toDouble(), paciente?.direccion_longitud!!.toDouble()), DEFAULT_ZOOM.toFloat()))
        mMap.addMarker(MarkerOptions().position(LatLng(paciente?.direccion_latitud!!.toDouble(), paciente?.direccion_longitud!!.toDouble())).title("Visita a realizar"))
    }

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)
    }
}