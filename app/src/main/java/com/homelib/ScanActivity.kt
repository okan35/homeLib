package com.homelib


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.Result
import com.homelib.bookTemplateGoogle.BookBase
import com.homelib.bookTemplateOpenLibrary.BookDetails
import com.homelib.data.Book
import com.homelib.util.Status
import com.homelib.viewmodels.BookViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import me.dm7.barcodescanner.zxing.ZXingScannerView
import kotlin.coroutines.CoroutineContext


class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler, CoroutineScope {
    private var TAG: String = "ScanActivity"
    private var job: Job = Job()
    private var mScannerView: ZXingScannerView? = null
    private var bookBarcode: String = ""
    var isbn = ""
    var title = ""
    var author = ""
    var publishedDate = ""
    var thumbnail = ""
    private lateinit var book2: Book
    private lateinit var bookViewModel: BookViewModel
    private var isBookSaved: Boolean = false

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        mScannerView = ZXingScannerView(this)   // Programmatically initialize the scanner view

        //setContentView(mScannerView)                // Set the scanner view as the content view
        val parent = RelativeLayout(this)
        parent.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )


        val button = Button(this)
        button.text = "FLASH"


        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.addRule(RelativeLayout.BELOW, button.id)
        mScannerView!!.layoutParams = params
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
        bookBarcode = p0!!.text
        CoroutineScope(IO).launch {
            val result = async {
                isBookSaved = bookViewModel.isBookExisting(bookBarcode)
            }.await()

            Log.d(TAG, " isSaved $isBookSaved")
            CoroutineScope(Main).launch {
                if (!isBookSaved) {
                    getBookFromGoogle(bookBarcode)
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ScanActivity, "Book is already saved ", Toast.LENGTH_SHORT)
                            .show()
                        onBackPressed()
                    }
                }
            }
        }

    }

    private fun getBookFromOpenLibrary(isbn: String) {
        runOnUiThread {
            Toast.makeText(this, "Checking open library", Toast.LENGTH_LONG).show()
        }

        bookViewModel.getBookFromOpenLibrary(isbn).observe(this, Observer {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users -> retrieveListOpenLibrary(users) }
                    }
                    Status.ERROR -> {
                        runOnUiThread {
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                        onBackPressed()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    private fun getBookFromGoogle(isbn: String) {
        runOnUiThread {
            Toast.makeText(this, "Checking google books", Toast.LENGTH_LONG).show()
        }

        bookViewModel.getBookFromGoogle(isbn).observe(this, Observer {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users -> retrieveListGoogleBooks(listOf(users)) }
                    }
                    Status.ERROR -> {
                        runOnUiThread {
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                        onBackPressed()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    private fun retrieveListGoogleBooks(books: List<BookBase>) {
        if (books[0].totalItems != 0) {
            title = books[0].items[0].volumeInfo.title
            author = books[0].items[0].volumeInfo.authors[0]
            publishedDate = books[0].items[0].volumeInfo.publishedDate
            thumbnail = books[0].items[0].volumeInfo.imageLinks.smallThumbnail
            isbn = bookBarcode
            book2 = Book(
                title = title,
                author = author,
                year = publishedDate,
                imageLink = thumbnail,
                isbn = isbn
            )
            bookViewModel.insert(book2)

            onBackPressed()
        } else {
            getBookFromOpenLibrary("ISBN:$bookBarcode")
        }

    }

    private fun retrieveListOpenLibrary(books: HashMap<String, BookDetails>) {
        if (!books.toString().equals("{}")) {
            for ((key, value) in books) {

                title = if (value.details.title != null) value.details.title else "no title"
                author =
                    if (value.details.authors != null) value.details.authors[0].toString() else "no auth"
                publishedDate =
                    if (value.details.publish_date.toString() != null) value.details.publish_date.toString() else "no date"
                thumbnail = if (value.thumbnail_url != null) value.thumbnail_url else ""
                isbn = bookBarcode

                book2 = Book(
                    title = title,
                    author = author,
                    year = publishedDate,
                    imageLink = thumbnail,
                    isbn = isbn
                )

                bookViewModel.insert(book2)

                onBackPressed()
            }
        } else {
            runOnUiThread {
                Toast.makeText(this@ScanActivity, "Book was not found", Toast.LENGTH_SHORT).show()
            }
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

}

