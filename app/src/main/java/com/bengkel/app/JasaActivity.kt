package com.bengkel.app

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class JasaActivity : AppCompatActivity() {

    lateinit var etNamaJasa: EditText
    lateinit var etHargaJasa: EditText
    lateinit var listJasa: ListView
    lateinit var btnSimpan: Button

    lateinit var listData: ArrayList<String>
    lateinit var listKodeJasa: ArrayList<String>
    lateinit var listNamaJasa: ArrayList<String>
    lateinit var listHargaJasa: ArrayList<String>

    var kodeJasaDipilih = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_jasa)

        etNamaJasa = findViewById(R.id.etNamaJasa)
        etHargaJasa = findViewById(R.id.etHargaJasa)
        listJasa = findViewById(R.id.listJasa)
        btnSimpan = findViewById(R.id.btnSimpanJasa)

        val btnKembali =
            findViewById<Button>(R.id.btnKembali)

        listData = ArrayList()
        listKodeJasa = ArrayList()
        listNamaJasa = ArrayList()
        listHargaJasa = ArrayList()

        btnSimpan.setOnClickListener {
            if(kodeJasaDipilih == ""){
                tambahJasa()
            }else{
                updateJasa()
            }
        }

        btnKembali.setOnClickListener {
            finish()
        }

        listJasa.setOnItemClickListener {
                _, _, position, _ ->

            val pilihan =
                arrayOf(
                    "Edit",
                    "Hapus"
                )

            AlertDialog.Builder(this)
                .setTitle("Pilih Aksi")
                .setItems(pilihan){ _, which ->

                    if(which == 0){

                        kodeJasaDipilih =
                            listKodeJasa[position]

                        etNamaJasa.setText(
                            listNamaJasa[position]
                        )

                        etHargaJasa.setText(
                            listHargaJasa[position]
                        )

                        btnSimpan.text =
                            "Update Jasa"

                    }else{

                        hapusJasa(
                            listKodeJasa[position]
                        )
                    }
                }
                .show()
        }

        getJasa()
    }

    private fun tambahJasa(){

        val nama =
            etNamaJasa.text.toString().trim()

        val harga =
            etHargaJasa.text.toString().trim()

        if(nama == "" || harga == ""){

            Toast.makeText(
                this,
                "Data belum lengkap",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val url =
            "http://192.168.18.7/Bengkel_API/jasa/tambah_jasa.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,

            { response ->

                Toast.makeText(
                    this,
                    response,
                    Toast.LENGTH_LONG
                ).show()

                resetForm()
                getJasa()
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

                params["nama_jasa"] =
                    nama

                params["harga"] =
                    harga

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    private fun updateJasa(){

        val nama =
            etNamaJasa.text.toString().trim()

        val harga =
            etHargaJasa.text.toString().trim()

        if(nama == "" || harga == ""){

            Toast.makeText(
                this,
                "Data belum lengkap",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val url =
            "http://192.168.18.7/Bengkel_API/jasa/update_jasa.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,

            { response ->

                Toast.makeText(
                    this,
                    response,
                    Toast.LENGTH_LONG
                ).show()

                resetForm()
                getJasa()
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

                params["kode_jasa"] =
                    kodeJasaDipilih

                params["nama_jasa"] =
                    nama

                params["harga"] =
                    harga

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    private fun hapusJasa(kodeJasa: String){

        AlertDialog.Builder(this)
            .setTitle("Hapus Jasa")
            .setMessage("Yakin ingin menghapus jasa ini?")
            .setPositiveButton("Hapus"){ _, _ ->

                val url =
                    "http://192.168.18.7/Bengkel_API/jasa/delete_jasa.php"

                val request = object : StringRequest(
                    Request.Method.POST,
                    url,

                    { response ->

                        Toast.makeText(
                            this,
                            response,
                            Toast.LENGTH_LONG
                        ).show()

                        resetForm()
                        getJasa()
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

                        params["kode_jasa"] =
                            kodeJasa

                        return params
                    }
                }

                Volley.newRequestQueue(this).add(request)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun getJasa(){

        val url =
            "http://192.168.18.7/Bengkel_API/jasa/get_jasa.php"

        val request =
            JsonArrayRequest(
                Request.Method.GET,
                url,
                null,

                { response ->

                    listData.clear()
                    listKodeJasa.clear()
                    listNamaJasa.clear()
                    listHargaJasa.clear()

                    for(i in 0 until response.length()){

                        val data =
                            response.getJSONObject(i)

                        val kode =
                            data.getString("kode_jasa")

                        val nama =
                            data.getString("nama_jasa")

                        val harga =
                            data.getString("harga")

                        listKodeJasa.add(kode)
                        listNamaJasa.add(nama)
                        listHargaJasa.add(harga)

                        listData.add(
                            "$nama\nRp $harga"
                        )
                    }

                    listJasa.adapter =
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

    private fun resetForm(){

        etNamaJasa.setText("")
        etHargaJasa.setText("")
        kodeJasaDipilih = ""
        btnSimpan.text = "Simpan Jasa"
    }
}