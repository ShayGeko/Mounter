package com.example.mounter.directions

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mounter.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mounter.databinding.ActivityDirectionsBinding
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import org.json.JSONObject

class DirectionsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityDirectionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDirectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setMinZoomPreference(10.0f)
        map.setMaxZoomPreference(20.0f)
        // Add a marker in SFU and move the camera
        val sfu_burnaby = LatLng(49.276765, -122.917957)
        val sfu_surrey = LatLng(49.188680, -122.839940)


        map.addMarker(MarkerOptions().position(sfu_burnaby).title("Marker on Burnaby Campus"))
        map.addMarker(MarkerOptions().position(sfu_surrey).title("Marker on Surrey Campus"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sfu_surrey))

        val directionMode = "driving";
        val url = getUrl(sfu_surrey, sfu_burnaby, directionMode)

        val httpAsync = url
            .httpGet()
            .responseString { request, response, result ->
                println(request)
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex)
                    }
                    is Result.Success -> {
                        val data = result.get()
                        println(data)
                        val t = JSONObject(data)

                        val encodedPoints = t.getJSONArray("routes")
                            .getJSONObject(0)
                            .getJSONObject("overview_polyline")
                            .getString("points");

                        val latlngs = PolyUtil.decode(encodedPoints);

                        map.addPolyline(PolylineOptions()
                            .addAll(latlngs)
                            .width(5F)
                            .color(Color.RED));

                    }
                }
            }

        httpAsync.join()

    }

    fun getUrl(origin: LatLng, destination: LatLng, directionMode: String): String {
        val originStr = "origin=" + origin.latitude + "," + origin.longitude;
        val destinationStr = "destination=" + destination.latitude + "," + destination.longitude;
        val mode = "mode=$directionMode";
        val parameters = "$originStr&$destinationStr&$mode";
        val output = "json";
        val url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&key="+getString(
            R.string.google_maps_key
        )

        return url
    }
}