package com.bengkel.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class ServisMasukActivity : AppCompatActivity() {

    lateinit var btnSimpan: Button
    lateinit var etNama: EditText
    lateinit var etNoHp: EditText
    lateinit var etAlamat: EditText
    lateinit var etUsernameCustomer: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servis_masuk)

        val etPlat = findViewById<EditText>(R.id.etPlat)
        etNama = findViewById(R.id.etNama)
        etUsernameCustomer = findViewById(R.id.etUsernameCustomer)
        etNoHp = findViewById(R.id.etNoHp)
        etAlamat = findViewById(R.id.etAlamat)

        val etMerk = findViewById<EditText>(R.id.etMerk)
        val etTipe = findViewById<EditText>(R.id.etTipe)
        val etKeluhan = findViewById<EditText>(R.id.etKeluhan)

        btnSimpan = findViewById(R.id.btnSimpan)

        val btnCariAkun = findViewById<Button>(R.id.btnCariAkun)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        btnCariAkun.setOnClickListener {
            val keyword = etUsernameCustomer.text.toString().trim()
            cariAkunCustomer(keyword)
        }

        btnKembali.setOnClickListener {
            finish()
        }

        btnSimpan.setOnClickListener {
            btnSimpan.isEnabled = false
            btnSimpan.text = "Menyimpan..."

            simpanServis(
                etPlat.text.toString(),
                etNama.text.toString(),
                etUsernameCustomer.text.toString(),
                etNoHp.text.toString(),
                etAlamat.text.toString(),
                etMerk.text.toString(),
                etTipe.text.toString(),
                etKeluhan.text.toString()
            )
        }
    }

    private fun cariAkunCustomer(keyword: String) {
        val url =
            "http://192.168.18.7/Bengkel_API/customer/cari_akun_customer.php?keyword=$keyword"

        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                val status = response.getString("status")

                if (status == "success") {
                    val data = response.getJSONObject("data")

                    etUsernameCustomer.setText(
                        data.getString("username")
                    )

                    Toast.makeText(
                        this,
                        "Akun customer berhasil dihubungkan",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Akun tidak ditemukan",
                        Toast.LENGTH_SHORT
                    ).show()
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

    private fun simpanServis(
        plat: String,
        nama: String,
        usernameCustomer: String,
        noHp: String,
        alamat: String,
        merk: String,
        tipe: String,
        keluhan: String
    ) {
        val sharedPreferences =
            getSharedPreferences("LOGIN", MODE_PRIVATE)

        val username =
            sharedPreferences.getString("username", "")

        val url =
            "http://192.168.18.7/Bengkel_API/servis/tambah_servis.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,
            { response ->
                btnSimpan.isEnabled = true
                btnSimpan.text = "Simpan"

                Toast.makeText(
                    this,
                    response,
                    Toast.LENGTH_LONG
                ).show()
            },
            { error ->
                btnSimpan.isEnabled = true
                btnSimpan.text = "Simpan"

                Toast.makeText(
                    this,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()

                params["plat"] = plat
                params["nama"] = nama
                params["username_customer"] = usernameCustomer
                params["no_hp"] = noHp
                params["alamat"] = alamat
                params["merk"] = merk
                params["tipe"] = tipe
                params["keluhan"] = keluhan
                params["user_input"] = username.toString()

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}