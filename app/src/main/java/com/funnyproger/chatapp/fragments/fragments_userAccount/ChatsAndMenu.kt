package com.funnyproger.chatapp.fragments.fragments_userAccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.funnyproger.chatapp.R
import com.funnyproger.chatapp.databinding.FragmentChatsAndMenuBinding
import com.funnyproger.chatapp.fragments.fragments_userAccount.adapter.ChatsAdapter
import com.funnyproger.chatapp.fragments.fragments_userAccount.models.ChatsModel
import com.funnyproger.chatapp.fragments.fragments_userAccount.models.UserModel

class ChatsAndMenu : Fragment() {
    private lateinit var binding:FragmentChatsAndMenuBinding
    private lateinit var chatsAdapter: ChatsAdapter
    private var chatsList = mutableListOf<ChatsModel>()
    private lateinit var controller: NavController
    private lateinit var userModel: UserModel

    private var chatsMap = mutableMapOf(
        "Alex" to R.drawable.cookies,
        "Ben" to R.drawable.pretzel,
        "Maklll" to R.drawable.cookies,
        "Krio" to R.drawable.cookies,
        "Pattor" to R.drawable.pretzel,
        "Kisa" to R.drawable.cookies,
        "Rafik" to R.drawable.cookies,
        "Bartalameo" to R.drawable.pretzel,
        "Gifty" to R.drawable.cookies,
        "Laxy" to R.drawable.cookies,
        "Strai" to R.drawable.pretzel,
        "Kissdfs" to R.drawable.cookies,
        "Raffsdfs" to R.drawable.cookies,
        "Griie" to R.drawable.pretzel,
        "Psdfkle" to R.drawable.cookies,
        "Cjkker" to R.drawable.cookies,
        "Fuuree" to R.drawable.pretzel,
        )

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
            chatsAdapter.setList(getNewChatsList(chatsMap))
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
    }

    private fun getNewChatsList(chatsMap: MutableMap<String, Int>): MutableList<ChatsModel> {
        chatsList.clear()
        chatsMap.forEach {
            chatsList.add(ChatsModel(it.value, it.key))
        }
        return chatsList
    }


    private fun onClickSearch() {
        controller.navigate(R.id.action_chatsAndMenu_to_searchUser)
    }



}