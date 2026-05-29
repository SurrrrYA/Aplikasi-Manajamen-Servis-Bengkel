package com.bengkel.app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class InventoriBarangActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var listData: ArrayList<String>

    lateinit var listKode: ArrayList<String>
    lateinit var listNama: ArrayList<String>
    lateinit var listMerk: ArrayList<String>
    lateinit var listHarga: ArrayList<String>
    lateinit var listStok: ArrayList<String>

    lateinit var etNamaBarang: EditText
    lateinit var etMerk: EditText
    lateinit var etHarga: EditText
    lateinit var etStok: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_inventori_barang
        )

        etNamaBarang =
            findViewById(R.id.etNamaBarang)

        etMerk =
            findViewById(R.id.etMerk)

        etHarga =
            findViewById(R.id.etHarga)

        etStok =
            findViewById(R.id.etStok)

        val btnTambah =
            findViewById<Button>(R.id.btnTambah)

        val btnKembali =
            findViewById<Button>(R.id.btnKembali)

        listView =
            findViewById(R.id.listBarang)

        listData = ArrayList()
        listKode = ArrayList()
        listNama = ArrayList()
        listMerk = ArrayList()
        listHarga = ArrayList()
        listStok = ArrayList()

        btnTambah.setOnClickListener {
            tambahBarang()
        }

        btnKembali.setOnClickListener {
            finish()
        }

        listView.setOnItemClickListener { _, _, position, _ ->

            val intent =
                Intent(
                    this,
                    EditBarangActivity::class.java
                )

            intent.putExtra("kode_barang", listKode[position])
            intent.putExtra("nama_barang", listNama[position])
            intent.putExtra("merk", listMerk[position])
            intent.putExtra("harga", listHarga[position])
            intent.putExtra("stok", listStok[position])

            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        getBarang()
    }

    private fun getBarang(){

        val url =
            "http://192.168.18.7/Bengkel_API/barang/get_barang.php"

        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,

            { response ->

                listData.clear()
                listKode.clear()
                listNama.clear()
                listMerk.clear()
                listHarga.clear()
                listStok.clear()

                for(i in 0 until response.length()){

                    val data =
                        response.getJSONObject(i)

                    val kode =
                        data.getString("kode_barang")

                    val nama =
                        data.getString("nama_barang")

                    val merk =
                        data.getString("merk")

                    val harga =
                        data.getString("harga")

                    val stok =
                        data.getString("stok")

                    listKode.add(kode)
                    listNama.add(nama)
                    listMerk.add(merk)
                    listHarga.add(harga)
                    listStok.add(stok)

                    listData.add(
                        "$nama\n" +
                                "$merk\n" +
                                "Harga : Rp $harga\n" +
                                "Stok : $stok"
                    )
                }

                val adapter =
                    ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        listData
                    )

                listView.adapter = adapter

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

    private fun tambahBarang(){

        val url =
            "http://192.168.18.7/Bengkel_API/barang/tambah_barang.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,

            {

                Toast.makeText(
                    this,
                    it,
                    Toast.LENGTH_LONG
                ).show()

                etNamaBarang.setText("")
                etMerk.setText("")
                etHarga.setText("")
                etStok.setText("")

                getBarang()

            },

            { error ->

                Toast.makeText(
                    this,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()

            }

        ){

            override fun getParams():
                    MutableMap<String, String> {

                val params =
                    HashMap<String, String>()

                params["nama_barang"] =
                    etNamaBarang.text.toString()

                params["merk"] =
                    etMerk.text.toString()

                params["harga"] =
                    etHarga.text.toString()

                params["stok"] =
                    etStok.text.toString()

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}