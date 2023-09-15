package com.example.railwayconcession.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.railwayconcession.activities.concession_form
import com.example.railwayconcession.model.Users
import com.example.railwayconcession.databinding.PersonalFormBinding
import com.example.railwayconcession.firebaseConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.Locale

// this is initial activity
// stores the basic data
class forms : AppCompatActivity() {

//    private lateinit var database: DatabaseReference
    private lateinit var binding: PersonalFormBinding
    private lateinit var auth : FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PersonalFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        database = FirebaseDatabase.getInstance().getReference("Users")
        auth = Firebase.auth

        val tvNext: TextView = binding.tvNext
        val etFullName: TextView = binding.etFullName
        val etDivision: TextView = binding.etDivision
        val etClgId: TextView = binding.etClgId


        val currentUser = auth.currentUser

        var auth: FirebaseAuth = Firebase.auth
        val email = auth.currentUser?.email
        val clgId = email?.substring(0,11)?.toUpperCase()

        etClgId.text = clgId.toString()
        etClgId.isEnabled = false
        tvNext.setOnClickListener {
            val fullname = etFullName.text.toString()
            val division = etDivision.text.toString()
            val clgId = etClgId.text.toString().uppercase(Locale.ROOT)
            val userId = currentUser?.uid


            if (fullname.isNotEmpty() && division.isNotEmpty()) {
                val usersData = Users(fullname,division, clgId, userId) // Use clgId here
                firebaseConfig.createNewUserDetailsRef(clgId).setValue(usersData) // Use clgId here
                    .addOnCompleteListener {
                        Toast.makeText(this, "Data insert", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, concession_form::class.java)
                        intent.putExtra("clgId", clgId)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Data not insert", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Fill empty fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
