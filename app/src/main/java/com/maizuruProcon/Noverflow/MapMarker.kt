package com.maizuruProcon.Noverflow

import org.osmdroid.views.overlay.Marker
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MapMarker(private val mapView: MapView) {

    fun placeMarker(latitude: Double, longitude: Double) {
        val point = GeoPoint(latitude, longitude)
        val marker = Marker(mapView)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapView.overlays.add(marker)
        mapView.invalidate()
    }
}