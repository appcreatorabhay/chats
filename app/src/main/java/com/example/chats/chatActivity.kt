package com.example.chats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class chatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messagebox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter:MessageAdapter
    private lateinit var messagelist:ArrayList<Messages>
    private lateinit var mdref:DatabaseReference
    var Receiverroom:String?=null
    var senderROom:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name") // fix here
        val receiveruid = intent.getStringExtra("uid") // fix here
        val senderuid=FirebaseAuth.getInstance().currentUser?.uid
        mdref=FirebaseDatabase.getInstance().getReference()
        senderROom=receiveruid+senderuid
        Receiverroom=senderuid+receiveruid
        supportActionBar?.title=name
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messagebox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentbutton) // fix typo
        messagelist=ArrayList()
        messageAdapter=MessageAdapter(this,messagelist)
        chatRecyclerView.layoutManager=LinearLayoutManager(this)
        chatRecyclerView.adapter=messageAdapter
        mdref.child("chats").child(senderROom!!).child("message")
            .addValueEventListener(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messagelist.clear()
                    for(postSnapshot in snapshot.children){
                        val message=postSnapshot.getValue(Messages::class.java)
                        messagelist.add(message!!

                        )
                    }
                    messageAdapter.notifyDataSetChanged()
            }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })
        sendButton.setOnClickListener(){
            val message=messagebox.text.toString()
            val messageObject=Messages(message,senderuid)
            mdref.child("chats").child(senderROom!!).child("message").push()
                .setValue(messageObject).addOnSuccessListener {
                    mdref.child("chats").child(Receiverroom!!).child("message").push()
                        .setValue(messageObject)

                }
            messagebox.setText(" ");


        }

    }
}
