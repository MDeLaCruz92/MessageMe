package mdelacruz.com.messageme.ui.messages.items

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.recent_messages_row.view.*
import mdelacruz.com.messageme.R
import mdelacruz.com.messageme.data.ChatMessage
import mdelacruz.com.messageme.data.User

class RecentMessageRow(private val chatMessage: ChatMessage): Item<ViewHolder>() {
  var chatPartnerUser: User? = null

  override fun getLayout(): Int {
    return R.layout.recent_messages_row
  }

  override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.message_textview_recent_message.text = chatMessage.text

    val chatPartnerId = if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
      chatMessage.toId
    } else {
      chatMessage.fromId
    }

    val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
    ref.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(p0: DataSnapshot) {
        val user = p0.getValue(User::class.java)
        viewHolder.itemView.username_textview_recent_message.text = chatPartnerUser?.username

        val targetImageView = viewHolder.itemView.imageView_recent_message
        Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImageView)
      }
      override fun onCancelled(p0: DatabaseError) {
      }
    })
  }
}