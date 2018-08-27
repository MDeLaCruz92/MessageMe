package mdelacruz.com.messageme.ui.messages

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import mdelacruz.com.messageme.R
import mdelacruz.com.messageme.data.ChatMessage
import mdelacruz.com.messageme.data.User
import mdelacruz.com.messageme.ui.messages.items.ChatFromItem
import mdelacruz.com.messageme.ui.messages.items.ChatToItem
import mdelacruz.com.messageme.utils.textString

class ChatLogActivity : AppCompatActivity() {

  //==============================================================================================
  // Properties
  //==============================================================================================

  companion object {
    const val TAG = "ChatLog"
  }

  val adapter = GroupAdapter<ViewHolder>()
  var toUser: User? = null

  //==============================================================================================
  // Lifecycle
  //==============================================================================================

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat_log)

    recyclerview_chat_log.adapter = adapter

    toUser = intent.getParcelableExtra(NewMessageActivity.USER_KEY)
    supportActionBar?.title = toUser?.username

    listenForMessages()

    send_button_chat_log.setOnClickListener {
      Log.d(TAG, "Attempt to send message..")
      performSendMessage()
    }
  }

  //==============================================================================================
  // Instance Methods
  //==============================================================================================

  private fun listenForMessages() {
    val fromId = FirebaseAuth.getInstance().uid
    val toId = toUser?.uid
    val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

    ref.addChildEventListener(object : ChildEventListener {
      override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        val chatMessage = p0.getValue(ChatMessage::class.java)

        if (chatMessage != null) {
          Log.d(TAG, chatMessage.text)

          if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
            val currentUser = RecentMessagesActivity.currentUser ?: return
            adapter.add(ChatFromItem(chatMessage.text, currentUser))
          } else {
            adapter.add(ChatToItem(toUser!!, chatMessage.text))
          }
        }

        recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
      }

      override fun onCancelled(p0: DatabaseError) {
      }
      override fun onChildMoved(p0: DataSnapshot, p1: String?) {
      }
      override fun onChildChanged(p0: DataSnapshot, p1: String?) {
      }
      override fun onChildRemoved(p0: DataSnapshot) {
      }
    })
  }

  private fun performSendMessage() {
    val text = editText_chat_log.textString()

    val fromId = FirebaseAuth.getInstance().uid
    val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
    val toId = user.uid

    if (fromId == null) return

    val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
    val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

    val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
    reference.setValue(chatMessage)
        .addOnSuccessListener {
          Log.d(TAG, "Saved our chat message: ${reference.key}")
          editText_chat_log.text.clear()
          recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1) // scroll down to recent message.
        }
    toReference.setValue(chatMessage)

    val recentMessageRef = FirebaseDatabase.getInstance().getReference("recent-messages/$fromId/$toId")
    recentMessageRef.setValue(chatMessage)

    val recentMessageToRef = FirebaseDatabase.getInstance().getReference("recent-messages/$toId/$fromId")
    recentMessageToRef.setValue(chatMessage)

  }

}