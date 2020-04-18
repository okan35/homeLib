package com.homelib

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.zxing.Result
import com.homelib.db.DbHandler
import com.homelib.models.BookModel
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.json.JSONObject
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
        var dbHandler = DbHandler(this@ScanActivity)
        val isExistingBook = dbHandler.getBookByISBN(isbn.toLong())

        var title = ""
        var author = ""
        var publishedDate = ""
        var thumbnail = ""
        var book: BookModel
        if (!isExistingBook) {
            AsyncTask.execute {
                try {
                    var apiResponse = URL("https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn").readText()
                    var jsonObject = JSONObject(apiResponse)
                    if (jsonObject.getInt("totalItems") > 0) {
                        val items: JSONObject = jsonObject.getJSONArray("items").getJSONObject(0)
                        val volumeInfo: JSONObject = items.getJSONObject("volumeInfo")
                        title = volumeInfo.getString("title")
                        author = volumeInfo.getJSONArray("authors").get(0).toString()
                        publishedDate = volumeInfo.getString("publishedDate")
                        thumbnail = volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail")
                    } else {
                        apiResponse =
                            URL("https://openlibrary.org/api/books?bibkeys=ISBN:$isbn&jscmd=details&format=json").readText()
                        jsonObject = JSONObject(apiResponse)
                        if (jsonObject.length() > 0) {

                            val detailsObject = jsonObject.getJSONObject("ISBN:$isbn").getJSONObject("details")
                            title = detailsObject.getString("title")
                            thumbnail = jsonObject.getJSONObject("ISBN:$isbn").getString("thumbnail_url")

                            publishedDate = detailsObject.getString("publish_date")
                            author = detailsObject.getJSONArray("authors").getJSONObject(0).getString("name")
                        }
                    }
                    Log.d("isbn saved ", isbn)
                    book = BookModel(title = title, author = author, year = publishedDate, image = thumbnail, isbn = isbn)
                    dbHandler = DbHandler(this@ScanActivity)
                    val status = dbHandler.addBook(book)

                    runOnUiThread {
                        if (status > -1) {
                            Toast.makeText(applicationContext, "record save", Toast.LENGTH_LONG).show()
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Json parsing error ", Toast.LENGTH_LONG).show()
                    }
                }

            }
        } else {
            Toast.makeText(this@ScanActivity, "Book already saved", Toast.LENGTH_SHORT).show()
        }



        onBackPressed()
    }
}

