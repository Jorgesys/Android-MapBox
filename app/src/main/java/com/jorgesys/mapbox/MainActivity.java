package com.jorgesys.mapbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;
import com.mapbox.mapboxsdk.plugins.localization.MapLocale;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final LatLngBounds GERMANY_BBOX = new LatLngBounds.Builder()
            .include(new LatLng(55.055637, 5.865639))
            .include(new LatLng(47.275776, 15.039889)).build();
   // public static final MapLocale GERMANY = new MapLocale(Locale.GERMANY, GERMANY_BBOX);
    private MapView mapView;
    private MapboxMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Using MapView requires calling Mapbox.getInstance(Context context, String accessToken) before inflating or creating the view.
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(47.151726, 27.587914)) //Iași, Romania
                        .zoom(10) // Sets the zoom
                        .bearing(180) // Rotate the camera 180 degrees
                        .tilt(30) // Set the camera tilt
                        .build(); // Creates a CameraPosition from the builder

                mapboxMap.setCameraPosition(cameraPosition);

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        LocalizationPlugin localizationPlugin = new LocalizationPlugin(mapView, mapboxMap, style);

                         //localizationPlugin.setCameraToLocaleCountry(GERMANY_BBOX);

                        try {
                            localizationPlugin.matchMapLanguageWithDeviceDefault();
                        } catch (RuntimeException exception) {
                            Log.d(TAG, exception.toString());
                        }

                    }
                });
            }
        });



    }
}