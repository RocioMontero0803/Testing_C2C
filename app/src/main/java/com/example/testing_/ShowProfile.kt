package com.example.testing_

import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testing_.databinding.ActivityShowProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class ShowProfile : AppCompatActivity() {
    private lateinit var binding : ActivityShowProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var dialog: Dialog
    private lateinit var user: User
    private lateinit var uid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if (uid.isNotEmpty()){

            getUserData()
        }
        binding.updateBtn.setOnClickListener {
            val intent = Intent(this, ProfileInput::class.java)
            startActivity(intent)
        }
        binding.NavBtn.setOnClickListener{
            val intent = Intent(this, MainNavActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                binding.tvname.text = user.name
                binding.tvstatus.text = user.status
                getUserProfile()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ShowProfile,"Failed to get user info",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getUserProfile() {
        storageReference = FirebaseStorage.getInstance().reference.child("Users/$uid.jpg")
        val localFile = File.createTempFile("tempImage", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.imageView8.setImageBitmap(bitmap)

        }.addOnFailureListener{
            Toast.makeText(this@ShowProfile,"Failed to get image",Toast.LENGTH_SHORT).show()
        }
    }
}