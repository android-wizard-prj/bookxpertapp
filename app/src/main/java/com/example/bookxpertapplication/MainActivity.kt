package com.example.bookxpertapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookxpertapplication.models.AuthViewModel
import com.example.bookxpertapplication.models.UserViewModel
import com.example.bookxpertapplication.roomdatabase.UserEntity
import com.example.bookxpertapplication.screens.HomeScreen
import com.example.bookxpertapplication.screens.LoginScreen
import com.example.bookxpertapplication.ui.theme.BookXpertApplicationTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: com.google.android.gms.auth.api.signin.GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup Google Sign-In Options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // from Firebase project settings
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            BookXpertApplicationTheme {
                val authViewModel: AuthViewModel = viewModel()
                val currentUser by authViewModel.currentUser.collectAsState()
                val userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

                // Launcher for Google Sign-In intent
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.result
                        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                        Log.d("pp..", "idtoken ${account.idToken}")
                        Log.d("pp..", "credential $credential")
                        FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnSuccessListener {
                                Log.d("pp..", "inside addOnSuccessListener")
                                authViewModel.updateUser(FirebaseAuth.getInstance().currentUser)
                                Log.d("pp..", "currentUser $currentUser")
                                val firebaseUser = FirebaseAuth.getInstance().currentUser
                                if (firebaseUser != null) {
                                    val user = UserEntity(
                                        uid = firebaseUser.uid,
                                        name = firebaseUser.displayName,
                                        email = firebaseUser.email,
                                        profilePicUrl = firebaseUser.photoUrl?.toString()
                                    )
                                    userViewModel.saveUser(user)

                                }

                            }
                    } catch (e: Exception) {
                        Log.e("SignIn", "Sign-in failed", e)
                    }

                }

                // UI Rendering
                if (currentUser != null) {
                    HomeScreen(user = currentUser!!)
                } else {
                    LoginScreen(
                        onSignInClick = {
                            val signInIntent = googleSignInClient.signInIntent
                            launcher.launch(signInIntent)
                        }
                    )
                }
            }
        }
    }
}
