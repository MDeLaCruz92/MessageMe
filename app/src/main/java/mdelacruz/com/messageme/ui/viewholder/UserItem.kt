package mdelacruz.com.messageme.ui.viewholder

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import mdelacruz.com.messageme.R
import mdelacruz.com.messageme.data.User

class UserItem(val user: User): Item<ViewHolder>() {
  override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.username_textview_new_message.text = user.username

    Picasso.get()
        .load(user.profileImageUrl)
        .into(viewHolder.itemView.imageView_new_message)
  }

  override fun getLayout(): Int {
    return R.layout.user_row_new_message
  }
}