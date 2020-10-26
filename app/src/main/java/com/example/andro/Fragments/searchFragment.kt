package com.example.andro.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.andro.Adapter.LojaArrayAdapter
import com.example.andro.Model.Food
import com.example.andro.Model.Loja
import com.example.andro.Network.RetrofitBuilder
import com.example.andro.R
import com.example.andro.loadFragment
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class searchFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var foodListResponse: ArrayList<Loja>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private fun fetchData(nomeLojaSearch: String){

        viewOfLayout.findViewById<TextView>(R.id.notfound).visibility = View.INVISIBLE
        viewOfLayout.findViewById<ProgressBar>(R.id.loadSearch).visibility = View.VISIBLE

        val lojarequest = Loja("", nomeLojaSearch, "", "", "")

        RetrofitBuilder.androApi.getLojaSearch(lojarequest).enqueue(object : Callback<ArrayList<Loja>> {
            override fun onResponse(
                call: Call<ArrayList<Loja>>,
                response: Response<ArrayList<Loja>>
            ) {
                println(response.body())

                if(response.isSuccessful){

                    viewOfLayout.findViewById<ProgressBar>(R.id.loadSearch).visibility = View.INVISIBLE
                    viewOfLayout.findViewById<LinearLayout>(R.id.resultLayout).visibility = View.VISIBLE
                    viewOfLayout.findViewById<TextView>(R.id.notfound).visibility = View.INVISIBLE

                    if(response.body()?.size!! > 0){
                        foodListResponse = response.body()!!

                        val arrayAdapter = context?.let { LojaArrayAdapter(it, foodListResponse) }



                        var mListView = viewOfLayout.findViewById<ListView>(R.id.userlist)

                        arrayAdapter?.notifyDataSetChanged()

                        mListView.adapter = arrayAdapter

                       /* onItemsLoadComplete()*/
                    }else{
                        viewOfLayout.findViewById<ProgressBar>(R.id.loadSearch).visibility = View.INVISIBLE
                        viewOfLayout.findViewById<LinearLayout>(R.id.resultLayout).visibility = View.INVISIBLE
                        viewOfLayout.findViewById<TextView>(R.id.notfound).visibility = View.VISIBLE
                        viewOfLayout.findViewById<TextView>(R.id.notfound).text = "Nenhum restaurante encontrado"
                    }

                }


            }
            override fun onFailure(call: Call<ArrayList<Loja>>, t: Throwable) {

                /*val builder = context?.let {
                    AlertDialog.Builder(it)
                }

                builder?.setTitle("Erro")
                builder?.setMessage("Falha na Conexão com o Servidor")


                val dialog: AlertDialog = builder!!.create()
                dialog.show()*/


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

        viewOfLayout = inflater!!.inflate(R.layout.fragment_search, container, false)
        val context = activity as AppCompatActivity

        val search = viewOfLayout.findViewById<SearchView>(R.id.searchView)

        viewOfLayout.findViewById<TextView>(R.id.notfound).visibility = View.VISIBLE
        viewOfLayout.findViewById<TextView>(R.id.notfound).text = "Pesquise por estabelecimentos"
//onActionViewExpanded()
        search.onActionViewExpanded()

            search.setOnQueryTextListener(object :  SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?):Boolean{

                    fetchData(newText.toString())

                    return false
                }
            })




        /*viewOfLayout.userlist.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val args = Bundle()
            val fragment = buyFragment()
            args.putString("color", foodListResponse?.get(i)._id)
            fragment.arguments = args
            context.loadFragment(fragment)
        }*/



        /*viewOfLayout.swiperefresh.setOnRefreshListener {
            fetchData();
        }*/


        return  viewOfLayout
    }




}