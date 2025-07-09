package com.example.a306_simulation2.services;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.example.a306_simulation2.utils.Constants;
import com.example.a306_simulation2.utils.StepDetector;

public class SensorService implements SensorEventListener {
    private HandlerThread sensorThread;
    private Handler sensorHandler;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private StepDetector stepDetector;
    private Handler uiHandler;

    public SensorService(Context context, Handler uiHandler, StepDetector stepDetector) {
        this.uiHandler = uiHandler;
        this.stepDetector = stepDetector;

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorThread = new HandlerThread("SensorThread");
        sensorThread.start();
        sensorHandler = new Handler(sensorThread.getLooper());
    }

    public void start() {
        sensorHandler.post(() -> {
            sensorManager.registerListener(this, accelerometer, Constants.SENSOR_DELAY);
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            if (stepDetector.detectStep(values[0], values[1], values[2])) {
                // Update step count
                Message stepMsg = uiHandler.obtainMessage(1, stepDetector.getStepCount(), 0);
                uiHandler.sendMessage(stepMsg);

                // Also send update to map service
                Message mapMsg = uiHandler.obtainMessage(3, 1, 0); // x=1, y=0 for each step
                uiHandler.sendMessage(mapMsg);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void stop() {
        sensorManager.unregisterListener(this);
        sensorThread.quit();
    }
}