package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDashboardActivity extends AppCompatActivity {

    private TextView totalUserTextView, usernameTextView;
    private SearchView searchView;
    private DatabaseReference databaseReference;
    private ImageView cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        // Initialize views
        totalUserTextView = findViewById(R.id.totaluser);
        usernameTextView = findViewById(R.id.username);
        searchView = findViewById(R.id.searchView);
        cancelButton = findViewById(R.id.cancelButton);
        // Reference to the Firebase "Users" node
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Fetch all user data initially
        fetchUsers();

        // Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUser(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // Reload all user data when the search query is cleared
                    fetchUsers();
                } else {
                    searchUser(newText);
                }
                return true;
            }
        });
        cancelButton.setOnClickListener(v -> finish());
    }

    private void fetchUsers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalUsers = 0;
                StringBuilder usernames = new StringBuilder();

                // Iterate through each user in the "Users" node
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String username = snapshot.child("username").getValue(String.class);
                    if (username != null) {
                        usernames.append(username).append("\n");
                        totalUsers++;
                    }
                }

                // Update UI with total users and list of usernames
                totalUserTextView.setText("Total Users: " + totalUsers);
                usernameTextView.setText(usernames.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(UserDashboardActivity.this, "Failed to fetch data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void searchUser(String query) {
        databaseReference.orderByChild("username").equalTo(query).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    StringBuilder result = new StringBuilder();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String username = snapshot.child("username").getValue(String.class);
                        if (username != null) {
                            result.append("Username: ").append(username).append("\n");
                        }
                    }
                    usernameTextView.setText(result.toString());
                } else {
                    Toast.makeText(UserDashboardActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                    usernameTextView.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserDashboardActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}