@file:Suppress("DEPRECATION")

package com.mychat.messageapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.mychat.messageapp.Fragment.AramaFragment
import com.mychat.messageapp.Fragment.ChatFragment
import com.mychat.messageapp.Fragment.EditProfileFragment
import com.mychat.messageapp.Fragment.EklemeFragment
import com.mychat.messageapp.Fragment.HomeFragment
import com.mychat.messageapp.Fragment.KullaniciAdapter
import com.mychat.messageapp.Fragment.ProfilFragment
import com.mychat.messageapp.Model.Kullanici
import com.mychat.messageapp.databinding.ActivityMain4Binding

//class MainActivity4 : AppCompatActivity() {
//    lateinit var binding: ActivityMain4Binding
//    private lateinit var auth: FirebaseAuth // FirebaseAuth nesnesi
//    private lateinit var database: DatabaseReference // Firebase Realtime Database referansı
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivityMain4Binding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//
//        val bottomNavigationView = binding.bottomNav
//
//        // Son seçilen itemId'yi saklamak için bir değişken
//        var lastSelectedItemId: Int = R.id.home
//
//        // Başlangıçta "Home" fragment'ını yükle
//        loadFragment(HomeFragment())
//
//        bottomNavigationView.setOnItemSelectedListener { item ->
//            // Eğer aynı item tekrar seçildiyse, hiçbir işlem yapma
//            if (item.itemId == lastSelectedItemId) {
//                return@setOnItemSelectedListener true
//            }
//
//            // Yeni item seçildiğinde fragment yükleme işlemini yap
//            when (item.itemId) {
//                R.id.home -> {
//                    loadFragment(HomeFragment())
//                    true
//                }
//
//                R.id.arama -> {
//                    loadFragment(AramaFragment())
//                    true
//                }
//
//                R.id.profil -> {
//                    loadFragment(ProfilFragment())
//                    true
//                }
//
//                R.id.chat -> {
//                    loadFragment(ChatFragment())
//                    true
//                }
//
//                R.id.ekle -> {
//                    loadFragment(EklemeFragment())
//                    true
//                }
//
//                else -> return@setOnItemSelectedListener false
//            }
//
//            // Son seçilen itemId'yi güncelle
//            lastSelectedItemId = item.itemId
//            return@setOnItemSelectedListener true
//        }
//    }
//
//
//    // Kullanıcının online durumunu veritabanında güncelle
//    private fun updateUserStatus(isOnline: Boolean) {
//        val currentUser: FirebaseUser? = auth.currentUser
//        if (currentUser != null) {
//            val userId = currentUser.uid
//            // Kullanıcının durumunu Realtime Database'de güncelle
//            database.child("Kullanicilar").child(userId).child("isOnline").setValue(isOnline)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.d("MainActivity4", "Kullanıcı durumu başarılı şekilde güncellendi.")
//                    } else {
//                        Log.d("MainActivity4", "Kullanıcı durumu güncellenirken hata oluştu: ${task.exception?.message}")
//                    }
//                }
//        }
//    }
//
//    @SuppressLint("SuspiciousIndentation")
//    private fun loadFragment(fragment: Fragment) {
//        val gecis = supportFragmentManager.beginTransaction()
//        gecis.apply {
//            replace(R.id.fragmentContainerView, fragment)
//        }
//        gecis.addToBackStack(null)
//        gecis.commit()
//    }
//}

//class MainActivity4 : AppCompatActivity() {
//    lateinit var binding: ActivityMain4Binding
//    private lateinit var auth: FirebaseAuth // FirebaseAuth nesnesi
//    private lateinit var database: DatabaseReference // Firebase Realtime Database referansı
//    private lateinit var currentUserId: String // Kullanıcı ID'si
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivityMain4Binding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // FirebaseAuth ve FirebaseDatabase'ı başlat
//        auth = FirebaseAuth.getInstance()
//        database = FirebaseDatabase.getInstance().reference
//
//        // Kullanıcıyı kontrol et
//        val currentUser: FirebaseUser? = auth.currentUser
//        currentUser?.let {
//            currentUserId = it.uid
//            // Kullanıcı giriş yaptıysa, online durumunu kontrol et
//            setUserStatusIfNeeded()
//        }
//
//        setupBottomNavigation()
//    }
//
//    // Aşağıdaki işlemler Bottom Navigation için düzenlenmiştir
//    private fun setupBottomNavigation() {
//        val bottomNavigationView = binding.bottomNav
//        var lastSelectedItemId: Int = R.id.home
//        loadFragment(HomeFragment())
//
//        bottomNavigationView.setOnItemSelectedListener { item ->
//            if (item.itemId == lastSelectedItemId) return@setOnItemSelectedListener true
//
//            when (item.itemId) {
//                R.id.home -> {
//                    loadFragment(HomeFragment())
//                    true
//                }
//                R.id.arama -> {
//                    loadFragment(AramaFragment())
//                    true
//                }
//                R.id.profil -> {
//                    loadFragment(ProfilFragment())
//                    true
//                }
//                R.id.chat -> {
//                    loadFragment(ChatFragment())
//                    true
//                }
//                R.id.ekle -> {
//                    loadFragment(EklemeFragment())
//                    true
//                }
//                else -> false
//            }.also { lastSelectedItemId = item.itemId }
//        }
//    }
//
//    // Kullanıcı online mı kontrol et
//    private fun setUserStatusIfNeeded() {
//        val userRef = getUserRef().child("isOnline")
//        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val currentStatus = snapshot.getValue(String::class.java)
//                if (currentStatus == null || currentStatus == "Offline") {
//                    updateUserStatus(true) // Kullanıcıyı online olarak ayarla
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("MainActivity4", "Durum kontrolü sırasında hata oluştu: ${error.message}")
//            }
//        })
//    }
//
//    // Kullanıcının online durumunu Firebase Realtime Database'de güncelle
//    private fun updateUserStatus(isOnline: Boolean) {
//        val status = if (isOnline) "Online" else "Offline"
//        getUserRef().child("isOnline").setValue(status)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.d("MainActivity4", "Kullanıcı durumu başarılı şekilde güncellendi: $status")
//                } else {
//                    Log.e("MainActivity4", "Kullanıcı durumu güncellenirken hata oluştu: ${task.exception?.message}")
//                }
//            }
//    }
//
//    // Kullanıcı referansını almak için yardımcı metod
//    private fun getUserRef(): DatabaseReference {
//        return database.child("Kullanicilar").child(currentUserId)
//    }
//
//    // Uygulama ön planda iken, kullanıcının online durumu
//    override fun onResume() {
//        super.onResume()
//        // Kullanıcıyı online yap
//        updateUserStatus(true)
//
//        // Kullanıcı, uygulama ön planda iken online olarak güncellenmeli
//        getUserRef().child("isOnline").setValue("Online")
//    }
//
//    // Uygulama arka planda iken, kullanıcının offline durumu
//    override fun onPause() {
//        super.onPause()
//        // Kullanıcıyı offline yap
//        updateUserStatus(false)
//
//        // Kullanıcı uygulamayı kapattığında veya başka bir aktiviteye geçtiğinde offline olacak
//        getUserRef().child("isOnline").setValue("Offline")
//    }
//
//    // Fragment yükleme işlemi
//    @SuppressLint("SuspiciousIndentation")
//    private fun loadFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.fragmentContainerView, fragment)
//            addToBackStack(null)
//            commit()
//        }
//    }
//}
class MainActivity4 : AppCompatActivity() {
    lateinit var binding: ActivityMain4Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // FirebaseAuth ve Firestore başlat
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser: FirebaseUser? = auth.currentUser
        currentUser?.let {
            currentUserId = it.uid
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = binding.bottomNav
        var lastSelectedItemId: Int = R.id.home
        loadFragment(HomeFragment())

        bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId == lastSelectedItemId) return@setOnItemSelectedListener true

            when (item.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.arama -> {
                    loadFragment(AramaFragment())
                    true
                }
                R.id.profil -> {
                    loadFragment(EditProfileFragment())
                    true
                }
                R.id.ekle -> {
                    loadFragment(EklemeFragment())
                    true
                }
                else -> false
            }.also { lastSelectedItemId = item.itemId }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUserStatus(true)
    }

    override fun onPause() {
        super.onPause()
        updateUserStatus(false)
    }

    private fun updateUserStatus(isOnline: Boolean) {
        val status = if (isOnline) "Online" else "Offline"
        if (::currentUserId.isInitialized) {
            firestore.collection("Kullanicilar").document(currentUserId)
                .update("isOnline", status)
                .addOnSuccessListener {
                    Log.d("MainActivity4", "Kullanıcı durumu güncellendi: $status")
                }
                .addOnFailureListener {
                    Log.e("MainActivity4", "Kullanıcı durumu güncellenemedi: ${it.message}")
                }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            addToBackStack(null)
            commit()
        }
    }
}






