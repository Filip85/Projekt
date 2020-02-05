package com.example.narucime.View.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.narucime.R
import com.example.narucime.SharedPreferences.MyPreference
import com.example.narucime.View.adapters.fragmentsAdapters.MyFragmentAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.title = "My appointments"

        setUpUi()
    }

    private fun setUpUi() {
        verifyUserIsLoggedIn()

        viewPager.adapter =
            MyFragmentAdapter(
                supportFragmentManager
            )
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        Log.d("HomeActivity", uid.toString())

        if(uid == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        else{
            val uid = FirebaseAuth.getInstance().currentUser!!.uid

            //val pref = FirebaseDatabase.getInstance().getReference("username/") //String::class.java
            val ref = FirebaseDatabase.getInstance().getReference("users/${uid}/username")

            ref.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("HomeActivity", "Error")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    //val uid = FirebaseAuth.getInstance().currentUser!!.uid

                    Log.d("filip", uid.toString())

                    val user = p0.getValue(String::class.java)

                    val pref = MyPreference(this@HomeActivity)
                    pref.setUsername(user!!)

                    Log.d("filip", user)
                }

            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, RegistrationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        return super.onOptionsItemSelected(item)
    }
}
