package com.maizuruProcon.Noverflow
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat

import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView


class FourActivity : AppCompatActivity(){
    private lateinit var mapView: MapView
    private lateinit var locationTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Configuration.getInstance().load(
            applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        setContentView(R.layout.activity_four)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mapView = findViewById(R.id.mapView)
        //locationTextView = findViewById(R.id.location_text)
        mapView.setMultiTouchControls(true)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        } else {
            setupMap()
        }

        val btnTest :Button = findViewById(R.id.btnTest)
        //3)戻るボタン(アクティビティの終了)
        btnTest.setOnClickListener {
            finish()
        }

    }

    private fun setupMap() {
        val mapSetupController = MapSetupController(this, mapView)
        mapSetupController.setupMapWithLocation { currentLocation ->
            val destination = GeoPoint(37.41690641728752, -122.08539203847516)

            // 経路を取得して描画
            val routeFetcher = RouteFetcher()
            routeFetcher.fetchRoute(currentLocation, destination) { routePoints ->
                val routeController = RouteController(mapView)
                routeController.drawRoute(routePoints)
            }

            // 目的地にマーカーを配置
            val mapMarkerController = MapMarker(mapView)
            mapMarkerController.placeMarker(destination.latitude, destination.longitude)
        }
    }
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            setupMap()
        }
    }
}