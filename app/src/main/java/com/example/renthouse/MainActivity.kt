package com.example.renthouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnLogin = findViewById<Button>(R.id.button)
        btnLogin.setOnClickListener {
            // Chuyển sang trang đăng nhập
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        val btnRegister = findViewById<Button>(R.id.button2)
        btnRegister.setOnClickListener {
            // Chuyển sang trang đăng nhập
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            //finish()
        }
    }
}