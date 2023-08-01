package com.example.testing_.ui.study

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testing_.ui.study.models.DataClass
import com.example.testing_.ui.study.models.MyAdapter
import com.google.firebase.database.*
import java.util.*
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testing_.databinding.FragmentStudyBinding
import com.google.firebase.auth.FirebaseAuth


class StudyFragment : Fragment() {

    private var _binding: FragmentStudyBinding? = null
    var databaseReference: DatabaseReference? = null
    var eventListener: ValueEventListener? = null
    private lateinit var dataList: ArrayList<DataClass>
    private lateinit var adapter: MyAdapter
    private lateinit var uid : String
    private lateinit var auth : FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val gridLayoutManager = GridLayoutManager(activity, 1)
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.search.clearFocus()

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        dataList = ArrayList()
        adapter = activity?.let { MyAdapter(it, dataList) }!!
        binding.recyclerView.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().getReference("Todo List")

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
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        binding.fab.setOnClickListener {
            val intent = Intent(activity, UploadActivity::class.java)
            startActivity(intent)
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference!!.child(uid).child("status").get().addOnSuccessListener {
            Log.i("firebase", "Got role ${it.value}")
            if (it.value == "Student") {
                binding.fab.visibility = View.GONE
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }
        })
        return root
    }

    fun searchList(text: String) {
        val searchList = ArrayList<DataClass>()
        for (dataClass in dataList) {
            if (dataClass.dataPriority?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true
            ) {
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}