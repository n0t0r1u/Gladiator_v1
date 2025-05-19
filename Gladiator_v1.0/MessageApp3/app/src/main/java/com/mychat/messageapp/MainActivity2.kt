package com.mychat.messageapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mychat.messageapp.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMain2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth= Firebase.auth
    }


//    fun kayitOl(view: View) {
//        val email = binding.etkayitMail.text.toString()
//        val pass = binding.etkayitPass.text.toString()
//        val kadi=binding.etkadi.text.toString()
//
//        if (email != "" && pass != "") {
//            val intent = Intent(this, MainActivity3::class.java)
//            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//
//                    val kullanici=auth.currentUser
//
//                    val profilGuncelleme = userProfileChangeRequest {
//                        displayName=kadi
//                    }
//
//                    kullanici?.updateProfile(profilGuncelleme)?.addOnCompleteListener { task->
//                        if (task.isSuccessful){
//                            Toast.makeText(applicationContext,"Kullanıcı Adı Eklendi:  $kadi",Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    kullanici!!.sendEmailVerification().addOnCompleteListener { task->
//                        if (task.isSuccessful){
//                            Toast.makeText(applicationContext, "Doğrulama Maili Gönderildi", Toast.LENGTH_LONG).show()
//                        }
//                    }
//                    Toast.makeText(applicationContext, "Başarıyla Kayıt Oldunuz.", Toast.LENGTH_LONG).show()
//                    startActivity(intent)
//                    finish()
//                }
//            }.addOnFailureListener { exception ->
//                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
//                    .show()
//            }
//        }else{
//            Toast.makeText(applicationContext,"Lütfen Boş Alanları Doldurunuz.",Toast.LENGTH_SHORT).show()
//        }
//
//    }
//fun kayitOl(view: View) {
//    val email = binding.etkayitMail.text.toString()
//    val pass = binding.etkayitPass.text.toString()
//    val kadi = binding.etkadi.text.toString()
//
//    if (email.isNotEmpty() && pass.isNotEmpty()) {
//        val intent = Intent(this, MainActivity3::class.java)
//
//        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val kullanici = auth.currentUser
//
//                val profilGuncelleme = userProfileChangeRequest {
//                    displayName = kadi
//                }
//
//                kullanici?.updateProfile(profilGuncelleme)?.addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(
//                            applicationContext,
//                            "Kullanıcı Adı Eklendi: $kadi",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//                kullanici?.sendEmailVerification()?.addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(
//                            applicationContext,
//                            "Doğrulama Maili Gönderildi",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//
//                // Firestore'a kullanıcı bilgilerini ekleme
//                val firestore = FirebaseFirestore.getInstance()
//                val userMap = hashMapOf(
//                    "email" to email,
//                    "kullaniciAdi" to kadi,
//                    "parola" to pass,
//                    "uid" to kullanici?.uid
//                )
//
//                firestore.collection("Kullanicilar")
//                    .document(kullanici!!.uid) // Kullanıcı UID'sini belge adı olarak kullanıyoruz
//                    .set(userMap)
//                    .addOnSuccessListener {
//                        Toast.makeText(applicationContext, "Kullanıcı Bilgileri Kaydedildi.", Toast.LENGTH_SHORT).show()
//                        startActivity(intent)
//                        finish()
//                    }
//                    .addOnFailureListener { exception ->
//                        Toast.makeText(
//                            applicationContext,
//                            "Bilgiler Kaydedilirken Hata: ${exception.localizedMessage}",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//
//                Toast.makeText(applicationContext, "Başarıyla Kayıt Oldunuz.", Toast.LENGTH_LONG).show()
//            }
//        }.addOnFailureListener { exception ->
//            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
//        }
//    } else {
//        Toast.makeText(applicationContext, "Lütfen Boş Alanları Doldurunuz.", Toast.LENGTH_SHORT).show()
//    }
//}
fun kayitOl(view: View) {
    val email = binding.etkayitMail.text.toString().trim()
    val pass = binding.etkayitPass.text.toString().trim()
    val kadi = binding.etkadi.text.toString().trim()

    if (email.isNotEmpty() && pass.isNotEmpty() && kadi.isNotEmpty()) {
        val intent = Intent(this, MainActivity4::class.java)

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val kullanici = auth.currentUser

                val profilGuncelleme = userProfileChangeRequest {
                    displayName = kadi
                }

                kullanici?.updateProfile(profilGuncelleme)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Kullanıcı Adı Eklendi: $kadi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                kullanici?.sendEmailVerification()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Doğrulama Maili Gönderildi",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                // Firestore'a kullanıcı bilgilerini ekleme
                val firestore = FirebaseFirestore.getInstance()
                val userMap = hashMapOf(
                    "email" to email,
                    "kullaniciAdi" to kadi.lowercase(),
                    "parola" to pass,
                    "isOnline" to "Offline",
                    "uid" to kullanici?.uid
                )

                firestore.collection("Kullanicilar")
                    .document(kullanici!!.uid) // Kullanıcı UID'sini belge adı olarak kullanıyoruz
                    .set(userMap)
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, "Kullanıcı Bilgileri Kaydedildi.", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            applicationContext,
                            "Bilgiler Kaydedilirken Hata: ${exception.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                Toast.makeText(applicationContext, "Başarıyla Kayıt Oldunuz.", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    } else {
        when {
            email.isEmpty() -> Toast.makeText(applicationContext, "Lütfen Email Alanını Doldurunuz.", Toast.LENGTH_SHORT).show()
            pass.isEmpty() -> Toast.makeText(applicationContext, "Lütfen Parola Alanını Doldurunuz.", Toast.LENGTH_SHORT).show()
            kadi.isEmpty() -> Toast.makeText(applicationContext, "Lütfen Kullanıcı Adı Alanını Doldurunuz.", Toast.LENGTH_SHORT).show()
        }
    }
}



    fun geriDon(view: View){
        val intent = Intent (this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}