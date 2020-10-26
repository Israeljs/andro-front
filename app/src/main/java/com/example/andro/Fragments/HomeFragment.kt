package com.example.andro.Fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import com.example.andro.Adapter.FoodArrayAdapter
import com.example.andro.Model.Food
import com.example.andro.Network.RetrofitBuilder
import com.example.andro.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Response
import retrofit2.Callback
import retrofit2.Call
import com.example.andro.Fragments.*
import androidx.appcompat.app.AppCompatActivity
import com.example.andro.loadFragment

class HomeFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var foodListResponse: ArrayList<Food>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchData()
    }



    private fun fetchData(){

        RetrofitBuilder.androApi.getFood().enqueue(object : Callback<ArrayList<Food>>{
            override fun onResponse(
                call: Call<ArrayList<Food>>,
                response: Response<ArrayList<Food>>
            ) {

                if(response.isSuccessful){

                    if(response.body()?.size!! > 0){
                        foodListResponse = response.body()!!

                        val arrayAdapter = context?.let { FoodArrayAdapter(it, foodListResponse) }


                        var mListView = viewOfLayout.findViewById<ListView>(R.id.userlist)

                        mListView.adapter = arrayAdapter

                        onItemsLoadComplete()
                    }else{
                        withoutData()
                    }


                }


            }
            override fun onFailure(call: Call<ArrayList<Food>>, t: Throwable) {

                /*val builder = context?.let {
                    AlertDialog.Builder(it)
                }

                builder?.setTitle("Erro")
                builder?.setMessage("Falha na Conexão com o Servidor")


                val dialog: AlertDialog = builder!!.create()
                dialog.show()
                */
                onItemsLoadComplete()

                val context = activity as AppCompatActivity
                val args = Bundle()
                val fragment = HelperFragment()
                args.putString("type", "error")
                fragment.arguments = args
                context.loadFragment(fragment)

                Log.d("main", "onFailure: ${t.message}")

            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater!!.inflate(R.layout.fragment_home, container, false)
        viewOfLayout.swiperefresh.isRefreshing = true
        val context = activity as AppCompatActivity

        viewOfLayout.userlist.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val args = Bundle()
            val fragment = buyFragment()
            args.putString("color", foodListResponse?.get(i)._id)
            fragment.arguments = args
            context.loadFragment(fragment)
        }



        viewOfLayout.swiperefresh.setOnRefreshListener {
            fetchData();
        }


        return  viewOfLayout
    }

    fun onItemsLoadComplete() {
        viewOfLayout.swiperefresh.isRefreshing = false
    }

    fun withoutData(){
        onItemsLoadComplete()
        val context = activity as AppCompatActivity
        val args = Bundle()
        val fragment = HelperFragment()
        args.putString("type", "notFound")
        fragment.arguments = args
        context.loadFragment(fragment)
    }



}
