package com.example.wahyustoryapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView

class CreditFragment : Fragment(R.layout.fragment_credit) {
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val credit1 = view.findViewById<TextView>(R.id.credit1)
        val credit2 = view.findViewById<TextView>(R.id.credit2)
        val creditUrl1 =
            "https://www.freepik.com/free-vector/minimal-line-art-hands-vector-floral-orange-pastel-aesthetic-illustration_15845701.htm#page=2&query=aestetic&position=17&from_view=search"
        val creditUrl2 =
            "https://www.freepik.com/free-vector/moon-inside-sun-with-two-open-palm-hands-celestial-black-background_16359181.htm#page=2&query=aestetic&position=22&from_view=search#position=22&page=2&query=aestetic"
        credit1.text = creditUrl1
        credit2.text = creditUrl2

        credit1.setOnClickListener {
            Intent(Intent.ACTION_VIEW).also {
                it.data = Uri.parse(creditUrl1)
                startActivity(it)
            }
        }

        credit2.setOnClickListener {
            Intent(Intent.ACTION_VIEW).also {
                it.data = Uri.parse(creditUrl2)
                startActivity(it)
            }
        }

    }
}