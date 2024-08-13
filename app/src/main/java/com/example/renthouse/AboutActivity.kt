package com.example.renthouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val back : ImageView = findViewById(R.id.imageView28)
        back.setOnClickListener{
            finish()
        }
    }
}