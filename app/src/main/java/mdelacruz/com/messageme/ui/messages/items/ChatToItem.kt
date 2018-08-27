package mdelacruz.com.messageme.ui.messages.items

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_to_row.view.*
import mdelacruz.com.messageme.R
import mdelacruz.com.messageme.data.User

class ChatToItem(val user: User, val text: String): Item<ViewHolder>() {
  override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.textView_to_row.text = text

    val uri = user.profileImageUrl
    val targetImageView = viewHolder.itemView.imageView_to_row
    Picasso.get()
        .load(uri)
        .into(targetImageView)
  }

  override fun getLayout(): Int {
    return R.layout.chat_from_row
  }
}