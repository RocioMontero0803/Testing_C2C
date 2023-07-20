package com.example.testing_.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing_.LandingPage
import com.example.testing_.PostsAdapterI
import com.example.testing_.R
import com.example.testing_.User
import com.example.testing_.databinding.FragmentProfileBinding
import com.example.testing_.databinding.FragmentScrollingHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference

class ScrollingHomeFragment : Fragment() {

    private var _binding: FragmentScrollingHomeBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var databaseReference: DatabaseReference
    private var mList = mutableListOf<String>()
    private lateinit var adapter: PostsAdapterI
    private lateinit var uid : String
    private lateinit var user: User

    //private val packageName = context?.packageName

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScrollingHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


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


        binding.fab.setOnClickListener {
            val intent = Intent(activity, LandingPage::class.java)
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
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }

        return root
    }


    private fun initVars() {
        firebaseFirestore = FirebaseFirestore.getInstance()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}