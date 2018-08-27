package mdelacruz.com.messageme.ui.messages.items

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import mdelacruz.com.messageme.R

class ChatItem: Item<ViewHolder>() {

  override fun getLayout(): Int {
    return R.layout.chat_from_row
  }

  override fun bind(viewHolder: ViewHolder, position: Int) {

  }
}