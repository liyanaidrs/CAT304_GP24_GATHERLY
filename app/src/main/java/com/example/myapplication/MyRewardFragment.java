package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.FragmentMyRewardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRewardFragment extends Fragment {

    private FragmentMyRewardBinding binding;
    private DatabaseReference userCompletedRef, eventCheckpointsRef;
    private String username;
    private ArrayList<Event> completedEvents = new ArrayList<>();
    private EventAdapter eventAdapter;
    private int totalStars = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyRewardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Get username from arguments
        if (getArguments() != null) {
            username = getArguments().getString("username");
            if (username == null || username.isEmpty()) {
                Toast.makeText(getContext(), "Username not found!", Toast.LENGTH_SHORT).show();
                return view;
            }
        }

        // Initialize Firebase references
        userCompletedRef = FirebaseDatabase.getInstance().getReference("UserCompleted").child(username);
        eventCheckpointsRef = FirebaseDatabase.getInstance().getReference("EventCheckpoints");

        // Set up RecyclerView and adapter
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventAdapter = new EventAdapter(completedEvents, event -> {
            Intent intent = new Intent(getActivity(), DetailRewardActivity.class);
            intent.putExtra("eventKey", event.getEventKey());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(eventAdapter);

        // Close the activity on close icon click
        ImageView closeIcon = view.findViewById(R.id.cancelButton);
        closeIcon.setOnClickListener(v -> requireActivity().finish()); // Use requireActivity() to close the activity

        // Load completed events
        loadCompletedEvents();

        return view;
    }

    private void loadCompletedEvents() {
        // Fetch events that user has completed
        userCompletedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int completedEventCount = 0;

                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        String eventName = eventSnapshot.getKey();
                        Boolean isCompleted = eventSnapshot.getValue(Boolean.class);

                        if (Boolean.TRUE.equals(isCompleted)) {
                            completedEventCount++;
                            fetchEventDetails(eventName);
                        }
                    }

                    // Calculate total stars (5 stars per completed event)
                    totalStars = completedEventCount * 5;

                    // Update UI to show total stars
                    binding.totalStarsTextView.setText("x" + totalStars);
                } else {
                    Toast.makeText(getActivity(), "No completed events found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchEventDetails(String eventName) {
        eventCheckpointsRef.child(eventName).child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String eventKey = snapshot.child("eventKey").getValue(String.class);
                    String eventName = snapshot.child("eventName").getValue(String.class);

                    // Check if event data exists
                    if (eventKey != null && eventName != null) {
                        // Ensure completed events list is updated properly
                        completedEvents.add(new Event(eventKey, eventName));
                        eventAdapter.notifyItemInserted(completedEvents.size() - 1); // Notify only the last item inserted
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
