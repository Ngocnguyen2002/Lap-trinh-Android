package com.example.renthouse.Fragmet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.renthouse.AboutActivity
import com.example.renthouse.ChangePassActivity
import com.example.renthouse.DieuActivity
import com.example.renthouse.InfoActivity
import com.example.renthouse.MainActivity
import com.example.renthouse.R
import com.example.renthouse.SharedPreferencesUtil.getValueFromSharedPreferences
import com.example.renthouse.SharedPreferencesUtil.removeValueFromSharedPreferences



class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val value = context?.getValueFromSharedPreferences("user_info")
        val btnLogout = view.findViewById<Button>(R.id.button9)
        btnLogout.setOnClickListener {
            context?.removeValueFromSharedPreferences("user_info")
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }

        val btnAbout = view.findViewById<Button>(R.id.button7)
        btnAbout.setOnClickListener {
            val intent = Intent(requireActivity(), AboutActivity::class.java)
            startActivity(intent)
        }
        val btnDk = view.findViewById<Button>(R.id.button8)
        btnDk.setOnClickListener {
            val intent = Intent(requireActivity(), DieuActivity::class.java)
            startActivity(intent)
        }

        val btnChangePass = view.findViewById<Button>(R.id.button6)
        btnChangePass.setOnClickListener {
            val intent = Intent(requireActivity(), ChangePassActivity::class.java).apply {
                putExtra("ID", value?.id.toString());
            }
            startActivity(intent)
        }
        val btnInfo = view.findViewById<Button>(R.id.button5)
        btnInfo.setOnClickListener {
            val intent = Intent(requireActivity(), InfoActivity::class.java).apply {
                putExtra("ID", value?.id.toString());
                putExtra("NAME_U", value?.name);
                putExtra("ADDRESS_U", value?.address.toString());
                putExtra("EMAIL_U", value?.email.toString());
                putExtra("PHONE_U", value?.phone.toString());
            }
            startActivity(intent)
        }



        val name = view.findViewById<TextView>(R.id.textView21)
        name.text = value?.name
        val image = view.findViewById<ImageView>(R.id.imageView3)
        Glide.with(this).load(value?.imageAvt).into(image)
        return view
    }

    companion object {

    }
}