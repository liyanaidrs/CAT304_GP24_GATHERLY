package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ChatAdapter extends android.widget.ArrayAdapter<ChatMessage> {

    public ChatAdapter(@NonNull Context context, @NonNull ArrayList<ChatMessage> chatMessages) {
        super(context, 0, chatMessages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMessage message = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_message_item, parent, false);
        }

        TextView senderTextView = convertView.findViewById(R.id.senderTextView);
        TextView messageTextView = convertView.findViewById(R.id.messageTextView);

        if (message != null) {
            senderTextView.setText(message.getSender());
            messageTextView.setText(message.getMessage());
        }

        return convertView;
    }
}