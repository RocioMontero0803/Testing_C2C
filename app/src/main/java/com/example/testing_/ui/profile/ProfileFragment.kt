package com.example.testing_.ui.profile

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.testing_.ui.profile.models.User
import com.example.testing_.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri? = null
    private lateinit var user: User
    private lateinit var uid : String
    val userStatus = arrayOf("Student", "Teacher")


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val spinner = binding.spinner
        val arrayAdapter = activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                userStatus
            )
        }
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(
                    activity,
                    "Status selected " + userStatus[position] + " press update",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        // user auth
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        initVars()

        // click on imageview then gets drive image folder
        binding.imageView8.setOnClickListener{
            resultLauncher.launch("image/*")
        }


        // checking if user is not empty
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if (uid.isNotEmpty()){

            getUserData()
        }

        // allows the binding of text box to user input
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        binding.sBtn.setOnClickListener{
            val name = binding.etName.text.toString()
            val status = binding.etStatus.text.toString()
            val select = binding.spinner.selectedItem.toString()


            val user = User(name,status, select)

            // checking if there is a user and if so gets the function to upload pic
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

    // this allows for the information to be snapshot and stay on page
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
    // launches the drive to get the pic url
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {

        imageUri = it
        binding.imageView8.setImageURI(it)
    }

    // accesses the storage
    private fun initVars() {

        storageReference = FirebaseStorage.getInstance().reference.child("Users")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

// uploads the picture to firebase
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

    // allows to get image from firebase
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