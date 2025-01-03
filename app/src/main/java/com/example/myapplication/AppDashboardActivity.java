package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppDashboardActivity extends AppCompatActivity {

    private TextView appList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_dashboard);

        appList = findViewById(R.id.appList);
        databaseReference = FirebaseDatabase.getInstance().getReference("EventCheckpoints");

        ImageView closeIcon = findViewById(R.id.cancelButton);
        closeIcon.setOnClickListener(v -> finish());

        fetchAndDisplayEventNamesUsersAndCheckpoints();
    }

    private void fetchAndDisplayEventNamesUsersAndCheckpoints() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    appList.setText("No events found.");
                    return;
                }

                StringBuilder displayText = new StringBuilder();

                // Iterate through the snapshot to get the event names
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    String eventName = eventSnapshot.getKey(); // Get event name
                    Log.d("FirebaseData", "Event Name: " + eventName);

                    if (eventName != null) {
                        displayText.append("<br><br><b>").append(eventName).append("</b>").append("<br>");

                        // Iterate through the users for each event
                        for (DataSnapshot userSnapshot : eventSnapshot.getChildren()) {
                            String username = userSnapshot.getKey(); // Get username
                            if (username != null) {
                                Log.d("FirebaseData", "Username: " + username);
                                displayText.append("&emsp;<i> <br>Username:</i> ").append(username).append("<br>");

                                // Fetch checkpoint statuses
                                Boolean checkpoint1 = userSnapshot.child("checkpoint1").getValue(Boolean.class);
                                Boolean checkpoint2 = userSnapshot.child("checkpoint2").getValue(Boolean.class);
                                Boolean checkpoint3 = userSnapshot.child("checkpoint3").getValue(Boolean.class);

                                // Convert status to Completed/Pending
                                String checkpoint1Status = (checkpoint1 != null && checkpoint1) ? "Completed" : "Pending";
                                String checkpoint2Status = (checkpoint2 != null && checkpoint2) ? "Completed" : "Pending";
                                String checkpoint3Status = (checkpoint3 != null && checkpoint3) ? "Completed" : "Pending";

                                // Append checkpoint statuses to the display text
                                displayText.append("&emsp;&emsp;<i>Checkpoint 1:</i> ")
                                        .append(checkpoint1Status)
                                        .append("<br>");
                                displayText.append("&emsp;&emsp;<i>Checkpoint 2:</i> ")
                                        .append(checkpoint2Status)
                                        .append("<br>");
                                displayText.append("&emsp;&emsp;<i>Checkpoint 3:</i> ")
                                        .append(checkpoint3Status)
                                        .append("<br>");

                            }
                        }

                        displayText.append("<br>"); // Add space between events
                    }
                }

                // Set the text to the TextView with HTML formatting
                appList.setText(android.text.Html.fromHtml(displayText.toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppDashboardActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AppDashboardActivity", "Database error: " + error.getMessage());
            }
        });
    }
}
