package com.example.railwayconcession.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.railwayconcession.activities.login
import com.example.railwayconcession.databinding.FragmentProfileBinding
import com.example.railwayconcession.firebaseConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile, container, false)

        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        //firebase auth for user
        auth = Firebase.auth

        //prog bar
        val progBar: ProgressBar = binding.progressBar

        binding.logOutButton.setOnClickListener {

            progBar.visibility = View.VISIBLE

            auth.signOut()


            activity?.finish()
            startActivity(Intent(this.context, login::class.java))
        }


        binding.notifications.setOnClickListener {

        }

        val currentUser = auth.currentUser
//        val clgId = arguments?.getString("clgId")

        var auth: FirebaseAuth = Firebase.auth
        val email = auth.currentUser?.email
        val clgId = email?.substring(0, 11)?.toUpperCase()
// Check if the user is authenticated
        if (currentUser != null && clgId != null) {
//            val userId = currentUser.uid
            val databaseReference = firebaseConfig.userRef.child("$clgId/USERD")

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Retrieve user data from the snapshot
                        val userName = snapshot.child("fullname").getValue(String::class.java)
                        val userClass = snapshot.child("division").getValue(String::class.java)
                        val userIdNo = snapshot.child("clgId").getValue(String::class.java)
//                        val userEmail = snapshot.child("email").getValue(String::class.java)
//                        val userContactNo = snapshot.child("contactNo").getValue(String::class.java)

                        // Update the TextViews with user data
                        binding.profileName.text = userName
                        binding.prfileClass.text = userClass
                        binding.profileClgId.text = userIdNo
//                        binding.userEmailTextView.text = userEmail
//                        binding.userContactNoTextView.text = userContactNo
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database read error
                }
            })
        }


        return binding.root
    }

}