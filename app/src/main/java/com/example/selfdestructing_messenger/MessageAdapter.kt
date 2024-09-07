package com.example.selfdestructing_messenger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.selfdestructing_messenger.R
import com.example.selfdestructing_messenger.models.Message
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(context: Context, private val messages: List<Message>, private val currentUserId: String) :
    ArrayAdapter<Message>(context, 0, messages) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val message = getItem(position)

        val layoutRes = if (message?.senderId == currentUserId) {
            R.layout.item_message_sent
        } else {
            R.layout.item_message_received
        }

        val view = convertView ?: LayoutInflater.from(context).inflate(layoutRes, parent, false)

        val messageText = view.findViewById<TextView>(R.id.messageText)
        val messageTimestamp = view.findViewById<TextView>(R.id.messageTimestamp)

        messageText.text = message?.text

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        messageTimestamp.text = message?.timestamp?.let { sdf.format(it) }

        return view
    }
}
