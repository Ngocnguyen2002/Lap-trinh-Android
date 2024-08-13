package com.example.renthouse.adaptar


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.renthouse.Message
import com.example.renthouse.R

class MessageAdapter(
    private val messages: List<Message>,
    private val user: String
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_mess, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentItem = messages[position]
        val isCurrentUser = user == currentItem.from

        // Thiết lập ràng buộc cho layout itemMessRight và itemMessLeft
        val layoutParams = holder.itemView.findViewById<ConstraintLayout>(R.id.constraintLayout10).layoutParams as ConstraintLayout.LayoutParams
        layoutParams.apply {
            if (isCurrentUser) {
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                startToStart = ConstraintLayout.LayoutParams.UNSET
            } else {
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.UNSET
            }
        }
        holder.itemView.findViewById<ConstraintLayout>(R.id.constraintLayout10).layoutParams = layoutParams

        holder.textViewMessage.text = currentItem.mess
    }

    override fun getItemCount() = messages.size

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewMessage: TextView = itemView.findViewById(R.id.textViewMessage)
    }
}
