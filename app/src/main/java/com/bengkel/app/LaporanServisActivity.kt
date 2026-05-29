package com.bengkel.app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class LaporanServisActivity : AppCompatActivity() {

    lateinit var spStatus: Spinner
    lateinit var spTanggal: Spinner
    lateinit var etCari: EditText
    lateinit var listLaporan: ListView

    lateinit var listData: ArrayList<String>

    var isSpinnerReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_laporan_servis)

        spStatus = findViewById(R.id.spStatus)
        spTanggal = findViewById(R.id.spTanggal)
        etCari = findViewById(R.id.etCari)
        listLaporan = findViewById(R.id.listLaporan)

        val btnKembali =
            findViewById<Button>(R.id.btnKembali)

        listData = ArrayList()

        spStatus.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf(
                    "Semua",
                    "Menunggu",
                    "Proses",
                    "Selesai"
                )
            )

        spTanggal.adapter =
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

        spStatus.onItemSelectedListener =
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

        spTanggal.onItemSelectedListener =
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

        etCari.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                    if(isSpinnerReady){
                        getData()
                    }
                }
            }
        )

        btnKembali.setOnClickListener {
            finish()
        }

        isSpinnerReady = true
        getData()
    }

    private fun getData(){

        val status =
            spStatus.selectedItem.toString()

        val tanggal =
            spTanggal.selectedItem.toString()

        val keyword =
            etCari.text.toString()

        val url =
            "http://192.168.18.7/Bengkel_API/laporan/get_laporan_servis.php" +
                    "?status=$status" +
                    "&tanggal=$tanggal" +
                    "&keyword=$keyword"

        val request =
            JsonArrayRequest(
                Request.Method.GET,
                url,
                null,

                { response ->

                    listData.clear()

                    for(i in 0 until response.length()){

                        val data =
                            response.getJSONObject(i)

                        val kode =
                            data.getString("kode_servis")

                        val plat =
                            data.getString("plat_nomor")

                        val keluhan =
                            data.getString("keluhan")

                        val statusServis =
                            data.getString("status_servis")

                        val statusBayar =
                            data.getString("status_bayar")

                        val tanggalMasuk =
                            if(data.isNull("tanggal_masuk")){
                                "-"
                            }else{
                                data.getString("tanggal_masuk")
                            }

                        val tanggalSelesai =
                            if(data.isNull("tanggal_selesai")){
                                "-"
                            }else{
                                data.getString("tanggal_selesai")
                            }

                        listData.add(
                            "$kode\n" +
                                    "Plat : $plat\n" +
                                    "Keluhan : $keluhan\n\n" +
                                    "Status : $statusServis\n" +
                                    "Pembayaran : $statusBayar\n\n" +
                                    "Tanggal Masuk : $tanggalMasuk\n" +
                                    "Tanggal Selesai : $tanggalSelesai"
                        )
                    }

                    listLaporan.adapter =
                        ArrayAdapter(
                            this,
                            android.R.layout.simple_list_item_1,
                            listData
                        )
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