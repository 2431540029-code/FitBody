package com.example.fitbody.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.adapter.WorkoutAdapter
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.Workout
import com.example.fitbody.ui.detail.WorkoutDetailActivity
import com.example.fitbody.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var recyclerFavorite: RecyclerView

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        recyclerFavorite =
            view.findViewById(R.id.recyclerFavorite)

        recyclerFavorite.layoutManager =
            LinearLayoutManager(requireContext())

        loadFavorites()
    }

    private fun loadFavorites() {

        val session =
            SessionManager(requireContext())

        val userId =
            session.getUserId()

        RetrofitClient.instance
            .getWorkoutFavorites(userId)
            .enqueue(object : Callback<List<Workout>> {

                override fun onResponse(
                    call: Call<List<Workout>>,
                    response: Response<List<Workout>>
                ) {

                    if (response.isSuccessful) {

                        val list =
                            response.body() ?: emptyList()

                        recyclerFavorite.adapter =
                            WorkoutAdapter(list)
                    }
                }

                override fun onFailure(
                    call: Call<List<Workout>>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        requireContext(),
                        "Không tải được bài tập yêu thích",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}