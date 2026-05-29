package com.bengkel.app

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections

import org.json.JSONObject

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PembayaranServisActivity : AppCompatActivity() {

    lateinit var txtKode: TextView
    lateinit var txtPlat: TextView
    lateinit var txtKeluhan: TextView
    lateinit var txtTotal: TextView
    lateinit var listPembayaran: ListView
    lateinit var spinnerBayar: Spinner
    lateinit var spinnerMetode: Spinner

    lateinit var listData: ArrayList<String>
    lateinit var listItemStruk: ArrayList<String>

    var kodeServis = ""
    var kodeTransaksi = ""
    var platNomor = ""
    var keluhanServis = ""
    var totalBayar = 0

    var namaToko = "Bengkel"
    var alamatToko = "-"
    var noHpToko = "-"
    var headerStruk = "Struk Pembayaran"
    var footerStruk = "Terima kasih"
    var templateStruk = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pembayaran_servis)

        txtKode = findViewById(R.id.txtKode)
        txtPlat = findViewById(R.id.txtPlat)
        txtKeluhan = findViewById(R.id.txtKeluhan)
        txtTotal = findViewById(R.id.txtTotal)
        listPembayaran = findViewById(R.id.listPembayaran)
        spinnerBayar = findViewById(R.id.spinnerBayar)
        spinnerMetode = findViewById(R.id.spinnerMetode)

        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnCetak = findViewById<Button>(R.id.btnCetak)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        kodeServis = intent.getStringExtra("kode_servis").toString()
        txtKode.text = kodeServis

        listData = ArrayList()
        listItemStruk = ArrayList()

        spinnerMetode.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf("Cash", "QRIS", "Transfer", "Debit")
            )

        spinnerBayar.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf("Belum Lunas", "Lunas")
            )

        btnSimpan.setOnClickListener {
            simpanPembayaran()
        }

        btnCetak.setOnClickListener {
            getSettingStruk()
        }

        btnKembali.setOnClickListener {
            finish()
        }

        requestBluetoothPermission()
        getPembayaran()
    }

    private fun requestBluetoothPermission(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                ),
                100
            )
        }
    }

    private fun formatRupiah(angka: Int): String{

        return NumberFormat
            .getInstance(
                Locale("in", "ID")
            )
            .format(angka)
    }

    private fun getTanggalSekarang(): String{

        return SimpleDateFormat(
            "dd-MM-yyyy HH:mm",
            Locale("in", "ID")
        ).format(Date())
    }

    private fun getPembayaran(){

        val url =
            "http://192.168.18.7/Bengkel_API/servis/get_pembayaran_servis.php?kode_servis=$kodeServis"

        val request =
            JsonArrayRequest(
                Request.Method.GET,
                url,
                null,

                { response ->

                    listData.clear()
                    listItemStruk.clear()
                    totalBayar = 0

                    for(i in 0 until response.length()){

                        val data =
                            response.getJSONObject(i)

                        platNomor =
                            data.getString("plat_nomor")

                        keluhanServis =
                            data.getString("keluhan")

                        txtPlat.text =
                            "Plat : $platNomor"

                        txtKeluhan.text =
                            "Keluhan : $keluhanServis"

                        if(!data.isNull("nama_item")){

                            val nama =
                                data.getString("nama_item")

                            val qty =
                                data.getInt("qty")

                            val harga =
                                data.getInt("harga")

                            val subtotal =
                                data.getInt("subtotal")

                            totalBayar += subtotal

                            listData.add(
                                "$nama\n" +
                                        "Qty : $qty\n" +
                                        "Harga : Rp ${formatRupiah(harga)}\n" +
                                        "Subtotal : Rp ${formatRupiah(subtotal)}"
                            )

                            listItemStruk.add(
                                "$nama|$qty|$harga|$subtotal"
                            )
                        }
                    }

                    txtTotal.text =
                        "Rp ${formatRupiah(totalBayar)}"

                    listPembayaran.adapter =
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

    private fun getSettingStruk(){

        val url =
            "http://192.168.18.7/Bengkel_API/struk/get_setting_struk.php"

        val request =
            com.android.volley.toolbox.JsonObjectRequest(
                Request.Method.GET,
                url,
                null,

                { response ->

                    namaToko =
                        response.optString("nama_toko", "Bengkel")

                    alamatToko =
                        response.optString("alamat", "-")

                    noHpToko =
                        response.optString("no_hp", "-")

                    headerStruk =
                        response.optString("header_struk", "Struk Pembayaran")

                    footerStruk =
                        response.optString("footer_struk", "Terima kasih")

                    templateStruk =
                        response.optString("template_struk", "")

                    cetakStruk()
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

    private fun cetakStruk(){

        try{

            val bluetoothAdapter =
                BluetoothAdapter.getDefaultAdapter()

            if(bluetoothAdapter == null){

                Toast.makeText(
                    this,
                    "Bluetooth tidak tersedia",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            if(!bluetoothAdapter.isEnabled){

                Toast.makeText(
                    this,
                    "Aktifkan Bluetooth dulu",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            val printerConnection =
                BluetoothPrintersConnections.selectFirstPaired()

            if(printerConnection == null){

                Toast.makeText(
                    this,
                    "Printer belum dipairing",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            val printer =
                EscPosPrinter(
                    printerConnection,
                    203,
                    48f,
                    32
                )

            var itemText = ""

            for(item in listItemStruk){

                val split =
                    item.split("|")

                val nama =
                    split[0]

                val qty =
                    split[1]

                val harga =
                    split[2].toInt()

                val subtotal =
                    split[3].toInt()

                itemText +=
                    "[L]$nama\n" +
                            "[L]$qty x Rp ${formatRupiah(harga)}[R]Rp ${formatRupiah(subtotal)}\n"
            }

            val metode =
                spinnerMetode.selectedItem.toString()

            val status =
                spinnerBayar.selectedItem.toString()

            val tanggalTransaksi =
                getTanggalSekarang()

            if(kodeTransaksi == ""){
                kodeTransaksi =
                    "TRX-" + kodeServis
            }

            var struk =
                templateStruk

            if(struk == ""){

                struk =
                    "[C]<b>{nama_toko}</b>\n" +
                            "[C]{alamat}\n" +
                            "[C]Telp : {no_hp}\n" +
                            "[C]------------------------------\n" +
                            "[C]{header_struk}\n" +
                            "[L]Kode Servis : {kode_servis}\n" +
                            "[L]Kode Trans  : {kode_transaksi}\n" +
                            "[L]Plat        : {plat_nomor}\n" +
                            "[L]Tanggal     : {tanggal_transaksi}\n" +
                            "[C]------------------------------\n" +
                            "{item}\n" +
                            "[C]------------------------------\n" +
                            "[L]Metode : {metode_bayar}\n" +
                            "[L]Status : {status_bayar}\n" +
                            "[L]TOTAL[R]Rp {total_bayar}\n" +
                            "[C]------------------------------\n" +
                            "[C]{footer_struk}\n\n\n"
            }

            struk =
                struk.replace(
                    "{nama_toko}",
                    namaToko
                )

            struk =
                struk.replace(
                    "{alamat}",
                    alamatToko
                )

            struk =
                struk.replace(
                    "{no_hp}",
                    noHpToko
                )

            struk =
                struk.replace(
                    "{header_struk}",
                    headerStruk
                )

            struk =
                struk.replace(
                    "{footer_struk}",
                    footerStruk
                )

            struk =
                struk.replace(
                    "{kode_servis}",
                    kodeServis
                )

            struk =
                struk.replace(
                    "{kode_transaksi}",
                    kodeTransaksi
                )

            struk =
                struk.replace(
                    "{plat_nomor}",
                    platNomor
                )

            struk =
                struk.replace(
                    "{keluhan}",
                    keluhanServis
                )

            struk =
                struk.replace(
                    "{tanggal_transaksi}",
                    tanggalTransaksi
                )

            struk =
                struk.replace(
                    "{metode_bayar}",
                    metode
                )

            struk =
                struk.replace(
                    "{status_bayar}",
                    status
                )

            struk =
                struk.replace(
                    "{total_bayar}",
                    formatRupiah(totalBayar)
                )

            struk =
                struk.replace(
                    "{item}",
                    itemText
                )

            printer.printFormattedText(
                struk
            )

            Toast.makeText(
                this,
                "Struk berhasil dicetak",
                Toast.LENGTH_SHORT
            ).show()

        }catch (e: Exception){

            Toast.makeText(
                this,
                e.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun simpanPembayaran(){

        val url =
            "http://192.168.18.7/Bengkel_API/servis/update_pembayaran_servis.php"

        val request =
            object : StringRequest(
                Request.Method.POST,
                url,

                { response ->

                    try{

                        val json =
                            JSONObject(response)

                        kodeTransaksi =
                            json.optString(
                                "kode_transaksi",
                                ""
                            )

                        Toast.makeText(
                            this,
                            json.optString(
                                "message",
                                response
                            ),
                            Toast.LENGTH_LONG
                        ).show()

                    }catch (e: Exception){

                        Toast.makeText(
                            this,
                            response,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    getPembayaran()
                },

                { error ->

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

                    params["biaya_servis"] =
                        totalBayar.toString()

                    params["status_bayar"] =
                        spinnerBayar.selectedItem.toString()

                    params["metode_bayar"] =
                        spinnerMetode.selectedItem.toString()

                    return params
                }
            }

        Volley.newRequestQueue(this).add(request)
    }
}