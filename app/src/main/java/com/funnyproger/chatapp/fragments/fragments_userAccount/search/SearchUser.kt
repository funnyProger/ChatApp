package com.funnyproger.chatapp.fragments.fragments_userAccount.search

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.funnyproger.chatapp.R
import com.funnyproger.chatapp.constants.Constants
import com.funnyproger.chatapp.databinding.FragmentSearchUserBinding
import com.funnyproger.chatapp.fragments.fragments_userAccount.adapter.SearchAdapter
import com.funnyproger.chatapp.fragments.fragments_userAccount.models.SearchModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.DelicateCoroutinesApi

class SearchUser : Fragment() {
    private lateinit var binding: FragmentSearchUserBinding
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var controller: NavController
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private var searchUserList = mutableListOf<SearchModel>()
    private var nameMap = mutableMapOf<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchUserBinding.inflate(inflater)
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //initialization
        Constants.SearchUserContext = this
        controller = findNavController()
        searchAdapter = SearchAdapter()
        firebaseFirestore = Firebase.firestore
        storageReference = Firebase.storage.reference

        binding.apply {
            idImgBtnReturnOnChatsAndMenuFragment.setOnClickListener {
                controller.navigate(R.id.action_searchUser_to_chatsAndMenu)
            }

            idRecyclerViewSearchUser.layoutManager = GridLayoutManager(activity, 1)
            idRecyclerViewSearchUser.adapter = searchAdapter
        }
        search()
    }


    @SuppressLint("CheckResult")
    private fun search() {
        Observable.create<String> {emitter ->
            binding.idEditTextSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!emitter.isDisposed) {
                        if(s.toString() == "") {
                            nameMap.clear()
                            searchUserList.clear()
                            searchAdapter.setList(searchUserList)
                        } else {
                            nameMap.clear()
                            searchUserList.clear()
                            searchAdapter.setList(searchUserList)
                            emitter.onNext(s.toString())
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d(TAG, "Search: $it")
                    searchUserName(it)
                },
                {
                    Log.e(TAG, "Error: $it")
                },
                {
                    Log.d(TAG, "Complete")
                }
            )
    }

    private fun searchUserName(str: String) {
        firebaseFirestore.collection("users")
            .get()
            .addOnSuccessListener {result ->
                for(document in result) {
                    if(document.data["userName"].toString().contains(str)) {
                        nameMap[document.id] = document.data["userName"].toString()
                    }
                }
                searchUserImage()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Error getting database documents :(", Toast.LENGTH_LONG).show()
            }
    }

    private fun searchUserImage() {
        for (map in nameMap) {
            storageReference.child("images/${map.key}").downloadUrl.addOnSuccessListener {
                searchUserList.add(SearchModel(it, map.value))
            }
                .addOnFailureListener {
                    //Toast.makeText(activity, "Data not downloaded :(", Toast.LENGTH_LONG).show()
                }
        }
        searchAdapter.setList(searchUserList)
    }


}