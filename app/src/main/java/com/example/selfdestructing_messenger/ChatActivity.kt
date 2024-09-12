package com.example.selfdestructing_messenger

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.selfdestructing_messenger.adapters.MessageAdapter
import com.example.selfdestructing_messenger.models.Message
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var messageList: MutableList<Message>
    private lateinit var adapter: MessageAdapter

    private lateinit var listView: ListView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var switchUserButton: Button

    private var currentUserId: String = "user1" // Start with user1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mDatabase = FirebaseDatabase.getInstance().getReference("messages")
        messageList = mutableListOf()

        listView = findViewById(R.id.listView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)
        switchUserButton = findViewById(R.id.switchUserButton)

        adapter = MessageAdapter(this, messageList, currentUserId)
        listView.adapter = adapter

        // Switch user on button click
        switchUserButton.setOnClickListener {
            switchUser()
        }

        sendButton.setOnClickListener {
            sendMessage(currentUserId)
        }

        // Listen for new messages
        mDatabase.orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messageList.clear()
                for (snapshot in dataSnapshot.children) {
                    val message = snapshot.getValue(Message::class.java)
                    if (message != null) {
                        messageList.add(message)
                    }
                }
                adapter.notifyDataSetChanged()
                listView.smoothScrollToPosition(adapter.count - 1)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun sendMessage(senderId: String) {
        val messageText = messageInput.text.toString()
        if (!TextUtils.isEmpty(messageText)) {
            val key = mDatabase.push().key
            val message = Message(messageText, System.currentTimeMillis(), senderId)
            if (key != null) {
                mDatabase.child(key).setValue(message)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            messageInput.setText("")
                        }
                    }
            }
        }
    }

    // Function to switch between user1 and user2
    private fun switchUser() {
        if (currentUserId == "user1") {
            currentUserId = "user2"
        } else {
            currentUserId = "user1"
        }
        adapter = MessageAdapter(this, messageList, currentUserId)
        listView.adapter = adapter
    }
}