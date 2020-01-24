package com.example.narucime

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.narucime.View.ui.RegistrationActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val detailsIntent: Intent = Intent(this, RegistrationActivity::class.java)
        startActivity(detailsIntent)
    }
}
