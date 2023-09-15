package com.example.railwayconcession.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.railwayconcession.R
import com.example.railwayconcession.firebaseConfig
import com.example.railwayconcession.fragments.Home
import com.example.railwayconcession.model.Users
import com.example.railwayconcession.model.userConccessionDetails
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// this is actual concession form .
class concession_form : AppCompatActivity() {

//    private lateinit var database: DatabaseReference
    private lateinit var clgId: Users

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concession_form)

        val etVoucherNo: EditText = findViewById(R.id.et_voucher_no)
        val etClass: RadioGroup = findViewById(R.id.rg_class)
        val etConcessionPeriod: EditText = findViewById(R.id.et_concesion_period)
        val etAppliedDate: EditText = findViewById(R.id.et_applied_date)
        val etSource: EditText = findViewById(R.id.et_source)
        val etDestination: EditText = findViewById(R.id.et_destination)
        val btnSubmit: Button = findViewById(R.id.btn_submit)

        // Initialize Firebase database
//        database = FirebaseDatabase.getInstance().reference


        val clgId = intent.getStringExtra("clgId")
        Log.i("Info", "$clgId")

        btnSubmit.setOnClickListener {
            val voucherNo: String = etVoucherNo.text.toString()
//            val trainClass : RadioGroup = etClass.checkedRadioButtonId
            val concessionPeriod: String = etConcessionPeriod.text.toString()
            val appliedDate: String = etAppliedDate.text.toString()
            val source: String = etSource.text.toString()
            val destination: String = etDestination.text.toString()

            if (concessionPeriod.isNotEmpty() && appliedDate.isNotEmpty() && source.isNotEmpty() && destination.isNotEmpty()) {
                val users = userConccessionDetails(
                    voucherNo, concessionPeriod, appliedDate, source, destination
                )
                if (clgId != null) {
                    firebaseConfig.createNewUserCListRef(clgId).setValue(users)
//                    database.child("Users").child(clgId).child("ConcessionDetails").setValue(users)
                        .addOnCompleteListener {

                            Toast.makeText(this, "Data insert", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this, Home::class.java))
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Data not insert", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "The value is $clgId", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill empty fields", Toast.LENGTH_SHORT).show()
            }
        }


    }
}