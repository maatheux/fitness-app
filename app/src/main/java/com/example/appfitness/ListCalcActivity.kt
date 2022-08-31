package com.example.appfitness

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appfitness.model.Calc
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ListCalcActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)

        val type = intent?.extras?.getString("type") ?: throw IllegalStateException("type not found")

        Thread {
            val app = application as App
            val dao = app.db.calcDao()
            val response = dao.getRegisterByType(type)

            runOnUiThread {
                val adapter = ListCalAdapter(response)
                val rvListCalc = findViewById<RecyclerView>(R.id.rvListCalc)
                rvListCalc.adapter = adapter
                rvListCalc.layoutManager = LinearLayoutManager(this)
            }

        }.start()

    }

    private inner class ListCalAdapter(private val calcItems : List<Calc>) : RecyclerView.Adapter<ListCalAdapter.ListCalcViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCalcViewHolder {
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            return ListCalcViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListCalcViewHolder, position: Int) {
            val currentItem = calcItems[position]
            if (currentItem.type == "imc") holder.bind(currentItem)
        }

        override fun getItemCount(): Int {
            return calcItems.size
        }

        private inner class ListCalcViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: Calc) {
                val textItem = itemView.findViewById<TextView>(android.R.id.text1)

                val createdDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    item.createdDate.toString().format(DateTimeFormatter.ofPattern("dd/mm/yyyy"))
                } else {
                    item.createdDate.toLocaleString()
                }

                textItem.text = "${item.id} - ${String.format("%.2f", item.res)} - $createdDate"

            }
        }


    }

}