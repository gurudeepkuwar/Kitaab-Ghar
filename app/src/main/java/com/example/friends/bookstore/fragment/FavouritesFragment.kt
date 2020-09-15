package com.example.friends.bookstore.fragment


import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.room.Room
import com.example.friends.bookstore.R
import com.example.friends.bookstore.adapter.DashboardRecyclerAdapter
import com.example.friends.bookstore.adapter.FavouriteRecyclerAdapter
import com.example.friends.bookstore.database.BookDatabase
import com.example.friends.bookstore.database.BookEntity
import com.example.friends.bookstore.model.Book


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FavouritesFragment : Fragment() {

    lateinit var recyclerFavourite : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var recyclerAdapter : FavouriteRecyclerAdapter
    var dbBookList = listOf<BookEntity>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_favourites, container, false)

        recyclerFavourite=view.findViewById(R.id.recyclerFavourite)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        layoutManager = GridLayoutManager(activity as Context,2)

        dbBookList = RetrieveFavourites(activity as Context).execute().get()

        if(activity!=null){
            progressLayout.visibility=View.GONE
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, dbBookList)
            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager= layoutManager

        }
        return view

    }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void,Void,List<BookEntity>>(){
        override fun doInBackground(vararg p0: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
            return db.bookDao().getAllBooks()
        }

    }


}
