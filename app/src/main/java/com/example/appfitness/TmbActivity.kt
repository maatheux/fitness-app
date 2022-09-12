package com.example.appfitness

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.appfitness.model.Calc
import java.util.*

class TmbActivity : AppCompatActivity() {

    private lateinit var lifestyle: AutoCompleteTextView
    private lateinit var editWeight: EditText
    private lateinit var editHeight: EditText
    private lateinit var editAge: EditText
    private lateinit var items: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmb)

        editWeight = findViewById(R.id.edit_tmb_weight)
        editHeight = findViewById(R.id.edit_tmb_heigth)
        editAge = findViewById(R.id.edit_tmb_age)

        lifestyle = findViewById(R.id.auto_lifestyle)
        items = resources.getStringArray(R.array.tmb_lifestyle)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        lifestyle.setAdapter(adapter)

        val btnSend: Button = findViewById(R.id.btn_imc_send)

        btnSend.setOnClickListener {
            if (!validate()){
                Toast.makeText(this, R.string.fields_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val weight = editWeight.text.toString().toInt()
            val height = editHeight.text.toString().toInt()
            val age = editAge.text.toString().toInt()

            val result = calculateTmb(weight, height, age)
            val response = tmbRequest(result)

            AlertDialog.Builder(this).apply {
                setMessage(getString(R.string.tmb_response, response))
                setPositiveButton(android.R.string.ok) { dialog, which ->

                }
                setNegativeButton(R.string.save) {dialog, wich ->
                    Thread {

                        val isUpdate = intent?.extras?.getString("Is Update")
                        val newId = intent?.extras?.getString("Id")

                        val app = application as App
                        val dao = app.db.calcDao()

                        isUpdate?.let {
                            dao.updateRegister(newId?.toInt() ?: 0, result, Date())
                            Log.i("Update successfully", "yes")
                            openListActivity()
                            return@Thread
                        }

                        dao.insert(Calc(type = "tmb", res = result))

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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search) {
            finish()
            openListActivity()
        }

        return super.onOptionsItemSelected(item) // utilizado qnd tem vários items
    }

    private fun openListActivity() {
        val intent = Intent(this, ListCalcActivity::class.java)
        intent.putExtra("type", "tmb")
        startActivity(intent)
    }

    private fun validate(): Boolean {
        return (
                editWeight.text.toString().isNotEmpty() &&
                !editWeight.text.toString().startsWith("0") &&
                editHeight.text.toString().isNotEmpty() &&
                !editHeight.text.toString().startsWith("0") &&
                editAge.text.toString().isNotEmpty() &&
                !editAge.text.toString().startsWith("0")
        )
    }

    private fun calculateTmb(weight: Int, height: Int, age: Int): Double {
        return 66 + (13.8 * weight) + (5 * height) - (6.8 * age)
    }

    private fun tmbRequest(tmb: Double) : Double {
        return when (lifestyle.text.toString()) {
            items[0] -> tmb * 1.2
            items[1] -> tmb * 1.375
            items[2] -> tmb * 1.55
            items[3] -> tmb * 1.725
            items[4] -> tmb * 1.9
            else -> 0.0
        }
    }

}