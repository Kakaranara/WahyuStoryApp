package com.example.wahyustoryapp.ui.main.maps

import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.wahyustoryapp.R
import com.example.wahyustoryapp.authDataStore
import com.example.wahyustoryapp.constant.MapArgs
import com.example.wahyustoryapp.data.network.ApiConfig
import com.example.wahyustoryapp.preferences.AuthPreference

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale

class MapsFragment : Fragment() {

    val args: MapsFragmentArgs by navArgs()


    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        when(args.types){
            MapArgs.CheckMyLocation -> {
                Toast.makeText(requireActivity(), "Check my location.", Toast.LENGTH_SHORT).show()
            }
            MapArgs.CheckAllMaps -> {
                Toast.makeText(requireActivity(), "Check all maps!", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            val token =
                AuthPreference.getInstance(requireActivity().authDataStore).getToken().first()
            try {
                val response =
                    ApiConfig.getApiService().getAllStory("Bearer $token", size = 150, location = 1)
                if (response.isSuccessful) {
                    response.body()?.let {
                        val firstStory = it.listStory.first()
                        googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    firstStory.lat ?: 0.0, firstStory.lon ?: 0.0
                                ), 8f
                            )
                        )
                        Log.d(TAG, "list size :  $it.size ")
                        it.listStory.forEach { storyList ->
                            if (storyList.lat != null && storyList.lon != null) {
                                val address = getAddressName(storyList.lat, storyList.lon)
                                googleMap.addMarker(
                                    MarkerOptions().position(
                                        LatLng(
                                            storyList.lat, storyList.lon
                                        )
                                    ).title(storyList.name).snippet(address)
                                )
                            }
                        }
                    }
                } else {
                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Suppress("DEPRECATION") //? only deprecated on api < 33
    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            list?.takeIf { it.isNotEmpty() }?.let {
                addressName = it[0].locality
                Log.d(TAG, "getAddressName: $addressName")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return addressName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


    }

    companion object {
        const val TAG = "MapsFragment"
    }
}