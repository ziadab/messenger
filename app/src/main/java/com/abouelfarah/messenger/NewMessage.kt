package com.abouelfarah.messenger

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        val adapter = GroupAdapter<ViewHolder>()

        adapter.add(UserItem())

        recyclerViewer.adapter = adapter


    }
}


class UserItem(): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        // xi le"ba
    }

    override fun getLayout(): Int {
        return R.layout.user_new_msg_row
    }

}