package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;



import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends AppCompatActivity {

    private PieChart pieChart;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        pieChart = findViewById(R.id.pieChart);

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Event Created");

        loadEventOrganiserData();

        // Close the activity on close icon click
        ImageView closeIcon = findViewById(R.id.cancelButton);
        closeIcon.setOnClickListener(v -> finish());
    }

    private void loadEventOrganiserData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
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
                    // To hold the organizer names and colors for display under the chart
                    List<String> organizerNames = new ArrayList<>();

                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        String organiser = eventSnapshot.child("dataLang").getValue(String.class);

                        if (organiser != null && !organiser.isEmpty()) {
                            boolean found = false;
                            for (PieEntry entry : entries) {
                                if (entry.getLabel().equals(organiser)) {
                                    // Increment value
                                    entry.setY(entry.getValue() + 1);
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                entries.add(new PieEntry(1, organiser)); // Add new organiser
                                // Use custom color from the color palette
                                colors.add(customColors[colorIndex % customColors.length]);
                                organizerNames.add(organiser); // Store organizer name for legend
                                colorIndex++;
                            }
                        }
                    }

                    PieDataSet dataSet = new PieDataSet(entries, "Event Organisers");
                    dataSet.setColors(colors); // Set the custom colors

                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.BLACK);

                    pieChart.setDrawEntryLabels(false);
                    pieChart.setData(data);
                    pieChart.invalidate(); // Refresh chart
                    pieChart.setDrawHoleEnabled(false);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.getLegend().setEnabled(false); // Disable the default legend


                    // Customizing the legend under the pie chart
                    displayOrganizerLegend(organizerNames, colors);

                    pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, Highlight h) {
                            String selectedLabel = ((PieEntry) e).getLabel();
                            updateLegendStyle(selectedLabel, organizerNames, colors);
                        }

                        @Override
                        public void onNothingSelected() {
                            resetLegendStyle(organizerNames, colors);
                        }
                    });

                } else {
                    Toast.makeText(PieChartActivity.this, "No data found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(PieChartActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLegendStyle(String selectedLabel, List<String> organizerNames, List<Integer> colors) {
        LinearLayout organizerLegendLayout = findViewById(R.id.organizerLegendLayout);
        organizerLegendLayout.removeAllViews();

        for (int i = 0; i < organizerNames.size(); i++) {
            LinearLayout legendItem = new LinearLayout(this);
            legendItem.setOrientation(LinearLayout.HORIZONTAL);
            legendItem.setPadding(8, 0, 8, 8);
            legendItem.setGravity(Gravity.CENTER_VERTICAL);

            ImageView colorBox = new ImageView(this);
            LinearLayout.LayoutParams colorParams = new LinearLayout.LayoutParams(20, 20);
            colorBox.setLayoutParams(colorParams);
            colorBox.setBackgroundColor(colors.get(i));

            TextView organizerNameText = new TextView(this);
            organizerNameText.setText(organizerNames.get(i));
            organizerNameText.setPadding(16, 0, 0, 0);
            organizerNameText.setTextColor(Color.BLACK);

            // Bold the selected legend
            if (organizerNames.get(i).equals(selectedLabel)) {
                organizerNameText.setTypeface(null, android.graphics.Typeface.BOLD);
                colorBox.setScaleX(1.2f); // Slightly enlarge color box
                colorBox.setScaleY(1.2f);
            }

            legendItem.addView(colorBox);
            legendItem.addView(organizerNameText);
            organizerLegendLayout.addView(legendItem);
        }
    }

    private void resetLegendStyle(List<String> organizerNames, List<Integer> colors) {
        displayOrganizerLegend(organizerNames, colors);
    }

    private void displayOrganizerLegend(List<String> organizerNames, List<Integer> colors) {
        // Reference to the layout where we will add the organizers and their colors
        LinearLayout organizerLegendLayout = findViewById(R.id.organizerLegendLayout);

        // Clear previous content in the layout
        organizerLegendLayout.removeAllViews();

        // Loop through organizers and add each one to the layout with color
        for (int i = 0; i < organizerNames.size(); i++) {
            // Create a new LinearLayout to hold the color and name
            LinearLayout legendItem = new LinearLayout(this);
            legendItem.setOrientation(LinearLayout.HORIZONTAL);
            legendItem.setPadding(8, 0, 8, 8);
            legendItem.setGravity(Gravity.CENTER_VERTICAL);  // Ensures vertical alignment of both color and name

            // Create a colored box for the legend
            ImageView colorBox = new ImageView(this);
            LinearLayout.LayoutParams colorParams = new LinearLayout.LayoutParams(20, 20);
            colorBox.setLayoutParams(colorParams);
            colorBox.setBackgroundColor(colors.get(i));

            // Create a TextView to display the organizer name
            TextView organizerNameText = new TextView(this);
            organizerNameText.setText(organizerNames.get(i));
            organizerNameText.setPadding(16, 0, 0, 0);  // Adjust padding to add space between color and name
            organizerNameText.setTextColor(Color.BLACK);

            // Set vertical alignment for the text (shift it up a little)
            organizerNameText.setGravity(Gravity.CENTER_VERTICAL);  // Ensure text is vertically centered to the color box

            // Add the color box and name to the legend item layout
            legendItem.addView(colorBox);
            legendItem.addView(organizerNameText);

            // Add the legend item to the main legend layout
            organizerLegendLayout.addView(legendItem);
        }
    }



}


