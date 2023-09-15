package com.example.railwayconcession.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.railwayconcession.firebaseConfig
import com.example.railwayconcession.model.userConccessionDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

// here need to add recyclee view to show all applied one
class Views : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    // In Compose world
                    concessionApplicationScreen()

                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun concessionApplicationScreen() {
    var list by remember { mutableStateOf(emptyList<userConccessionDetails>()) }

    DisposableEffect(firebaseConfig.rootReference) {
//        loading = true
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newMessages =
                    snapshot.children.mapNotNull { it.getValue(userConccessionDetails::class.java) }
                Log.d("DataFetch", "Fetched messages: $newMessages")
                list = newMessages
//                loading = false
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DataFetch", "Data fetch error: $error")
//                loading = false
            }
        }
        var auth: FirebaseAuth = Firebase.auth
        val email = auth.currentUser?.email
        val clgId = email?.substring(0, 11)?.toUpperCase()
        firebaseConfig.userRef.child("$clgId/CLIST").addValueEventListener(listener)
//        database.child(studentClassName).child("ALL").addValueEventListener(listener)

        onDispose {
            firebaseConfig.userRef.child("VU1F2122010").removeEventListener(listener)
            Log.d("DataFetch", "Listener removed")
        }
    }
    Column {
        Text(text = "Your Status")
        Divider()
        LazyColumn {
            items(list) { item ->
                ConcessionListItem(item)
            }
        }
    }

}

@Composable
fun ConcessionListItem(
    item: userConccessionDetails,
//    source: String,
//    destination: String,
//    classs: String,
//    duration: String,
//    voucherNo: String,
//    appliedDate: String
) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .wrapContentHeight(), shape = RoundedCornerShape(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start, modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "Source : ")
                    Text(text = item.source.toString())
                }
//                Spacer(modifier = Modifier.weight(1f))
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "Destination : ")
                    Text(text = item.destination.toString())
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start, modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "Class : ")
                    Text(text = "class ")
                }
//                Spacer(modifier = Modifier.weight(1f))
                Row(
                    horizontalArrangement = Arrangement.End, modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "Duration : ")
                    Text(text = item.concessionPeriod.toString())
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(text = "Voucher Number : ")
                Text(text = item.voucherNo.toString())
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(text = "Application Date : ")
                Text(text = item.appliedDate.toString())
            }
        }
    }
}