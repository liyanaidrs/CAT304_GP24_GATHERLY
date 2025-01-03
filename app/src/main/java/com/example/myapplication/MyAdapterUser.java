package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapterUser extends RecyclerView.Adapter<MyViewHolderUser> {

    private Context context;
    private List<DataClass> dataList;

    String username;
    public MyAdapterUser(Context context, List<DataClass> dataList, String username) {
        this.context = context;
        this.dataList = dataList;
        this.username=username;
    }

    @NonNull
    @Override
    public MyViewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderUser holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getDataTitle());
        holder.recDesc.setText(dataList.get(position).getDataDesc());
        holder.recLang.setText(dataList.get(position).getDataLang());
        holder.recDate.setText(dataList.get(position).getDataDate());
        holder.recParticipant.setText(dataList.get(position).getDataParticipant());
//        holder.c1.setText(dataList.get(position).getC1());
//        holder.c2.setText(dataList.get(position).getC2());
//        holder.c3.setText(dataList.get(position).getC3());
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailUserActivity.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDataDesc());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getDataTitle());
                intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("Language", dataList.get(holder.getAdapterPosition()).getDataLang());
                intent.putExtra("Date", dataList.get(holder.getAdapterPosition()).getDataDate());
                intent.putExtra("Participant",dataList.get(holder.getAdapterPosition()).getDataParticipant());
                intent.putExtra("currentUser", username);
                intent.putExtra("c1",dataList.get(holder.getAdapterPosition()).getC1());
                intent.putExtra("c2",dataList.get(holder.getAdapterPosition()).getC2());
                intent.putExtra("c3",dataList.get(holder.getAdapterPosition()).getC3());
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolderUser extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recTitle, recDesc, recLang, recDate, recParticipant,c1,c2,c3;
    CardView recCard;

    public MyViewHolderUser(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recDesc = itemView.findViewById(R.id.recDesc);
        recLang = itemView.findViewById(R.id.recLang);
        recTitle = itemView.findViewById(R.id.recTitle);
        recDate=itemView.findViewById(R.id.recDate);
        recParticipant=itemView.findViewById(R.id.recParticipant);
        c1=itemView.findViewById(R.id.c1);
        c2=itemView.findViewById(R.id.c2);
        c3=itemView.findViewById(R.id.c3);
    }
}