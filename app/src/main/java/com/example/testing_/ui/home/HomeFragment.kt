package com.example.testing_.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.testing_.ImagesActivity
import com.example.testing_.Posts
import com.example.testing_.R
import com.example.testing_.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initVars()
        registerClickEvents()
//        posts = mutableListOf()
//        firestoreDB = FirebaseFirestore.getInstance()
//        val postsReference = firestoreDB
//            .collection("posts")
//            .limit(20)
//            .orderBy("time_stamp", Query.Direction.DESCENDING)
//        postsReference.addSnapshotListener {snapshot, exception ->
//            if(exception !=null || snapshot == null){
//                Log.e(TAG, "Exception when querying posts", exception)
//                return@addSnapshotListener
//            }
//            val postList = snapshot.toObjects(Posts::class.java)
//            for (post in postList) {
//                Log.i(TAG, "Post ${post}")
//            }
//        }


        return root
    }

    private fun registerClickEvents() {
        binding.uploadBtn.setOnClickListener {
            uploadImage()
        }

        binding.showAllBtn.setOnClickListener {
            val intent = Intent(context, ImagesActivity::class.java)
            startActivity(intent)
            // startActivity(Intent(this, ImagesActivity::class.java))
        }

        binding.imageView.setOnClickListener {
            resultLauncher.launch("image/*")
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {

        imageUri = it
        binding.imageView.setImageURI(it)
    }


    private fun initVars() {

        storageRef = FirebaseStorage.getInstance().reference.child("Images")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    private fun uploadImage() {
        binding.progressBar.visibility = View.VISIBLE
        storageRef = storageRef.child(System.currentTimeMillis().toString())
        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    storageRef.downloadUrl.addOnSuccessListener { uri ->

                        val map = HashMap<String, Any>()
                        map["pic"] = uri.toString()

                        firebaseFirestore.collection("images").add(map)
                            .addOnCompleteListener { firestoreTask ->

                                if (firestoreTask.isSuccessful) {
                                    Toast.makeText(activity, "upload success", Toast.LENGTH_SHORT)
                                        .show()

                                }
                                //else {
//                                    Toast.makeText(
//                                        activity,
//                                        firestoreTask.exception?.message,
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//
//                                }
                                binding.progressBar.visibility = View.GONE
                                binding.imageView.setImageResource(R.drawable.upload_vector)
                            }

                    }
                }
                                else {
                                     Toast.makeText(activity, "upload not successful", Toast.LENGTH_SHORT).show()
                                    binding.progressBar.visibility = View.GONE
                                    binding.imageView.setImageResource(R.drawable.upload_vector)
                                }

    }
}
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}