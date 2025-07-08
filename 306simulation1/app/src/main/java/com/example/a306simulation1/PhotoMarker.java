package com.example.a306simulation1;

import android.graphics.Bitmap;
import com.amap.api.maps2d.model.LatLng;
import java.util.Date;

public class PhotoMarker {
    private LatLng latLng;
    private Bitmap thumbnail;
    private String title;
    private String snippet;
    private Date timestamp;

    public PhotoMarker(LatLng latLng, Bitmap thumbnail) {
        this.latLng = latLng;
        this.thumbnail = thumbnail;
        this.timestamp = new Date();
        this.title = "Building Photo";
        this.snippet = "Taken on " + timestamp;
    }

    // Getters and setters
    public LatLng getLatLng() {
        return latLng;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}