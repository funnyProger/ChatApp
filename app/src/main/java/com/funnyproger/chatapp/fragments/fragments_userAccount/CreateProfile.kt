package com.funnyproger.chatapp.fragments.fragments_userAccount

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.funnyproger.chatapp.R
import com.funnyproger.chatapp.constants.Constants
import com.funnyproger.chatapp.databinding.FragmentCreateProfileBinding
import com.funnyproger.chatapp.fragments.fragments_userAccount.models.UserModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class CreateProfile : Fragment() {
    private lateinit var binding: FragmentCreateProfileBinding
    private lateinit var controller: NavController
    private lateinit var launcher: ActivityResultLauncher<String>
    private lateinit var dataBaseReference: DatabaseReference
    private lateinit var fireStoreReference: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private var userProfileAva: Uri? = null
    private var userProfileName: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initialization
        controller = findNavController()

        dataBaseReference = FirebaseDatabase
            .getInstance("https://chatapp-d0a4f-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users")
        storageReference = Firebase.storage.reference
        fireStoreReference = Firebase.firestore

        launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                binding.idBtnSelectAvatar.setImageURI(it)
                userProfileAva = it
            }
        }

        //listeners
        binding.apply {
            idBtnNext.setOnClickListener {
                if(validateUserName()) {
                    Constants.userName = userProfileName
                    addUserDataToDatabase()
                }
            }
            idBtnSelectAvatar.setOnClickListener {
                launcher.launch("image/*")
            }
        }
    }

    private fun validateUserName(): Boolean {
        binding.apply {
            userProfileName = idProfileName.text.toString()
        }
        return if (userProfileName == "") {
            Toast.makeText(
                activity,
                "Inter Name...",
                Toast.LENGTH_SHORT,
            ).show()
            false
        } else {
            true
        }
    }


    private fun addUserDataToDatabase() {
        //                                  add to realtime database
        /*val userProfileChangeRequest: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
            .setDisplayName(userProfileName).build()
        val firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        firebaseUser.updateProfile(userProfileChangeRequest)
        val user = UserModel(Constants.userID,
            userProfileName, Constants.userEmail, Constants.userPassword)
        dataBaseReference.child(Constants.userID).setValue(user)*/

        val userModel = UserModel(Constants.userID,
            Constants.userName, Constants.userEmail, Constants.userPassword, userProfileAva)

        val userHash = hashMapOf(
            "userID" to userModel.userID,
            "userName" to userModel.userName,
            "userEmail" to userModel.userEmail,
            "userPassword" to userModel.userPassword
        )

        fireStoreReference.collection("users")
            .document(userModel.userID)
            .set(userHash)
            .addOnSuccessListener {
                userProfileAva?.let {
                    storageReference.child("images/${userModel.userID}").putFile(it)
                        .addOnSuccessListener {
                            Toast.makeText(activity, "Data saved successfully!", Toast.LENGTH_LONG).show()
                            controller.navigate(R.id.action_createProfile_to_chatsAndMenu)
                        }.addOnFailureListener {
                            Toast.makeText(activity, "Data not saved!", Toast.LENGTH_LONG).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(activity, "Data not saved!", Toast.LENGTH_LONG).show()
            }



    }
}

