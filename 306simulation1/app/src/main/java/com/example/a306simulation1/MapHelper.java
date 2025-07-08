package com.example.a306simulation1;

import android.content.Context;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.AMap;

import android.graphics.Bitmap;

public class MapHelper {
    private AMap aMap;
    private Context context;

    public MapHelper(AMap aMap, Context context) {
        this.aMap = aMap;
        this.context = context;
    }

    /**
     * Adds a marker to the map with a photo thumbnail
     * @param latLng The location of the marker
     * @param thumbnail The photo thumbnail to display
     * @param title The title of the marker
     * @param snippet Additional information about the marker
     * @return The created Marker object
     */
    public Marker addPhotoMarker(LatLng latLng, Bitmap thumbnail, String title, String snippet) {
        if (aMap == null) return null;

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromBitmap(thumbnail));

        return aMap.addMarker(markerOptions);
    }

    /**
     * Centers the map on a specific location with zoom level
     * @param latLng The location to center on
     * @param zoomLevel The zoom level (2-20)
     */
    public void centerMapOnLocation(LatLng latLng, float zoomLevel) {
        if (aMap != null) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        }
    }

    /**
     * Updates an existing marker with new information
     * @param marker The marker to update
     * @param newTitle The new title
     * @param newSnippet The new snippet
     */
    public void updateMarkerInfo(Marker marker, String newTitle, String newSnippet) {
        if (marker != null) {
            marker.setTitle(newTitle);
            marker.setSnippet(newSnippet);
            marker.showInfoWindow(); // Refresh the info window
        }
    }

    /**
     * Clears all markers from the map
     */
    public void clearAllMarkers() {
        if (aMap != null) {
            aMap.clear();
        }
    }

    /**
     * Enables/disables the current location layer
     * @param enabled Whether to enable the location layer
     */
    public void setMyLocationEnabled(boolean enabled) {
        if (aMap != null) {
            aMap.setMyLocationEnabled(enabled);
        }
    }
}