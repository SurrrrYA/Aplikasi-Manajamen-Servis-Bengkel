package com.bengkel.app

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import org.json.JSONObject

class RegisterCustomerActivity : AppCompatActivity() {

    lateinit var etNama: EditText
    lateinit var etUsername: EditText
    lateinit var etNoHp: EditText
    lateinit var etPassword: EditText
    lateinit var etKonfirmasiPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register_customer)

        etNama = findViewById(R.id.etNama)
        etUsername = findViewById(R.id.etUsername)
        etNoHp = findViewById(R.id.etNoHp)
        etPassword = findViewById(R.id.etPassword)
        etKonfirmasiPassword = findViewById(R.id.etKonfirmasiPassword)

        val btnDaftar = findViewById<Button>(R.id.btnDaftar)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        btnDaftar.setOnClickListener {
            daftarCustomer()
        }

        btnKembali.setOnClickListener {
            finish()
        }
    }

    private fun daftarCustomer(){

        val nama = etNama.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val noHp = etNoHp.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val konfirmasi = etKonfirmasiPassword.text.toString().trim()

        if(nama == "" || username == "" || noHp == "" || password == "" || konfirmasi == ""){
            Toast.makeText(this, "Data belum lengkap", Toast.LENGTH_SHORT).show()
            return
        }

        if(password != konfirmasi){
            Toast.makeText(this, "Konfirmasi password tidak sama", Toast.LENGTH_SHORT).show()
            return
        }

        val url =
            "http://192.168.18.7/Bengkel_API/auth/register_customer.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,
            { response ->

                val json = JSONObject(response)

                Toast.makeText(
                    this,
                    json.getString("message"),
                    Toast.LENGTH_LONG
                ).show()

                if(json.getString("status") == "success"){
                    finish()
                }
            },
            { error ->
                Toast.makeText(
                    this,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
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