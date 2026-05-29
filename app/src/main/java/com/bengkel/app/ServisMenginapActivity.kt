package com.bengkel.app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class ServisMenginapActivity : AppCompatActivity() {

    lateinit var listView: ListView

    lateinit var listData: ArrayList<String>
    lateinit var listKode: ArrayList<String>
    lateinit var listPlat: ArrayList<String>
    lateinit var listKeluhan: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_servis_menginap)

        listView =
            findViewById(R.id.listServisMenginap)

        val btnKembali =
            findViewById<Button>(R.id.btnKembali)

        btnKembali.setOnClickListener {
            finish()
        }

        listData = ArrayList()
        listKode = ArrayList()
        listPlat = ArrayList()
        listKeluhan = ArrayList()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData(){

        val url =
            "http://192.168.18.7/Bengkel_API/servis/get_servis_menginap.php"

        val request =
            JsonArrayRequest(
                Request.Method.GET,
                url,
                null,

                { response ->

                    listData.clear()
                    listKode.clear()
                    listPlat.clear()
                    listKeluhan.clear()

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

                        val customer =
                            data.getString("username_customer")

                        val tanggalMasuk =
                            if(data.isNull("tanggal_masuk")){
                                "-"
                            }else{
                                data.getString("tanggal_masuk")
                            }

                        val statusIcon =
                            if(status == "Proses"){
                                "🟡"
                            }else{
                                "🔴"
                            }

                        listKode.add(kode)
                        listPlat.add(plat)
                        listKeluhan.add(keluhan)

                        listData.add(
                            "$kode\n" +
                                    "Plat : $plat\n" +
                                    "Keluhan : $keluhan\n" +
                                    "Customer : $customer\n" +
                                    "Tanggal Masuk : $tanggalMasuk\n\n" +
                                    "$statusIcon $status"
                        )
                    }

                    listView.adapter =
                        ArrayAdapter(
                            this,
                            android.R.layout.simple_list_item_1,
                            listData
                        )

                    listView.setOnItemClickListener {
                            _, _, position, _ ->

                        val intent =
                            Intent(
                                this,
                                ProgressMenginapActivity::class.java
                            )

                        intent.putExtra(
                            "kode_servis",
                            listKode[position]
                        )

                        intent.putExtra(
                            "plat_nomor",
                            listPlat[position]
                        )

                        intent.putExtra(
                            "keluhan",
                            listKeluhan[position]
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