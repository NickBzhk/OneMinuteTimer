package com.bzhk.twospot.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bzhk.twospot.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.config.Configuration.*
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow
import org.osmdroid.views.overlay.infowindow.InfoWindow
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    private companion object {
        private const val DEFAULT_DELAY_TIME = 867L
        private val REQUIRED_PERMISSIONS: Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModel<MapObjectsViewModel>()

    private lateinit var map: MapView
    private lateinit var fusedClient: FusedLocationProviderClient
    private var isButtonSearchAreaEnabled = false
    private var isMapListenerEnabled = false
    private var isFocusRequested = false

    fun isUserLocationFocusRequested(): Boolean = isFocusRequested

    fun requestUserLocationFocus(flag: Boolean) {
        isFocusRequested = flag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        map = binding.map
        map.setTileSource(TileSourceFactory.MAPNIK)

        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        assignButtons()
        checkPermissions()
        centerCameraToUser()
        enableButtonSearchArea(false)
        enableCameraMovementListener(true)
        enableButtonFindUserLocation()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onStop() {
        super.onStop()
        fusedClient.removeLocationUpdates(locationCallback)
    }

    private fun assignButtons() {
        with(binding) {

            buttonSearchThisArea.setOnClickListener {
                drawMapObjects()
                enableButtonSearchArea(false)
            }

            buttonAlignToDeviceLocation.setOnClickListener {
                centerCameraToUser()
            }
        }
    }

    private val launcher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { map ->
            if (map.values.isNotEmpty() && map.values.all { it }) {
                centerCameraToUser()
            }
        }

    private fun checkPermissions() {
        if (REQUIRED_PERMISSIONS.all { permission ->
                ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            centerCameraToUser()
        } else {
            launcher.launch(REQUIRED_PERMISSIONS)
        }
        Priority.PRIORITY_HIGH_ACCURACY
    }

    private val locationCallback = object : LocationCallback() {
        private lateinit var currentDeviceLocation: Location

        override fun onLocationResult(p0: LocationResult) {
            val mapController = binding.map.controller
            currentDeviceLocation = p0.locations.last()
            if (isUserLocationFocusRequested()) {
                mapController.animateTo(
                    GeoPoint(currentDeviceLocation)
                )
                mapController.setZoom(15)
                requestUserLocationFocus(false)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun centerCameraToUser() {
        val request = LocationRequest.create()
            .setInterval(1_000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)

        fusedClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )

        requestUserLocationFocus(true)
    }

    private fun drawMapObjects() {
        val scope = lifecycleScope

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            log("Caught exception: $throwable", tag = "Exception")
        }

        scope.coroutineContext.job.cancelChildren()
        scope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val mapCenterLon =
                map.boundingBox.centerLongitude

            val mapCenterLat =
                map.boundingBox.centerLatitude

            val mapDiagonalLength =
                map.boundingBox.diagonalLengthInMeters.roundToInt()

            val apiCategoryString =
                "entertainment.museum"

            val apiBiasString =
                "proximity:${mapCenterLon},${mapCenterLat}"

            val apiFilterString =
                "circle:${mapCenterLon},${mapCenterLat},${mapDiagonalLength}"

            viewModel
                .getMapObjects(apiCategoryString, apiFilterString, apiBiasString)
                .collect { mapObject ->
                    mapObject?.features?.forEach { responseBody ->
                        val latitude =
                            responseBody.properties.lat

                        val longitude =
                            responseBody.properties.lon

                        val title =
                            "${responseBody.properties.datasource.raw.name ?: ""} " +
                                    "/ ${responseBody.properties.datasource.raw.nameEn ?: ""}"

                        val description =
                            """${responseBody.properties.formatted ?: ""}
                            |${responseBody.properties.addressLine2 ?: ""}
                            |${responseBody.properties.datasource.raw.openingHours ?: ""}
                            |${responseBody.properties.datasource.raw.contactPhone ?: ""}
                        """.trimMargin()

                        val id: String =
                            responseBody.properties.placeId

                        placeObjectMarker(id, latitude, longitude, title, description)
                    }
                }
        }
    }

    private fun placeObjectMarker(
        id: String,
        latitude: Double,
        longitude: Double,
        title: String = "Title",
        description: String = "Description"
    ) {
        val items = ArrayList<OverlayItem>()
        items.add(OverlayItem(id, title, description, GeoPoint(latitude, longitude)))

        items.forEach { item ->
            val marker = Marker(map)
            marker.position = item.point as GeoPoint
            marker.title = item.title
            marker.id = item.uid
            marker.subDescription = item.snippet
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.overlays.add(marker)
    }

    log("placing ObjectMarkers")
}

private fun enableCameraMovementListener(flag: Boolean) {
    isMapListenerEnabled = flag
    if (isMapListenerEnabled) {
        map.addMapListener(
            DelayedMapListener(
                object : MapListener {
                    fun useCommonConfiguration() {
                        enableButtonSearchArea(false)
                        drawMapObjects()
                        map.overlays.clear()
                    }

                    override fun onScroll(event: ScrollEvent): Boolean {
                        useCommonConfiguration()
                        return false
                    }

                    override fun onZoom(event: ZoomEvent): Boolean {
                        useCommonConfiguration()
                        return false
                    }
                }, DEFAULT_DELAY_TIME
            )
        )

    }
}

private fun enableButtonSearchArea(flag: Boolean) {
    isButtonSearchAreaEnabled = flag

    binding.buttonSearchThisArea.visibility = if (isButtonSearchAreaEnabled) {
        View.VISIBLE
    } else {
        View.GONE
    }

}

private fun enableButtonFindUserLocation() {
    val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
    mLocationOverlay.enableMyLocation()
    map.overlays.add(mLocationOverlay)
}

private fun log(msg: String, tag: String = "TAG") {
    Log.w(tag, msg)
}
}