package com.bengkel.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class BuatAkunKasirActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_buat_akun_kasir)

        val etNama = findViewById<EditText>(R.id.etNama)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etNoHp = findViewById<EditText>(R.id.etNoHp)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        btnSimpan.setOnClickListener {
            buatAkunKasir(
                etNama.text.toString(),
                etUsername.text.toString(),
                etNoHp.text.toString(),
                etPassword.text.toString()
            )
        }

        btnKembali.setOnClickListener {
            finish()
        }
    }

    private fun buatAkunKasir(
        nama: String,
        username: String,
        noHp: String,
        password: String
    ){
        val url =
            "http://192.168.18.7/Bengkel_API/kasir/register_kasir.php"

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
        ){
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()

                params["nama"] = nama
                params["username"] = username
                params["no_hp"] = noHp
                params["password"] = password

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}