package com.example.railwayconcession.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.railwayconcession.databinding.FragmentHomeBinding
import com.example.railwayconcession.activities.forms
import com.example.railwayconcession.firebaseConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)

        // clg id
        var auth: FirebaseAuth = Firebase.auth
        val email = auth.currentUser?.email
        val clgId = email?.substring(0,11)?.toUpperCase()

//        var name by remember { mutableStateOf() }
        binding.ClgID.text = clgId.toString()


        binding.btnApplyConcession.setOnClickListener {

            val intent = Intent(this.context, forms::class.java)
            startActivity(intent)

//            navController.navigate(R.id.action_home2_to_forms)

        }

        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//
//        navController = Navigation.findNavController(view)
//
//        super.onViewCreated(view, savedInstanceState)
//    }

}