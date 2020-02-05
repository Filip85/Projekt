package com.example.narucime.View.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.R
import com.example.narucime.Repository.CityRepository
import com.example.narucime.View.adapters.recyclerviewAdapters.CityAdapter
import kotlinx.android.synthetic.main.activity_city.*

class CityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)

        setUpUI()
    }

    private fun setUpUI() {
        this.title = "Cities"
        cityRecyclerView.layoutManager =  LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        cityRecyclerView.itemAnimator = DefaultItemAnimator()
        cityRecyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        cityRecyclerView.adapter =
            CityAdapter(CityRepository.cities)
    }
}
