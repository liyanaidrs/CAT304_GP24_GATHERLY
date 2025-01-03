package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class AdminCreateFragment extends Fragment {

    ImageView uploadImage;
    Button saveButton;
    EditText uploadTopic, uploadDesc, uploadLang, uploadDate, uploadparticipant, c1, c2, c3;
    String imageURL;
    Uri uri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_create, container, false);

        // Initialize views
        uploadImage = view.findViewById(R.id.uploadImage);
        uploadDesc = view.findViewById(R.id.uploadDesc);
        uploadTopic = view.findViewById(R.id.uploadTopic);
        uploadLang = view.findViewById(R.id.uploadLang);
        uploadDate = view.findViewById(R.id.uploadDate);
        uploadparticipant=view.findViewById(R.id.uploadParticipant);
        saveButton = view.findViewById(R.id.saveButton);
        c1=view.findViewById(R.id.c1);
        c2=view.findViewById(R.id.c2);
        c3=view.findViewById(R.id.c3);

        // Activity Result Launcher for image selection
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                uri = data.getData();
                                uploadImage.setImageURI(uri);
                            }
                        } else {
                            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Image upload on click
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        // Date Picker for selecting date
        uploadDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // DatePickerDialog to select a date
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Display selected date in the EditText
                                uploadDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Save data on click
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    saveData();
                }
            }
        });

        return view;
    }

    // Validate input fields
    private boolean validateInputs() {
        if (TextUtils.isEmpty(uploadTopic.getText().toString())) {
            uploadTopic.setError("Topic is required");
            return false;
        }
        if (TextUtils.isEmpty(uploadDesc.getText().toString())) {
            uploadDesc.setError("Description is required");
            return false;
        }
        if (TextUtils.isEmpty(uploadLang.getText().toString())) {
            uploadLang.setError("Organizer is required");
            return false;
        }
        if (TextUtils.isEmpty(uploadDate.getText().toString())) {
            uploadDate.setError("Date is required");
            return false;
        }
        if (TextUtils.isEmpty(uploadparticipant.getText().toString())) {
            uploadparticipant.setError("Number of Participant is required");
            return false;
        }
        if (TextUtils.isEmpty(c1.getText().toString())) {
            c1.setError("Checkpoint activity is required");
            return false;
        }
        if (TextUtils.isEmpty(c2.getText().toString())) {
            c2.setError("Checkpoint activity is required");
            return false;
        }
        if (TextUtils.isEmpty(c2.getText().toString())) {
            c2.setError("Checkpoint activity is required");
            return false;
        }
        if (uri == null) {
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Save data to Firebase Storage and Database
    public void saveData() {
        // Firebase Storage Reference
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Event Images")
                .child(System.currentTimeMillis() + "_" + uri.getLastPathSegment());

        // Progress dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Upload image
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageURL = uri.toString(); // Get image URL
                        uploadData(); // Save data to database
                        dialog.dismiss();
                    }
                }).addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Failed to get image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Upload data to Firebase Database
    public void uploadData() {
        String title = uploadTopic.getText().toString();
        String desc = uploadDesc.getText().toString();
        String lang = uploadLang.getText().toString();
        String date = uploadDate.getText().toString();
        String participant = uploadparticipant.getText().toString();
        String C1=c1.getText().toString();
        String C2=c2.getText().toString();
        String C3=c3.getText().toString();
        // Create DataClass object
        DataClass dataClass = new DataClass(title, participant,desc, lang, date, imageURL, C1, C2,C3);

        // Generate unique key
        String uniqueKey = FirebaseDatabase.getInstance().getReference("Event Created").push().getKey();
        dataClass.setKey(uniqueKey);

        // Save data
        FirebaseDatabase.getInstance().getReference("Event Created").child(uniqueKey)
                .setValue(dataClass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                        clearInputs();
                    } else {
                        Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to Save: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Clear input fields after successful save
    private void clearInputs() {
        uploadImage.setImageResource(R.drawable.baseline_image_24); // Replace with your placeholder image
        uploadTopic.setText("");
        uploadDesc.setText("");
        uploadLang.setText("");
        uploadDate.setText("");
        uploadparticipant.setText("");
        c1.setText("");
        c2.setText("");
        c3.setText("");
        uri = null;
    }
}