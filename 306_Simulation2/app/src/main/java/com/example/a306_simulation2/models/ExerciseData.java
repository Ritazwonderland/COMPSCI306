package com.example.a306_simulation2.models;

import java.util.Date;
import java.util.Locale;

public class ExerciseData {
    private String date;
    private int steps;
    private double distance; // in meters
    private long duration; // in minutes
    private String musicMode; // "Fast" or "Slow"

    // Constructor, getters, and setters
    public ExerciseData(String date, int steps, double distance, long duration, String musicMode) {
        this.date = date;
        this.steps = steps;
        this.distance = distance;
        this.duration = duration;
        this.musicMode = musicMode;
    }

    // CSV format for easy export
    public String toCSV() {
        return String.format(Locale.US, "%s,%d,%.2f,%d,%s", date, steps, distance, duration, musicMode);
    }
}