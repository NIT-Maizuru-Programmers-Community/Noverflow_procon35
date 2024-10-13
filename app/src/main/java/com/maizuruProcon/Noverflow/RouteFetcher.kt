package com.maizuruProcon.Noverflow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.osmdroid.util.GeoPoint

class RouteFetcher {

    private val client = OkHttpClient()

    fun fetchRoute(currentLocation: GeoPoint, destination: GeoPoint, callback: (List<GeoPoint>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf6248dd0dd3ee7ead4ac294b52e9f16a281c1&start=${currentLocation.longitude},${currentLocation.latitude}&end=${destination.longitude},${destination.latitude}"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            if (responseBody != null) {
                val json = JSONObject(responseBody)
                val coordinates = json.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates")
                val routePoints = mutableListOf<GeoPoint>()
                for (i in 0 until coordinates.length()) {
                    val point = coordinates.getJSONArray(i)
                    val geoPoint = GeoPoint(point.getDouble(1), point.getDouble(0))
                    routePoints.add(geoPoint)
                }
                callback(routePoints)
            }
        }
    }
}
