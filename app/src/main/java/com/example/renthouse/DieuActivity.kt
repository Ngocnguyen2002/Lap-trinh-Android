package com.example.renthouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class DieuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dieu)
        val back : ImageView = findViewById(R.id.imageView29)
        back.setOnClickListener{
            finish()
        }
    }
}