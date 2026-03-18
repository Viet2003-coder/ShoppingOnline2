package com.example.shoppingonline.respository

import android.content.Context
import com.example.shoppingonline.CheckOnline
import com.example.shoppingonline.data.model.Comment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class CommentRepository {
    private val dbFirebase = FirebaseDatabase.getInstance().reference
    suspend fun addToComment(context: Context, comment: Comment): Result<String> {
        val sucessString = "Đã thêm bình luận"
        if (!CheckOnline.isOnline(context)) {
            return Result.failure(Exception("Vui lòng kết nối internet"))
        }
        return runCatching {
            dbFirebase.child("comments").child(comment.productId).push().setValue(comment)
            sucessString
        }
    }
    // Bỏ suspend vì Listener sẽ tự chạy ngầm và gọi callback khi có dữ liệu mới
    fun getComments(productId: String, onResult: (List<Comment>) -> Unit) {
        dbFirebase.child("comments").child(productId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listComments = mutableListOf<Comment>()
                    for (childSnapshot in snapshot.children) {
                        val comment = childSnapshot.getValue(Comment::class.java)
                        comment?.let { listComments.add(it) }
                    }
                    // Đảo ngược danh sách để cái mới nhất lên đầu
                    onResult(listComments.reversed())
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý lỗi nếu cần (ví dụ: log error)
                }
            })
    }

}