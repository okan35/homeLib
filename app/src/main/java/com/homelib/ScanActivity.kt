package com.homelib

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import android.widget.Toast
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView





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
        Toast.makeText(this@ScanActivity,p0?.text,Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

    /*fun fetchJson(bookIsbn: String) {
        var url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + bookIsbn

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                println("Error in executing call")
            }

            override fun onResponse(call: Call?, response: Response?) {

                try {
                    val body = response?.body()?.string()
                    val jsonObject = JSONObject(body)
                    if (jsonObject.getString("totalItems").equals("0")) {
                        val intent = Intent(this@ScanActivity, MainActivity::class.java)
                        intent.putExtra("title", "Book was not found")
                        startActivity(intent)
                        return
                    }

                    val items = jsonObject.getJSONArray("items").getJSONObject(0)


                    val id = items.getString("id")
                    val volumeInfo = items.getJSONObject("volumeInfo")
                    val title = volumeInfo.getString("title")
                    val author = volumeInfo.getJSONArray("authors").get(0).toString()
                    val publishedDate = volumeInfo.getString("publishedDate")
                    val bookCover = volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail")

                    val book = Book(id, author, title, publishedDate, bookCover)

                    //println("id " + book.id + " title " + book.title + "bookcover " + bookCover)

                    val bookDB = BookDB(this@ScanActivity)
                    val result : Long = bookDB.insertBook(book)
                    print("result " + result)
                    val intent = Intent(this@ScanActivity, MainActivity::class.java)
                    intent.putExtra("id", book.id)
                    startActivity(intent)
                } catch (e:Exception) {
                    e.printStackTrace()
                }



            }
        })

    }*/
}

class HomeFeed(val Book1: List<Book1>)

class Book1(val title: String, val author: String, val isbn: Long, val year: String, val publisher: String)



