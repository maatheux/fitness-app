package com.example.appfitness

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.example.appfitness.model.Calc
import java.lang.Math.pow

class ImcActivity : AppCompatActivity() {

    private lateinit var editWeight: EditText
    private lateinit var editHeight: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc)

        editWeight = findViewById(R.id.edit_imc_weight)
        editHeight = findViewById(R.id.edit_imc_heigth)

        val btnSend: Button = findViewById(R.id.btn_imc_send)
        btnSend.setOnClickListener {
            if (!validate()){
                Toast.makeText(this, R.string.fields_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val imcResult = calculateImc(editWeight.text.toString(), editHeight.text.toString())
            Log.i("Values", "${String.format("%.1f", imcResult)}")

            val imcResponseId = imcResponse(imcResult)

            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.imc_response, imcResult))
                setMessage(imcResponseId)
                setPositiveButton(android.R.string.ok) { dialog, which ->

                }
                setNegativeButton(R.string.save) {dialog, wich ->
                    Thread {
                        val app = application as App
                        val dao = app.db.calcDao()
                        dao.insert(Calc(type = "imc", res = imcResult))

                        runOnUiThread {
                           openListActivity()
                        }

                    }.start()
                }
                create()
                show()
            }

            val service = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            service.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true // irá ficar visivel (true) ou não (false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search) {
            openListActivity()
        }

        return super.onOptionsItemSelected(item) // utilizado qnd tem vários items
    }

    private fun openListActivity() {
        val intent = Intent(this@ImcActivity, ListCalcActivity::class.java)
        intent.putExtra("type", "imc")
        startActivity(intent)
    }

    private fun validate(): Boolean {
        return (
                editWeight.text.toString().isNotEmpty() &&
                !editWeight.text.toString().startsWith("0") &&
                editHeight.text.toString().isNotEmpty() &&
                !editHeight.text.toString().startsWith("0")
        )
    }

    private fun calculateImc(weight: String, height: String): Double {
        return weight.toDouble() / pow(height.toDouble() / 100.0, 2.0)
    }

    @StringRes
    private fun imcResponse(imc: Double): Int {
        when (imc) {
            in 0.0..15.0 -> return R.string.imc_severely_low_weight
            in 15.1..16.0 -> return R.string.imc_very_low_weight
            in 16.1..18.5 -> return R.string.imc_low_weight
            in 18.6..25.0 -> return R.string.normal
            in 25.1..30.0 -> return R.string.imc_high_weight
            in 30.1..35.0 -> return R.string.imc_so_high_weight
            in 35.1..40.0 -> return R.string.imc_severely_high_weight
            else -> return R.string.imc_extreme_weight
        }
    }

}