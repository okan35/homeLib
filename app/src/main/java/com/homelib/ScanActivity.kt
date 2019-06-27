package com.homelib

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.google.gson.*
import com.google.zxing.Result
import com.homelib.db.DbHandler
import com.homelib.models.BookModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import me.dm7.barcodescanner.zxing.ZXingScannerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.net.URL
import com.homelib.MainActivity as MainActivity1


class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {


    private var mScannerView: ZXingScannerView? = null
    var barcode: Long = 0
    //var books = ArrayList<Book>()
    //  var booksMutable = mutableListOf<Book>()

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        mScannerView = ZXingScannerView(this)   // Programmatically initialize the scanner view
        setContentView(mScannerView)                // Set the scanner view as the content view
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView!!.startCamera()          // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()           // Stop camera on pause
    }

    override fun handleResult(p0: Result?) {
        Toast.makeText(this@ScanActivity, p0?.text, Toast.LENGTH_SHORT).show()
        fetchJson(p0!!.text)
        //onBackPressed()
    }

    fun fetchJson(isbn: String) {
            AsyncTask.execute(Runnable {
                try {
                    val apiResponse = URL("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn).readText()
                    val jsonObject = JSONObject(apiResponse)
                    val items:JSONObject = jsonObject.getJSONArray("items").getJSONObject(0)
                    val volumeInfo : JSONObject = items.getJSONObject("volumeInfo")
                    val title = volumeInfo.getString("title")
                    val author = volumeInfo.getJSONArray("authors").get(0).toString()
                    val year = volumeInfo.getString("publishedDate")
                    val bookCover = volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail")
                    val book = BookModel(title,author,year,bookCover)
                    val dbHandler = DbHandler(this@ScanActivity)
                    val status = dbHandler.addBook(book)
                    //Log.d("cdcd "," title" + title  + " author " + author + " year " + year + "bookcover " + bookCover)
                    runOnUiThread {
                        if (status > -1) {
                            Toast.makeText(applicationContext, "record save", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e:Exception) {
                    runOnUiThread {

                        Toast.makeText(applicationContext, "Json parsing error ", Toast.LENGTH_LONG).show()

                    }
                }

            })


        onBackPressed()



    }





}

