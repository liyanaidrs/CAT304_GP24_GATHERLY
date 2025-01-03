package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LineChartActivity extends AppCompatActivity {

    private LineChart lineChart;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        lineChart = findViewById(R.id.lineChart);

        // Firebase reference to "Event Created"
        databaseReference = FirebaseDatabase.getInstance().getReference("Event Created");

        ImageView closeIcon = findViewById(R.id.cancelButton);
        closeIcon.setOnClickListener(v -> finish());

        loadChartData();
    }

    private void loadChartData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // LinkedHashMap to store event counts per month while maintaining the correct order
                    Map<String, Integer> monthlyEventCounts = new LinkedHashMap<>();

                    for (int i = 1; i <= 12; i++) {
                        String monthName = new DateFormatSymbols().getMonths()[i - 1].substring(0, 3); // Jan, Feb, etc.
                        monthlyEventCounts.put(monthName, 0); // Initialize each month with 0 events
                    }

                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        String eventDate = eventSnapshot.child("dataDate").getValue(String.class);
                        if (eventDate != null && !eventDate.isEmpty()) {
                            String monthName = convertToMonthName(eventDate);
                            if (monthlyEventCounts.containsKey(monthName)) {
                                monthlyEventCounts.put(monthName, monthlyEventCounts.get(monthName) + 1);
                            }
                        }
                    }

                    // Prepare data entries and labels for the chart
                    List<Entry> entries = new ArrayList<>();
                    List<String> months = new ArrayList<>(monthlyEventCounts.keySet());
                    int index = 0;

                    for (Map.Entry<String, Integer> entry : monthlyEventCounts.entrySet()) {
                        entries.add(new Entry(index, entry.getValue())); // Add data entry
                        index++;
                    }

                    displayLineChart(entries, months);
                } else {
                    Toast.makeText(LineChartActivity.this, "No data found in the database.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(LineChartActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayLineChart(List<Entry> entries, List<String> months) {
        if (entries.isEmpty() || months.isEmpty()) {
            Toast.makeText(this, "No data available to display.", Toast.LENGTH_SHORT).show();
            return;
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Number of Events");
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setCircleColor(Color.RED);
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setValueTextSize(0f);

        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);

        // Configure X-Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter((value, axis) -> {
            int intValue = (int) value;
            if (intValue < 0 || intValue >= months.size()) return "";
            return months.get(intValue);
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(45); // Rotate labels for readability
        xAxis.setDrawGridLines(false);  // Disable grid lines for x-axis
        xAxis.setDrawAxisLine(true);    // Enable axis line for x-axis
        xAxis.setGridColor(Color.BLACK);

        // Configure Y-Axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setDrawGridLines(true); // Enable grid lines for y-axis
        leftAxis.setGridColor(Color.BLACK);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawAxisLine(true);   // Enable axis line for y-axis

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Style the chart
        lineChart.getDescription().setEnabled(false);
        lineChart.animateX(1000);
        lineChart.invalidate();
    }


    // Helper method to convert "YYYY-MM-DD" to month name
    private String convertToMonthName(String date) {
        String[] parts = date.split("-");
        if (parts.length < 2) return "";
        int monthIndex = Integer.parseInt(parts[1]) - 1; // Month is zero-based
        return new DateFormatSymbols().getMonths()[monthIndex].substring(0, 3); // Short name (e.g., Jan)
    }
}
