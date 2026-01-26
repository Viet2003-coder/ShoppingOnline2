package com.example.shoppingonline.ui.product.oder

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingonline.R
import com.example.shoppingonline.ui.product.Auth.AuthViewModel
import com.example.shoppingonline.ui.product.Auth.Login

class OderFragment : Fragment() {
    private val oderModel: OderModel by viewModels()
    private val autModel: AuthViewModel by viewModels()
    private lateinit var oderAdapter: OderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_oder, container, false)
        val rcv=view.findViewById<RecyclerView>(R.id.rvOrders)
        val layoutEmpty=view.findViewById<LinearLayout>(R.id.layoutEmpty)
        autModel.loadUser()
        oderAdapter= OderAdapter{oder ->
            val intent= Intent(requireContext(), OderDetail::class.java)
            intent.apply {
                putExtra("oderId",oder.orderId)
            }
            startActivity(intent)
        }
        rcv.adapter=oderAdapter
        val layoutManager= GridLayoutManager(requireContext(),1)
        rcv.layoutManager=layoutManager
        autModel.user.observe(viewLifecycleOwner){user ->
            if (user==null){
                startActivity(Intent(requireContext(), Login::class.java))
                return@observe
            }
            oderModel._oders.observe(viewLifecycleOwner){orders ->
                if (orders.isEmpty()) {
                    layoutEmpty.visibility = View.VISIBLE
                    rcv.visibility = View.GONE
                } else {
                    layoutEmpty.visibility = View.GONE
                    rcv.visibility = View.VISIBLE
                }
                oderAdapter.submitData(orders)
            }
            oderModel.getOders(user.uid)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        autModel.loadUser()
        autModel.user.observe(viewLifecycleOwner){user ->
            if (user==null){
                startActivity(Intent(requireContext(), Login::class.java))
                return@observe
            }
            oderModel._oders.observe(viewLifecycleOwner){orders ->
                oderAdapter.submitData(orders)
            }
            oderModel.getOders(user.uid)
        }
    }
}