package com.maizuruProcon.Noverflow

import android.graphics.Color
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class RouteController(private val mapView: MapView) {

    fun drawRoute(routePoints: List<GeoPoint>) {
        val polyline = Polyline()
        polyline.setPoints(routePoints)

        // Paint オブジェクトを作成して色と幅を設定
        val paint = polyline.outlinePaint
        paint.color = Color.BLUE
        paint.strokeWidth = 5f

        mapView.overlays.add(polyline)
        mapView.invalidate()
    }
}