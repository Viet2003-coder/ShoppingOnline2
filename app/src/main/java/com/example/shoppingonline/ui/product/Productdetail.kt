package com.example.shoppingonline.ui.product

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingonline.Model.CartItem
import com.example.shoppingonline.Model.User
import com.example.shoppingonline.R
import com.example.shoppingonline.Translate
import com.example.shoppingonline.data.model.Comment
import com.example.shoppingonline.databinding.ActivityProductdetailBinding
import com.example.shoppingonline.ui.product.Auth.AuthViewModel
import com.example.shoppingonline.ui.product.Auth.Login
import com.example.shoppingonline.ui.product.Comment.CommentAdapter
import com.example.shoppingonline.ui.product.Comment.CommentViewModel
import com.example.shoppingonline.ui.product.cart.CartViewModel
import com.example.shoppingonline.ui.product.oder.BuyActivity

class Productdetail : AppCompatActivity() {
    private lateinit var binding: ActivityProductdetailBinding
    private val viewModel: CartViewModel by viewModels()
    private val AuthModel: AuthViewModel by viewModels()
    private var currenUser: User?=null
    private lateinit var adapterComment: CommentAdapter
    private var id:String=""
    private val commentViewModel: CommentViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return CommentViewModel(com.example.shoppingonline.respository.CommentRepository()) as T
            }
        }
    }
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProductdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val title =intent.getStringExtra("product_title")?:""
        id =intent.getStringExtra("product_id")?:""
        val price=intent.getStringExtra("product_price")?.toDoubleOrNull() ?:0.0
        val des= intent.getStringExtra("product_des")?:""
        val imgLink=intent.getStringExtra("product_img")?:""
        val recyclerComment = findViewById<RecyclerView>(R.id.recyclerComments)
        AuthModel.loadUser()
        AuthModel.user.observe(this){user ->
            currenUser=user
        }
        if (id.isEmpty()){
            Toast.makeText(this, "Product ID is null", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        binding.tvTitle.text=title
        binding.tvPrice.text=price.toString()
        binding.tvDesdetail.text=des
        Translate.translateEnToVi(des){
            runOnUiThread {
                binding.tvDesdetail.text=it
            }
        }

        Glide.with(this@Productdetail).load(imgLink)
            .placeholder(R.drawable.outline_download_for_offline_24)
            .error(R.drawable.outline_hide_image_24)
            .into(binding.imgProduct)
        //hiển thị comments
        adapterComment = CommentAdapter()
        recyclerComment.layoutManager = GridLayoutManager(this, 1)
        recyclerComment.adapter = adapterComment
        commentViewModel.comments.observe(this) {
            adapterComment.submitData(it)
        }
        commentViewModel.fetchComments(id)
        //add Comment
        binding.btnSendComment.setOnClickListener {
            addComment()
        }
        commentViewModel.message.observe(this){
            Log.e("Lỗi hiển thị listttttttttttttttttttttttttttttttttttttttt",it)
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.btnAddToCart.setOnClickListener {
                val user=currenUser
                if (user==null){
                    startActivity(Intent(this, Login::class.java))
                    return@setOnClickListener
                }
                val userId= user.uid
                val cart= CartItem(userId , id, title, price, imgLink,des)
                viewModel.addToCart(this,cart)
        }
        viewModel.messages.observe(this){message->
            Toast.makeText(this,message.toString(), Toast.LENGTH_SHORT).show()
        }
        binding.btnBuyNow.setOnClickListener {
            val intent1= Intent(this@Productdetail, BuyActivity::class.java)
            intent1.putExtra("product_id",id)
            startActivity(intent1)
        }
    }
    private fun addComment(){
        val user=currenUser
        if (user==null){
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Login::class.java))
            return
        }
        if (binding.edtComment.text.toString().isEmpty()){
            Toast.makeText(this, "Vui lòng nhập bình luận", Toast.LENGTH_SHORT).show()
            return
        }
        val comment= Comment(user.uid,user.name?:"Người dùng",id,binding.edtComment.text.toString(),binding.ratingInput.rating,System.currentTimeMillis(),user.avatarUrl)
        commentViewModel.addComment(this,comment)
        binding.edtComment.text.clear()
        binding.ratingInput.rating=5f
    }
}