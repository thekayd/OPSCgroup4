package com.kayodedaniel.signinloginpoe

data class UserTask(
    val id: String?,
    val name: String,
    val category: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val description: String,
    val minGoal: Double,
    val maxGoal: Double,
    val username: String
)
