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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mDatabase = FirebaseDatabase.getInstance().getReference("messages")
        messageList = mutableListOf()

        listView = findViewById(R.id.listView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)

        adapter = MessageAdapter(this, messageList)
        listView.adapter = adapter

        sendButton.setOnClickListener {
            sendMessage()
        }

        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messageList.clear()
                for (snapshot in dataSnapshot.children) {
                    val message = snapshot.getValue(Message::class.java)
                    if (message != null) {
                        messageList.add(message)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun sendMessage() {
        val messageText = messageInput.text.toString()
        if (!TextUtils.isEmpty(messageText)) {
            val key = mDatabase.push().key
            val message = Message(messageText)
            if (key != null) {
                mDatabase.child(key).setValue(message)
                messageInput.setText("")
            }
        }
    }
}