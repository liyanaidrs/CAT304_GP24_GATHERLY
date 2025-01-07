package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OrganiserChatFragment extends Fragment {

    private EditText messageInput;
    private Button sendButton;
    private ListView chatListView;

    private DatabaseReference chatRoomRef;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> chatMessages;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.activity_chat_room, container, false);

        // Initialize views
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);
        chatListView = view.findViewById(R.id.chatListView);

        // Retrieve username from arguments
//        if (getArguments() != null) {
//            username = getArguments().getString("username");
//        }
//
//        // Check if username is null or empty
//        if (username == null || username.isEmpty()) {
//            Toast.makeText(getContext(), "Username not found!", Toast.LENGTH_SHORT).show();
//            return view;
//        }

        // Firebase reference and adapter setup
        chatRoomRef = FirebaseDatabase.getInstance().getReference("chatroom");
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(requireContext(), chatMessages);
        chatListView.setAdapter(chatAdapter);

        // Load chat messages
        loadMessages();

        // Handle send button click
        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!TextUtils.isEmpty(message)) {
                sendMessage("organiser", message);
            } else {
                Toast.makeText(getContext(), "Message cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void loadMessages() {
        chatRoomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessages.clear();
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    ChatMessage message = messageSnapshot.getValue(ChatMessage.class);
                    if (message != null) {
                        chatMessages.add(message);
                    }
                }
                chatAdapter.notifyDataSetChanged();
                chatListView.setSelection(chatMessages.size() - 1); // Auto-scroll to the last message
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load messages: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String username, String message) {
        HashMap<String, Object> messageData = new HashMap<>();
        messageData.put("sender", " EcoNation Organiser");
        messageData.put("message", message);

        chatRoomRef.push().setValue(messageData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                messageInput.setText(""); // Clear input after sending
            } else {
                Toast.makeText(getContext(), "Failed to send message", Toast.LENGTH_SHORT).show();
            }
        });
    }
}