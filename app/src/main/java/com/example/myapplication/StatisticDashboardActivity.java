package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class StatisticDashboardActivity extends AppCompatActivity {

    private Button buttonPieChart;
    private Button buttonBarChart;
    private Button buttonLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_dashboard);

        // Initialize buttons
        //buttonPieChart = findViewById(R.id.buttonPieChart);
        buttonBarChart = findViewById(R.id.buttonBarChart);
        buttonLineChart = findViewById(R.id.buttonLineChart);

//        // Set onClickListeners for buttons
//        buttonPieChart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Start PieChartActivity
//                Intent intent = new Intent(StatisticDashboardActivity.this, PieChartActivity.class);
//                startActivity(intent);
//            }
//        });

        buttonBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start BarChartActivity
                Intent intent = new Intent(StatisticDashboardActivity.this, BarChartActivity.class);
                startActivity(intent);
            }
        });

        buttonLineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start RadarChartActivity
                Intent intent = new Intent(StatisticDashboardActivity.this, LineChartActivity.class);
                startActivity(intent);
            }
        });

        ImageView closeIcon = findViewById(R.id.cancelButton);
        closeIcon.setOnClickListener(v -> finish());
    }
}
