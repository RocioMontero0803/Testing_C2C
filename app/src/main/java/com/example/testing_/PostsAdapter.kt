package com.example.testing_
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testing_.databinding.EachItemBinding
import com.example.testing_.databinding.FragmentHomeBinding
import com.squareup.picasso.Picasso

class PostsAdapter(private var Posts: List<String>) :
    RecyclerView.Adapter<PostsAdapter.ImagesViewHolder>() {

    inner class ImagesViewHolder(var binding: EachItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding = EachItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return ImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        with(holder.binding){
            with(Posts[position]){
                Picasso.get().load(this).into(imageView)
            }
        }
    }

    override fun getItemCount(): Int {
        return Posts.size
    }
}
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.testing_.databinding.FragmentHomeBinding
//import com.google.firebase.database.core.Context
//
//
//class PostsAdapter(var posts: List<Posts> ) : RecyclerView.Adapter<PostsAdapter.ViewHolder>(){
//    //RecyclerView.ViewHolder(binding.root)
////val context: Context, val posts: List<Posts>
////val context: Context, val binding: List<Posts>
//    inner class ViewHolder(val binding: FragmentHomeBinding) : RecyclerView.ViewHolder(binding.root)
//   // override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//      //  val binding = FragmentHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//       val binding = FragmentHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        with(holder){
//            with(Posts[position]){
//                binding.tvname.text = this.name
//                binding.tv.text = this.description
//            }
//        }
//    }
//
//
//    override fun getItemCount(): Int {
//       return posts.size
//    }
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//       // holder.bind(posts[position])
//       // binding.tvname.text = posts[position].user
//    }
//    inner class ViewHolder(itemView: FragmentHomeBinding) : RecyclerView.ViewHolder(itemView){
//        fun bind(post:Posts){
//            itemView.tvname.text = post.user.name
//            itemView.tvdescription.text = post.description
//        }
//    }
//}