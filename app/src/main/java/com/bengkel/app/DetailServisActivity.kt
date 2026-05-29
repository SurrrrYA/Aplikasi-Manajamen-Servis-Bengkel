package com.bengkel.app

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import org.json.JSONObject

import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class DetailServisActivity : AppCompatActivity() {

    lateinit var tvKode: TextView
    lateinit var tvPlat: TextView
    lateinit var tvKeluhan: TextView

    lateinit var spStatus: Spinner
    lateinit var autoBarangJasa: AutoCompleteTextView
    lateinit var etQty: EditText
    lateinit var listDetailBarang: ListView

    lateinit var listNama: ArrayList<String>
    lateinit var listKode: ArrayList<String>
    lateinit var listHarga: ArrayList<String>
    lateinit var listTipe: ArrayList<String>

    lateinit var listDetail: ArrayList<DetailItemServis>

    var selectedKode = ""
    var selectedNama = ""
    var selectedHarga = ""
    var selectedTipe = ""

    var kodeServis = ""
    var plat = ""
    var keluhan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail_servis)

        tvKode = findViewById(R.id.tvKode)
        tvPlat = findViewById(R.id.tvPlat)
        tvKeluhan = findViewById(R.id.tvKeluhan)

        spStatus = findViewById(R.id.spStatus)
        autoBarangJasa = findViewById(R.id.autoBarangJasa)
        etQty = findViewById(R.id.etQty)
        listDetailBarang = findViewById(R.id.listDetailBarang)

        val btnUpdateStatus = findViewById<Button>(R.id.btnUpdateStatus)
        val btnTambahBarang = findViewById<Button>(R.id.btnTambahBarang)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        kodeServis = intent.getStringExtra("kode_servis").toString()
        plat = intent.getStringExtra("plat_nomor").toString()
        keluhan = intent.getStringExtra("keluhan").toString()

        tvKode.text = kodeServis
        tvPlat.text = "Plat : $plat"
        tvKeluhan.text = "Keluhan : $keluhan"

        listNama = ArrayList()
        listKode = ArrayList()
        listHarga = ArrayList()
        listTipe = ArrayList()
        listDetail = ArrayList()

        val statusArray = arrayOf(
            "Menunggu",
            "Proses",
            "Selesai"
        )

        spStatus.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                statusArray
            )

        autoBarangJasa.setOnItemClickListener {
                parent, _, position, _ ->

            val pilihan =
                parent.getItemAtPosition(position).toString()

            val namaDipilih =
                pilihan
                    .replace("[Barang] ", "")
                    .replace("[Jasa] ", "")

            val index =
                listNama.indexOf(namaDipilih)

            if(index != -1){

                selectedNama = listNama[index]
                selectedKode = listKode[index]
                selectedHarga = listHarga[index]
                selectedTipe = listTipe[index]

                if(selectedTipe == "jasa"){
                    etQty.setText("1")
                }
            }
        }

        btnUpdateStatus.setOnClickListener {
            updateStatus()
        }

        btnTambahBarang.setOnClickListener {
            tambahBarangJasaServis()
        }

        btnKembali.setOnClickListener {
            finish()
        }

        getBarangJasa()
        getDetailBarang()
    }

    private fun updateStatus(){

        val url =
            "http://192.168.18.7/Bengkel_API/servis/edit_status_servis.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,
            { response ->
                Toast.makeText(
                    this,
                    response,
                    Toast.LENGTH_LONG
                ).show()

                finish()
            },
            { error ->
                Toast.makeText(
                    this,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        ){
            override fun getParams(): MutableMap<String, String> {

                val params =
                    HashMap<String, String>()

                params["kode_servis"] =
                    kodeServis

                params["status_servis"] =
                    spStatus.selectedItem.toString()

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    private fun getBarangJasa(){

        val url =
            "http://192.168.18.7/Bengkel_API/servis/get_barang_jasa.php"

        val request =
            JsonArrayRequest(
                Request.Method.GET,
                url,
                null,

                { response ->

                    listNama.clear()
                    listKode.clear()
                    listHarga.clear()
                    listTipe.clear()

                    val tampilan =
                        ArrayList<String>()

                    for(i in 0 until response.length()){

                        val data =
                            response.getJSONObject(i)

                        val kode =
                            data.getString("kode")

                        val nama =
                            data.getString("nama")

                        val harga =
                            data.getString("harga")

                        val tipe =
                            data.getString("tipe")

                        listKode.add(kode)
                        listNama.add(nama)
                        listHarga.add(harga)
                        listTipe.add(tipe)

                        if(tipe == "barang"){
                            tampilan.add("[Barang] $nama")
                        }else{
                            tampilan.add("[Jasa] $nama")
                        }
                    }

                    autoBarangJasa.setAdapter(
                        ArrayAdapter(
                            this,
                            android.R.layout.simple_dropdown_item_1line,
                            tampilan
                        )
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

    private fun tambahBarangJasaServis(){

        if(selectedKode == ""){
            Toast.makeText(
                this,
                "Pilih barang atau jasa dulu",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val qty =
            etQty.text.toString().trim()

        if(qty == ""){
            Toast.makeText(
                this,
                "Qty belum diisi",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val url =
            "http://192.168.18.7/Bengkel_API/servis/tambah_item_servis.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,
            { response ->
                Toast.makeText(
                    this,
                    response,
                    Toast.LENGTH_LONG
                ).show()

                autoBarangJasa.setText("")
                etQty.setText("")

                selectedKode = ""
                selectedNama = ""
                selectedHarga = ""
                selectedTipe = ""

                getDetailBarang()
            },
            { error ->
                Toast.makeText(
                    this,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        ){
            override fun getParams(): MutableMap<String, String> {

                val params =
                    HashMap<String, String>()

                params["kode_servis"] =
                    kodeServis

                if(selectedTipe == "barang"){
                    params["kode_barang"] =
                        selectedKode
                }else{
                    params["kode_barang"] =
                        ""
                }

                params["nama_item"] =
                    selectedNama

                params["qty"] =
                    qty

                params["harga"] =
                    selectedHarga

                params["tipe_item"] =
                    selectedTipe

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    private fun getDetailBarang(){

        val url =
            "http://192.168.18.7/Bengkel_API/servis/get_item_servis.php?kode_servis=$kodeServis"

        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->

                listDetail.clear()

                for(i in 0 until response.length()){

                    val data =
                        response.getJSONObject(i)

                    val kodeItem =
                        data.getString("kode_item")

                    val nama =
                        data.getString("nama_item")

                    val qty =
                        data.getString("qty")

                    listDetail.add(
                        DetailItemServis(
                            kodeItem,
                            nama,
                            qty
                        )
                    )
                }

                val adapter =
                    DetailServisListAdapter(
                        this,
                        listDetail,
                        { kodeItem ->
                            tambahQty(kodeItem)
                        },
                        { kodeItem ->
                            kurangQty(kodeItem)
                        },
                        { kodeItem ->
                            hapusItem(kodeItem)
                        }
                    )

                listDetailBarang.adapter = adapter
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

    private fun hapusItem(kodeItem: String){

        val url =
            "http://192.168.18.7/Bengkel_API/servis/delete_item_servis.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,
            { response ->

                val json =
                    JSONObject(response)

                Toast.makeText(
                    this,
                    json.getString("message"),
                    Toast.LENGTH_LONG
                ).show()

                getDetailBarang()
            },
            { error ->
                Toast.makeText(
                    this,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        ){
            override fun getParams(): MutableMap<String, String> {

                val params =
                    HashMap<String, String>()

                params["kode_item"] =
                    kodeItem

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    private fun tambahQty(kodeItem: String){

        val url =
            "http://192.168.18.7/Bengkel_API/servis/tambah_qty_item.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,
            { response ->

                val json =
                    JSONObject(response)

                Toast.makeText(
                    this,
                    json.getString("message"),
                    Toast.LENGTH_LONG
                ).show()

                getDetailBarang()
            },
            { error ->
                Toast.makeText(
                    this,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        ){
            override fun getParams(): MutableMap<String, String> {

                val params =
                    HashMap<String, String>()

                params["kode_item"] =
                    kodeItem

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    private fun kurangQty(kodeItem: String){

        val url =
            "http://192.168.18.7/Bengkel_API/servis/kurang_qty_item.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,
            { response ->

                val json =
                    JSONObject(response)

                Toast.makeText(
                    this,
                    json.getString("message"),
                    Toast.LENGTH_LONG
                ).show()

                getDetailBarang()
            },
            { error ->
                Toast.makeText(
                    this,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        ){
            override fun getParams(): MutableMap<String, String> {

                val params =
                    HashMap<String, String>()

                params["kode_item"] =
                    kodeItem

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}