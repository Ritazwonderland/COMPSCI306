package com.example.a306_simulation2.utils;

import com.example.a306_simulation2.utils.Constants;

public class StepDetector {
    private int stepCount = 0;
    private float lastZ = 0;
    private boolean wasHigh = false;

    public boolean detectStep(float x, float y, float z) {
        float magnitude = (float) Math.sqrt(x*x + y*y + z*z);

        if (magnitude > Constants.STEP_THRESHOLD && !wasHigh) {
            wasHigh = true;
            stepCount++;
            return true;
        } else if (magnitude < Constants.STEP_THRESHOLD) {
            wasHigh = false;
        }
        return false;
    }

    public int getStepCount() {
        return stepCount;
    }
}