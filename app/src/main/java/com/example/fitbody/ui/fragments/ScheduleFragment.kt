package com.example.fitbody.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.adapter.ScheduleAdapter
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.Schedule
import com.example.fitbody.model.SimpleResponse
import com.example.fitbody.utils.SessionManager
import com.example.fitbody.utils.WorkoutReceiver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleFragment : Fragment(R.layout.fragment_schedule) {

    private lateinit var btnBack: TextView
    private lateinit var edtDayName: EditText
    private lateinit var edtWorkoutPlan: EditText
    private lateinit var btnAddSchedule: Button
    private lateinit var recyclerSchedule: RecyclerView

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        btnBack = view.findViewById(R.id.btnBack)
        edtDayName = view.findViewById(R.id.edtDayName)
        edtWorkoutPlan = view.findViewById(R.id.edtWorkoutPlan)
        btnAddSchedule = view.findViewById(R.id.btnAddSchedule)
        recyclerSchedule = view.findViewById(R.id.recyclerSchedule)

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        recyclerSchedule.layoutManager =
            LinearLayoutManager(requireContext())

        loadSchedule()

        btnAddSchedule.setOnClickListener {
            addSchedule()
        }
    }

    private fun loadSchedule() {
        val userId =
            SessionManager(requireContext()).getUserId()

        RetrofitClient.instance
            .getSchedule(userId)
            .enqueue(object : Callback<List<Schedule>> {

                override fun onResponse(
                    call: Call<List<Schedule>>,
                    response: Response<List<Schedule>>
                ) {
                    if (response.isSuccessful) {
                        val list =
                            response.body() ?: emptyList()

                        recyclerSchedule.adapter =
                            ScheduleAdapter(
                                list,

                                { schedule ->
                                    deleteSchedule(schedule.id)
                                },

                                { schedule ->
                                    completeSchedule(schedule.id)
                                }
                            )
                    }
                }

                override fun onFailure(
                    call: Call<List<Schedule>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Không tải được lịch tập",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun addSchedule() {
        val dayName =
            edtDayName.text.toString().trim()

        val workoutPlan =
            edtWorkoutPlan.text.toString().trim()

        if (dayName.isEmpty() || workoutPlan.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Vui lòng nhập đầy đủ lịch tập",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val userId =
            SessionManager(requireContext()).getUserId()

        if (userId == 0) {
            Toast.makeText(
                requireContext(),
                "Bạn cần đăng nhập lại",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        RetrofitClient.instance
            .addSchedule(
                userId,
                dayName,
                workoutPlan
            )
            .enqueue(object : Callback<SimpleResponse> {

                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Đã thêm lịch tập",
                        Toast.LENGTH_SHORT
                    ).show()

                    scheduleWorkoutReminder(workoutPlan)

                    edtDayName.text.clear()
                    edtWorkoutPlan.text.clear()

                    loadSchedule()
                }

                override fun onFailure(
                    call: Call<SimpleResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Lỗi kết nối server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun deleteSchedule(
        scheduleId: Int
    ) {
        RetrofitClient.instance
            .deleteSchedule(scheduleId)
            .enqueue(object : Callback<SimpleResponse> {

                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Đã xóa lịch tập",
                        Toast.LENGTH_SHORT
                    ).show()

                    loadSchedule()
                }

                override fun onFailure(
                    call: Call<SimpleResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Lỗi xóa lịch tập",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun completeSchedule(
        scheduleId: Int
    ) {
        RetrofitClient.instance
            .completeSchedule(scheduleId)
            .enqueue(object : Callback<SimpleResponse> {

                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Đã hoàn thành lịch tập",
                        Toast.LENGTH_SHORT
                    ).show()

                    loadSchedule()
                }

                override fun onFailure(
                    call: Call<SimpleResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Lỗi cập nhật trạng thái",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun scheduleWorkoutReminder(
        workoutPlan: String
    ) {
        val intent =
            Intent(
                requireContext(),
                WorkoutReceiver::class.java
            )

        intent.putExtra(
            "message",
            "Đến giờ tập: $workoutPlan 💪"
        )

        val pendingIntent =
            PendingIntent.getBroadcast(
                requireContext(),
                System.currentTimeMillis().toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val alarmManager =
            requireContext().getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 10000,
            pendingIntent
        )

        Toast.makeText(
            requireContext(),
            "FitGym sẽ nhắc bạn sau 10 giây để demo",
            Toast.LENGTH_LONG
        ).show()
    }
}