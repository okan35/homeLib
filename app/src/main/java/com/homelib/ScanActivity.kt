package com.homelib

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.Result
import com.homelib.data.Book
import com.homelib.viewmodels.BookViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.json.JSONObject
import java.net.URL
import kotlin.coroutines.CoroutineContext


class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler, CoroutineScope {

    private var job: Job = Job()
    private var mScannerView: ZXingScannerView? = null
    var barcode: Long = 0
    var title = ""
    var author = ""
    var publishedDate = ""
    var thumbnail = ""
    private lateinit var book2: Book
    private lateinit var bookViewModel: BookViewModel

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        mScannerView = ZXingScannerView(this)   // Programmatically initialize the scanner view

        //setContentView(mScannerView)                // Set the scanner view as the content view
        val parent = RelativeLayout(this)
        parent.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)


        val button = Button(this)
        button.text="FLASH"


        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.BELOW,button.id)
        mScannerView!!.layoutParams=params
        parent.addView(mScannerView)

        parent.addView(button)
        setContentView(parent)

        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)


        button.setOnClickListener {
            mScannerView!!.flash = !mScannerView!!.flash
        }             // Set the scanner view as the content view
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
    }

    fun fetchJson(isbn: String) {
        launch {
            try {
                if (!bookViewModel.isBookExisting(isbn.toLong())) {
                    AsyncTask.execute {
                        try {
                            if (parseJson(isbn)){
                                book2 = Book(title = title, author = author, year = publishedDate, imageLink = thumbnail, isbn = isbn)
                                bookViewModel.insert(book2)
                                val status2 = bookViewModel.isBookSaved
                                runOnUiThread {
                                    if (status2 > -1) {
                                        Toast.makeText(applicationContext, "record save", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                runOnUiThread{
                                    Toast.makeText(applicationContext, "Book was not found", Toast.LENGTH_LONG).show()

                                }
                            }
                            onBackPressed()//IF THIS IS NOT CALLED HERE THE ON CREATE ON MAIN ACTIVITYSOMEHOW DOES NOT FILL LIST.
                        } catch (e: Exception) {
                            e.printStackTrace()
                            runOnUiThread {
                                Toast.makeText(applicationContext, "Json parsing error ", Toast.LENGTH_LONG).show()
                                onBackPressed()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@ScanActivity, "Book already saved", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            } catch (e: Exception){
                Toast.makeText(this@ScanActivity, e.message, Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        }
    }

    fun parseJson(isbn: String): Boolean{
        var apiResponse = URL("https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn").readText()
        var jsonObject = JSONObject(apiResponse)
        if (jsonObject.getInt("totalItems") > 0) {
            val items: JSONObject = jsonObject.getJSONArray("items").getJSONObject(0)
            val volumeInfo: JSONObject = items.getJSONObject("volumeInfo")
            title = volumeInfo.getString("title")
            author = volumeInfo.getJSONArray("authors").get(0).toString()
            publishedDate = volumeInfo.getString("publishedDate")
            thumbnail = volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail")
            runOnUiThread {
                Toast.makeText(applicationContext,"Source: google books",Toast.LENGTH_SHORT).show()
            }
            return true
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
                runOnUiThread {
                    Toast.makeText(applicationContext,"Source: open library",Toast.LENGTH_SHORT).show()
                }
                return true

            }
        }
        return false
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}

