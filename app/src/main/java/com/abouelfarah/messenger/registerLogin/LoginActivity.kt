package com.abouelfarah.messenger.registerLogin

import android.content.Intent
import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import android.support.v7.app.AppCompatActivity
import com.abouelfarah.messenger.R
import com.abouelfarah.messenger.messages.LastMessageActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        Login.setOnClickListener {
            var email = emailLogin.text.toString()
            var pwd = pwdLogin.text.toString()

            if(email.isEmpty() || pwd.isEmpty()){
                Toast.makeText(this, "check email/password",Toast.LENGTH_SHORT).show()
            }else{
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pwd).addOnCompleteListener{
                    Log.d("LoginActivity", "signInWithEmail:success : ")
                    val intent = Intent(this, LastMessageActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)


                   /* //val user = FirebaseAuth.getInstance().currentUser
                    // checkIfEmailVerified()*/

                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    /*private fun checkIfEmailVerified(){
        val user = FirebaseAuth.getInstance().getCurrentUser()
        if(user!!.isEmailVerified()) {
            startActivity(Intent(this, LastMessageActivity::class.java))
            finish()
        }else{
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Your email is not Verified. please go and confirm it.", Toast.LENGTH_SHORT).show()
        }
    }*/


}