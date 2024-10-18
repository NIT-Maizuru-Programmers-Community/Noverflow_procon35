package com.maizuruProcon.Noverflow

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.FirebaseFirestore
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class FourActivity : AppCompatActivity(){
    private lateinit var mapView: MapView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
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
        mapView.setMultiTouchControls(true)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        } else {
            setupMap()
        }

        val btnTest: Button = findViewById(R.id.btnTest)
        // 3) 戻るボタン(アクティビティの終了)
        btnTest.setOnClickListener {
            finish()
        }
    }

    private fun setupMap() {
        val mapSetupController = MapSetupController(this, mapView)
        mapSetupController.setupMapWithLocation { currentLocation ->

            readAllLatLonFromCollection(
                onSuccess = { destinations, infos ->

                    // 取得したdestinationsリストを使って最寄りの場所を検索
                    val nearestDestination = destinations.zip(infos)
                        .filter { it.second < 500 }
                        .minByOrNull { currentLocation.distanceToAsDouble(it.first) }
                        ?.first

                    if (nearestDestination != null) {
                        // 経路を取得して描画
                        val routeFetcher = RouteFetcher()
                        routeFetcher.fetchRoute(currentLocation, nearestDestination) { routePoints ->
                            val routeController = RouteController(mapView)
                            routeController.drawRoute(routePoints)
                        }
                    }

                    // 取得した緯度経度にマーカーを配置
                    val mapMarker = MapMarker(mapView)
                    destinations.forEachIndexed { index, destination ->
                        mapMarker.placeMarker(destination.latitude, destination.longitude, infos[index])
                    }
                },
                onFailure = { exception ->
                    Log.e("Firestore", "Error fetching lat/lon: ", exception)
                }
            )
        }
    }

    private fun readAllLatLonFromCollection(onSuccess: (List<GeoPoint>, List<Int>) -> Unit, onFailure: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("garbageBoxes")  // コレクション名を指定

        collectionRef.get()
            .addOnSuccessListener { result ->
                val geoPoints = mutableListOf<GeoPoint>()
                val infos = mutableListOf<Int>()

                for (document in result) {
                    val latitude = document.getDouble("location.latitude")
                    val longitude = document.getDouble("location.longitude")

                    // trashフィールドを取得し、型をチェックする
                    val trashValue: Any? = document.get("trash") // Any型で取得

                    val trashInt = when (trashValue) {
                        is Number -> trashValue.toInt()  // Number型の場合
                        is String -> trashValue.toIntOrNull() ?: 0 // String型であれば変換（失敗時は0）
                        else -> 0  // その他の場合は0を設定
                    }

                    if (latitude != null && longitude != null) {
                        geoPoints.add(GeoPoint(latitude, longitude))
                        infos.add(trashInt)  // infoリストに追加
                    }
                }

                onSuccess(geoPoints, infos)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
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
