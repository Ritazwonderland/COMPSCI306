package com.example.a306_simulation2.services;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import com.example.a306_simulation2.utils.Constants;

public class MapService {
    private HandlerThread mapThread;
    private Handler mapHandler;
    private Handler uiHandler;
    private int stepCount = 0;

    public MapService(Handler uiHandler) {
        this.uiHandler = uiHandler;
        mapThread = new HandlerThread("MapThread");
        mapThread.start();
        mapHandler = new Handler(mapThread.getLooper());
    }

    public void start() {
        mapHandler.postDelayed(mapUpdater, 1000);
    }

    private Runnable mapUpdater = new Runnable() {
        @Override
        public void run() {
            // Simulate movement based on steps
            int xMovement = stepCount*10;
            int yMovement = stepCount*10;

            Message msg = uiHandler.obtainMessage(3, xMovement, yMovement);
            uiHandler.sendMessage(msg);

            mapHandler.postDelayed(this, 1000);
        }
    };

    public void updateStepCount(int steps) {
        this.stepCount = steps;
    }

    public void stop() {
        mapThread.quit();
    }
}