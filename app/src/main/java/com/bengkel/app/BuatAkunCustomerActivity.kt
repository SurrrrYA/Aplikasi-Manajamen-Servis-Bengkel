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

class BuatAkunCustomerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_buat_akun_customer)

        val etNama = findViewById<EditText>(R.id.etNama)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etNoHp = findViewById<EditText>(R.id.etNoHp)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        btnSimpan.setOnClickListener {
            buatAkunCustomer(
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

    private fun buatAkunCustomer(
        nama: String,
        username: String,
        noHp: String,
        password: String
    ){
        val url =
            "http://192.168.18.7/Bengkel_API/customer/register_customer.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,

            Response.Listener {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                finish()
            },

            Response.ErrorListener { error ->
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