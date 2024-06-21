package com.example.schedule.repository
data class DataClass(
    var users: List<User>,
    var week: List<Day>
)

data class User(
    var uid: String = "",
    var isAdmin:Boolean = false,
)
data class Day(
    var name: String = "",
   var lesson: List<Lesson> = emptyList()
)
data class Lesson(
    var uid:String = "",
    var name:String = "",
    var time:String = "",
)