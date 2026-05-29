package com.bengkel.app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class LaporanPendapatanActivity : AppCompatActivity() {

    lateinit var spFilterTanggal: Spinner
    lateinit var spFilterMetode: Spinner
    lateinit var txtTotalPendapatan: TextView
    lateinit var txtTotalTransaksi: TextView
    lateinit var listPendapatan: ListView
    lateinit var etCari: EditText

    lateinit var listData: ArrayList<String>

    var totalPendapatan = 0
    var isSpinnerReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_laporan_pendapatan)

        spFilterTanggal =
            findViewById(R.id.spFilterTanggal)

        spFilterMetode =
            findViewById(R.id.spFilterMetode)

        txtTotalPendapatan =
            findViewById(R.id.txtTotalPendapatan)

        txtTotalTransaksi =
            findViewById(R.id.txtTotalTransaksi)

        listPendapatan =
            findViewById(R.id.listPendapatan)

        etCari =
            findViewById(R.id.etCari)

        val btnKembali =
            findViewById<Button>(R.id.btnKembali)

        btnKembali.setOnClickListener {
            finish()
        }

        listData = ArrayList()

        spFilterTanggal.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf(
                    "Hari Ini",
                    "7 Hari Terakhir",
                    "1 Bulan Terakhir",
                    "Semua"
                )
            )

        spFilterMetode.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf(
                    "Semua",
                    "Cash",
                    "QRIS",
                    "Transfer",
                    "Debit"
                )
            )

        spFilterTanggal.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {

                    if(isSpinnerReady){
                        getLaporan()
                    }
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }
            }

        spFilterMetode.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {

                    if(isSpinnerReady){
                        getLaporan()
                    }
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }
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
                        getLaporan()
                    }
                }
            }
        )

        isSpinnerReady = true

        getLaporan()
    }

    private fun getLaporan(){

        val filterTanggal =
            spFilterTanggal.selectedItem.toString()

        val filterMetode =
            spFilterMetode.selectedItem.toString()

        val keyword =
            etCari.text.toString()

        val url =
            "http://192.168.18.7/Bengkel_API/laporan/get_laporan_pendapatan.php" +
                    "?filter_tanggal=$filterTanggal" +
                    "&filter_metode=$filterMetode" +
                    "&keyword=$keyword"

        val request =
            JsonArrayRequest(
                Request.Method.GET,
                url,
                null,

                { response ->

                    listData.clear()

                    totalPendapatan = 0

                    for(i in 0 until response.length()){

                        val data =
                            response.getJSONObject(i)

                        val kode =
                            data.getString("kode_transaksi")

                        val kodeServis =
                            data.getString("kode_servis")

                        val total =
                            data.getInt("total_bayar")

                        val metode =
                            data.getString("metode_bayar")

                        val tanggal =
                            data.getString("tanggal_transaksi")

                        totalPendapatan += total

                        listData.add(
                            "$kode\n" +
                                    "Kode Servis : $kodeServis\n" +
                                    "Metode : $metode\n" +
                                    "Tanggal : $tanggal\n" +
                                    "Total : Rp $total"
                        )
                    }

                    txtTotalPendapatan.text =
                        "Total Pendapatan : Rp $totalPendapatan"

                    txtTotalTransaksi.text =
                        "Total Transaksi : ${response.length()}"

                    listPendapatan.adapter =
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