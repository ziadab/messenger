package com.abouelfarah.messenger.messages

import android.support.v7.app.AppCompatActivity
import com.abouelfarah.messenger.R
import android.os.Bundle
import com.abouelfarah.messenger.messages.NewMessage.Companion.USER_NAME
import com.abouelfarah.messenger.models.ChatMessage
import com.abouelfarah.messenger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_row_from.view.*
import kotlinx.android.synthetic.main.chat_row_to.view.*

class ChatLog : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    val username: User = intent.getParcelableExtra<User>(USER_NAME)!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = username.fullname

        chat_log.adapter = adapter

        listenToMessage()

        send_button.setOnClickListener {
            performSending()
        }
    }

    private fun listenToMessage(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages")
        ref.addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(ChatMessage::class.java)
                if (message != null){
                    if(message.uid == FirebaseAuth.getInstance().uid){
                        val currentUser = LastMessageActivity.currentUser
                        adapter.add(ChatItemTo(message.message, currentUser!!))
                    }else{

                        adapter.add(ChatItemFrom(message.message, username))
                    }
                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }


        })
    }

    private fun performSending(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()
        val message = message.text.toString()
        val fromId = FirebaseAuth.getInstance().uid ?: return
        val username = intent.getParcelableExtra<User>(USER_NAME)
        val toId = username.uid

        ref.setValue(ChatMessage(ref.key!!, message, fromId, toId, System.currentTimeMillis()))
    }

}


class ChatItemFrom(val text : String, val user : User) : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_row_from
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.msgTextFrom.text = text
        Picasso.get().load(user.profileImgUrl).into(viewHolder.itemView.profileImageFrom)
    }

}


class ChatItemTo(val text: String, val user: User) : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_row_to
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.msgTextTo.text = text
        Picasso.get().load(user.profileImgUrl).into(viewHolder.itemView.profileImageTo)
    }

}
