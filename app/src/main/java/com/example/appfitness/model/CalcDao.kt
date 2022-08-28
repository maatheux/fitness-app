package com.example.appfitness.model

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface CalcDao {

    @Insert
    fun insert(calc: Calc)

    //Delete - @Delete
    //Update - @Update
    //Get - @Query
}