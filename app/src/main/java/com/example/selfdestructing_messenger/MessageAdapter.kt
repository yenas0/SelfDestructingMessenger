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

class MessageAdapter(context: Context, messages: List<Message>) :
    ArrayAdapter<Message>(context, 0, messages) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)

        val message = getItem(position)

        val messageText = view.findViewById<TextView>(R.id.messageText)
        val messageTimestamp = view.findViewById<TextView>(R.id.messageTimestamp)

        messageText.text = message?.text

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        messageTimestamp.text = message?.timestamp?.let { sdf.format(it) }

        return view
    }
}
