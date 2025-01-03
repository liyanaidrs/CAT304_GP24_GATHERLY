package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarChartActivity extends AppCompatActivity {

    private BarChart barChart;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        barChart = findViewById(R.id.barChart);

        // Firebase reference to "UserCompleted" node
        databaseReference = FirebaseDatabase.getInstance().getReference("UserCompleted");

        ImageView closeIcon = findViewById(R.id.cancelButton);
        closeIcon.setOnClickListener(v -> finish());

        // Load and display chart data
        loadChartData();
    }

    private void loadChartData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Map to store event counts for each user
                    Map<String, Integer> userEventCounts = new HashMap<>();

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String username = userSnapshot.getKey();
                        int eventCount = (int) userSnapshot.getChildrenCount();
                        // Multiply by 5 to represent 5 stars per completed event
                        int stars = eventCount * 5;
                        userEventCounts.put(username, stars);
                    }

                    // Prepare data entries and labels for the chart
                    List<BarEntry> entries = new ArrayList<>();
                    List<String> labels = new ArrayList<>();
                    int index = 0;

                    for (Map.Entry<String, Integer> entry : userEventCounts.entrySet()) {
                        entries.add(new BarEntry(index, entry.getValue()));
                        labels.add(entry.getKey());
                        index++;
                    }

                    // Display chart with the prepared data
                    displayBarChart(entries, labels);
                } else {
                    Toast.makeText(BarChartActivity.this, "No data found in the database.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(BarChartActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayBarChart(List<BarEntry> entries, List<String> labels) {
        if (entries.isEmpty() || labels.isEmpty()) {
            Toast.makeText(this, "No data available to display.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a dataset with the entries
        BarDataSet barDataSet = new BarDataSet(entries, "Performance (Stars per User)");
        barDataSet.setColor(ContextCompat.getColor(this, R.color.emeraldgreen));
        //barDataSet.setValueTextColor(ContextCompat.getColor(this, R.color.purple_200));
        barDataSet.setValueTextSize(0f);

        // Prepare the BarData
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.8f); // Set bar width

        // Configure and style the chart
        configureBarChart(labels);

        // Set data and refresh the chart
        barChart.setData(barData);
        barChart.invalidate(); // Refresh chart view
    }

    private void configureBarChart(List<String> labels) {
        // Disable description text
        barChart.getDescription().setEnabled(false);

        // Enable bar fitting to the chart
        barChart.setFitBars(true);

        // Configure the X-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); // Display usernames on X-axis
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextColor(ContextCompat.getColor(this, R.color.black));
        xAxis.setTextSize(12f);

        // Optionally, adjust the angle of X-axis labels for better readability
        xAxis.setLabelRotationAngle(45);  // Rotating labels for better visibility

        // Configure the left Y-axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextColor(ContextCompat.getColor(this, R.color.black));
        leftAxis.setTextSize(12f);

        // Set Y-axis value formatter to display integers only
        leftAxis.setValueFormatter((value, axis) -> String.valueOf((int) value));

        // Optionally, set the Y-axis range
        leftAxis.setAxisMinimum(0); // Start from 0
        leftAxis.setGranularity(5f); // Set steps of 5 stars for clarity

        // Disable the right Y-axis
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Add animation for better visual appeal
        barChart.animateY(1000);
        barChart.animateX(1000);

        // Disable legend (optional, can be customized)
        barChart.getLegend().setEnabled(false);
    }

}
