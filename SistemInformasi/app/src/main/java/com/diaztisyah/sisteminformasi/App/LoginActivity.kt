package com.diaztisyah.sisteminformasi.App

import android.app.Service
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.marufaldi.sisteminformasi.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity(){

    var context = this
    var connectivity: ConnectivityManager? = null
    var info: NetworkInfo? = null

    fun isConnected(): Boolean {

        connectivity = context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            info = connectivity!!.activeNetworkInfo
            if (info != null) {
                if (info!!.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener{
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()

            Log.d("Login", "Attempt login with email or password: $email/***"+email)

            //Firebase Authentication to create a user with email adn password
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{

                    if (!it.isSuccessful){ return@addOnCompleteListener
                        val intent = Intent (this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    else
                        Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent (this, DashboardActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener{
                    Log.d("Main", "Failed Login: ${it.message}")
                    Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show()


                }

        }
        back_to_register_textview.setOnClickListener{
            val intent = Intent (this, RegisterActivity::class.java)
            startActivity(intent)
        }
        btn_cek_koneksi.setOnClickListener {
            if (isConnected()) {
                Toast.makeText(context, "Anda Terhubung Dengan Internet", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Anda Tidak Terhubung Dengan Internet", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}