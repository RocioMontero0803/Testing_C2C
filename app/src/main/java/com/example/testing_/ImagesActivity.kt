package com.example.testing_

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing_.databinding.ActivityImagesBinding
//import com.example.testing_.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.example.testing_.User
import com.example.testing_.ui.home.ScrollingHomeFragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ImagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImagesBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var databaseReference: DatabaseReference
    private var mList = mutableListOf<String>()
    private lateinit var adapter: PostsAdapterI
    private lateinit var uid : String
    private lateinit var auth : FirebaseAuth
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()


     //   databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//        if(uid != null && user.status == "teacher"){
//        databaseReference.child(uid).setValue(user.status).addOnCompleteListener {
////                    uploadProfilePic()
//            // uploadImage()
//            binding.fab.setOnClickListener {
//                val intent = Intent(this, HomeFragment::class.java)
//                startActivity(intent)
//            }
//        }
//       }

        binding.fab.setOnClickListener{
            val intent = Intent(this, ScrollingHomeFragment::class.java)
            startActivity(intent)
        }


        initVars()
        getImages()

        // user role for upload function hides it if not teacher
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(uid).child("status").get().addOnSuccessListener {
            Log.i("firebase", "Got role ${it.value}")
            if (it.value == "Student") {
                binding.fab.visibility = View.GONE
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }


    private fun initVars() {
        firebaseFirestore = FirebaseFirestore.getInstance()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
       adapter = PostsAdapterI(mList)
        binding.recyclerView.adapter = adapter

    }



    @SuppressLint("NotifyDataSetChanged")
    private fun getImages(){
        binding.progressBar.visibility = View.VISIBLE
        firebaseFirestore.collection("images")
            .get().addOnSuccessListener {
                for(i in it){
                    mList.add(i.data["pic"].toString())
                }
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }
    }

}