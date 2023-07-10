package com.example.testing_

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.testing_.databinding.ActivityProfileInputBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileInput : AppCompatActivity() {
    private lateinit var binding: ActivityProfileInputBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        binding.backBtn.setOnClickListener {
            val intent = Intent(this, ShowProfile::class.java)
            startActivity(intent)
        }
        binding.sBtn.setOnClickListener {
            val name = binding.etName.text.toString()
            val status = binding.etStatus.text.toString()

            val user = User(name, status)

            if (uid != null) {
                databaseReference.child(uid).setValue(user).addOnCompleteListener {

                    if (it.isSuccessful) {
                        uploadProfilePic()
                    } else {
                        Toast.makeText(this@ProfileInput, "Failed to update profile", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

//        val textView: TextView = binding.textSlideshow
//        slideshowViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }


        }

        private fun uploadProfilePic() {
            imageUri = Uri.parse("android.resource://$packageName/${R.drawable.profile_icon}")
            storageReference = FirebaseStorage.getInstance().getReference("Users/"+auth.currentUser?.uid + ".jpg")
            storageReference.putFile(imageUri).addOnSuccessListener {
                Toast.makeText(this@ProfileInput, "Profile success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this@ProfileInput, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }


}