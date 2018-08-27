package mdelacruz.com.messageme.ui.messages

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import mdelacruz.com.messageme.R
import mdelacruz.com.messageme.data.User
import mdelacruz.com.messageme.ui.viewholder.UserItem

class NewMessageActivity : AppCompatActivity() {

  companion object {
    const val USER_KEY = "USER_KEY"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_new_message)

    supportActionBar?.title = "Select User"

    fetchUsers()
  }

  private fun fetchUsers() {
    val ref = FirebaseDatabase.getInstance().getReference("/users")
    ref.addListenerForSingleValueEvent(object : ValueEventListener {

      override fun onDataChange(p0: DataSnapshot) {
        val adapter = GroupAdapter<ViewHolder>()

        p0.children.forEach {
          Log.d("NewMessage", it.toString())
          val user = it.getValue(User::class.java)

          if (user != null ) {
            adapter.add(UserItem(user))
          }
        }

        adapter.setOnItemClickListener { item, view ->
          val userItem = item as UserItem
          val intent = Intent(view.context, ChatLogActivity::class.java)
          intent.putExtra(USER_KEY, userItem.user)
          startActivity(intent)
          finish()
        }

        recyclerview_newmessage.adapter = adapter
      }
      override fun onCancelled(p0: DatabaseError) {
      }
    })
  }
}




