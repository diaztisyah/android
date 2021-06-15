package com.diaztisyah.sisteminformasi.App

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.marufaldi.sisteminformasi.R
import kotlinx.android.synthetic.main.activity_akun.*
import kotlinx.android.synthetic.main.activity_akun.btn_kembali

class AkunActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akun)

        btn_logout_akun.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent (this, LoginActivity::class.java)
            Toast.makeText(this, "Logout Berhasil", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        btn_hapus_akun.setOnClickListener {
            val uid = FirebaseAuth.getInstance().uid ?:""
            val ref = FirebaseDatabase.getInstance().getReference("user/$uid")
            ref.removeValue()
            val user = FirebaseAuth.getInstance().currentUser
            user?.delete()
                ?.addOnCompleteListener{
                    if (it.isSuccessful){
                        Log.d("delete", "delete account")
                        Toast.makeText(this, "Akun Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                    }
                }
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Log.d("delete","Delete")

        }

        btn_kembali.setOnClickListener{
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}
