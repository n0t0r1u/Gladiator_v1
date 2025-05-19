package com.mychat.messageapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.mychat.messageapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityMainBinding
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = Firebase.auth
        //val etPass = findViewById<EditText>(R.id.etpass)
        val etPass = binding.etpass

// Başlangıç durumu
        var isPasswordVisible = false

        etPass.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                // Soldaki drawable'a dokunulup dokunulmadığını kontrol et
                if (event.rawX <= (etPass.left + etPass.compoundDrawables[0].bounds.width())) {
                    isPasswordVisible = !isPasswordVisible
                    if (isPasswordVisible) {
                        // Şifreyi göster
                        etPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        etPass.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.lockopen, 0, 0, 0
                        )
                    } else {
                        // Şifreyi gizle
                        etPass.transformationMethod = PasswordTransformationMethod.getInstance()
                        etPass.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.lockofe, 0, 0, 0
                        )
                    }
                    etPass.setSelection(etPass.text.length) // İmleci metnin sonuna taşı
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }

        val kullanici=auth.currentUser

        if (kullanici!=null){
            intent(MainActivity4::class.java)
        }
    }
    fun kayitOl(view: View){
        intent(MainActivity2::class.java)
    }
    fun girisYap(view: View) {
        val mail = binding.etmail.text.toString()
        val pass = binding.etpass.text.toString()

        if (mail.isNotEmpty() && pass.isNotEmpty()) {
            auth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val kullanici = auth.currentUser
                    val firestore = FirebaseFirestore.getInstance()

                    // Firestore'dan kullanıcı bilgilerini al
                    firestore.collection("Kullanicilar")
                        .document(kullanici!!.uid) // Kullanıcının UID'si ile belgeyi bul
                        .get()
                        .addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                val kullaniciAdi = document.getString("kullaniciAdi") ?: "Bilinmiyor"
                                Toast.makeText(
                                    applicationContext,
                                    "Hoşgeldin: $kullaniciAdi",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Başarılı giriş sonrası MainActivity4'e yönlendirme
                                //val intent = Intent(this, MainActivity4::class.java)
                                //startActivity(intent)
                                //finish()
                                intent(MainActivity4::class.java)
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Kullanıcı bilgileri bulunamadı.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                applicationContext,
                                "Bilgiler alınırken hata: ${exception.localizedMessage}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(applicationContext, "Lütfen Boş Alanları Doldurunuz.", Toast.LENGTH_SHORT).show()
        }
    }

    fun intent(targetActivity: Class<*>) {
        val intent = Intent(this, targetActivity)
        startActivity(intent)
        finish()
    }


}