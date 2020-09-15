package com.example.friends.bookstore.fragment


import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.friends.bookstore.R
import com.example.friends.bookstore.adapter.DashboardRecyclerAdapter
import com.example.friends.bookstore.model.Book
import com.example.friends.bookstore.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var recyclerAdapter : DashboardRecyclerAdapter
    val bookInfoList = arrayListOf<Book>()

    var ratingComparator = Comparator<Book>{ book1, book2 ->

        if((book1.bookRating.compareTo(book2.bookRating,true))==0)
            book1.bookName.compareTo(book2.bookName,true)
        else{
            book1.bookRating.compareTo(book2.bookRating,true)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_dashboard, container, false)
        setHasOptionsMenu(true)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        layoutManager=LinearLayoutManager(activity)


        progressBar=view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE


        val queue=Volley.newRequestQueue(activity as Context)
        val url: String = "http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest = object: JsonObjectRequest(Request.Method.GET,url,null,Response.Listener {
                try{
                    progressLayout.visibility=View.GONE
                    val success=it.getBoolean("success")
                    if(success){
                        val data=it.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val bookJsonObject=data.getJSONObject(i)
                            val bookObject= Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("image")
                            )
                            bookInfoList.add(bookObject)
                            recyclerAdapter= DashboardRecyclerAdapter(activity as Context,bookInfoList)
                            recyclerDashboard.adapter=recyclerAdapter
                            recyclerDashboard.layoutManager=layoutManager

                        }

                    }
                    else{
                        Toast.makeText(activity as Context,"Error Occurred",Toast.LENGTH_SHORT).show()
                    }
                }
            catch (e:JSONException){
                Toast.makeText(activity as Context,"Some unexpected error occured",Toast.LENGTH_SHORT).show()
            }
            }, Response.ErrorListener {
                if(activity!=null)
                    Toast.makeText(activity as Context,"Volley Error ocuured",Toast.LENGTH_SHORT).show()

            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="4fad5fbc45d8bf"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)

        }
        else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings"){text,Listener->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,Listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }


        return view


    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_dashboard,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id =item?.itemId
        if(id==R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }


}
