package com.example.a306_simulation2.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Locale;

public class MockMapView extends View {
    private Paint gridPaint = new Paint();
    private Paint pathPaint = new Paint();
    private Path exercisePath = new Path();
    private float currentX = 50, currentY = 50;

    private Paint currentPositionPaint = new Paint();

    public MockMapView(Context context, AttributeSet attrs) {
        super(context, attrs);

        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1);

        pathPaint.setColor(Color.BLUE);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(5);

        currentPositionPaint.setColor(Color.RED);
        currentPositionPaint.setStyle(Paint.Style.FILL);
        currentPositionPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw grid
        int width = getWidth();
        int height = getHeight();
        int gridSize = 50;

        for (int x = 0; x < width; x += gridSize) {
            canvas.drawLine(x, 0, x, height, gridPaint);
        }
        for (int y = 0; y < height; y += gridSize) {
            canvas.drawLine(0, y, width, y, gridPaint);
        }

        // Draw path
        canvas.drawPath(exercisePath, pathPaint);

        // Draw current position with larger, more visible dot
        canvas.drawCircle(currentX, currentY, 15, currentPositionPaint); // Increased radius

        // Draw coordinates text
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(24);
        canvas.drawText(String.format(Locale.getDefault(), "X: %.1f", currentX), 20, 30, textPaint);
        canvas.drawText(String.format(Locale.getDefault(), "Y: %.1f", currentY), 20, 60, textPaint);
    }

    public void updatePosition(float x, float y) {
        float movementScale = 8f; // Increased movement scale
        currentX += x * movementScale;
        currentY += y * movementScale;

        // Keep within bounds with margin
        float margin = 30f;
        currentX = Math.max(margin, Math.min(getWidth() - margin, currentX));
        currentY = Math.max(margin, Math.min(getHeight() - margin, currentY));

        if (exercisePath.isEmpty()) {
            exercisePath.moveTo(currentX, currentY);
        } else {
            exercisePath.lineTo(currentX, currentY);
        }

        // Force redraw
        postInvalidate();

        Log.d("MockMapView", String.format("Position updated to: %.1f, %.1f", currentX, currentY));
    }
}