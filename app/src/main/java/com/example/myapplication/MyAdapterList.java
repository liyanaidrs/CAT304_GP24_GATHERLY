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

public class MyAdapterList extends RecyclerView.Adapter<MyViewHolderList> {

    private Context context;
    private List<ApplicationData> dataList;

    String username;
    public MyAdapterList(Context context, List<ApplicationData> dataList, String username) {
        this.context = context;
        this.dataList = dataList;
        this.username=username;
    }

    @NonNull
    @Override
    public MyViewHolderList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolderList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderList holder, int position) {
        Glide.with(context).load(dataList.get(position).getImage()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getTitle());
        holder.recDesc.setText(dataList.get(position).getDescription());
        holder.recLang.setText(dataList.get(position).getLanguage());
        holder.recDate.setText(dataList.get(position).getDate());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailListActivity.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getEventKey());
                intent.putExtra("Language", dataList.get(holder.getAdapterPosition()).getLanguage());
                intent.putExtra("Date", dataList.get(holder.getAdapterPosition()).getDate());
                intent.putExtra("currentUser", dataList.get(holder.getAdapterPosition()).getUsername());

                //intent.putExtra("currentUser", username);
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<ApplicationData> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolderList extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recTitle, recDesc, recLang, recDate;
    CardView recCard;

    public MyViewHolderList(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recDesc = itemView.findViewById(R.id.recDesc);
        recLang = itemView.findViewById(R.id.recLang);
        recTitle = itemView.findViewById(R.id.recTitle);
        recDate=itemView.findViewById(R.id.recDate);
    }
}