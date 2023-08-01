package com.example.testing_.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
//import com.example.testing_.PostAdapter
//import com.example.testing_.daos.PostDao
import com.example.testing_.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
//    private lateinit var postDao: PostDao
//    private lateinit var adapter: PostAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        binding.fab.setOnClickListener{
//            val intent = Intent(activity, CreatePostActivity::class.java)
//            startActivity(intent)
//        }
     //   setUpRecyclerView()
        return root
    }
//    private fun setUpRecyclerView() {
//        postDao = PostDao()
//        val postsCollections = postDao.postCollections
//        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
//        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()
//
//        adapter = PostAdapter(recyclerViewOptions, this)
//
//        binding.recyclerViewPost.adapter = adapter
//        binding.recyclerViewPost.layoutManager = LinearLayoutManager(activity)
//    }
//
//    override fun onStart() {
//        super.onStart()
//        adapter.startListening()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        adapter.stopListening()
//    }
//
//    override fun onLikeClicked(postId: String) {
//        postDao.updateLikes(postId)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}