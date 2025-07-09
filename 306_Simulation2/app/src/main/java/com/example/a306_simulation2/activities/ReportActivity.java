package com.example.a306_simulation2.activities;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a306_simulation2.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        displayReport();
    }

    private void displayReport() {
        TextView reportView = findViewById(R.id.report_text);
        StringBuilder content = new StringBuilder();

        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    "exercise_report_" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".csv");

            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line).append("\n");
                }
                br.close();
            } else {
                content.append("No exercise data recorded today");
            }
        } catch (Exception e) {
            content.append("Error loading report\n").append(e.getMessage());
        }

        reportView.setText(content.toString());
    }
}