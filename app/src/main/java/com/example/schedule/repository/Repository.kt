package com.example.schedule.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RepositoryInMemoryImpl {

    private var databaseUsersReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private var databaseWeekReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("week")
    private val uid:String = Firebase.auth.currentUser!!.uid

    var dataClass = DataClass(
        emptyList(),
        emptyList(),
    )
    private val data = MutableLiveData(dataClass)

    fun getAll() = data

    fun loadUser() {
        val listener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataClass.users = emptyList()
                dataSnapshot.children.mapNotNull { it.getValue(User::class.java) }.forEach{
                    dataClass.users = dataClass.users.plus(it)
                }
                data.value = dataClass
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        databaseUsersReference.addValueEventListener(listener)
    }

    fun addUser() {
        dataClass.users = dataClass.users.plus(User(uid = uid))
        data.value = dataClass
        databaseUsersReference.child(uid).removeValue()
        databaseUsersReference.child(uid).setValue(User(uid = uid))
    }
    fun editDay(day: Day) {
        dataClass.week.forEach {
            if (it.name == day.name){
                it.lesson = day.lesson
            }
        }
        data.value = dataClass
        databaseWeekReference.setValue(dataClass.week)
    }
    fun editLesson(lesson: Lesson, nameDay: String) {
        if (lesson.uid != "")
            dataClass.week.forEach {day ->
                if (day.name == nameDay)
                    day.lesson.forEach{
                        if (it.uid == lesson.uid){
                            if (lesson.name == "" && lesson.time == "")
                                day.lesson =  day.lesson.filter { f-> f.uid != it.uid }
                                it.apply {
                                    name = lesson.name
                                    time = lesson.time
                             }
                        }
                    }

            }
        else
            dataClass.week.forEach {day ->
                if (day.name == nameDay)
                    day.lesson = day.lesson.plus(lesson.copy(uid = day.lesson.size.toString()))
            }

        data.value = dataClass
        databaseWeekReference.setValue(dataClass.week)
    }
    fun getCurrentUser():User{
        dataClass.users.forEach {
            if (it.uid == uid) return it
        }
        return User()
    }

    fun loadWeek() {
        val listener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataClass.week = emptyList()
                dataSnapshot.children.mapNotNull { it.getValue(Day::class.java) }.forEach{
                    dataClass.week = dataClass.week.plus(it)
                }
                data.value = dataClass
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        databaseWeekReference.addValueEventListener(listener)
//        databaseWeekReference.setValue(
//            listOf(
//                Day(
//                    "Понедельник",
//                    listOf(
//                        Lesson("0","География",""),
//                        Lesson("1","Биология",""),
//                        Lesson("2","ОБЖ",""),
//                        Lesson("3","Физкультура",""),
//                        Lesson("4","Геометрия",""),
//                        Lesson("5","Физика",""),
//                        Lesson("6","Геометрия",""),
//                    )
//                ),
//                Day(
//                    "Вторник",
//                    listOf(
//                        Lesson("0","Физика",""),
//                        Lesson("1","Геометрия",""),
//                        Lesson("2","Физкультура",""),
//                        Lesson("3","Геометрия",""),
//                    )
//                ),
//                Day(
//                    "Среда",
//                    listOf(
//                        Lesson("0","География",""),
//                        Lesson("1","Физика",""),
//                        Lesson("2","Геометрия",""),
//                        Lesson("3","Биология",""),
//                        Lesson("4","Геометрия",""),
//                        Lesson("5","ОБЖ",""),
//                        Lesson("6","Физкультура",""),
//                    )
//                ),
//                Day(
//                    "Четверг",
//                    listOf(
//                        Lesson("0","География",""),
//                        Lesson("1","Биология",""),
//                        Lesson("2","Физкультура",""),
//                        Lesson("3","Физика",""),
//                        Lesson("4","Геометрия",""),
//                    )
//                ),
//                Day(
//                    "Пятница",
//                    listOf(
//                        Lesson("0","География",""),
//                        Lesson("1","Физкультура",""),
//                        Lesson("2","Геометрия",""),
//                        Lesson("3","Физика",""),
//                    )
//                ),
//            )
//        )
    }


}


class DataViewModel : ViewModel() {
    private val repository = RepositoryInMemoryImpl()
    val data = repository.getAll()
    val uid:String = Firebase.auth.currentUser!!.uid
    fun getUser() = repository.getCurrentUser()
    fun addUser() = repository.addUser()
    fun loadUsers() = repository.loadUser()
    fun loadWeek() = repository.loadWeek()
    fun editDay(day: Day) = repository.editDay(day)
    fun editLesson(lesson: Lesson,nameDay: String) = repository.editLesson(lesson, nameDay)
}