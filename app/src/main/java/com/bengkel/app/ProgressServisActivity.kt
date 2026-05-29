package com.bengkel.app

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class ProgressServisActivity : AppCompatActivity() {

    lateinit var listView: ListView

    lateinit var listData: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_progress_servis)

        listView = findViewById(R.id.listProgress)

        listData = ArrayList()

        getData()
    }

    private fun getData(){

        val sharedPreferences =
            getSharedPreferences("LOGIN", MODE_PRIVATE)

        val username =
            sharedPreferences.getString("username","")

        val url =
            "http://192.168.18.7/Bengkel_API/progress/get_progress.php?username=$username"

        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,

            { response ->

                for(i in 0 until response.length()){

                    val data = response.getJSONObject(i)

                    val progress =
                        data.getString("deskripsi_progress")

                    val tanggal =
                        data.getString("tanggal_progress")

                    listData.add(
                        "$progress\n$tanggal"
                    )
                }

                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    listData
                )

                listView.adapter = adapter

            },

            {

            }

        )

        Volley.newRequestQueue(this).add(request)
    }
}