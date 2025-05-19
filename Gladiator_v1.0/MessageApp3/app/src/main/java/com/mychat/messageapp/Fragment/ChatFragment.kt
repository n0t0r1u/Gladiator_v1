package com.mychat.messageapp.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.type.Date
import com.mychat.messageapp.Message
import com.mychat.messageapp.R
import com.mychat.messageapp.databinding.FragmentChatBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ChatFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    //private lateinit var chatAdapter: ChatListAdapter
    private lateinit var chatList: MutableList<Message>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUserId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

//        recyclerView = view.findViewById(R.id.mesajlistesirec)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        chatList = mutableListOf()
//        //chatAdapter = ChatListAdapter(chatList)
//       // recyclerView.adapter = chatAdapter
//
//        firestore = FirebaseFirestore.getInstance()
//        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
//
//        //if (currentUserId.isNotEmpty()) {
//        //    fetchChats()
//        // } else {
//        //     Log.e("ChatFragment", "User not logged in")
//        //}

        return view
    }
}

//    private fun generateChatId(user1: String, user2: String): String {
//        return if (user1 < user2) "$user1$user2" else "$user2$user1"
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private fun fetchChats() {
//        // Burada chatId'yi doğru şekilde oluşturmalısınız.
//        firestore.collection("chats")
//            .whereArrayContains("messages", currentUserId)  // currentUserId'nin bulunduğu tüm chatleri alıyoruz
//            .addSnapshotListener { snapshots, e ->
//                if (e != null) {
//                    Log.e("ChatFragment", "Error fetching chats: ", e)
//                    return@addSnapshotListener
//                }
//
//                if (snapshots != null) {
//                    Log.d("ChatFragment", "Fetched chats: ${snapshots.documents.size}")
//                    chatList.clear()  // Yeni verileri eklemek için listeyi sıfırlıyoruz.
//
//                    val fetchTasks = mutableListOf<Task<Void>>()
//
//                    snapshots.documents.forEach { chatDocument ->
//                        // Mesajlar koleksiyonunu alıyoruz.
//                        val messagesCollection = chatDocument.reference.collection("messages")
//
//                        val task = messagesCollection
//                            .orderBy("timestamp", Query.Direction.DESCENDING)
//                            .limit(1)
//                            .get()
//                            .addOnSuccessListener { messageSnapshot ->
//                                if (!messageSnapshot.isEmpty) {
//                                    val lastMessage = messageSnapshot.documents[0].toObject(Message::class.java)
//                                    if (lastMessage != null &&
//                                        (lastMessage.senderId == currentUserId || lastMessage.receiverId == currentUserId)
//                                    ) {
//                                        val otherUserId = if (lastMessage.senderId == currentUserId) {
//                                            lastMessage.receiverId
//                                        } else {
//                                            lastMessage.senderId
//                                        }
//
//                                        fetchUserName(otherUserId) { userName ->
//                                            lastMessage.receiverId = userName
//                                            chatList.add(lastMessage)
//
//                                            // RecyclerView'ı güncelliyoruz.
//                                            activity?.runOnUiThread {
//                                                chatAdapter.notifyItemInserted(chatList.size - 1)
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            .addOnFailureListener { e ->
//                                Log.e("ChatFragment", "Error fetching messages: ", e)
//                            }
//
//                        fetchTasks.add(task.continueWith { null })
//                    }
//
//                    Tasks.whenAllComplete(fetchTasks).addOnCompleteListener {
//                        activity?.runOnUiThread {
//                            chatAdapter.notifyDataSetChanged()
//                        }
//                    }
//                }
//            }
//    }
//
//    private fun fetchUserName(userId: String, callback: (String) -> Unit) {
//        firestore.collection("Kullanicilar").document(userId)
//            .get()
//            .addOnSuccessListener { document ->
//                val kullaniciAdi = document?.getString("kullaniciAdi") ?: "Unknown"
//                callback(kullaniciAdi)
//            }
//            .addOnFailureListener {
//                callback("Unknown")
//            }
//    }
//
//    class ChatListAdapter(private val chatList: MutableList<Message>) :
//        RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
//            val view = LayoutInflater.from(parent.context)
//                .inflate(R.layout.item_chat, parent, false)
//            return ChatViewHolder(view)
//        }
//
//        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
//            val message = chatList[position]
//            holder.bind(message)
//        }
//
//        override fun getItemCount(): Int = chatList.size
//
//        inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//            private val userNameTextView: TextView = itemView.findViewById(R.id.participantTextView)
//            private val lastMessageTextView: TextView = itemView.findViewById(R.id.lastMessageTextView)
//            private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
//
//            fun bind(message: Message) {
//                userNameTextView.text = message.receiverId  // Kullanıcı adı
//                lastMessageTextView.text = message.message
//               // timestampTextView.text = formatTimestamp(message.timestamp)
//            }
//
//            private fun formatTimestamp(timestamp: Long): String {
//                val sdf = SimpleDateFormat("HH:mm, dd MMM", Locale.getDefault())
//                return sdf.format(java.util.Date(timestamp))
//            }
//        }
//    }
//}
//



//class ChatListAdapter(private val chatList: List<Message>) :
//    RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
//        return ChatViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
//        val message = chatList[position]
//        holder.bind(message)
//    }
//
//    override fun getItemCount(): Int = chatList.size
//
//    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val userNameTextView: TextView = itemView.findViewById(R.id.participantTextView)
//        private val lastMessageTextView: TextView = itemView.findViewById(R.id.lastMessageTextView)
//        private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
//
//        fun bind(message: Message) {
//            userNameTextView.text = message.receiverId // Karşı tarafın kullanıcı ID'si
//            lastMessageTextView.text = message.message
//            timestampTextView.text = formatTimestamp(message.timestamp)
//        }
//
//        private fun formatTimestamp(timestamp: Long): String {
//            val sdf = SimpleDateFormat("HH:mm, dd MMM", Locale.getDefault())
//            return sdf.format(java.util.Date(timestamp))
//        }
//    }
//}


//class ChatAdapter(private val chats: List<Chat>) :
//    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
//
//    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val lastMessageTextView: TextView = itemView.findViewById(R.id.lastMessageTextView)
//        val participantTextView: TextView = itemView.findViewById(R.id.participantTextView)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
//        return ChatViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
//        val chat = chats[position]
//        holder.lastMessageTextView.text = chat.lastMessage
//        holder.participantTextView.text = chat.participants.joinToString(", ") { it }
//    }
//
//    override fun getItemCount(): Int = chats.size
//}
//
//data class Chat(
//    val participants: List<String> = listOf(),
//    val lastMessage: String = "",
//    val timestamp: Long = 0L
//)