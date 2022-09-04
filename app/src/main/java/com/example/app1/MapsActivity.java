package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.app1.databinding.ActivityMapsBinding;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Alta_Tension = new LatLng(7.141861155784611, -73.12220172104759);
        mMap.addMarker(new MarkerOptions().position(Alta_Tension).title("Alta Tension").snippet("LAT").icon(BitmapDescriptorFactory.fromResource(R.drawable.altatension)));

        LatLng Biblioteca = new LatLng(7.140995, -73.120896);
        mMap.addMarker(new MarkerOptions().position(Biblioteca).title("Biblioteca").icon(BitmapDescriptorFactory.fromResource(R.drawable.biblioteca)));

        LatLng CamiloTorres = new LatLng(7.140019, -73.120896);
        mMap.addMarker(new MarkerOptions().position(CamiloTorres).title("Camilo Torres").icon(BitmapDescriptorFactory.fromResource(R.drawable.camilotorres)));

        LatLng Centic = new LatLng(7.140763471889684, -73.12154839115854);
        mMap.addMarker(new MarkerOptions().position(Centic).title("Centic").icon(BitmapDescriptorFactory.fromResource(R.drawable.centic)));

        LatLng DisIndus = new LatLng(7.141493 , -73.121287);
        mMap.addMarker(new MarkerOptions().position(DisIndus).title("Diseño Industrial").icon(BitmapDescriptorFactory.fromResource(R.drawable.diseno)));

        LatLng E3T = new LatLng(7.142059434467157, -73.12125049953123);
        mMap.addMarker(new MarkerOptions().position(E3T).title("E3T").icon(BitmapDescriptorFactory.fromResource(R.drawable.e3t)));

        LatLng Entrada = new LatLng(7.138825716032372, -73.12028608701051);
        mMap.addMarker(new MarkerOptions().position(Entrada).title("Entrada 27 UIS").icon(BitmapDescriptorFactory.fromResource(R.drawable.entrada)));

        LatLng Humanas = new LatLng(7.139145991450505, -73.12122273351629);
        mMap.addMarker(new MarkerOptions().position(Humanas).title("Edificio Humanas").icon(BitmapDescriptorFactory.fromResource(R.drawable.humanas)));

        LatLng Lenguas = new LatLng(7.141427606086793, -73.12076723596894);
        mMap.addMarker(new MarkerOptions().position(Lenguas).title("Edificio de lenguas").icon(BitmapDescriptorFactory.fromResource(R.drawable.lenguas)));

        LatLng PistAtlet = new LatLng(7.140834203501612, -73.11848326644137);
        mMap.addMarker(new MarkerOptions().position(PistAtlet).title("Pista Atletica UIS").icon(BitmapDescriptorFactory.fromResource(R.drawable.pistaatletica)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Biblioteca,16));
    }
}