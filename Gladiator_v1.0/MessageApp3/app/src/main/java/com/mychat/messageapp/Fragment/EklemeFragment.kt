package com.mychat.messageapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mychat.messageapp.R

class EklemeFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var shareButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ekleme, container, false)

        // Firebase initialization
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // UI elements
        titleEditText = view.findViewById(R.id.editTextTitle)
        contentEditText = view.findViewById(R.id.editTextContent)
        shareButton = view.findViewById(R.id.buttonShare)

        shareButton.setOnClickListener {
            sharePost()
        }

        return view
    }

    private fun sharePost() {
        val title = titleEditText.text.toString()
        val content = contentEditText.text.toString()

        if (title.isBlank() || content.isBlank()) {
            Toast.makeText(context, "Başlık ve içerik boş bırakılamaz!", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = auth.currentUser
        currentUser?.let {
            val userId = it.uid
            val post = hashMapOf(
                "title" to title,
                "content" to content,
                "userId" to userId,
                "timestamp" to System.currentTimeMillis(),
                "kullaniciAdi" to currentUser.displayName
            )

            firestore.collection("Yazilar").add(post)
                .addOnSuccessListener {
                    Toast.makeText(context, "Yazı başarıyla paylaşıldı!", Toast.LENGTH_SHORT).show()
                    titleEditText.text.clear()
                    contentEditText.text.clear()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Yazı paylaşımı başarısız: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            Toast.makeText(context, "Kullanıcı oturumu açık değil!", Toast.LENGTH_SHORT).show()
        }
    }
}