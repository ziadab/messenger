package com.abouelfarah.messenger.messages

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.abouelfarah.messenger.R
import com.abouelfarah.messenger.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_new_msg_row.view.*
import java.io.Serializable

class NewMessage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        fetchUser()

    }


    private fun fetchUser(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage",it.toString())
                    val user = it.getValue(User::class.java)
                    if(user != null){
                        adapter.add(UserItem(user))
                    }

                }

                adapter.setOnItemClickListener { item, view ->

                    val user = item as UserItem

                    val intent = Intent(view.context, ChatLog::class.java)
                    intent.putExtra("FULLNAME", user.user.fullname)
                    intent.putExtra("UID", user.user.uid)
                    intent.putExtra("EMAIL", user.user.email)
                    intent.putExtra("PASSWORD", user.user.password)
                    intent.putExtra("PROFIL_URL", user.user.profileImgUrl)
                    startActivity(intent)
                    finish()
                }

                recyclerViewer.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }
}


class UserItem(val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username.text = user.fullname
        Picasso.get().load(user.profileImgUrl).into(viewHolder.itemView.userimage)
    }

    override fun getLayout(): Int {
        return R.layout.user_new_msg_row
    }

}