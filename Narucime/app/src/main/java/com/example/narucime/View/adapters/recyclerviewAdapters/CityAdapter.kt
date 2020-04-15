package com.example.narucime.View.adapters.recyclerviewAdapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.Context.MyApplication
import com.example.narucime.R
import com.example.narucime.SharedPreferences.MyPreference
import com.example.narucime.View.ui.HospitalsActivity
import kotlinx.android.synthetic.main.item_city.view.*

class CityAdapter(val cities: MutableList<String>): RecyclerView.Adapter<CityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val cityView = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        val cityHolder =
            CityHolder(cityView)
        return cityHolder
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        val city = cities[position]
        holder.bind(city)
        Log.d("CityAdapter", city.toString())
    }

}

class CityHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(city: String) {
        itemView.city.text = city

        itemView.setOnClickListener {
            //val pos = adapterPosition
            Log.d("nekiGrad", city.toString())


            val intent = Intent(MyApplication.ApplicationContext, HospitalsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(HospitalsActivity.CITYNAME, city)

            val myPreference = MyPreference(MyApplication.ApplicationContext)
            myPreference.setCityName(city)

            MyApplication.ApplicationContext.startActivity(intent)
        }
    }
}
