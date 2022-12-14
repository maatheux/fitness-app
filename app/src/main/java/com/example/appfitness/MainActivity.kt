package com.example.appfitness

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var rvMain: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainItems = mutableListOf<MainItem>()
        mainItems.add(
            MainItem(
                id = 1,
                drawableId = R.drawable.ic_baseline_person_24,
                textStringId = R.string.label_imc
            )
        )
        mainItems.add(
            MainItem(
                id = 2,
                drawableId = R.drawable.ic_baseline_directions_run_24,
                textStringId = R.string.label_tmb
            )
        )
        mainItems.add(
            MainItem(
                id = 3,
                drawableId = R.drawable.ic_baseline_directions_bike_24,
                textStringId = R.string.label_activity
            )
        )
        mainItems.add(
            MainItem(
                id = 4,
                drawableId = R.drawable.ic_baseline_food_bank_24,
                textStringId = R.string.label_food
            )
        )
        mainItems.add(
            MainItem(
                id = 5,
                drawableId = R.drawable.ic_baseline_timer_24,
                textStringId = R.string.label_timer
            )
        )
        mainItems.add(
            MainItem(
                id = 6,
                drawableId = R.drawable.ic_baseline_timeline_24,
                textStringId = R.string.label_indicators
            )
        )

        val adapter = MainAdapter(mainItems) { id: Int ->
                when(id) {
                    1 -> {
                        var intent = Intent(this@MainActivity, ImcActivity::class.java)
                        startActivity(intent)
                    }
                    2 -> {
                        var intent = Intent(this@MainActivity, TmbActivity::class.java)
                        startActivity(intent)
                    }
                }
        }
        rvMain = findViewById(R.id.rv_main)
        rvMain.adapter = adapter
        rvMain.layoutManager = GridLayoutManager(this, 2)


    }

    private inner class MainAdapter(private val mainItems: List<MainItem>, private val onItemClickListener: (Int) -> Unit) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val itemCurrent = mainItems[position]
            holder.bind(itemCurrent) // ir?? fazer a conex??o com a celula desejada
        }

        override fun getItemCount(): Int {
            return mainItems.size
        }

        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: MainItem) {
                val itemContainerImc: LinearLayout = itemView.findViewById(R.id.item_container_imc)
                val itemImageIcon: ImageView = itemView.findViewById(R.id.item_img_icon)
                val itemTextName: TextView = itemView.findViewById(R.id.item_txt_name)

                itemImageIcon.setImageResource(item.drawableId)
                itemTextName.setText(item.textStringId)

                itemContainerImc.setOnClickListener {
                    onItemClickListener.invoke(item.id)
                }

            }

        }

    }

}