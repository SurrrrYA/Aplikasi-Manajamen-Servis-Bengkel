package com.bengkel.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class EditBarangActivity : AppCompatActivity() {

    lateinit var etNamaBarang: EditText
    lateinit var etMerk: EditText
    lateinit var etHarga: EditText
    lateinit var etStok: EditText

    var kodeBarang = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_barang)

        etNamaBarang = findViewById(R.id.etNamaBarang)
        etMerk = findViewById(R.id.etMerk)
        etHarga = findViewById(R.id.etHarga)
        etStok = findViewById(R.id.etStok)

        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        kodeBarang = intent.getStringExtra("kode_barang").toString()

        etNamaBarang.setText(intent.getStringExtra("nama_barang"))
        etMerk.setText(intent.getStringExtra("merk"))
        etHarga.setText(intent.getStringExtra("harga"))
        etStok.setText(intent.getStringExtra("stok"))

        btnUpdate.setOnClickListener {
            updateBarang()
        }

        btnKembali.setOnClickListener {
            finish()
        }
    }

    private fun updateBarang() {

        val url =
            "http://192.168.18.7/Bengkel_API/barang/update_barang.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,
            { response ->
                Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                finish()
            },
            { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()

                params["kode_barang"] = kodeBarang
                params["nama_barang"] = etNamaBarang.text.toString()
                params["merk"] = etMerk.text.toString()
                params["harga"] = etHarga.text.toString()
                params["stok"] = etStok.text.toString()

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}