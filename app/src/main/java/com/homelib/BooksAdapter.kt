package com.homelib

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.homelib.models.BookModel
import kotlinx.android.synthetic.main.book_row.view.*

class BooksAdapter (val bookList: ArrayList<BookModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.book_row, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BookViewHolder).bind(bookList[position])
    }

    override fun getItemCount() = bookList.size

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(part: BookModel) {
            itemView.title.text = part.title
            itemView.year.text = part.year
            //itemView.bookCover.text = part.author
            itemView.author.text = part.author

        }
    }

    fun clearData(){
        bookList.clear()
        this.notifyDataSetChanged()
    }

    fun updateData(data: List<BookModel>) {
        bookList.clear()
        bookList.addAll(data)
        notifyDataSetChanged()
    }
}