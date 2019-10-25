package com.homelib.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.homelib.models.BookModel
import android.database.DatabaseUtils




class DbHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "books.db"
        private val TABLE_BOOKS = "books_table"
        private val COL_TITLE = "title"
        private val COL_AUTHOR = "author"
        private val COL_IMAGE_LINK = "image_link"
        private val COL_YEAR = "year"
        private val COL_ISBN = "isbn"
        private val COL_ID = "id"
    }
    override fun onCreate(db: SQLiteDatabase?) {

        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_BOOKS + "("
                + COL_ID + " INTEGER PRIMARY KEY," + COL_ISBN + " INTEGER ," + COL_AUTHOR + " TEXT," + COL_IMAGE_LINK + " TEXT," + COL_YEAR + " TEXT,"
                + COL_TITLE + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS)
        onCreate(db)
    }


    //method to insert data
    fun addBook(book: BookModel):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(COL_AUTHOR, book.author)
        contentValues.put(COL_TITLE, book.title)
        contentValues.put(COL_IMAGE_LINK, book.imageLink)
        contentValues.put(COL_YEAR, book.year)
        contentValues.put(COL_ISBN, book.isbn)

        // Inserting Row
        val success = db.insert(TABLE_BOOKS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to read data
    fun viewBooks():ArrayList<BookModel>{
        val empList:ArrayList<BookModel> = ArrayList<BookModel>()
        val selectQuery = "SELECT  * FROM $TABLE_BOOKS"
        val db = this.readableDatabase
        val cursor: Cursor
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var bookAuthor: String
        var bookTitle: String
        var bookYear: String
        var bookImageLink : String
        var bookIsbn : String
        if (cursor.moveToFirst()) {
            do {

                bookAuthor = cursor.getString(cursor.getColumnIndex(COL_AUTHOR))
                bookTitle = cursor.getString(cursor.getColumnIndex(COL_TITLE))
                bookYear = cursor.getString(cursor.getColumnIndex(COL_YEAR))
                bookImageLink = cursor.getString(cursor.getColumnIndex(COL_IMAGE_LINK))
                bookIsbn= cursor.getString(cursor.getColumnIndex(COL_ISBN))
                val emp= BookModel(title = bookTitle, author = bookAuthor, year= bookYear,imageLink = bookImageLink, isbn = bookIsbn)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return empList
    }

    fun getBookByISBN(isbn: Long):Boolean {
        return DatabaseUtils.queryNumEntries(this.readableDatabase, TABLE_BOOKS, "$COL_ISBN = ?", arrayOf("$isbn")) > 0
    }


    //method to update data
    /*fun updateEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName) // EmpModelClass Name
        contentValues.put(KEY_EMAIL,emp.userEmail ) // EmpModelClass Email

        // Updating Row
        val success = db.update(TABLE_BOOKS, contentValues,"id="+emp.userId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_BOOKS,"id="+emp.userId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }*/
}