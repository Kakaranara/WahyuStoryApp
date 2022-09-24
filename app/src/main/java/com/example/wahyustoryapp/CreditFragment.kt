package com.example.wahyustoryapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class CreditFragment : Fragment(R.layout.fragment_credit){
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val credit = view.findViewById<TextView>(R.id.allCredit)


        credit.setOnClickListener {
            val creditSrc = "https://www.freepik.com/free-vector/minimal-line-art-hands-vector-floral-orange-pastel-aesthetic-illustration_15845701.htm#page=2&query=aestetic&position=17&from_view=search"
            Intent(Intent.ACTION_VIEW).also {
                it.data = Uri.parse(creditSrc)
                startActivity(it)
            }
        }
        credit.text = """
            <a href="https://www.freepik.com/free-vector/minimal-line-art-hands-vector-floral-orange-pastel-aesthetic-illustration_15845701.htm#page=2&query=aestetic&position=17&from_view=search">Image by rawpixel.com</a> on Freepik
        """.trimIndent()
    }
}