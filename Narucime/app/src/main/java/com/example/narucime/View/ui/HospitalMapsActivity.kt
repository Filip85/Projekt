package com.example.narucime.View.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.narucime.FirebaseSource.DataClass
import com.example.narucime.Model.City
import com.example.narucime.Model.Hospital
import com.example.narucime.R
import com.example.narucime.SharedPreferences.MyPreference
import com.example.narucime.View.listeners.FetchAllHospitals
import com.example.narucime.View.listeners.FetchHospitalsName
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_hospital_maps.*
import kotlin.math.absoluteValue


class HospitalMapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?): Boolean {
        Log.d("WhereIAm", city.cityName)
        val pref = MyPreference(this)
        Log.d("WhereIAm", pref.getHospitalName()!!)
        val intent = Intent(this, ExamsActivity::class.java)
        intent.putExtra(ExamsActivity.CITYNAMEE, city.cityName)
        intent.putExtra(ExamsActivity.HOSPITALNAME, pref.getHospitalName())
        startActivity(intent)
        return true
    }

    lateinit var map: GoogleMap
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var lastLocation: Location
    lateinit var hospital: Hospital
    lateinit var city: City
    lateinit var path: String

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun init() {

        inputSearch.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE
                || actionId == EditorInfo.IME_ACTION_SEARCH
                || event.action == KeyEvent.ACTION_DOWN
                || event.action == KeyEvent.KEYCODE_ENTER) {

                if(inputSearch.text.toString() == "") {
                    Toast.makeText(this, "Please, enter Address, City or Zip Code", Toast.LENGTH_LONG).show()
                }
                else {
                    //geoLocate()
                }
                true
            } else {
                false
            }
        }
    }

    private fun geoLocate(hospitalName: MutableList<String>, currentLatLin: LatLng) {
        val serchString = inputSearch.text.toString()

        val geocoder = Geocoder(this)
        var addresses: List<Address>?
        var address: Address?
        var long = 0.0
        var lat = 0.0
        var initLong = 0.0
        var initLat = 0.0
        var hName = "null"

        addresses = geocoder.getFromLocationName(hospitalName[1], 1)

        if(addresses.size > 0) {
            address = addresses.get(0)

            initLong = address.longitude - currentLatLin.longitude
            initLat = address.latitude - currentLatLin.latitude
        }

        for(h in hospitalName) {
            addresses = geocoder.getFromLocationName(h, 1)

            if(addresses.size > 0) {
                address = addresses.get(0)

                long = address.longitude - currentLatLin.longitude
                lat = address.latitude - currentLatLin.latitude

                if(long.absoluteValue < initLong.absoluteValue && lat.absoluteValue < initLat.absoluteValue) {
                    initLong = address.longitude
                    initLat = address.latitude
                    hName = h
                }
            }
        }

        val hospitalLatLng = LatLng(initLat, initLong)

        placeMarkerOnMap(hospitalLatLng, hName)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(hospitalLatLng, 12f))

        //Log.d("WhereIAm", address.toString())
        Log.d("WhereIAm", hospitalLatLng.toString())

        /*if(addresses.size > 0) {
            address = addresses.get(0)

            if((currentLatLin.latitude >= address.latitude) && (currentLatLin.longitude >= address.longitude)) {
                long = address.longitude
                lat = address.latitude
            }
            val hospitalLatLng = LatLng(address.latitude, address.longitude)

            placeMarkerOnMap(hospitalLatLng)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(hospitalLatLng, 12f))

            Log.d("WhereIAm", address.toString())
            Log.d("WhereIAm", hospitalLatLng.toString())

        }*/
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap()

        init()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
                return
        }

        map.isMyLocationEnabled = true
        //map.mapType = GoogleMap.MAP_TYPE_TERRAIN

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                //val markerOptions = MarkerOptions().position(currentLatLng)
                //placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                Log.d("WhereIAm", currentLatLng.toString())

                find_hospital.setOnClickListener{
                    findNearestHospital(currentLatLng)
                }
            }
        }
    }

    private fun findNearestHospital(currentLatLin: LatLng) {
        val dataClass = DataClass()
        path = "cities/"

        dataClass.getAllHospitals(path, object : FetchAllHospitals{
            override fun getHospotals(listOfHospitals: MutableList<String>) {
                geoLocate(listOfHospitals, currentLatLin)
            }
        })
    }

    private fun placeMarkerOnMap(location: LatLng, hospitalName: String) {

        map.clear()

        //val markerOptions = MarkerOptions().position(location)

        val titleStr = hospitalName + ", " + getAddress(location)

        Log.d("WhereIAm", titleStr)
        //markerOptions.title(titleStr)

        //map.addMarker(markerOptions)

        map.addMarker(MarkerOptions()
            .position(LatLng(location.latitude, location.longitude))
            .title(titleStr)).showInfoWindow()
    }

    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        if(addresses.size > 0) {
            address = addresses[0]
            addressText += address.getAddressLine(0)
            city = City(0, address.locality)

            val add = addressText.split(",")

            val dataClass = DataClass()
            path = "cities/${city.cityName}"

            val pref = MyPreference(this)

            dataClass.getHospotalName(path, add.get(0), object : FetchHospitalsName {
                override fun getHospitalName(hospitalName: String?) {
                    Log.d("HospitalnameHMActivity", hospitalName)
                    pref.setHospitalName(hospitalName!!)
                    //hospital = Hospital(hospitalName, add.get(0))
                }
            })

            //hospital = Hospital(pref.getHospitalName()!!, add.get(0))
            //Log.d("NameH", hospital.hositalName)
        }

        Log.d("adresaneka", addressText)
        return addressText
    }

}
