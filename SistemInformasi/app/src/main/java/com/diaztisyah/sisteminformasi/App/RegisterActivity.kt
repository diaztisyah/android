package com.diaztisyah.sisteminformasi.App

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.marufaldi.sisteminformasi.R
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        register_button_register.setOnClickListener{
            PerformRegister()
        }
        already_have_account_text_view.setOnClickListener{
            Log.d("RegisterActivity", "Try to show login activity")
            //ini untuk jalanin login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        selectphoto_button_register.setOnClickListener {
            Log.d("RegisterActivity","Try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)

        }
    }

    var SelectedPhotoUri: Uri?=null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==0 && resultCode == Activity.RESULT_OK&& data != null){
            Log.d("RegisterActivity", "Photo Was Selected")
            Toast.makeText(this, "Foto Berhasil Dipilih", Toast.LENGTH_SHORT).show()

            SelectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, SelectedPhotoUri)

            selectphoto_imageview_register.setImageBitmap(bitmap)

            selectphoto_button_register.alpha=0f
            // val bitmapDrawable = BitmapDrawable(bitmap)
            //selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }
    private fun PerformRegister(){
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Tolong Masukan Email dan Password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "Email is: "+ email)
        Log.d("RegisterActivity","Password: $password")

        //Firebase Authentication to create a user with email adn password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(!it.isSuccessful)return@addOnCompleteListener

                //eles if successfull
                Log.d("Main","Succesfully created user with uid: ${it.result?.user?.uid}")
                Toast.makeText(this, "Akun Berhasil Dibuat", Toast.LENGTH_SHORT).show()
                uploadImageToFirebaseStorage()
                val intent = Intent (this, LoginActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener{
                Log.d("Main", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Akun Gagal Dibuat ${it.message}", Toast.LENGTH_SHORT).show()
            }


    }
    private fun uploadImageToFirebaseStorage(){
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(SelectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Register", "Seccesfully upload images: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    it.toString()
                    Log.d("RegisterActivity", "File Location: $it")
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener{
                //do some logging here
            }
    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/user/$uid")

        val user = user(
            uid,
            username_edittext_register.text.toString()
        )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Finaly we saved the user to firebase database")
            }
    }

    class user(val email: String, val username: String)
}

