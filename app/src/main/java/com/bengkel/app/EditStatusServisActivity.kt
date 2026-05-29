package com.bengkel.app

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class EditStatusServisActivity : AppCompatActivity() {

    lateinit var spinner: Spinner

    var kodeServis = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_edit_status_servis
        )

        val txtKode =
            findViewById<TextView>(R.id.txtKode)

        spinner =
            findViewById(R.id.spinnerStatus)

        val btnUpdate =
            findViewById<Button>(R.id.btnUpdate)

        val btnKembali =
            findViewById<Button>(R.id.btnKembali)

        kodeServis =
            intent.getStringExtra(
                "kode_servis"
            ).toString()

        txtKode.text = kodeServis

        val statusArray = arrayOf(
            "Menunggu",
            "Proses",
            "Selesai"
        )

        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                statusArray
            )

        spinner.adapter = adapter

        btnUpdate.setOnClickListener {
            updateStatus()
        }

        btnKembali.setOnClickListener {
            finish()
        }
    }

    private fun updateStatus(){

        val url =
            "http://192.168.18.7/Bengkel_API/servis/edit_status_servis.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,

            Response.Listener {

                Toast.makeText(
                    this,
                    it,
                    Toast.LENGTH_LONG
                ).show()

                finish()

            },

            Response.ErrorListener { error ->

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

                params["kode_servis"] =
                    kodeServis

                params["status_servis"] =
                    spinner.selectedItem.toString()

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}