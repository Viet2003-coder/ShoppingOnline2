package com.example.shoppingonline.ui.product.Auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.shoppingonline.Model.User
import com.example.shoppingonline.R
import com.example.shoppingonline.UserSession
import com.example.shoppingonline.ui.product.cart.CartModel
import com.example.shoppingonline.ui.product.oder.OderModel
import com.google.firebase.auth.FirebaseAuth

class InformationFragment : Fragment(R.layout.fragment_information) {
    private val authViewModel: AuthViewModel by viewModels()
    private val cartModel: CartModel by viewModels()
    private val oderModel: OderModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val edtName = view.findViewById<EditText>(R.id.edtName)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val tvQuatityOder = view.findViewById<TextView>(R.id.tvOrderCount)
        val tvQuatityCart = view.findViewById<TextView>(R.id.tvCartCount)
        val edtPhone = view.findViewById<EditText>(R.id.edtPhone)
        val btnEditPhone = view.findViewById<ImageButton>(R.id.btnEditPhone)
        val btnEditName = view.findViewById<ImageButton>(R.id.btnEditName)
        val btnResetPassword = view.findViewById<Button>(R.id.btnChangePassword)
        authViewModel.loadUser()
        authViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                // Chưa login → đá về Login
                startActivity(Intent(requireContext(), Login::class.java))
                requireActivity().finish()
                return@observe
            }
            setupEditableField(edtName,btnEditName,{name->
                authViewModel.updateField(user.uid,"name",name)
            })
            setupEditableField(edtPhone,btnEditPhone,{phone->
                authViewModel.updateField(user.uid,"phone",phone)
            })
            oderModel.getOders(user.uid)
            cartModel.loadCart(user.uid)
            btnResetPassword.setOnClickListener {
                authViewModel.resetPassword(user.email)
            }
            // Có user rồi → dùng luôn
            edtName.setText(user.name)
            tvEmail.text = user.email
            edtPhone.setText(user.phone)
            if (user.phone.isEmpty()){
                edtPhone.hint="Vui lòng thêm số điện thoại!!!"
            }
                    btnLogout.setOnClickListener {
                        authViewModel.logOut {
                            val intent = Intent(requireContext(), Login::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    }
                }
        oderModel.orderCount.observe(viewLifecycleOwner) {
            tvQuatityOder.text = it.toString()
        }
        cartModel.cartCount.observe(viewLifecycleOwner) {
            tvQuatityCart.text = it.toString()
        }
        authViewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(),it.toString(), Toast.LENGTH_SHORT).show()
        }
            }
    fun setupEditableField(
        editText: EditText,
        button: ImageButton,
        onSave: (String) -> Unit
    ) {
        var isEditing = false
        button.setOnClickListener {
            if (!isEditing) {
                // 👉 Edit
                isEditing = true
                editText.isEnabled = true
                editText.requestFocus()
                button.setImageResource(R.drawable.outline_save_as_24)
            } else {
                // 👉 Save
                val value = editText.text.toString().trim()
                if (value.isEmpty()) {
                    editText.error = "Không được để trống"
                    return@setOnClickListener
                }
                onSave(value)
                isEditing = false
                editText.isEnabled = false
                button.setImageResource(R.drawable.outline_edit_24)
            }
        }
    }
}