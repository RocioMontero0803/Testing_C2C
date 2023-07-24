package com.example.testing_


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testing_.databinding.ActivityMainBinding


import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.*
import java.util.*


class MainActivity : AppCompatActivity() {
    var databaseReference: DatabaseReference? = null
    var eventListener: ValueEventListener? = null
    private lateinit var dataList: ArrayList<DataClass>
    private lateinit var adapter: MyAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayoutManager = GridLayoutManager(this@MainActivity, 1)
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.search.clearFocus()

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        dataList = ArrayList()
        adapter = MyAdapter(this@MainActivity, dataList)
        binding.recyclerView.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().getReference("Todo List")
        dialog.show()

        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataClass::class.java)
                    if (dataClass != null) {
                        dataList.add(dataClass)
                    }
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })

        binding.fab.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@MainActivity, UploadActivity::class.java)
            startActivity(intent)
        })

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }
        })
    }
    fun searchList(text: String) {
        val searchList = java.util.ArrayList<DataClass>()
        for (dataClass in dataList) {
            if (dataClass.dataPriority?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true
            ) {
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)
    }
}
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//    }
//
//}
//package com.example.testing_
//
//import android.os.Bundle
//import com.google.android.material.snackbar.Snackbar
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.WindowCompat
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.navigateUp
//import androidx.navigation.ui.setupActionBarWithNavController
//import android.view.Menu
//import android.view.MenuItem
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import com.example.testing_.databinding.ActivityMainBinding
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityMainBinding
//
//    lateinit var username : EditText
//    lateinit var password: EditText
//    lateinit var loginButton: Button
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        binding.loginButton.setOnClickListener(View.OnClickListener {
//            if (binding.username.text.toString() == "user" && binding.password.text.toString() == "1234"){
//                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//}

