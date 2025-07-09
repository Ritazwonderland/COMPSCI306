package com.example.a306_simulation2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a306_simulation2.R;
import com.example.a306_simulation2.services.*;
import com.example.a306_simulation2.utils.StepDetector;
import com.example.a306_simulation2.views.MockMapView;

public class MainActivity extends AppCompatActivity {
    private TextView stepCountView, distanceView;
    private MockMapView mockMapView;
    private Button btnPlay, btnPause, btnStop;
    private TextView tvStatus;

    // Services
    private SensorService sensorService;
    private MapService mapService;
    private FileService fileService;
    private MusicService musicService;

    private StepDetector stepDetector = new StepDetector();
    private long exerciseStartTime;
    private String currentMusicType = "Normal";

    private Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: // Update step count
                    stepCountView.setText(getString(R.string.steps_label, msg.arg1));
                    break;
                case 2: // Update distance
                    distanceView.setText(getString(R.string.distance_label, msg.arg1));
                    break;
                case 3: // Update map
                    mockMapView.updatePosition(msg.arg1, msg.arg2);
                    updateStatus(msg.arg1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        stepCountView = findViewById(R.id.step_count);
        distanceView = findViewById(R.id.distance);
        mockMapView = findViewById(R.id.mock_map);
        btnPlay = findViewById(R.id.btn_play);
        btnPause = findViewById(R.id.btn_pause);
        btnStop = findViewById(R.id.btn_stop);
        tvStatus = findViewById(R.id.tv_status);

        // Initialize services
        sensorService = new SensorService(this, uiHandler, stepDetector);
        mapService = new MapService(uiHandler);
        fileService = new FileService(this, uiHandler);
        musicService = new MusicService(this);

        // Start services
        sensorService.start();
        mapService.start();

        exerciseStartTime = System.currentTimeMillis();

        // Set button listeners
        btnPlay.setOnClickListener(v -> {
            musicService.start();
            exerciseStartTime = System.currentTimeMillis(); // Reset timer when starting
        });

        btnPause.setOnClickListener(v -> {
            musicService.pause();
            logExerciseData(); // Log data when pausing
        });

        btnStop.setOnClickListener(v -> {
            musicService.stop();
            logExerciseData(); // Log data when stopping
            fileService.generateDailyReport();
        });
        Button btnViewReport = findViewById(R.id.btn_view_report);
        btnViewReport.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ReportActivity.class));
        });
    }

    private void updateStatus(int stepRate) {
        if (stepRate > 120) {
            tvStatus.setText(R.string.status_running);
            currentMusicType = "Fast";
        } else if (stepRate > 0) {
            tvStatus.setText(R.string.status_walking);
            currentMusicType = "Normal";
        } else {
            tvStatus.setText(R.string.status_ready);
            currentMusicType = "None";
        }
    }

    private float calculateDistance(int steps) {
        return steps * 0.762f; // 0.762 meters per step (30 inches)
    }

    private void logExerciseData() {
        long duration = (System.currentTimeMillis() - exerciseStartTime) / 1000;
        fileService.logExerciseData(
                stepDetector.getStepCount(),
                calculateDistance(stepDetector.getStepCount()),
                duration,
                currentMusicType
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up services
        sensorService.stop();
        mapService.stop();
        fileService.stop();
        musicService.stop();
    }


}