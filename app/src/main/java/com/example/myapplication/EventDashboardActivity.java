package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Html;
import android.text.Spanned;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EventDashboardActivity extends AppCompatActivity {

    private TextView eventListTextView;
    private SearchView searchView;
    private DatabaseReference databaseReference;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
    private List<Event> allEvents = new ArrayList<>();
    private List<String> monthOrder = new ArrayList<>();
    private ImageView cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_dashboard);

        eventListTextView = findViewById(R.id.eventList);
        searchView = findViewById(R.id.searchView);
        cancelButton = findViewById(R.id.cancelButton);
        monthOrder.add("January");
        monthOrder.add("February");
        monthOrder.add("March");
        monthOrder.add("April");
        monthOrder.add("May");
        monthOrder.add("June");
        monthOrder.add("July");
        monthOrder.add("August");
        monthOrder.add("September");
        monthOrder.add("October");
        monthOrder.add("November");
        monthOrder.add("December");

        databaseReference = FirebaseDatabase.getInstance().getReference("Event Created");

        loadAndDisplayEvents();

        // Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchEvent(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // Reload all events if search query is cleared
                    displayEventsByMonth(allEvents);
                } else {
                    searchEvent(newText);
                }
                return true;
            }
        });

        cancelButton.setOnClickListener(v -> finish());
    }

    private void loadAndDisplayEvents() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    allEvents.clear();
                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        String title = eventSnapshot.child("dataTitle").getValue(String.class);
                        String description = eventSnapshot.child("dataDesc").getValue(String.class);
                        String date = eventSnapshot.child("dataDate").getValue(String.class);
                        String organiser = eventSnapshot.child("dataLang").getValue(String.class);
                        String participant = eventSnapshot.child("dataParticipant").getValue(String.class);

                        if (title != null && date != null && description != null && organiser != null && participant != null) {
                            allEvents.add(new Event(title, description, date, organiser, participant));
                        }
                    }

                    displayEventsByMonth(allEvents);
                } else {
                    Toast.makeText(EventDashboardActivity.this, "No events found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EventDashboardActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayEventsByMonth(List<Event> events) {
        Map<String, List<Event>> eventsByMonth = new HashMap<>();
        for (Event event : events) {
            try {
                Date eventDate = dateFormat.parse(event.getDate());
                String month = monthFormat.format(eventDate);
                eventsByMonth.putIfAbsent(month, new ArrayList<>());
                eventsByMonth.get(month).add(event);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        StringBuilder displayText = new StringBuilder();
        for (String month : monthOrder) {
            if (eventsByMonth.containsKey(month)) {
                // Making the month bold
                displayText.append("<b><br><br>").append(month).append("</b>").append("<br><br>");

                for (Event event : eventsByMonth.get(month)) {
                    // Making the title, date, and description bold
                    displayText.append("  <b>Title: </b>").append(event.getTitle()).append("<br>")
                            .append("  <b>Description: </b>").append(event.getDescription()).append("<br>")
                            .append("  <b>Date: </b>").append(event.getDate()).append("<br>")
                            .append("  <b>Organiser: </b>").append(event.getOrganiser()).append("<br>")
                            .append("  <b>Participants: </b>").append(event.getParticipant()).append("<br><br>")
                            .append("\n");
                }
            }
        }

        // Convert the formatted HTML string to a Spanned object and set it to the TextView
        Spanned formattedText = Html.fromHtml(displayText.toString(), Html.FROM_HTML_MODE_LEGACY);
        eventListTextView.setText(formattedText);
    }

    private void searchEvent(String query) {
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : allEvents) {
            if (event.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredEvents.add(event);
            }
        }

        if (filteredEvents.isEmpty()) {
            Toast.makeText(this, "No events found with the title: " + query, Toast.LENGTH_SHORT).show();
        } else {
            displayEventsByMonth(filteredEvents);
        }
    }

    private static class Event {
        private String title;
        private String description;
        private String date;
        private String organiser;
        private String participant;

        public Event(String title, String description, String date, String organiser, String participant) {
            this.title = title;
            this.description = description;
            this.date = date;
            this.organiser = organiser;
            this.participant = participant;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getDate() {
            return date;
        }

        public String getOrganiser() {
            return organiser;
        }

        public String getParticipant() {
            return participant;
        }
    }
}