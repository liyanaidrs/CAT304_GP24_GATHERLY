package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartActivity extends AppCompatActivity {

    private PieChart pieChart;
    private DatabaseReference userEventApplicationRef, eventCreatedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        pieChart = findViewById(R.id.pieChart);

        // Firebase references
        userEventApplicationRef = FirebaseDatabase.getInstance().getReference("UserEventApplication");
        eventCreatedRef = FirebaseDatabase.getInstance().getReference("Event Created");

        loadParticipantData();

        // Close the activity on close icon click
        ImageView closeIcon = findViewById(R.id.cancelButton);
        closeIcon.setOnClickListener(v -> finish());
    }

    private void loadParticipantData() {
        userEventApplicationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userEventSnapshot) {
                if (userEventSnapshot.exists()) {
                    Map<String, Integer> eventParticipantCounts = new HashMap<>();

                    // Step 1: Count participants for each key (event ID)
                    for (DataSnapshot keySnapshot : userEventSnapshot.getChildren()) {
                        String eventId = keySnapshot.getKey(); // The key is the event ID
                        if (eventId != null) {
                            int participantCount = (int) keySnapshot.getChildrenCount(); // Count usernames under each key
                            eventParticipantCounts.put(eventId, participantCount);
                        }
                    }

                    // Step 2: Fetch event names from the Event Created collection
                    fetchEventNames(eventParticipantCounts);
                } else {
                    Toast.makeText(PieChartActivity.this, "No participant data found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(PieChartActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchEventNames(Map<String, Integer> eventParticipantCounts) {
        eventCreatedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot eventSnapshot) {
                if (eventSnapshot.exists()) {
                    Map<String, String> eventIdToNameMap = new HashMap<>();
                    List<PieEntry> entries = new ArrayList<>();
                    List<Integer> colors = new ArrayList<>();

                    // Define custom colors for the pie chart
                    int[] customColors = {
                            Color.parseColor("#FFB6C1"), // Light Pink
                            Color.parseColor("#FFD700"), // Gold
                            Color.parseColor("#ADD8E6"), // Light Blue
                            Color.parseColor("#FF6347"), // Tomato
                            Color.parseColor("#2E8B57"), // Sea Green
                            Color.parseColor("#B0E0E6"), // Powder Blue
                            Color.parseColor("#F0E68C"), // Khaki
                            Color.parseColor("#4F7942"), // Fern Green
                            Color.parseColor("#D3D3D3"), // Light Grey
                            Color.parseColor("#FF8C00"), // Dark Orange
                            Color.parseColor("#E6E6FA"), // Lavender
                    };

                    int colorIndex = 0;

                    // Map event IDs to event names
                    for (DataSnapshot eventSnapshotChild : eventSnapshot.getChildren()) {
                        String eventId = eventSnapshotChild.getKey();
                        String eventName = eventSnapshotChild.child("dataTitle").getValue(String.class);

                        if (eventId != null && eventName != null) {
                            eventIdToNameMap.put(eventId, eventName);
                        }
                    }

                    // Prepare Pie Chart Entries
                    for (Map.Entry<String, Integer> entry : eventParticipantCounts.entrySet()) {
                        String eventId = entry.getKey();
                        int participantCount = entry.getValue();
                        String eventName = eventIdToNameMap.get(eventId);

                        if (eventName != null) {
                            entries.add(new PieEntry(participantCount, eventName));
                            colors.add(customColors[colorIndex % customColors.length]);
                            colorIndex++;
                        }
                    }

                    // Set up Pie Chart
                    PieDataSet dataSet = new PieDataSet(entries, "Event Participants");
                    dataSet.setColors(colors); // Set custom colors

                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.BLACK);

                    pieChart.setDrawEntryLabels(false);
                    pieChart.setData(data);
                    pieChart.invalidate(); // Refresh chart
                    pieChart.setDrawHoleEnabled(false);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.getLegend().setEnabled(false); // Disable the default legend

                    // Display a custom legend
                    displayEventLegend(new ArrayList<>(eventIdToNameMap.values()), colors);
                } else {
                    Toast.makeText(PieChartActivity.this, "No event data found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(PieChartActivity.this, "Failed to fetch event names: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayEventLegend(List<String> eventNames, List<Integer> colors) {
        LinearLayout eventLegendLayout = findViewById(R.id.organizerLegendLayout);
        eventLegendLayout.removeAllViews();

        for (int i = 0; i < eventNames.size(); i++) {
            LinearLayout legendItem = new LinearLayout(this);
            legendItem.setOrientation(LinearLayout.HORIZONTAL);
            legendItem.setPadding(8, 0, 8, 8);

            ImageView colorBox = new ImageView(this);
            LinearLayout.LayoutParams colorParams = new LinearLayout.LayoutParams(20, 20);
            colorBox.setLayoutParams(colorParams);
            colorBox.setBackgroundColor(colors.get(i));

            TextView eventNameText = new TextView(this);
            eventNameText.setText(eventNames.get(i));
            eventNameText.setPadding(16, 0, 0, 0);
            eventNameText.setTextColor(Color.BLACK);

            legendItem.addView(colorBox);
            legendItem.addView(eventNameText);
            eventLegendLayout.addView(legendItem);
        }
    }
}
