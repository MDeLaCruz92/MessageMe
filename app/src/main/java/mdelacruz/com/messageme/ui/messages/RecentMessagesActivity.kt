package mdelacruz.com.messageme.ui.messages

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_recent_messages.*
import mdelacruz.com.messageme.R
import mdelacruz.com.messageme.data.ChatMessage
import mdelacruz.com.messageme.data.User
import mdelacruz.com.messageme.ui.messages.items.RecentMessageRow
import mdelacruz.com.messageme.ui.onboarding.RegisterActivity
import mdelacruz.com.messageme.utils.start

class RecentMessagesActivity : AppCompatActivity() {

  //==============================================================================================
  // Properties
  //==============================================================================================

   companion object {
     const val TAG = "RecentMessages"
     var currentUser: User? = null
   }

  private val recentMessagesMap = HashMap<String, ChatMessage>()
  private val adapter = GroupAdapter<ViewHolder>()

  //==============================================================================================
  // Lifecycle
  //==============================================================================================

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_recent_messages)

    recyclerview_recent_messages.adapter = adapter
    recyclerview_recent_messages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

    // set item click listener on adapter
    adapter.setOnItemClickListener { item, _ ->
      Log.d(TAG, "testing")

      val intent = Intent(this, ChatLogActivity::class.java)

      val row = item as RecentMessageRow

      intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
      start(ChatLogActivity::class.java)
    }

    listenForRecentMessages()
    fetchCurrentUser()
    verifyUserIsLoggedIn()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.nav_menu, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.menu_new_message -> {
        start(NewMessageActivity::class.java)
      }
      R.id.menu_sign_out -> {
        FirebaseAuth.getInstance().signOut()
        startRegisterActivity()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  //==============================================================================================
  // Instance Methods
  //==============================================================================================

  private fun refreshRecyclerViewMessages() {
    adapter.clear()
    recentMessagesMap.values.forEach {
      adapter.add(RecentMessageRow(it))
    }
  }

  private fun startRegisterActivity() {
    val intent = Intent(this, RegisterActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
  }

  //==============================================================================================
  // Firebase
  //==============================================================================================

  private fun listenForRecentMessages() {
    val fromId = FirebaseAuth.getInstance().uid
    val ref = FirebaseDatabase.getInstance().getReference("/recent-messages/$fromId")
    ref.addChildEventListener(object : ChildEventListener {
      override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
        recentMessagesMap[p0.key!!] = chatMessage
        refreshRecyclerViewMessages()
      }

      override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
        recentMessagesMap[p0.key!!] = chatMessage
        refreshRecyclerViewMessages()
      }

      override fun onCancelled(p0: DatabaseError) {}
      override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
      override fun onChildRemoved(p0: DataSnapshot) {}
    })
  }

  private fun fetchCurrentUser() {
    val uid = FirebaseAuth.getInstance().uid
    val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(p0: DataSnapshot) {
        currentUser = p0.getValue(User::class.java)
        Log.d(TAG, "Current user ${currentUser?.profileImageUrl}")
      }

      override fun onCancelled(p0: DatabaseError) {

      }
    })
  }

  private fun verifyUserIsLoggedIn() {
    val uid = FirebaseAuth.getInstance().uid
    if (uid == null) {
      startRegisterActivity()
    }
  }

}
