package com.funnyproger.chatapp.fragments.fragments_userAccount.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.funnyproger.chatapp.R
import com.funnyproger.chatapp.constants.Constants
import com.funnyproger.chatapp.databinding.SearchModelBinding
import com.funnyproger.chatapp.fragments.fragments_userAccount.ChatsAndMenu
import com.funnyproger.chatapp.fragments.fragments_userAccount.models.SearchModel

class SearchAdapter() : RecyclerView.Adapter<SearchAdapter.SearchHolder>() {
    private var searchUserList = mutableListOf<SearchModel>()
    private var launcher: ActivityResultLauncher<Intent>? = null


    class SearchHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = SearchModelBinding.bind(item)

        fun bind(searchUser: SearchModel) = with(binding) {
            Glide
                .with(Constants.SearchUserContext)
                .load(searchUser.userImageSearch)
                .into(idImageViewSearchUserImage)
            idTextViewSearchUserTitle.text = searchUser.userTitleSearch
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_model, parent, false)
        return SearchHolder(view)
    }

    override fun getItemCount(): Int {
        return searchUserList.size
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.bind(searchUserList[position])

        holder.itemView.setOnClickListener {
            val intent = Intent()
            intent.putExtra("title", searchUserList[position].userTitleSearch)
            intent.putExtra("image", searchUserList[position].userImageSearch)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(searchUserList: MutableList<SearchModel>) {
        this.searchUserList = searchUserList
        notifyDataSetChanged()
    }

}