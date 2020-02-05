package com.example.narucime.View.adapters.recyclerviewAdapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.Context.MyApplication
import com.example.narucime.R
import com.example.narucime.SharedPreferences.MyPreference
import com.example.narucime.View.ui.ExamsActivity
import kotlinx.android.synthetic.main.item_hospital.view.*

class HospitalAdapter(val hospitals: MutableList<String>, val cityname: String): RecyclerView.Adapter<HospitalHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalHolder {
        val hospitalView = LayoutInflater.from(parent.context).inflate(R.layout.item_hospital, parent, false)
        val hospitalHolder =
            HospitalHolder(hospitalView)
        return hospitalHolder
    }

    override fun getItemCount(): Int {
        return hospitals.size
    }

    override fun onBindViewHolder(holder: HospitalHolder, position: Int) {
        val hospital = hospitals[position]
        val cityname = cityname
        holder.bind(hospital, cityname)
    }
}

class HospitalHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(hospital: String, cityname: String) {
        itemView.hospital.text = hospital

        itemView.setOnClickListener{

            val myPreference = MyPreference(MyApplication.ApplicationContext)
            myPreference.setHospitalName(hospital)

            val intent = Intent(MyApplication.ApplicationContext, ExamsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(ExamsActivity.HOSPITALNAME, hospital)
            intent.putExtra(ExamsActivity.CITYNAMEE, cityname)

            MyApplication.ApplicationContext.startActivity(intent)
        }
    }
}
