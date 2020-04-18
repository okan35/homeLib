package com.homelib

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.homelib.db.DbHandler
import com.homelib.models.BookModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    val mutableList = mutableListOf<BookModel>()
    val dbHandler = DbHandler(this@MainActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        validatePermission()
        for (book in  dbHandler.viewBooks()){
            Log.d("BOOK " ,"title"+book.title+" author " + book.author + " year " + book.year + " image " + book.image + " isbn " + book.isbn)
            mutableList.add(BookModel(book.title, book.author,book.year,book.image,book.isbn))
        }
        Log.d("MAIN ACTIVITY " ,  dbHandler.viewBooks().size.toString())

        books_recycler_view.layoutManager = LinearLayoutManager(this)
        books_recycler_view.adapter = BooksAdapter(mutableList)
        //fillList()

/*
        books_recycler_view.layoutManager = LinearLayoutManager(this)


        //rv_parts.adapter = PartAdapter(testData)
        var mutableList = arrayListOf<BookModel>()
        val adapter = BooksAdapter(mutableList)

        adapter.clearData()
        mutableList = createTestData()

        books_recycler_view.adapter = BooksAdapter(mutableList)


        // Create the PartAdapter
        // 1st parameter: our generated testData
        // 2nd parameter: item click handler function (implemented below) as function parameter

        books_recycler_view.adapter?.notifyDataSetChanged()*/
        button_scan.setOnClickListener {
            performAction()
        }
    }

    private fun performAction() {
        val intent = Intent(this, ScanActivity::class.java)
        startActivity(intent)

    }



    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
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
                    Toast.makeText(this, "Feature will not work without camera permission", Toast.LENGTH_SHORT).show()
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
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA),
                    1)

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



