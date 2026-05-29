package com.bengkel.app

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class DataCustomerActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var listData: ArrayList<String>
    lateinit var listKode: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_data_customer)

        listView =
            findViewById(R.id.listCustomer)

        val btnKembali =
            findViewById<Button>(R.id.btnKembali)

        btnKembali.setOnClickListener {
            finish()
        }

        listData = ArrayList()
        listKode = ArrayList()
    }

    override fun onResume() {
        super.onResume()

        getCustomer()
    }

    private fun getCustomer(){

        listData.clear()
        listKode.clear()

        val url =
            "http://192.168.18.7/Bengkel_API/customer/get_customer.php"

        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,

            { response ->

                for(i in 0 until response.length()){

                    val data =
                        response.getJSONObject(i)

                    val kode =
                        data.getString("kode_customer")

                    val nama =
                        data.getString("nama_customer")

                    val hp =
                        data.getString("no_hp")

                    val alamat =
                        data.getString("alamat")

                    listKode.add(kode)

                    listData.add(
                        "$nama\n$hp\n$alamat"
                    )
                }

                val adapter =
                    ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        listData
                    )

                listView.adapter = adapter

                listView.onItemClickListener =
                    AdapterView.OnItemClickListener {
                            _, _, position, _ ->

                        val intent =
                            Intent(
                                this,
                                EditCustomerActivity::class.java
                            )

                        intent.putExtra(
                            "kode_customer",
                            listKode[position]
                        )

                        intent.putExtra(
                            "data_customer",
                            listData[position]
                        )

                        startActivity(intent)
                    }
            },

            {

            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}