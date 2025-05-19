package com.mychat.messageapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mychat.messageapp.databinding.ActivityMain5Binding

class MainActivity5 : AppCompatActivity() {
    private lateinit var binding: ActivityMain5Binding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUserId: String
    private lateinit var otherUserId: String
    private lateinit var messageAdapter: MessageAdapter
    private val messagesList = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.geritusu.setOnClickListener(){
            finish()
        }

        // Firebase ve kullanıcı bilgileri
        firestore = FirebaseFirestore.getInstance()
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        otherUserId = intent.getStringExtra("uid") ?: return

        val otherUserName = intent.getStringExtra("kullaniciAdi") ?: "Bilinmeyen Kullanıcı"
        binding.kadigor.text = otherUserName

        // RecyclerView setup
        messageAdapter = MessageAdapter(messagesList, currentUserId)
        binding.recmesaj.apply {
            layoutManager = LinearLayoutManager(this@MainActivity5)
            adapter = messageAdapter
        }

        // Mesaj gönderme
        binding.gonder.setOnClickListener {
            val messageText = binding.mesajgirdi.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                binding.mesajgirdi.text.clear()
            }
        }

        // Mesajları dinleme
        listenForMessages()
    }

    private fun sendMessage(messageText: String) {
        val chatId = generateChatId(currentUserId, otherUserId)
        val message = hashMapOf(
            "senderId" to currentUserId,
            "receiverId" to otherUserId,
            "message" to messageText,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("chats").document(chatId).collection("messages")
            .add(message)
            .addOnSuccessListener {
                Log.d("MessageSend", "Mesaj başarıyla gönderildi!")
            }
            .addOnFailureListener { e ->
                Log.e("MessageSend", "Mesaj gönderilemedi: ", e)
            }
    }

    private fun listenForMessages() {
        val chatId = generateChatId(currentUserId, otherUserId)

        firestore.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("MessageListen", "Mesaj dinlerken hata: ", e)
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    messagesList.clear()
                    for (doc in snapshots) {
                        val message = doc.toObject(Message::class.java)
                        messagesList.add(message)
                    }
                    messageAdapter.notifyDataSetChanged()
                    binding.recmesaj.scrollToPosition(messagesList.size - 1)
                }
            }
    }

    private fun generateChatId(user1: String, user2: String): String {
        return if (user1 < user2) "$user1$user2" else "$user2$user1"
    }
}
class MessageAdapter(
    private val messages: List<Message>,
    private val currentUserUid: String
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        if (message.senderId == currentUserUid) {
            holder.bindSender(message) // Gönderenin mesajlarını bağla
        } else {
            holder.bindReceiver(message) // Alıcının mesajlarını bağla
        }
    }

    override fun getItemCount(): Int = messages.size

    // ViewHolder Sınıfı
    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderTextView: TextView = itemView.findViewById(R.id.senderTextView)
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)

        fun bindSender(message: Message) {
            // Gönderen için görünüm ayarları
            senderTextView.visibility = View.GONE // Gönderenin adı gösterilmeyecek
            messageTextView.text = message.message // Mesaj içeriğini göster
            messageTextView.setBackgroundResource(R.drawable.message_background_right) // Sağda gösterim için arka plan
            alignMessageToEnd() // Mesajı sağa hizala
        }

        fun bindReceiver(message: Message) {
            // Alıcı için görünüm ayarları
            senderTextView.visibility = View.GONE // Gönderenin adını göster
            senderTextView.text = message.senderId // Gönderenin ID'sini göster
            messageTextView.text = message.message // Mesaj içeriğini göster
            messageTextView.setBackgroundResource(R.drawable.message_background_left) // Solda gösterim için arka plan
            alignMessageToStart() // Mesajı sola hizala
        }

        private fun alignMessageToEnd() {
            val layoutParams = messageTextView.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START)
            messageTextView.layoutParams = layoutParams
        }

        private fun alignMessageToStart() {
            val layoutParams = messageTextView.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE)
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
            messageTextView.layoutParams = layoutParams
        }
    }
}



