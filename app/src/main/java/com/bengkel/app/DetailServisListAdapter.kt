package com.bengkel.app

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class DetailServisListAdapter(

    private val activity: Activity,
    private val listData: ArrayList<DetailItemServis>,
    private val onTambah: (String) -> Unit,
    private val onKurang: (String) -> Unit,
    private val onHapus: (String) -> Unit

) : ArrayAdapter<DetailItemServis>(
    activity,
    R.layout.item_detail_servis,
    listData
){

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        val inflater =
            LayoutInflater.from(activity)

        val view =
            inflater.inflate(
                R.layout.item_detail_servis,
                parent,
                false
            )

        val tvNama =
            view.findViewById<TextView>(R.id.tvNamaItem)

        val tvQty =
            view.findViewById<TextView>(R.id.tvQty)

        val btnPlus =
            view.findViewById<Button>(R.id.btnPlus)

        val btnMinus =
            view.findViewById<Button>(R.id.btnMinus)

        val btnHapus =
            view.findViewById<Button>(R.id.btnHapus)

        val data =
            listData[position]

        tvNama.text =
            data.namaItem

        tvQty.text =
            data.qty

        btnPlus.setOnClickListener {

            onTambah(data.kodeItem)

        }

        btnMinus.setOnClickListener {

            onKurang(data.kodeItem)

        }

        btnHapus.setOnClickListener {

            onHapus(data.kodeItem)

        }

        return view
    }
}