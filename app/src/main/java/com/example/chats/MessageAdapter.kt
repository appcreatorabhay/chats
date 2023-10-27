package com.example.chats

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


class MessageAdapter(val context: Context, val messageList: ArrayList<Messages>) : RecyclerView.Adapter<ViewHolder>() {
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == ITEM_RECEIVE) {
            val view = LayoutInflater.from(context).inflate(R.layout.received, parent, false)
            ReceivedViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.itemViewType == ITEM_SENT) {
            val currentMessage = messageList[position]
            val viewHolder = holder as SentViewHolder
            viewHolder.sentMessages.text = currentMessage.message
        } else {
            val currentMessage = messageList[position]
            val viewHolder = holder as ReceivedViewHolder
            viewHolder.receivedMessages.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View) : ViewHolder(itemView) {
        val sentMessages = itemView.findViewById<TextView>(R.id.sent)
    }

    class ReceivedViewHolder(itemView: View) : ViewHolder(itemView) {
        val receivedMessages = itemView.findViewById<TextView>(R.id.receivedmessage)
    }
}
