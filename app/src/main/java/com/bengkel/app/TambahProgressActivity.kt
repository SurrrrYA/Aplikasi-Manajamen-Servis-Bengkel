package com.bengkel.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class TambahProgressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tambah_progress)

        val etKodeServis =
            findViewById<EditText>(R.id.etKodeServis)

        val etProgress =
            findViewById<EditText>(R.id.etProgress)

        val btnSimpan =
            findViewById<Button>(R.id.btnSimpan)

        val btnKembali =
            findViewById<Button>(R.id.btnKembali)

        btnKembali.setOnClickListener {

            finish()

        }

        btnSimpan.setOnClickListener {

            val kodeServis =
                etKodeServis.text.toString()

            val progress =
                etProgress.text.toString()

            simpanProgress(
                kodeServis,
                progress
            )

        }
    }

    private fun simpanProgress(
        kodeServis: String,
        progress: String
    ){

        val sharedPreferences =
            getSharedPreferences("LOGIN", MODE_PRIVATE)

        val username =
            sharedPreferences.getString("username","")

        val url =
            "http://192.168.18.7/Bengkel_API/progress/tambah_progress.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,

            Response.Listener {

                Toast.makeText(
                    this,
                    it,
                    Toast.LENGTH_SHORT
                ).show()

            },

            Response.ErrorListener {

            }

        ){

            override fun getParams():
                    MutableMap<String, String> {

                val params =
                    HashMap<String, String>()

                params["kode_servis"] = kodeServis
                params["deskripsi_progress"] = progress
                params["user_input"] = username.toString()

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}