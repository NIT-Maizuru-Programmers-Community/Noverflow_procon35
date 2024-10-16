package com.maizuruProcon.Noverflow

import org.osmdroid.views.overlay.Marker
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MapMarker(private val mapView: MapView) {

    fun placeMarker(latitude: Double, longitude: Double, info:Int) {
        val point = GeoPoint(latitude, longitude)
        val marker = Marker(mapView)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title="空き容量: ${info} /500L"
        marker.setOnMarkerClickListener{ maeker, mapView ->
            marker.showInfoWindow()
            true
        }
        mapView.overlays.add(marker)
        mapView.invalidate()
    }
}