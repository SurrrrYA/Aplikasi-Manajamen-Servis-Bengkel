package com.bengkel.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class EditCustomerActivity : AppCompatActivity() {

    lateinit var etNama: EditText
    lateinit var etHp: EditText
    lateinit var etAlamat: EditText

    var kodeCustomer = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_customer)

        etNama =
            findViewById(R.id.etNama)

        etHp =
            findViewById(R.id.etHp)

        etAlamat =
            findViewById(R.id.etAlamat)

        val btnUpdate =
            findViewById<Button>(R.id.btnUpdate)

        val btnHapus =
            findViewById<Button>(R.id.btnHapus)

        val btnKembali =
            findViewById<Button>(R.id.btnKembali)

        kodeCustomer =
            intent.getStringExtra(
                "kode_customer"
            ).toString()

        Toast.makeText(
            this,
            kodeCustomer,
            Toast.LENGTH_LONG
        ).show()

        val data =
            intent.getStringExtra(
                "data_customer"
            ).toString()

        val pecah =
            data.split("\n")

        etNama.setText(pecah[0])
        etHp.setText(pecah[1])
        etAlamat.setText(pecah[2])

        btnUpdate.setOnClickListener {

            updateCustomer()


        }

        btnHapus.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Hapus")
                .setMessage("Yakin hapus customer?")
                .setPositiveButton("Ya"){ _, _ ->

                    hapusCustomer()

                }
                .setNegativeButton("Tidak", null)
                .show()

        }

        btnKembali.setOnClickListener {

            finish()

        }
    }

    private fun updateCustomer(){

        val url =
            "http://192.168.18.7/Bengkel_API/customer/edit_customer.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,

            Response.Listener {

                Toast.makeText(
                    this,
                    it,
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            },

            Response.ErrorListener {

            }

        ){

            override fun getParams():
                    MutableMap<String, String> {

                val params =
                    HashMap<String, String>()

                params["kode_customer"] =
                    kodeCustomer

                params["nama"] =
                    etNama.text.toString()

                params["no_hp"] =
                    etHp.text.toString()

                params["alamat"] =
                    etAlamat.text.toString()

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
    private fun hapusCustomer(){

        val url =
            "http://192.168.18.7/Bengkel_API/customer/delete_customer.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,

            Response.Listener {

                Toast.makeText(
                    this,
                    it,
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            },

            Response.ErrorListener {

            }

        ){

            override fun getParams():
                    MutableMap<String, String> {

                val params =
                    HashMap<String, String>()

                params["kode_customer"] =
                    kodeCustomer

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}