package com.mychat.messageapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mychat.messageapp.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityMain3Binding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMain3Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        auth= Firebase.auth


    }

    fun cikisYap(view: View){
        val kullanici = auth.currentUser?.displayName.toString()
        auth.signOut()
        Toast.makeText(applicationContext,"Çıkış Yapıldı. ${kullanici}",Toast.LENGTH_SHORT).show()
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}