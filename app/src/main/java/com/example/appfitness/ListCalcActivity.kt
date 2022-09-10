package com.example.appfitness

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appfitness.model.Calc
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class ListCalcActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)

        val result = mutableListOf<Calc>()
        val adapter = ListCalcAdapter(result) { id: Int, res: Double ->
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.resultOptionDialogTitle))
                setMessage(getString(R.string.resultOptionDialogMessage, res))
                setPositiveButton(R.string.editText) {dialog, which ->}
                setNegativeButton(R.string.deleteText) {dialog, which ->}
                create()
                show()
            }
        }
        val rvListCalc = findViewById<RecyclerView>(R.id.rvListCalc)
        rvListCalc.adapter = adapter
        rvListCalc.layoutManager = LinearLayoutManager(this)

        val type = intent?.extras?.getString("type") ?: throw IllegalStateException("type not found")

        Thread {
            val app = application as App
            val dao = app.db.calcDao()
            val response = dao.getRegisterByType(type)

            runOnUiThread {
                result.addAll(response)
                adapter.notifyDataSetChanged()
            }

        }.start()

    }

    private inner class ListCalcAdapter(private val calcItems : List<Calc>, private val onItemLongClickListener: (Int, Double) -> Unit ) : RecyclerView.Adapter<ListCalcAdapter.ListCalcViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCalcViewHolder {
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            return ListCalcViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListCalcViewHolder, position: Int) {
            val currentItem = calcItems[position]
            holder.bind(currentItem)
        }

        override fun getItemCount(): Int {
            return calcItems.size
        }

        private inner class ListCalcViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: Calc) {
                val tv = itemView as TextView // como o itemview nao tem filho, o proprio itemview e o textview

                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
                sdf.timeZone = TimeZone.getTimeZone("America/Sao_Paulo")
                val data = sdf.format(item.createdDate)
                val res = item.res

                tv.text = getString(R.string.list_response, res, data)
                tv.setTextColor(ContextCompat.getColor(this@ListCalcActivity, R.color.white))
                tv.background = ContextCompat.getDrawable(this@ListCalcActivity, R.drawable.result_style_bg)

                tv.setOnLongClickListener {
                    onItemLongClickListener.invoke(item.id, item.res)
                    return@setOnLongClickListener true
                }


            }
        }


    }

}