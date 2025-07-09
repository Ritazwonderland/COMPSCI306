package com.example.a306_simulation2.services;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import com.example.a306_simulation2.utils.Constants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileService {
    private HandlerThread fileThread;
    private Handler fileHandler;
    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    public FileService(Context context, Handler uiHandler) {
        this.context = context;
        fileThread = new HandlerThread("FileThread");
        fileThread.start();
        fileHandler = new Handler(fileThread.getLooper());

        // Initialize file with headers
        initializeFile();
    }

    private void initializeFile() {
        fileHandler.post(() -> {
            String headers = "Date,Time,Steps,Distance(m),Duration(s),Calories,MusicType\n";
            writeToFile(headers, false); // Don't append for first write
        });
    }

    public void logExerciseData(int steps, float distance, long duration, String musicType) {
        fileHandler.post(() -> {
            Date now = new Date();
            String date = dateFormat.format(now);
            String time = timeFormat.format(now);

            // Simple calorie calculation (0.04 calories per step)
            int calories = (int)(steps * 0.04);

            String data = String.format(Locale.getDefault(),
                    "%s,%s,%d,%.2f,%d,%d,%s\n",
                    date, time, steps, distance, duration, calories, musicType);

            writeToFile(data, true);

            Log.d(Constants.TAG, "Exercise data logged: " + data.trim());
        });
    }

    private synchronized void writeToFile(String data, boolean append) {
        FileOutputStream fos = null;
        try {
            File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            File file = new File(dir, "exercise_report_" + dateFormat.format(new Date()) + ".csv");
            Log.d("FILE_PATH", "Report saved to: " + file.getAbsolutePath());
            fos = new FileOutputStream(file, append);
            fos.write(data.getBytes());
        } catch (IOException e) {
            Log.e(Constants.TAG, "File write failed: " + e.toString());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(Constants.TAG, "File close failed: " + e.toString());
                }
            }
        }
    }

    public void generateDailyReport() {
        fileHandler.post(() -> {
            try {
                File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                File reportFile = new File(dir, "daily_exercise_report_" + dateFormat.format(new Date()) + ".csv");

                if (reportFile.exists()) {
                    // Read the file and process data if needed
                    // Here we'll just log that report exists
                    Log.d(Constants.TAG, "Daily report exists at: " + reportFile.getAbsolutePath());

                    // You could add code here to:
                    // 1. Calculate totals for the day
                    // 2. Create summary statistics
                    // 3. Format a nicer report
                }
            } catch (Exception e) {
                Log.e(Constants.TAG, "Report generation failed: " + e.toString());
            }
        });
    }

    public void stop() {
        fileThread.quitSafely();
    }
}