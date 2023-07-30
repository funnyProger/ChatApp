package com.funnyproger.chatapp.fragments.fragments_userAccount

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.funnyproger.chatapp.R
import com.funnyproger.chatapp.databinding.FragmentChatsAndMenuBinding
import com.funnyproger.chatapp.fragments.fragments_userAccount.adapter.ChatsAdapter
import com.funnyproger.chatapp.fragments.fragments_userAccount.models.SearchModel

class ChatsAndMenu : Fragment() {
    private lateinit var binding:FragmentChatsAndMenuBinding
    private lateinit var chatsAdapter: ChatsAdapter
    private var chatsList = mutableListOf<SearchModel>()
    private lateinit var controller: NavController
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsAndMenuBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = findNavController()
        chatsAdapter = ChatsAdapter()
        binding.apply {
            idRecyclerView.layoutManager = GridLayoutManager(activity, 1)
            idRecyclerView.adapter = chatsAdapter
            chatsAdapter.setList(chatsList)
        }

        //listeners
        binding.apply {
            idBtnSearch.setOnClickListener {
                onClickSearch()
            }
            idBtnProfile.setOnClickListener {

            }
            idBtnSettings.setOnClickListener {

            }
        }
        var name = ""
        var image: Uri

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK) {
                image = it.data?.getStringExtra("image") as Uri
                name = it!!.data?.getStringExtra("title").toString()
                chatsList.add(SearchModel(image, name))
            }
            chatsAdapter.setList(chatsList)
        }
    }

    private fun onClickSearch() {
        controller.navigate(R.id.action_chatsAndMenu_to_searchUser)
    }



}