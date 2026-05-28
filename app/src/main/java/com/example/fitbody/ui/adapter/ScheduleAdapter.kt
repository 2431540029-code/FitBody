package com.example.fitbody.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.model.Schedule

class ScheduleAdapter(

    private val list: List<Schedule>,

    private val onDeleteClick: (Schedule) -> Unit,

    private val onCompleteClick: (Schedule) -> Unit

) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtDayName: TextView =
            view.findViewById(R.id.txtDayName)

        val txtWorkoutPlan: TextView =
            view.findViewById(R.id.txtWorkoutPlan)

        val txtStatus: TextView =
            view.findViewById(R.id.txtStatus)

        val txtDelete: TextView =
            view.findViewById(R.id.txtDelete)

        val txtComplete: TextView =
            view.findViewById(R.id.txtComplete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_schedule,
                    parent,
                    false
                )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val schedule =
            list[position]

        holder.txtDayName.text =
            schedule.day_name

        holder.txtWorkoutPlan.text =
            schedule.workout_plan

        holder.txtStatus.text =
            schedule.status

        holder.txtDelete.setOnClickListener {
            onDeleteClick(schedule)
        }

        holder.txtComplete.setOnClickListener {
            onCompleteClick(schedule)
        }
    }
}