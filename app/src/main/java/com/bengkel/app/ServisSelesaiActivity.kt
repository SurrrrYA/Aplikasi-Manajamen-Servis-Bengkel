package com.bengkel.app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class ServisSelesaiActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var spFilterBayar: Spinner
    lateinit var spFilterTanggal: Spinner

    lateinit var listData: ArrayList<String>
    lateinit var listKode: ArrayList<String>

    var isSpinnerReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_servis_selesai)

        listView =
            findViewById(R.id.listServisSelesai)

        spFilterBayar =
            findViewById(R.id.spFilterBayar)

        spFilterTanggal =
            findViewById(R.id.spFilterTanggal)

        val btnKembali =
            findViewById<Button>(R.id.btnKembali)

        btnKembali.setOnClickListener {
            finish()
        }

        listData = ArrayList()
        listKode = ArrayList()

        spFilterBayar.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf(
                    "Semua",
                    "Belum Lunas",
                    "Lunas"
                )
            )

        spFilterTanggal.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf(
                    "Semua",
                    "Hari Ini",
                    "Kemarin",
                    "7 Hari Terakhir",
                    "1 Bulan Terakhir"
                )
            )

        spFilterBayar.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    if(isSpinnerReady){
                        getData()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        spFilterTanggal.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    if(isSpinnerReady){
                        getData()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        isSpinnerReady = true
        getData()
    }

    override fun onResume() {
        super.onResume()

        if(isSpinnerReady){
            getData()
        }
    }

    private fun getData(){

        val filterBayar =
            spFilterBayar.selectedItem.toString()

        val filterTanggal =
            spFilterTanggal.selectedItem.toString()

        val url =
            "http://192.168.18.7/Bengkel_API/servis/get_servis_selesai.php" +
                    "?filter_bayar=$filterBayar" +
                    "&filter_tanggal=$filterTanggal"

        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,

            { response ->

                listData.clear()
                listKode.clear()

                for(i in 0 until response.length()){

                    val data =
                        response.getJSONObject(i)

                    val kode =
                        data.getString("kode_servis")

                    val plat =
                        data.getString("plat_nomor")

                    val keluhan =
                        data.getString("keluhan")

                    val status =
                        data.getString("status_servis")

                    val tanggalSelesai =
                        if(data.isNull("tanggal_selesai")){
                            "-"
                        }else{
                            data.getString("tanggal_selesai")
                        }

                    val statusBayar =
                        data.getString("status_bayar")

                    val bayarIcon =
                        if(statusBayar == "Lunas"){
                            "🟢"
                        }else{
                            "🔴"
                        }

                    listKode.add(kode)

                    listData.add(
                        "$kode\n" +
                                "Plat : $plat\n" +
                                "Keluhan : $keluhan\n\n" +
                                "Tanggal Selesai : $tanggalSelesai\n\n" +
                                "🟢 Status Servis : $status\n" +
                                "$bayarIcon Status Bayar : $statusBayar"
                    )
                }

                listView.adapter =
                    ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        listData
                    )

                listView.onItemClickListener =
                    AdapterView.OnItemClickListener {
                            _, _, position, _ ->

                        val intent =
                            Intent(
                                this,
                                PembayaranServisActivity::class.java
                            )

                        intent.putExtra(
                            "kode_servis",
                            listKode[position]
                        )

                        startActivity(intent)
                    }
            },

            { error ->
                Toast.makeText(
                    this,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}