package com.te.busco

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.provider.Settings

class MainActivity : AppCompatActivity() {

private lateinit var locationManager: LocationManager
private lateinit var wifiManager: WifiManager

override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContentView(R.layout.activity_main)

locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

val btnCreateHotspot: Button = findViewById(R.id.btnCreateHotspot)
val btnGetLocation: Button = findViewById(R.id.btnGetLocation)

btnCreateHotspot.setOnClickListener {
openNetworkSettings()
}

btnGetLocation.setOnClickListener {
if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
} else {
getLocation()
}
}
}

private fun openNetworkSettings() {
val intent = Intent(Settings.ACTION_SETTINGS)
startActivity(intent)
Log.d("MainActivity", "Abriendo configuración de red.")
}

private fun getLocation() {
Log.d("MainActivity", "Solicitando ubicación...")

val locationListener = object : LocationListener {
override fun onLocationChanged(location: Location) {
Log.d("Ubicación", "Ubicación obtenida: ${location.latitude}, ${location.longitude}")
Toast.makeText(this@MainActivity, "Ubicación: ${location.latitude}, ${location.longitude}", Toast.LENGTH_LONG).show()
}

override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
Log.d("Ubicación", "Estado cambiado: $status")
}

override fun onProviderEnabled(provider: String) {
Log.d("Ubicación", "Proveedor habilitado: $provider")
}

override fun onProviderDisabled(provider: String) {
Log.d("Ubicación", "Proveedor deshabilitado: $provider")
}
}

if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, locationListener)
Log.d("MainActivity", "Actualizaciones de ubicación solicitadas.")
} else {
Log.d("MainActivity", "Permiso de ubicación no concedido.")
}
}

override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
super.onRequestPermissionsResult(requestCode, permissions, grantResults)
if (requestCode == 1) {
if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
getLocation()
} else {
Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
}
}
}
}