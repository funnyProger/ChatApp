package com.funnyproger.chatapp.fragments.fragments_userAccount.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.funnyproger.chatapp.constants.Constants
import com.funnyproger.chatapp.databinding.FragmentSearchUserBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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
        nameMap.clear()

        GlobalScope.launch {
            metod()
            cancel()
        }

        binding.apply {
            idRecyclerViewSearchUser.layoutManager = GridLayoutManager(activity, 1)
            idRecyclerViewSearchUser.adapter = searchAdapter
        }
    }

    private fun createSearchTextChangeObservable(idEditTextSearch: EditText): Observable<String> {
        val textChangeObservable = Observable.create<String> {emitter ->
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    emitter.onNext(s.toString())
                }
                override fun afterTextChanged(s: Editable?) = Unit
            }

            idEditTextSearch.addTextChangedListener(textWatcher)
        }
        return textChangeObservable
    }






    private fun createSearchTextObserver(): Observer<String> {
        return object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
            }

            override fun onComplete() {
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onNext(t: String) {
                if(nameMap.isEmpty()) {
                    Toast.makeText(activity, "Data not received :(", Toast.LENGTH_LONG).show()
                } else {
                    searchUserList.clear()
                    if (t == "") {
                        searchAdapter.setList(searchUserList)
                    } else {
                        for (map in nameMap) {
                            if (map.value.contains(t)) {
                                val strRef = storageReference.child("images/${map.key}")
                                strRef.downloadUrl.addOnSuccessListener {
                                    searchUserList.add(SearchModel(it, map.value))
                                }
                                    .addOnFailureListener {
                                        //Toast.makeText(activity, "Data not downloaded :(", Toast.LENGTH_LONG).show()
                                    }
                            }
                        }
                        searchAdapter.setList(searchUserList)
                    }
                }

            }

        }

    }

    private fun getAllUsersFromDB(){
        firebaseFirestore.collection("users")
            .get()
            .addOnSuccessListener {result ->
                for(document in result) {
                    nameMap[document.id] = document.data["userName"].toString()
                }
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Error getting database documents :(", Toast.LENGTH_LONG).show()
            }
    }

    private suspend fun metod() = coroutineScope {
        val job: Job = launch {
            getAllUsersFromDB()
        }
        job.join()

        //create observable and observer, subscribe observer to observable
        binding.apply {
            val observer = createSearchTextObserver()
            val searchTextObservable = createSearchTextChangeObservable(idEditTextSearch)
            searchTextObservable.subscribe(observer)
        }
    }
}