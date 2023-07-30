package com.funnyproger.chatapp.fragments.fragments_userAccount.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.funnyproger.chatapp.R
import com.funnyproger.chatapp.constants.Constants
import com.funnyproger.chatapp.databinding.ChatsModelBinding
import com.funnyproger.chatapp.fragments.fragments_userAccount.models.SearchModel

class ChatsAdapter : RecyclerView.Adapter<ChatsAdapter.ChatsHolder>() {
    private var chatsList = mutableListOf<SearchModel>()

    class ChatsHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ChatsModelBinding.bind(item)
        fun bind(chat: SearchModel) = with(binding) {
            Glide
                .with(Constants.SearchUserContext)
                .load(chat.userImageSearch)
                .into(idImageViewChatImage)
            idTextViewChatTitle.text = chat.userTitleSearch
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
        holder.itemView.setOnClickListener {
            Toast.makeText(Constants.FirstActivityContext, "Вы выбрали чат ${position}", Toast.LENGTH_SHORT).show()
        }
    }

    fun setList(chatsList: MutableList<SearchModel>) {
        this.chatsList = chatsList
    }

}