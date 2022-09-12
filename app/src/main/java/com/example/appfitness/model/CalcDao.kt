package com.example.appfitness.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface CalcDao {

    @Insert
    fun insert(calc: Calc)

    @Query("SELECT * FROM Calc WHERE type = :type")
    fun getRegisterByType(type: String) : List<Calc>

    @Query("DELETE FROM Calc WHERE id = :id")
    fun deleteRegister(id: Int)

    @Query("UPDATE Calc SET res = :res, created_date = :date WHERE id = :id")
    fun updateRegister(id: Int, res: Double, date: Date)

    //Delete - @Delete
    //Update - @Update
}