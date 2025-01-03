package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    TextView profileName, profileEmail, profileUsername, profilePassword;
    Button editProfile, Logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileUsername = view.findViewById(R.id.profileUsername);
        profilePassword = view.findViewById(R.id.profilePassword);
        editProfile = view.findViewById(R.id.editButton);
        Logout=view. findViewById(R.id.logoutButton);

        showAllUserData();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve details from the current arguments
                Bundle currentArgs = getArguments();
                if (currentArgs != null) {
                    String username = currentArgs.getString("username");
                    String name = profileName.getText().toString();
                    String email = profileEmail.getText().toString();
                    String password = profilePassword.getText().toString();


                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("name", name);
                    bundle.putString("email", email);
                    bundle.putString("password", password);

                    EditProfileFragment editProfileFragment = new EditProfileFragment();
                    editProfileFragment.setArguments(bundle);

                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame_layout, editProfileFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the main activity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                // Optionally, finish the current activity to prevent the user from returning to it
                getActivity().finish();
            }
        });

        return view;
    }

    public void showAllUserData() {
        // Get username from arguments (passed from the activity or fragment)
        Bundle args = getArguments();
        if (args != null) {
            String username = getArguments().getString("username");

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            // Query Firebase to get data for the given username
            reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Get data from the snapshot
                        String nameFromDB = snapshot.child("name").getValue(String.class);
                        String emailFromDB = snapshot.child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child("username").getValue(String.class);
                        String passwordFromDB = snapshot.child("password").getValue(String.class);

                        profileName.setText(nameFromDB);
                        profileEmail.setText(emailFromDB);
                        profileUsername.setText(usernameFromDB);
                        profilePassword.setText(passwordFromDB);
                    } else {
                        // Handle the case where the user data is not found
                        Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors if the query fails
                    Toast.makeText(getContext(), "Error retrieving data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
