package com.example.friends.bookstore.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.friends.bookstore.R
import com.example.friends.bookstore.database.BookEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context : Context,val bookList : List<BookEntity>): RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyler_favorite_single_row,parent,false)
        return FavouriteViewHolder(view)

    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val book = bookList[position]
        holder.txtBookName.text= book.bookName
        holder.txtBookAuthor.text=book.bookAuthor
        holder.txtBookPrice.text=book.bookPrice
        holder.txtBookRating.text=book.bookRating
        //holder.textBookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.txtBookImage);

    }

    class FavouriteViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val txtBookName: TextView = view.findViewById(R.id.txtFavBookTitle)
        var txtBookAuthor: TextView = view.findViewById(R.id.txtFavBookAuthor)
        var txtBookPrice: TextView = view.findViewById(R.id.txtFavBookPrice)
        var txtBookRating: TextView = view.findViewById(R.id.txtFavBookRating)
        var txtBookImage: ImageView = view.findViewById(R.id.imgFavBookImage)
        val llContent: LinearLayout = view.findViewById(R.id.llFavContent)
    }
}