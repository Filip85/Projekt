package com.example.narucime

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.SharedPreferences.MyPreference
import kotlinx.android.synthetic.main.item_examination.view.*

class ExaminationAdapter(val exams: MutableList<String>): RecyclerView.Adapter<ExaminationHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExaminationHolder {
        val examinationView = LayoutInflater.from(parent.context).inflate(R.layout.item_examination, parent,false)
        val examinationHolder = ExaminationHolder(examinationView)
        return examinationHolder
    }

    override fun getItemCount(): Int {
        return exams.size
    }

    override fun onBindViewHolder(holder: ExaminationHolder, position: Int) {
        val examination = exams[position]
        holder.bind(examination)
    }
}

class ExaminationHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(exams: String) {
        itemView.examination.text = exams

        itemView.setOnClickListener {

            val position = adapterPosition + 1

            val myPreference = MyPreference(MyApplication.ApplicationContext)
            myPreference.setExamination(exams)

            val intent = Intent(MyApplication.ApplicationContext, CalendarActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(CalendarActivity.POSITION, position.toString())



            MyApplication.ApplicationContext.startActivity(intent)
        }
    }
}

