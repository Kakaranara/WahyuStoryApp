package com.example.wahyustoryapp.ui.main.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.wahyustoryapp.MapsViewModelFactory
import com.example.wahyustoryapp.R
import com.example.wahyustoryapp.constant.MapArgs
import com.example.wahyustoryapp.data.network.response.ListStoryItem
import com.example.wahyustoryapp.databinding.FragmentMapsBinding
import com.example.wahyustoryapp.di.Injection
import com.example.wahyustoryapp.helper.Async
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsFragment : Fragment() {

    private val args: MapsFragmentArgs by navArgs()
    private val viewModel by viewModels<MapsViewModel> {
        MapsViewModelFactory(Injection.provideMapsRepository(requireActivity()))
    }
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private var gMaps: GoogleMap? = null

    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            activateLocation()
        } else {
            Toast.makeText(requireActivity(), "Permission not granted.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun activateLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext,
                finePermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            gMaps?.isMyLocationEnabled = true
        } else {
            launcher.launch(finePermission)
        }
    }


    private val callback = OnMapReadyCallback { googleMap ->
        gMaps = googleMap

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        googleMap.uiSettings.apply {
            isMapToolbarEnabled = true
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
        }
        activateLocation()

        when (args.types) {
            MapArgs.CheckMyLocation -> {
                Toast.makeText(requireActivity(), "Check my location.", Toast.LENGTH_SHORT).show()
            }
            MapArgs.CheckAllMaps -> {
                Toast.makeText(requireActivity(), "Check all maps!", Toast.LENGTH_SHORT).show()
                viewModel.data.observe(viewLifecycleOwner) {
                    when (it) {
                        is Async.Error -> {
                            binding.mapsProgressBar.visibility = View.GONE
                            Toast.makeText(requireActivity(), it.error, Toast.LENGTH_SHORT).show()
                        }
                        is Async.Loading -> {
                            binding.mapsProgressBar.visibility = View.VISIBLE
                        }
                        is Async.Success -> {
                            binding.mapsProgressBar.visibility = View.GONE
                            showAllStoryMarker(it.data, googleMap)
                        }
                    }
                }
            }
        }
    }

    private fun showAllStoryMarker(list: List<ListStoryItem>, gmaps: GoogleMap) {
        val firstItem = list.first()
        gmaps.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    firstItem.lat ?: 0.0,
                    firstItem.lon ?: 0.0
                ), 8f
            )
        )

        list.forEach { item ->
            item.takeIf { it.lat != null || it.lon != null }?.let {
                val latLng = LatLng(it.lat!!, it.lon!!) //? i have checked it in takeIf, don't worry
                val city = getAddressName(it.lat, it.lon)
                gmaps.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(it.name)
                        .snippet(city)
                )
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
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        gMaps = null
        _binding = null
    }

    companion object {
        private const val TAG = "MapsFragment"
        const val finePermission = Manifest.permission.ACCESS_FINE_LOCATION
    }
}