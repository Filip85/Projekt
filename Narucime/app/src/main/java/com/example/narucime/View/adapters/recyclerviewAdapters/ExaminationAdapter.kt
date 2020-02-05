package com.example.narucime.View.adapters.recyclerviewAdapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.Context.MyApplication
import com.example.narucime.R
import com.example.narucime.SharedPreferences.MyPreference
import com.example.narucime.View.ui.CalendarActivity
import kotlinx.android.synthetic.main.item_examination.view.*

class ExaminationAdapter(val exams: MutableList<String>, val cityname: String, val hospitalname: String): RecyclerView.Adapter<ExaminationHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExaminationHolder {
        val examinationView = LayoutInflater.from(parent.context).inflate(R.layout.item_examination, parent,false)
        val examinationHolder =
            ExaminationHolder(
                examinationView
            )
        return examinationHolder
    }

    override fun getItemCount(): Int {
        return (exams.size - 1)
    }

    override fun onBindViewHolder(holder: ExaminationHolder, position: Int) {
        val examination = exams[position]
        val cityname: String = cityname
        val hospitalname: String = hospitalname
        holder.bind(examination, cityname, hospitalname)
    }
}

class ExaminationHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(exams: String, cityname: String, hospitalname: String) {
        /*if(exams != "address") {
            itemView.examination.text = exams
        }*/

        itemView.examination.text = exams

        itemView.setOnClickListener {

            val position = adapterPosition + 1

            val myPreference = MyPreference(MyApplication.ApplicationContext)
            myPreference.setExamination(exams)

            val intent = Intent(MyApplication.ApplicationContext, CalendarActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(CalendarActivity.POSITION, position.toString())
            intent.putExtra(CalendarActivity.CITY, cityname)
            intent.putExtra(CalendarActivity.HOSPITALNAME, hospitalname)
            intent.putExtra(CalendarActivity.EXAMINATION, exams)

            MyApplication.ApplicationContext.startActivity(intent)
        }
    }
}

