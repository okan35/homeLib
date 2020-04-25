package com.homelib.adapters

import com.homelib.R
import com.homelib.data.Book
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class BookListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var books = emptyList<Book>() // Cached copy of words

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorTextView: TextView = itemView.findViewById(R.id.author)
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val yearTextView: TextView = itemView.findViewById(R.id.year)
        val bookCoverView: ImageView = itemView.findViewById(R.id.bookCover)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = inflater.inflate(R.layout.book_row, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val current = books[position]

        holder.authorTextView.text = current.author
        holder.titleTextView.text = current.title
        holder.yearTextView.text = current.year
        if (current.imageLink?.isNotEmpty()!!){
            Picasso.get().load(current.imageLink).into(holder.bookCoverView)
        }


    }

    fun setBooks(books: List<Book>) {
        this.books = books
        notifyDataSetChanged()
    }

    override fun getItemCount() = books.size
}