package com.bengkel.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import org.json.JSONObject

import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import java.io.IOException

class ProgressMenginapActivity : AppCompatActivity() {

    lateinit var tvKode: TextView
    lateinit var tvPlat: TextView
    lateinit var tvKeluhan: TextView
    lateinit var etProgress: EditText
    lateinit var imgPreview: ImageView
    lateinit var listProgress: ListView

    lateinit var listData: ArrayList<String>

    var kodeServis = ""
    var plat = ""
    var keluhan = ""

    var imageUri: Uri? = null
    val PICK_IMAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_progress_menginap)

        tvKode = findViewById(R.id.tvKode)
        tvPlat = findViewById(R.id.tvPlat)
        tvKeluhan = findViewById(R.id.tvKeluhan)
        etProgress = findViewById(R.id.etProgress)
        imgPreview = findViewById(R.id.imgPreview)
        listProgress = findViewById(R.id.listProgress)

        val btnPilihFoto = findViewById<Button>(R.id.btnPilihFoto)
        val btnTambahProgress = findViewById<Button>(R.id.btnTambahProgress)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        kodeServis = intent.getStringExtra("kode_servis").toString()
        plat = intent.getStringExtra("plat_nomor").toString()
        keluhan = intent.getStringExtra("keluhan").toString()

        tvKode.text = kodeServis
        tvPlat.text = "Plat : $plat"
        tvKeluhan.text = "Keluhan : $keluhan"

        listData = ArrayList()

        btnPilihFoto.setOnClickListener {
            val intent =
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )

            startActivityForResult(
                intent,
                PICK_IMAGE
            )
        }

        btnTambahProgress.setOnClickListener {
            tambahProgress()
        }

        btnKembali.setOnClickListener {
            finish()
        }

        getProgress()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if(requestCode == PICK_IMAGE &&
            resultCode == RESULT_OK &&
            data != null){

            imageUri =
                data.data

            imgPreview.setImageURI(
                imageUri
            )
        }
    }

    private fun tambahProgress(){

        val progress =
            etProgress.text.toString().trim()

        if(progress == ""){
            Toast.makeText(
                this,
                "Progress belum diisi",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val url =
            "http://192.168.18.7/Bengkel_API/progress/tambah_progress.php"

        val sharedPreferences =
            getSharedPreferences("LOGIN", MODE_PRIVATE)

        val username =
            sharedPreferences.getString("username", "")

        val request = object : StringRequest(
            Request.Method.POST,
            url,

            { response ->

                val json =
                    JSONObject(response)

                Toast.makeText(
                    this,
                    json.getString("message"),
                    Toast.LENGTH_LONG
                ).show()

                if(json.getString("status") == "success"){

                    val kodeProgress =
                        json.getString("kode_progress")

                    if(imageUri != null){
                        uploadFoto(kodeProgress)
                    }else{
                        resetForm()
                        getProgress()
                    }
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

                val params =
                    HashMap<String, String>()

                params["kode_servis"] =
                    kodeServis

                params["deskripsi_progress"] =
                    progress

                params["user_input"] =
                    username.toString()

                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    private fun uploadFoto(kodeProgress: String){

        if(imageUri == null){
            return
        }

        try{

            val inputStream =
                contentResolver.openInputStream(imageUri!!)

            val bytes =
                inputStream!!.readBytes()

            val requestBody =
                MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "kode_progress",
                        kodeProgress
                    )
                    .addFormDataPart(
                        "foto",
                        "progress.jpg",
                        RequestBody.create(
                            "image/*".toMediaTypeOrNull(),
                            bytes
                        )
                    )
                    .build()

            val request =
                okhttp3.Request.Builder()
                    .url(
                        "http://192.168.18.7/Bengkel_API/uploads/upload_foto.php"
                    )
                    .post(requestBody)
                    .build()

            val client =
                OkHttpClient()

            client.newCall(request)
                .enqueue(object : Callback {

                    override fun onFailure(
                        call: Call,
                        e: IOException
                    ) {
                        runOnUiThread {
                            Toast.makeText(
                                this@ProgressMenginapActivity,
                                e.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onResponse(
                        call: Call,
                        response: okhttp3.Response
                    ) {
                        runOnUiThread {
                            Toast.makeText(
                                this@ProgressMenginapActivity,
                                "Foto progress berhasil diupload",
                                Toast.LENGTH_LONG
                            ).show()

                            resetForm()
                            getProgress()
                        }
                    }
                })

        }catch (e: Exception){

            Toast.makeText(
                this,
                e.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getProgress(){

        val url =
            "http://192.168.18.7/Bengkel_API/progress/get_progress_by_servis.php?kode_servis=$kodeServis"

        val request =
            JsonArrayRequest(
                Request.Method.GET,
                url,
                null,

                { response ->

                    listData.clear()

                    for(i in 0 until response.length()){

                        val data =
                            response.getJSONObject(i)

                        val progress =
                            data.getString("deskripsi_progress")

                        val tanggal =
                            data.getString("tanggal_progress")

                        val userInput =
                            data.getString("user_input")

                        listData.add(
                            "$progress\n" +
                                    "Tanggal : $tanggal\n" +
                                    "Input : $userInput"
                        )
                    }

                    listProgress.adapter =
                        ArrayAdapter(
                            this,
                            android.R.layout.simple_list_item_1,
                            listData
                        )
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

    private fun resetForm(){

        etProgress.setText("")
        imageUri = null
        imgPreview.setImageDrawable(null)
    }
}