package com.example.testing_

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.testing_.databinding.ActivityImagesBinding
import com.example.testing_.databinding.ActivityLandingPageBinding
import com.example.testing_.databinding.FragmentHomeBinding
import com.example.testing_.ui.home.ScrollingHomeFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class LandingPage : AppCompatActivity() {

    private lateinit var binding: ActivityLandingPageBinding
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri? = null
    private var title: String? = ""
    private var description: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVars()
        registerClickEvents()

    }
    private fun registerClickEvents() {
        binding.uploadBtn.setOnClickListener {
            title = binding.tvnamepost.text.toString()
            description = binding.tvdescription.text.toString()
            uploadImage()
        }

        binding.showAllBtn.setOnClickListener {
            val intent = Intent(this@LandingPage, MainNavActivity::class.java)
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
                        map["title"] = title.toString()
                        map["description"] = description.toString()

                        firebaseFirestore.collection("images").add(map)
                            .addOnCompleteListener { firestoreTask ->

                                if (firestoreTask.isSuccessful) {
                                    Toast.makeText(this, "upload success", Toast.LENGTH_SHORT)
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
                                binding.tvnamepost.setText("")
                                binding.tvdescription.setText("")
                            }

                    }
                }
                else {
                    Toast.makeText(this, "upload not successful", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    binding.imageView.setImageResource(R.drawable.upload_vector)
                }

            }
        }
    }


}