package com.example.shoppingonline.ui.product.Comment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingonline.Model.Product
import com.example.shoppingonline.R
import com.example.shoppingonline.data.model.Comment

class CommentAdapter(): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    private val list = mutableListOf<Comment>()
    @SuppressLint("NotifyDataSetChanged")
    fun submitData(data: List<Comment>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment,parent,false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CommentViewHolder,
        position: Int
    ) {
        val comment=list[position]
        holder.userName.text=comment.userName
        holder.ratingBar.rating=comment.rating
        holder.content.text=comment.content
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName=itemView.findViewById<TextView>(R.id.tvCommentUser)
        val ratingBar=itemView.findViewById<RatingBar>(R.id.ratingSmall)
        val content=itemView.findViewById<TextView>(R.id.tvCommentContent)
    }
}