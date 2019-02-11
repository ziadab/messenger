package com.abouelfarah.messenger

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.register_activity.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)


        registerMe.setOnClickListener {
            //var name = nameInput.text.toString()
            val email = email.text.toString()
            val pwd = password.text.toString()
            val repwd = password_repeat.text.toString()

            if(email.isEmpty() || pwd.isEmpty() || repwd.isEmpty()) {
                Toast.makeText(this, "Please check your email/password",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{

            if(repwd == pwd){
                /*Log.d("Activity", "Name: $name")
                Log.d("Activity","Email: $email")
                Log.d("Activity", "Password: $pwd")*/
                // firebase here
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pwd).addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener
                    Log.d("RegisterActivity","Successfull shit is done : ${it.result!!.user.uid}")
                    sendEmailVerification()
                    uploadImageToFireBase()
                    startActivity(Intent(this, LastMessageActivity::class.java))

                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
            }

        }

        alreadyHaveAccount.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        imgPicker.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }


    }

    var selectedPhotoUri:Uri ?= null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            imgSelected.setImageBitmap(bitmap)
            imgPicker.alpha = 0f
            // val bitDrawable = BitmapDrawable(bitmap)
            // imgPicker.setBackgroundDrawable(bitDrawable)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadImageToFireBase(){
        if(selectedPhotoUri == null) return

        var filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
            Log.d("RegisterActivity", "Successfuly uploaded : ${it.metadata?.path}")
            ref.downloadUrl.addOnSuccessListener {
                saveUserToFirebaseDatabase(it.toString())
                Log.d("RegisterActivity", "file location : $it")
                //saveUserToFirebaseDatabase(it.toString())
            }
        }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl:String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/${uid.toString()}")
        val user = User(nameInput.text.toString(), uid, email.text.toString(), password.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
            Log.d("RegisterActivity", "The shit is saved in database")
                val intent = Intent(this, LastMessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
        }

        /*val database = FirebaseDatabase.getInstance()
val myRef = database.getReference("message")

myRef.setValue("Hello, World!")*/

    }


    private fun sendEmailVerification(){

        val user = FirebaseAuth.getInstance().getCurrentUser()
        user!!.sendEmailVerification().addOnSuccessListener {
            Toast.makeText(this, "Please confirme your email", Toast.LENGTH_LONG)
           Log.d("SendingEmail", "Email Send Successfully")
        }
    }

}

class User(val fullname:String, val uid:String, val email:String, val password:String, val profileImgUrl:String){
    constructor() : this("", "", "", "", "")
}