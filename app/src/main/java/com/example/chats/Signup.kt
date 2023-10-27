package com.example.chats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Signup : AppCompatActivity() {
    private lateinit var edtemail: EditText
    private lateinit var edtpassword: EditText
    private lateinit var btnsignup: Button
    private lateinit var edtName: EditText
    private lateinit var mauth: FirebaseAuth
    private lateinit var mdefred:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()
        mauth = FirebaseAuth.getInstance()
        edtemail = findViewById(R.id.Email123)
        edtpassword = findViewById(R.id.Password1)
        btnsignup = findViewById(R.id.singup)
        edtName = findViewById(R.id.Name1)
        btnsignup.setOnClickListener {
            val name=edtName.text.toString()
            val email = edtemail.text.toString()
            val password = edtpassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                signUp(name,email, password)
            } else {
                Toast.makeText(this@Signup, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUp(name:String,email: String, password: String) {
        mauth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //code for jumping to home
                    addusertodatabase(name,email,mauth.currentUser?.uid!!)
                    val intent = Intent(this@Signup,Login::class.java)
                    startActivity(intent)
                    finish() // close the Signup activity
                } else {
                    Toast.makeText(this@Signup, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun addusertodatabase(name: String,email: String,uid:String){
        mdefred=FirebaseDatabase.getInstance().getReference()
        mdefred.child("user").child(uid).setValue(User(name,email,uid))


    }
}
