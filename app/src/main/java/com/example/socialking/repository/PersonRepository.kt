package com.example.socialking.repository

import com.example.socialking.models.Person

class PersonRepository {
    fun getAllData () : List<Person>{
        return listOf(
            Person(
                id = 0,
                firstName = "Noam",
                lastName = "Ramadi",
                age = 25
            ),
            Person(
                id = 1,
                firstName = "Ariel",
                lastName = "Belay",
                age = 12
            ),
            Person(
                id = 2,
                firstName = "Ishay",
                lastName = "Cohen",
                age = 44
            ),Person(
                id = 0,
                firstName = "Noam",
                lastName = "Ramadi",
                age = 25
            ),
            Person(
                id = 1,
                firstName = "Ariel",
                lastName = "Belay",
                age = 12
            ),
            Person(
                id = 2,
                firstName = "Ishay",
                lastName = "Cohen",
                age = 44
            ),Person(
                id = 0,
                firstName = "Noam",
                lastName = "Ramadi",
                age = 25
            ),
            Person(
                id = 1,
                firstName = "Ariel",
                lastName = "Belay",
                age = 12
            ),
            Person(
                id = 2,
                firstName = "Ishay",
                lastName = "Cohen",
                age = 44
            ),
        )
    }
}