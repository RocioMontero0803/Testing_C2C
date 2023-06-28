package com.example.testing_


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

}
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

