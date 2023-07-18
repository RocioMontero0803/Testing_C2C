package com.example.testing_.ui.slideshow

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.testing_.R
import com.example.testing_.User
import com.example.testing_.databinding.FragmentSlideshowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.File

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri? = null
    private lateinit var user: User
    private lateinit var uid : String

    //private val packageName = context?.packageName

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        initVars()

        binding.imageView8.setOnClickListener{
            resultLauncher.launch("image/*")
        }


        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if (uid.isNotEmpty()){

            getUserData()
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        binding.sBtn.setOnClickListener{
            val name = binding.etName.text.toString()
            val status = binding.etStatus.text.toString()


            val user = User(name,status)

            if(uid != null){
                databaseReference.child(uid).setValue(user).addOnCompleteListener{

                    if (it.isSuccessful){
                        uploadProfilePic()
                       // uploadImage()
                    }else{
                        Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return root
    }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                binding.etName.setText(user.firstName)
                binding.etStatus.setText(user.lastName)
                uploadProfilePic()
                getUserProfile()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity,"Failed to get user info",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {

        imageUri = it
        binding.imageView8.setImageURI(it)
    }

    private fun initVars() {

        storageReference = FirebaseStorage.getInstance().reference.child("Users")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }


    private fun uploadProfilePic() {
        storageReference = FirebaseStorage.getInstance().getReference("Users/"+auth.currentUser?.uid + ".jpg")
        imageUri?.let {
            storageReference.putFile(it).addOnSuccessListener {
                Toast.makeText(context, "Profile Pic success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUserProfile() {
        storageReference = FirebaseStorage.getInstance().reference.child("Users/${uid}.jpg")
        val localFile = File.createTempFile("tempImage", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.imageView8.setImageBitmap(bitmap)

        }.addOnFailureListener{
            Toast.makeText(activity,"Failed to get image",Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}