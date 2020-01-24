package com.example.narucime.Repository

import com.example.narucime.Model.City

object CityRepository {

    val cities: MutableList<City>

    init {
        cities =
            retriveCitites()
    }

    private fun retriveCitites(): MutableList<City> {
        return mutableListOf(
            City(1, "Zagreb"),
            City(2, "Osijek"),
            City(3, "Split"),
            City(4, "Rijeka")
        )
    }
}