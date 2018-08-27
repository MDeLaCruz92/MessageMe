package mdelacruz.com.messageme.ui.messages.items

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import mdelacruz.com.messageme.R
import mdelacruz.com.messageme.data.User

class ChatFromItem(val text: String, val user: User): Item<ViewHolder>() {
  override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.textView_from_row.text = text

    val uri = user.profileImageUrl
    val targetImageView = viewHolder.itemView.imageView_from_row
    Picasso.get().load(uri).into(targetImageView)
  }

  override fun getLayout(): Int {
    return R.layout.chat_from_row
  }
}