package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminStatisticFragment} factory method to
 * create an instance of this fragment.
 */
public class AdminStatisticFragment extends Fragment {

    CardView imagesCard, organiserCard, eventCard, statisticCard, qrCard, appCard;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_statistic, container, false);

        imagesCard = view.findViewById(R.id.userCard);
        organiserCard=view.findViewById(R.id.OrganiserCard);
        eventCard=view.findViewById(R.id.EventCard);
        statisticCard=view.findViewById(R.id.StatisticCard);
        qrCard=view.findViewById(R.id.QRCard);
        appCard=view.findViewById(R.id.appCard);
        imagesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserDashboardActivity.class);
                startActivity(intent);
            }
        });
        organiserCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrganiserDashboardActivity.class);
                startActivity(intent);
            }
        });
        eventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EventDashboardActivity.class);
                startActivity(intent);
            }
        });

        statisticCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StatisticDashboardActivity.class);
                startActivity(intent);
            }
        });
        qrCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QRDashboardActivity.class);
                startActivity(intent);
            }
        });
        appCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AppDashboardActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}