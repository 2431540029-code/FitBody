package com.example.fitbody.ui.detail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitbody.R
import com.example.fitbody.adapter.WorkoutAdapter
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.Workout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrainerDetailActivity : AppCompatActivity() {

    private lateinit var recyclerWorkout: RecyclerView
    private lateinit var btnBack: TextView

    private lateinit var edtSearchWorkout: EditText

    private lateinit var btnAll: Button
    private lateinit var btnChest: Button
    private lateinit var btnArm: Button
    private lateinit var btnLeg: Button
    private lateinit var btnBackMuscle: Button
    private lateinit var btnShoulder: Button
    private lateinit var btnCardio: Button

    private var allWorkouts: List<Workout> = emptyList()
    private var currentKeyword: String = ""
    private var currentMuscle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_trainer_detail)

        btnBack = findViewById(R.id.btnBack)
        edtSearchWorkout = findViewById(R.id.edtSearchWorkout)

        btnAll = findViewById(R.id.btnAll)
        btnChest = findViewById(R.id.btnChest)
        btnArm = findViewById(R.id.btnArm)
        btnLeg = findViewById(R.id.btnLeg)
        btnBackMuscle = findViewById(R.id.btnBackMuscle)
        btnShoulder = findViewById(R.id.btnShoulder)
        btnCardio = findViewById(R.id.btnCardio)

        val imgTrainer =
            findViewById<ImageView>(R.id.imgTrainer)

        val txtName =
            findViewById<TextView>(R.id.txtName)

        val txtSpecialty =
            findViewById<TextView>(R.id.txtSpecialty)

        val txtCalories =
            findViewById<TextView>(R.id.txtCalories)

        val txtMuscle =
            findViewById<TextView>(R.id.txtMuscle)

        val txtSchedule =
            findViewById<TextView>(R.id.txtSchedule)

        recyclerWorkout =
            findViewById(R.id.recyclerWorkout)

        btnBack.setOnClickListener {
            finish()
        }

        recyclerWorkout.layoutManager =
            LinearLayoutManager(this)

        val trainerId =
            intent.getIntExtra("trainer_id", 0)

        val name =
            intent.getStringExtra("trainer_name") ?: ""

        val specialty =
            intent.getStringExtra("trainer_specialty") ?: ""

        val image =
            intent.getStringExtra("trainer_image") ?: ""

        val calories =
            intent.getStringExtra("trainer_calories") ?: ""

        val muscle =
            intent.getStringExtra("trainer_muscle") ?: ""

        val schedule =
            intent.getStringExtra("trainer_schedule") ?: ""

        txtName.text = name
        txtSpecialty.text = specialty
        txtCalories.text = calories
        txtMuscle.text = muscle
        txtSchedule.text = schedule

        Glide.with(this)
            .load(image)
            .into(imgTrainer)

        setupSearchAndFilter()

        loadWorkouts(trainerId)
    }

    private fun setupSearchAndFilter() {

        edtSearchWorkout.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    currentKeyword =
                        s.toString().trim()

                    filterWorkouts()
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        btnAll.setOnClickListener {
            currentMuscle = ""
            filterWorkouts()
        }

        btnChest.setOnClickListener {
            currentMuscle = "ngực"
            filterWorkouts()
        }

        btnArm.setOnClickListener {
            currentMuscle = "tay"
            filterWorkouts()
        }

        btnLeg.setOnClickListener {
            currentMuscle = "chân"
            filterWorkouts()
        }

        btnBackMuscle.setOnClickListener {
            currentMuscle = "lưng"
            filterWorkouts()
        }

        btnShoulder.setOnClickListener {
            currentMuscle = "vai"
            filterWorkouts()
        }

        btnCardio.setOnClickListener {
            currentMuscle = "cardio"
            filterWorkouts()
        }
    }

    private fun loadWorkouts(
        trainerId: Int
    ) {
        RetrofitClient.instance
            .getWorkouts(trainerId)
            .enqueue(object : Callback<List<Workout>> {

                override fun onResponse(
                    call: Call<List<Workout>>,
                    response: Response<List<Workout>>
                ) {
                    if (response.isSuccessful) {

                        allWorkouts =
                            response.body() ?: emptyList()

                        recyclerWorkout.adapter =
                            WorkoutAdapter(allWorkouts)
                    }
                }

                override fun onFailure(
                    call: Call<List<Workout>>,
                    t: Throwable
                ) {
                }
            })
    }

    private fun filterWorkouts() {

        var filteredList =
            allWorkouts

        if (currentKeyword.isNotEmpty()) {

            filteredList =
                filteredList.filter { workout ->

                    workout.workout_name.contains(
                        currentKeyword,
                        ignoreCase = true
                    ) ||
                            workout.muscle_group.contains(
                                currentKeyword,
                                ignoreCase = true
                            )
                }
        }

        if (currentMuscle.isNotEmpty()) {

            filteredList =
                filteredList.filter { workout ->

                    workout.muscle_group.contains(
                        currentMuscle,
                        ignoreCase = true
                    )
                }
        }

        recyclerWorkout.adapter =
            WorkoutAdapter(filteredList)
    }
}