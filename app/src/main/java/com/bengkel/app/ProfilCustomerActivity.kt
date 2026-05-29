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
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject

class ProfilCustomerActivity : AppCompatActivity() {

    lateinit var etNama: EditText
    lateinit var etEmail: EditText
    lateinit var etNoHp: EditText
    lateinit var etAlamat: EditText

    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profil_customer)

        etNama = findViewById(R.id.etNama)
        etEmail = findViewById(R.id.etEmail)
        etNoHp = findViewById(R.id.etNoHp)
        etAlamat = findViewById(R.id.etAlamat)

        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        val user = FirebaseAuth.getInstance().currentUser

        email = user?.email ?: ""

        etEmail.setText(email)
        etNama.setText(user?.displayName)

        getProfil()

        btnSimpan.setOnClickListener {
            simpanProfil()
        }

        btnKembali.setOnClickListener {
            finish()
        }
    }

    private fun getProfil() {

        val url =
            "http://192.168.18.7/Bengkel_API/customer/get_profil_customer.php?email=$email"

        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->

                if (response.has("nama")) {
                    etNama.setText(response.getString("nama"))
                    etNoHp.setText(response.getString("no_hp"))
                    etAlamat.setText(response.getString("alamat"))
                }

            },
            {

            }
        )

        Volley.newRequestQueue(this).add(request)
    }

    private fun simpanProfil() {

        val url =
            "http://192.168.18.7/Bengkel_API/customer/simpan_profil_customer.php"

        val request = object : StringRequest(
            Request.Method.POST,
            url,
            { response ->

                Toast.makeText(
                    this,
                    "Profil berhasil disimpan",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            },
            { error ->

                Toast.makeText(
                    this,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        ) {

            override fun getParams(): MutableMap<String, String> {

                val params = HashMap<String, String>()

                params["email"] = email
                params["nama"] = etNama.text.toString()
                params["no_hp"] = etNoHp.text.toString()
                params["alamat"] = etAlamat.text.toString()

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}