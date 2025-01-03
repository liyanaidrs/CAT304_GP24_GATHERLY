package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AdminProfileFragment extends Fragment {


    Button Logout;

    public AdminProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        // Initialize the Logout button
        Logout = view.findViewById(R.id.logoutButton);

        // Set up the Logout button's OnClickListener
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
}