package com.funnyproger.chatapp.fragments.fragments_userAccount.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.funnyproger.chatapp.R
import com.funnyproger.chatapp.databinding.ChatsModelBinding
import com.funnyproger.chatapp.fragments.fragments_userAccount.models.ChatsModel

class ChatsAdapter : RecyclerView.Adapter<ChatsAdapter.ChatsHolder>() {
    private var chatsList = mutableListOf<ChatsModel>()

    class ChatsHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ChatsModelBinding.bind(item)
        fun bind(chat: ChatsModel) = with(binding) {
            idImageViewCharImage.setImageResource(chat.chatImage)
            idTextViewChatTitle.text = chat.chatTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chats_model, parent, false)
        return ChatsHolder(view)
    }

    override fun getItemCount(): Int {
        return chatsList.size
    }

    override fun onBindViewHolder(holder: ChatsHolder, position: Int) {
        holder.bind(chatsList[position])
    }

    fun setList(chatsList: MutableList<ChatsModel>) {
        this.chatsList = chatsList
    }

}