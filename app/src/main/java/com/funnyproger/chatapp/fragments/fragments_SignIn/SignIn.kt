package com.funnyproger.chatapp.fragments.fragments_SignIn

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.funnyproger.chatapp.R
import com.funnyproger.chatapp.constants.Constants
import com.funnyproger.chatapp.databinding.FragmentSignInBinding
import com.funnyproger.chatapp.fragments.fragments_userAccount.models.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignIn : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private var launcher: ActivityResultLauncher<Intent>? = null
    private lateinit var controller: NavController
    private lateinit var emailInput: String
    private lateinit var passwordInput: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater)
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = findNavController()

        GlobalScope.launch {
            binding.apply {
                idConstraintSignIn.setOnClickListener {
                    onClickSignIn()
                }
                idConstraintSignUp.setOnClickListener {
                    onClickSignUp()
                }
                btnGoogleSign.setOnClickListener {
                    onClickGoogleSignIn()
                }
                idTextForgetPassword.setOnClickListener {
                    onClickForgetPassword()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            controller.navigate(R.id.chatsAndMenu)
        }
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == AppCompatActivity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                account.idToken?.let { it1 -> fireBaseAuth(it1) }
            }
        }
    }

    private fun fireBaseAuth(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    //переход на новое активити
                    Toast.makeText(activity, "SignIn :)", Toast.LENGTH_SHORT).show()
                    controller.navigate(R.id.chatsAndMenu)
                } else if(it.isCanceled) {
                    Toast.makeText(
                        activity,
                        "his email is already registered :(",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun onClickGoogleSignIn() {
        try {
            val intent = googleSignInClient.signInIntent
            launcher?.launch(intent)
        } catch (ex: Exception) {
            Toast.makeText(activity, "Google failed :(", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickSignIn() {
        emailInput = binding.idPlantTextLogin.text.toString()
        passwordInput = binding.idPlantTextPassword.text.toString()

        Constants.userEmail = emailInput
        Constants.userPassword = passwordInput

        if(emailInput == "" && passwordInput == "") {
            Toast.makeText(
                activity,
                "Enter email and password...",
                Toast.LENGTH_SHORT,
            ).show()
        } else if(emailInput != "" && passwordInput == "") {
            Toast.makeText(
                activity,
                "Enter password...",
                Toast.LENGTH_SHORT,
            ).show()
        } else if(emailInput == "" && passwordInput != "") {
            Toast.makeText(
                activity,
                "Enter email...",
                Toast.LENGTH_SHORT,
            ).show()
        } else if(Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            auth.signInWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "SignIn :)", Toast.LENGTH_SHORT).show()
                        controller.navigate(R.id.action_signIn_to_chatsAndMenu)
                    } else {
                        Toast.makeText(
                            activity,
                            "Incorrect email or password :(",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        } else {
            Toast.makeText(activity, "Incorrect email!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickSignUp() {
        emailInput = binding.idPlantTextLogin.text.toString()
        passwordInput = binding.idPlantTextPassword.text.toString()

        Constants.userEmail = emailInput
        Constants.userPassword = passwordInput

        if(emailInput == "" && passwordInput == "") {
            Toast.makeText(
                activity,
                "Enter email and password...",
                Toast.LENGTH_SHORT,
            ).show()
        } else if(emailInput != "" && passwordInput == "") {
            Toast.makeText(
                activity,
                "Enter password...",
                Toast.LENGTH_SHORT,
            ).show()
        } else if(emailInput == "" && passwordInput != "") {
            Toast.makeText(
                activity,
                "Enter email...",
                Toast.LENGTH_SHORT,
            ).show()
        } else if(Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            auth.createUserWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "SignUp :)", Toast.LENGTH_SHORT).show()
                        Constants.userID = FirebaseAuth.getInstance().uid!!
                        Constants.userEmail = emailInput
                        Constants.userPassword = passwordInput
                        controller.navigate(R.id.action_signIn_to_createProfile)
                    } else {
                        Toast.makeText(
                            activity,
                            "Sign-up failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        } else {
            Toast.makeText(activity, "Incorrect email!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickForgetPassword() {
        binding.apply {
            controller.navigate(R.id.forgotPassword)
        }
    }

}