package com.example.narucime.View.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.narucime.Model.User
import com.example.narucime.R
import com.example.narucime.SharedPreferences.MyPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        setUpUi()
    }

    private fun setUpUi() {

        alreadyHaveAnAccount.setOnClickListener {
            val detailsIntent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(detailsIntent)
        }

        buttonReg.setOnClickListener {
            val email = emailReg.text.toString()
            val password = passwordReg.text.toString()

            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email or password!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(!it.isSuccessful) {
                        return@addOnCompleteListener
                    }

                    uploadUserToFirebaseDatabase()

                    Log.d("RegistrationActivity", "Successfully created user with uid: ${it.result?.user?.uid}")
                }
            .addOnFailureListener {
                Log.d("RegistrationActivity", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun uploadUserToFirebaseDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")

        Log.d("RegistrationActivity",ref.toString())

        val user = User(uid, userReg.text.toString())

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegistrationActivity", "User saved to Firebase Database")

                val myPreference = MyPreference(this)
                myPreference.setCityName(user.username)

                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d("RegistrationActivity", "Failed to save user to Firebase Database")
            }
    }
}

//class User(val uid: String, val username: String) //napraviti geter za username
