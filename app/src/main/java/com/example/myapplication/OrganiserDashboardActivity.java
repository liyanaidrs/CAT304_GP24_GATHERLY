package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrganiserDashboardActivity extends AppCompatActivity {

    private TextView organiserTextView, totalOrganiserTextView;
    private SearchView searchView;
    private DatabaseReference databaseReference;
    private List<String> organiserList = new ArrayList<>();
    private ImageView cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_dashboard);

        organiserTextView = findViewById(R.id.organiser);
        totalOrganiserTextView = findViewById(R.id.totalorganiser);
        searchView = findViewById(R.id.searchView);
        cancelButton = findViewById(R.id.cancelButton);
        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Event Created");

        loadOrganiserData();

        // Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchOrganiser(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // Reload all user data when the search query is cleared
                    loadOrganiserData();
                } else {
                    searchOrganiser(newText);
                }
                return true;
            }});

        cancelButton.setOnClickListener(v -> finish());
    }

    private void loadOrganiserData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Set<String> organisers = new HashSet<>();
                    StringBuilder organiserListBuilder = new StringBuilder();

                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        String organiser = eventSnapshot.child("dataLang").getValue(String.class);
                        if (organiser != null && !organiser.isEmpty()) {
                            organisers.add(organiser);
                        }
                    }

                    // Convert to list for search
                    organiserList.clear();
                    organiserList.addAll(organisers);

                    // Display organisers
                    for (String organiser : organisers) {
                        organiserListBuilder.append(organiser).append("\n");
                    }
                    organiserTextView.setText(organiserListBuilder.toString().trim());

                    // Display total organisers
                    totalOrganiserTextView.setText("Total Organisers: " + organisers.size());
                } else {
                    Toast.makeText(OrganiserDashboardActivity.this, "No organisers found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(OrganiserDashboardActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchOrganiser(String query) {
        if (organiserList.contains(query)) {
            organiserTextView.setText("Found Organiser: " + query);
        } else {
            Toast.makeText(this, "Organiser not found.", Toast.LENGTH_SHORT).show();
        }
    }
}