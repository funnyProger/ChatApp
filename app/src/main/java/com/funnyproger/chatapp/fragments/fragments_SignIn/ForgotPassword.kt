package com.funnyproger.chatapp.fragments.fragments_SignIn

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.funnyproger.chatapp.R
import com.funnyproger.chatapp.databinding.FragmentForgotPasswordBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ForgotPassword : Fragment() {
    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var controller: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = findNavController()

        binding.apply {
            idLayoutSendMessage.setOnClickListener {
                if (Patterns.EMAIL_ADDRESS.matcher(idFragmentEmail.text.toString()).matches()) {
                    sendLetterOnEmail(idFragmentEmail.text.toString())
                } else {
                    Toast.makeText(activity, "Enter valid Email address!", Toast.LENGTH_SHORT).show()
                }


            }
            idImgBtnReturnOnSignFragment.setOnClickListener {
                onClickReturnOnSignFragment()
            }

        }
    }

    private fun sendLetterOnEmail(emailAddress: String) {
        if(emailAddress.isNotEmpty()) {
            try {
                Firebase.auth.sendPasswordResetEmail(emailAddress)
                    .addOnSuccessListener {
                        Toast.makeText(
                            activity,
                            "Check your email please :)",
                            Toast.LENGTH_LONG
                        ).show()
                        controller.navigate(R.id.signIn)
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            activity,
                            "Пользователь с данной почтой не был зарегестрирован :(",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            } catch (e: Exception) {
                Toast.makeText(activity, "Failed :(", Toast.LENGTH_LONG).show()
                println(e)
            }
        } else {
            Toast.makeText(activity, "Enter email...", Toast.LENGTH_LONG).show()
        }
    }


    private fun onClickReturnOnSignFragment() {
        controller.navigate(R.id.signIn)
    }

}