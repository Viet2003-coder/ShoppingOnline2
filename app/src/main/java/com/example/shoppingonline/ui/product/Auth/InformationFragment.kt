package com.example.shoppingonline.ui.product.Auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.shoppingonline.R
import com.example.shoppingonline.UserSession
import com.google.firebase.auth.FirebaseAuth

class InformationFragment : Fragment(R.layout.fragment_information) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        val user = UserSession.currentUser
        if (user == null) {
            // Chưa login → đá về Login
            startActivity(Intent(requireContext(), Login::class.java))
            requireActivity().finish()
            return
        }

        tvName.text = user.name
        tvEmail.text = user.email

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            UserSession.currentUser = null
            startActivity(Intent(requireContext(), Login::class.java))
            requireActivity().finish()
        }
    }
}