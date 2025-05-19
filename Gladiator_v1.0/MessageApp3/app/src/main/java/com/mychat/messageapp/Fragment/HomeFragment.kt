package com.mychat.messageapp.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.type.Date
import com.mychat.messageapp.R
import java.text.SimpleDateFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var postsAdapter: PostsAdapter
    private val postsList = mutableListOf<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Firebase Firestore
        firestore = FirebaseFirestore.getInstance()

        // RecyclerView setup
        recyclerView = view.findViewById(R.id.recyclerViewPosts)
        postsAdapter = PostsAdapter(postsList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = postsAdapter

        loadPosts()

        return view
    }

    private fun loadPosts() {
        firestore.collection("Yazilar")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                postsList.clear()
                val userIds = mutableSetOf<String>()

                for (document in result) {
                    val post = document.toObject(Post::class.java)
                    postsList.add(post)
                    userIds.add(post.userId) // Benzersiz kullanıcı kimliklerini topluyoruz
                }

                Log.d("LoadPosts", "UserIds: $userIds") // userIds'yi kontrol edin
                fetchUserNames(userIds) // Kullanıcı adlarını almak için çağırıyoruz
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Yazılar yüklenemedi: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun fetchUserNames(userIds: Set<String>) {
        val userNamesMap = mutableMapOf<String, String>()

        Log.d("FetchUserNames", "UserIds: $userIds") // userIds kümesinin içeriğini kontrol edin

        firestore.collection("Kullanicilar")
            .whereIn("uid", userIds.toList())
            .get()
            .addOnSuccessListener { result ->
                Log.d("FetchUserNames", "Firestore result size: ${result.size()}")

                for (document in result) {
                    val userId = document.getString("uid") ?: ""
                    val userName = document.getString("kullaniciAdi") ?: "Bilinmeyen"

                    Log.d("FetchUserNames", "Found user: $userId -> $userName")
                    userNamesMap[userId] = userName
                }

                // Kullanıcı adlarını yazılarla eşleştiriyoruz
                postsList.forEach { post ->
                    val userName = userNamesMap[post.userId] ?: "Bilinmeyen"
                    Log.d("FetchUserNames", "Post UserId: ${post.userId}, UserName: $userName")
                    post.kullaniciAdi = userName
                }

                postsAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Kullanıcı bilgileri yüklenemedi: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

}

data class Post(
    val title: String = "",
    val content: String = "",
    val userId: String = "",
    var kullaniciAdi: String = "", // Kullanıcı adı veya e-posta
    val timestamp: Long = 0
)

class PostsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val contentTextView: TextView = itemView.findViewById(R.id.textViewContent)
        val userNameTextView: TextView = itemView.findViewById(R.id.textViewUserName) // Kullanıcı adı
        val timestampTextView: TextView = itemView.findViewById(R.id.textViewTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.titleTextView.text = post.title
        holder.contentTextView.text = post.content
        holder.userNameTextView.text = "Paylaşan: ${post.kullaniciAdi}"

        // Timestamp'i okunabilir bir tarihe çevirme
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = java.util.Date(post.timestamp)
        holder.timestampTextView.text = dateFormat.format(date)
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}


