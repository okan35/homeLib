package com.homelib

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.homelib.db.DbHandler
import com.homelib.viewmodels.BookModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {


    val mutableList = mutableListOf<BookModel>()
    val dbHandler = DbHandler(this@MainActivity)
    private lateinit var bookAdapter: BooksAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        validatePermission()
        initRecyclerView()
        addData()
        button_scan.setOnClickListener {
            performAction()
        }

        Log.v("ON CREATE", "ON CREATE CALLED")
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.sync -> {
            // User chose the "Print" item
            AsyncTask.execute {
                try {
                    sendPostRequest()
                    //getRequest()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            Toast.makeText(this, "Sync action", Toast.LENGTH_LONG).show()
            true
        }
        android.R.id.home -> {
            Toast.makeText(this, "Home action", Toast.LENGTH_LONG).show()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    fun sendPostRequest() {

        val mURL = URL("http://192.168.64.104:8080/savebooks")

        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "POST"


            val wr = BufferedWriter(OutputStreamWriter(outputStream))
            wr.write(dbHandler.getRows().toString())
            wr.flush()
            wr.close()

            println("URL : $url")
            println("Response Code : $responseCode")

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                println("Response : $response")
            }
        }
    }

    fun getRequest() {
        val jsonStr = URL("http://192.168.64.104:8080/").readText()

        //val jsonStr = URL("http://0.0.0.0.xip.io:8080/").readText()
        Log.d("OKAN ", jsonStr)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun performAction() {
        val intent = Intent(this, ScanActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun addData() {
        /*for (book in  dbHandler.viewBooks()){
            mutableList.add(BookModel(book.title, book.author,book.year,book.imageLink,book.isbn))
        }*/
        Log.v("LIST ", dbHandler.viewBooks().toString())
        bookAdapter.updateData(dbHandler.viewBooks())
    }

    private fun initRecyclerView() {
        books_recycler_view.apply {
            books_recycler_view.layoutManager =
                LinearLayoutManager(this@MainActivity)
            bookAdapter = BooksAdapter(mutableList)
            books_recycler_view.adapter = bookAdapter
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    /*  setContentView(mScannerView)
                      mScannerView!!.startCamera()*/
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(
                        this,
                        "Feature will not work without camera permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun validatePermission() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    1
                )

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            /*setContentView(mScannerView)
            mScannerView!!.startCamera()*/
        }
    }


}



