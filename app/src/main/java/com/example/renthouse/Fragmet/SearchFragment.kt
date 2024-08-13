package com.example.renthouse.Fragmet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.renthouse.DetailActivity
import com.example.renthouse.ListSearchActivity
import com.example.renthouse.R
import com.example.renthouse.SharedPreferencesUtil.getValueFromSharedPreferences


class SearchFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val input_add = view.findViewById<EditText>(R.id.editTextPhone)
        val input_addw = view.findViewById<EditText>(R.id.editText)
        val btnLogout = view.findViewById<Button>(R.id.button3)
        btnLogout.setOnClickListener {
            val value = context?.getValueFromSharedPreferences("user_info")
            val intent = Intent(requireContext(), ListSearchActivity::class.java).apply {
                putExtra("ITEM_ADD", input_add.text.toString())
                putExtra("ITEM_PRICE", input_addw.text.toString())
                putExtra("ITEM_ID_USER", value?.toString())
            }
            startActivity(intent)
        }

        return view
    }
    companion object {

    }
}