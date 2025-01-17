package com.maizuruProcon.Noverflow

import android.graphics.Color
import android.widget.TextView
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MapTapController(private val mapView: MapView, private val locationTextView: TextView):AppCompatActivity() {



    init {
        val tapOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                if (p != null) {
                    clearMarkers()
                    placeMarker(p)
                    clearPolygon()
                    //placePolygon(p)
                    updateLocationText(p)
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }

            private fun clearMarkers() {
                val overlays = mapView.overlays
                for (i in overlays.size - 1 downTo 0) {
                    val overlay = overlays[i]
                    if (overlay is Marker) {
                        overlays.removeAt(i)
                    }
                }
            }

            private fun clearPolygon() {
                val overlays = mapView.overlays
                for (i in overlays.size - 1 downTo 0) {
                    val overlay = overlays[i]
                    if (overlay is Polygon) {
                        overlays.removeAt(i)
                    }
                }
            }

            private fun placeMarker(point: GeoPoint) {
                val marker = Marker(mapView)
                marker.position = point
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                mapView.overlays.add(marker)
                mapView.invalidate()
            }

            // 円形のポリゴンを描画する


            private fun updateLocationText(point: GeoPoint) {
                "緯度: ${point.latitude} 経度: ${point.longitude}".also { locationTextView.text = it }
            }
        })
        mapView.overlays.add(tapOverlay)
    }
}