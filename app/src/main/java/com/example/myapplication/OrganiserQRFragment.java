package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.HashMap;

public class OrganiserQRFragment extends Fragment {

    private Button scanQrBtn;
    private TextView scannedValueTv;
    private EditText eventInput, checkpoint;
    private DatabaseReference checkpointsDb, userCompleted;
    private String currentEventName; // Variable to hold the event name

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkpointsDb = FirebaseDatabase.getInstance().getReference("EventCheckpoints");
        userCompleted=FirebaseDatabase.getInstance().getReference("UserCompleted");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organiser_q_r, container, false);

        scanQrBtn = view.findViewById(R.id.scanQrBtn);
        scannedValueTv = view.findViewById(R.id.scannedValueTv);
        eventInput = view.findViewById(R.id.events);
       // checkpoint=view.findViewById(R.id.c);

        scanQrBtn.setOnClickListener(v -> {
            String eventName = eventInput.getText().toString().trim();
            if (eventName.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter an event name!", Toast.LENGTH_SHORT).show();
            } else {
                currentEventName = eventName; // Save the event name
                launchQrScanner();
            }
        });

        return view;
    }

    private void launchQrScanner() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan QR Code");
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        scannerLauncher.launch(options);
    }

    private final androidx.activity.result.ActivityResultLauncher<ScanOptions> scannerLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() == null) {
                    Toast.makeText(getActivity(), "Scan cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    String scannedUsername = result.getContents();
                    scannedValueTv.setText("Scanned User: " + scannedUsername);
                    verifyAndIncrementCheckpoints(scannedUsername);
                }
            });

    private void verifyAndIncrementCheckpoints(String username) {
        if (currentEventName == null || currentEventName.isEmpty()) {
            Toast.makeText(getActivity(), "Event name is not set. Please enter it again.", Toast.LENGTH_SHORT).show();
            return;
        }

        checkpointsDb.child(currentEventName).child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> updateMap = new HashMap<>();
                    int completedCheckpoints = 0;

                    // Update Checkpoints
                    for (int i = 1; i <= 3; i++) {
                        String checkpointKey = "checkpoint" + i;
                        if (!snapshot.child(checkpointKey).exists() || !(Boolean) snapshot.child(checkpointKey).getValue()) {
                            updateMap.put(checkpointKey, true);
                            break; // Update only one checkpoint per scan
                        } else {
                            completedCheckpoints++;
                        }
                    }

                    if (completedCheckpoints == 2) { // Notify when last checkpoint is completed
                        updateMap.put("eventcompleted", true);
                        userCompleted.child(username).child(currentEventName).setValue(true);
                        Toast.makeText(getActivity(), "User has completed all checkpoints!", Toast.LENGTH_SHORT).show();
                    }

                    checkpointsDb.child(currentEventName).child(username).updateChildren(updateMap)
                            .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Checkpoint updated successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to update checkpoint: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(getActivity(), "User is not registered for this event!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}