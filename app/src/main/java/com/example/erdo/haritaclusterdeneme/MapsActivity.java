package com.example.erdo.haritaclusterdeneme;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,ClusterManager.OnClusterClickListener<MyItem>, ClusterManager.OnClusterInfoWindowClickListener<MyItem>, ClusterManager.OnClusterItemClickListener<MyItem>, ClusterManager.OnClusterItemInfoWindowClickListener<MyItem> {

    private GoogleMap mMap;
    private ClusterManager<MyItem> clusterManager;
    private List<MyItem> items=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        //mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        clusterManager=new ClusterManager<MyItem>(MapsActivity.this,mMap);
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
        mMap.setOnInfoWindowClickListener(clusterManager);
        clusterManager.setOnClusterClickListener(this);
        clusterManager.setOnClusterItemInfoWindowClickListener(this);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").snippet("Cluster Snippet"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14f));
        MyItemCustomIcon renderer=new MyItemCustomIcon(this,mMap,clusterManager);
        clusterManager.setRenderer(renderer);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //mMap.addMarker(new MarkerOptions().position(latLng).title("Random").snippet("Random"));
                items.add(new MyItem(latLng,"Title","Snippet"));
                clusterManager.addItems(items);
                clusterManager.cluster();

            }
        });
    }

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {


        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        Toast.makeText(this,"Cluster Clicked",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MyItem> cluster) {

    }

    @Override
    public boolean onClusterItemClick(MyItem myItem) {

        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem myItem) {
        Toast.makeText(this,"Cluster Item  Info Clicked",Toast.LENGTH_SHORT).show();
    }
}
