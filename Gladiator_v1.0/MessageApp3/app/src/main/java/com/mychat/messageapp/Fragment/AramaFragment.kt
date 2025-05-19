
package com.mychat.messageapp.Fragment

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.mychat.messageapp.MainActivity5
import com.mychat.messageapp.Model.Kullanici
import com.mychat.messageapp.R
class AramaFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var rv: RecyclerView
    private lateinit var adapter: KullaniciAdapter
    private lateinit var aramaBar: EditText
    private val kullanicilar = mutableListOf<Kullanici>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_arama, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        rv = view.findViewById(R.id.arama_rec)
        aramaBar = view.findViewById(R.id.aramabar)

        rv.layoutManager = LinearLayoutManager(context)
        adapter = KullaniciAdapter(requireContext(), kullanicilar)
        rv.adapter = adapter


        aramaBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    fetchKullanicilar(query)
                } else {
                    kullanicilar.clear()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun fetchKullanicilar(query: String) {
        val normalizedQuery = query.lowercase()
        firestore.collection("Kullanicilar")
            .orderBy("kullaniciAdi")
            .startAt(normalizedQuery)
            .endAt("$normalizedQuery\uf8ff")
            .get()
            .addOnSuccessListener { documents ->
                kullanicilar.clear()
                for (document in documents) {
                    val displayName = document.getString("kullaniciAdi")
                    val email = document.getString("email")
                    val isOnline = document.getString("isOnline") ?: "Offline"
                    val uid = document.getString("uid") ?: ""

                    if (!displayName.isNullOrEmpty() && !email.isNullOrEmpty()) {
                        kullanicilar.add(Kullanici(email, displayName, isOnline, uid))
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Kullanıcılar alınamadı", Toast.LENGTH_SHORT).show()
            }
    }
}


class KullaniciAdapter(
    private val mContext: Context,
    private var Kullanicilar: MutableList<Kullanici>
) : RecyclerView.Adapter<KullaniciAdapter.ViewHolder>() {

    private val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.kullanici_ogesi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kullanici = Kullanicilar[position]
        holder.kullaniciAd.text = kullanici.kadi
        holder.itemView.findViewById<ImageButton>(R.id.kmesaj).setOnClickListener {
            val intent = Intent(mContext, MainActivity5::class.java)
            intent.putExtra("kullaniciAdi", kullanici.kadi) // Kullanıcı adını gönder
            intent.putExtra("uid", kullanici.uid)  // Kullanıcı ID'sini gönder
            mContext.startActivity(intent)
        }

        // Kullanıcının online durumunu kontrol et ve UI'yi güncelle
        updateOnlineStatus(holder, kullanici.isOnline)
    }

    override fun getItemCount(): Int = Kullanicilar.size

    // Kullanıcının online durumunu kontrol etme fonksiyonu
    private fun updateOnlineStatus(holder: ViewHolder, status: String?) {
        when (status) {
            "Online" -> {
                holder.onlineDurum.text = "Online"
                holder.onlineDurum.setTextColor(ContextCompat.getColor(mContext, R.color.green)) // Yeşil renk
            }
            "Offline" -> {
                holder.onlineDurum.text = "Offline"
                holder.onlineDurum.setTextColor(ContextCompat.getColor(mContext, R.color.red)) // Kırmızı renk
            }
            else -> {
                holder.onlineDurum.text = "Bilinmiyor"  // Durum gelmediğinde
                holder.onlineDurum.setTextColor(ContextCompat.getColor(mContext, R.color.gri)) // Gri renk
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilresim: ImageView = itemView.findViewById(R.id.resimKullanici)
        val kullaniciAd: TextView = itemView.findViewById(R.id.kullaniciAdi)
        val onlineDurum: TextView = itemView.findViewById(R.id.onlineDurum)
    }

    // Kullanıcı listesi güncellenirse, RecyclerView'i yenile
    fun updateKullaniciList(newList: MutableList<Kullanici>) {
        val filteredList = newList.filter { it.uid != firebaseUser?.uid }.toMutableList()
        Kullanicilar = newList
        notifyDataSetChanged()
    }

    // Firebase'den kullanıcı verilerini almak ve durumu güncellemek için ek bir fonksiyon:
    fun updateFirebaseStatus(userId: String) {
        val userRef = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(userId)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val kullanici = snapshot.getValue(Kullanici::class.java)
                kullanici?.let {
                    // Eğer kullanıcı verisi alınmışsa, giriş yapan kullanıcıyı hariç tutarak listeyi güncelle
                    if (kullanici.uid != firebaseUser?.uid) {
                        val position = Kullanicilar.indexOfFirst { it.uid == kullanici.uid }
                        if (position != -1) {
                            Kullanicilar[position] = kullanici
                            notifyItemChanged(position)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("KullaniciAdapter", "Veri alınamadı: ${error.message}")
            }
        })
    }
}
//class AramaFragment : Fragment() {
//
//    private lateinit var firestore: FirebaseFirestore
//    private lateinit var rv: RecyclerView
//    private lateinit var adapter: KullaniciAdapter
//    private lateinit var aramaBar: EditText
//    private val kullanicilar = mutableListOf<Kullanici>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? = inflater.inflate(R.layout.fragment_arama, container, false)
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        firestore = FirebaseFirestore.getInstance()
//        rv = view.findViewById(R.id.arama_rec)
//        aramaBar = view.findViewById(R.id.aramabar)
//
//        rv.layoutManager = LinearLayoutManager(context)
//        adapter = KullaniciAdapter(requireContext(), kullanicilar)
//        rv.adapter = adapter
//
//        // Arama çubuğu metin değişikliklerini dinleme
//        aramaBar.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val query = s.toString().trim()
//                if (query.isNotEmpty()) {
//                    fetchKullanicilar(query)
//                } else {
//                    kullanicilar.clear()
//                    adapter.notifyDataSetChanged()
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })
//    }
//
//    private fun fetchKullanicilar(query: String) {
//        val normalizedQuery = query.lowercase()
//        firestore.collection("Kullanicilar")
//            .orderBy("kullaniciAdi")
//            .startAt(normalizedQuery)
//            .endAt("$normalizedQuery\uf8ff")
//            .get()
//            .addOnSuccessListener { documents ->
//                kullanicilar.clear()
//                for (document in documents) {
//                    Log.d("FirestoreData", document.data.toString()) // Veriyi logla
//
//                    val displayName = document.getString("kullaniciAdi")
//                    val email = document.getString("email")
//                    val isOnline = document.getString("isOnline")!!
//
//                    if (!displayName.isNullOrEmpty() && !email.isNullOrEmpty()) {
//                        // Kullanıcıyı ekle
//                        kullanicilar.add(Kullanici(email, displayName, isOnline))
//                    }
//                }
//                adapter.notifyDataSetChanged()
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(context, "Hata: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
//            }
//    }
//}






