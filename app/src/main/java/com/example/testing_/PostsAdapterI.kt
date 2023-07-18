package com.example.testing_
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testing_.databinding.EachItemBinding
import com.example.testing_.databinding.FragmentHomeBinding
import com.squareup.picasso.Picasso

class PostsAdapterI(private var Posts: List<String>) :
    RecyclerView.Adapter<PostsAdapterI.ImagesViewHolder>() {

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