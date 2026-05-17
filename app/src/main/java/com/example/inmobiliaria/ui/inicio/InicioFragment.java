package com.example.inmobiliaria.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.inmobiliaria.databinding.FragmentInicioBinding;

import org.maplibre.android.MapLibre;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.camera.CameraUpdateFactory;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.OnMapReadyCallback;
import org.maplibre.android.maps.Style;
import org.maplibre.android.annotations.MarkerOptions;

public class InicioFragment extends Fragment implements OnMapReadyCallback {

    private FragmentInicioBinding binding;
    private MapLibreMap mapaLibre;

    public InicioFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapLibre.getInstance(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.mapa.onCreate(savedInstanceState);
        binding.mapa.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull MapLibreMap mapLibreMap) {
        this.mapaLibre = mapLibreMap;

        // Capa Satelital (Gratis y sin API Key)
        String jsonSatelite = "{" +
                "\"version\": 8," +
                "\"sources\": {" +
                "  \"esri-satelite\": {" +
                "    \"type\": \"raster\"," +
                "    \"tiles\": [\"https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}\"]," +
                "    \"tileSize\": 256" +
                "  }" +
                "}," +
                "\"layers\": [{" +
                "  \"id\": \"satelite\"," +
                "  \"type\": \"raster\"," +
                "  \"source\": \"esri-satelite\"" +
                "}]" +
                "}";

        mapaLibre.setStyle(new Style.Builder().fromJson(jsonSatelite), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                LatLng ulp = new LatLng(-33.1507, -66.3068);

                mapaLibre.addMarker(new MarkerOptions()
                        .position(ulp)
                        .title("Inmobiliaria - Sede ULP"));

                CameraPosition posicion = new CameraPosition.Builder()
                        .target(ulp)
                        .zoom(16.0)
                        .build();

                mapaLibre.animateCamera(CameraUpdateFactory.newCameraPosition(posicion), 2000);
            }
        });
    }

    @Override
    public void onStart() { super.onStart(); if (binding != null) binding.mapa.onStart(); }
    @Override
    public void onResume() { super.onResume(); if (binding != null) binding.mapa.onResume(); }
    @Override
    public void onPause() { super.onPause(); if (binding != null) binding.mapa.onPause(); }
    @Override
    public void onStop() { super.onStop(); if (binding != null) binding.mapa.onStop(); }
    @Override
    public void onLowMemory() { super.onLowMemory(); if (binding != null) binding.mapa.onLowMemory(); }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binding != null) {
            binding.mapa.onDestroy();
            binding = null;
        }
    }
}