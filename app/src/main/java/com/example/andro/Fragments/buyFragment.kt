package com.example.andro.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.andro.Model.Food
import com.example.andro.Network.RetrofitBuilder
import com.example.andro.R
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class buyFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var foodListResponse: Call<Food>
    private  var precoInitial: Int = 0
    private  var preco: Int = 0
    private  var quantidade: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater!!.inflate(R.layout.fragment_buy, container, false)

        val context = activity as AppCompatActivity

        val color = arguments?.getSerializable("color")
        fetchData(color.toString())

        val textTotal = viewOfLayout.findViewById<TextView>(R.id.total)

        val btMore = viewOfLayout.findViewById<Button>(R.id.btMore)
        val btLess = viewOfLayout.findViewById<Button>(R.id.btless)

        btMore.setOnClickListener {
            quantidade += 1
            calcaValMore()
        }

        btLess.setOnClickListener {
            quantidade -= 1
            calcaValLess()

        }

        return  viewOfLayout
    }

    @SuppressLint("SetTextI18n")
    private fun calcaValMore(){

        var novoPrecoTotal = precoInitial + preco
        preco = novoPrecoTotal

        var quantidadeText = viewOfLayout.findViewById<TextView>(R.id.quantidade)

        quantidadeText.text = "Quantidade: $quantidade"

        viewOfLayout.findViewById<TextView>(R.id.total).text = "R$:$preco"

    }

    @SuppressLint("SetTextI18n")
    private fun calcaValLess(){

        var novoPrecoTotal = 0
        if(preco == precoInitial){
            view?.let {
                Snackbar.make(it, "Preço minimo atingido", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            novoPrecoTotal = preco
        }else{
            novoPrecoTotal = preco - precoInitial

            var quantidadeText = viewOfLayout.findViewById<TextView>(R.id.quantidade)

            quantidadeText.text = "Quantidade: $quantidade"

            preco = novoPrecoTotal
        }

        viewOfLayout.findViewById<TextView>(R.id.total).text = "R$:$novoPrecoTotal"
    }


    private fun fetchData(id: String){
        RetrofitBuilder.androApi.getOneFood(id).enqueue(object : Callback<Food> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Food>, response: Response<Food>) {

                viewOfLayout.findViewById<ProgressBar>(R.id.progressBar).visibility=View.GONE
                viewOfLayout.findViewById<LinearLayout>(R.id.detailLayout).visibility=View.VISIBLE

                if(response.isSuccessful){

                    preco = response.body()!!.preco
                    precoInitial = response.body()!!.preco

                    viewOfLayout.findViewById<TextView>(R.id.total).text = "R$:$precoInitial"

                    viewOfLayout.findViewById<TextView>(R.id.foodname).text = response.body()!!.nome
                    viewOfLayout.findViewById<TextView>(R.id.priceFood).text = "R$" + response.body()!!.preco.toString()
                    viewOfLayout.findViewById<TextView>(R.id.lojaNome).text = response.body()!!.loja
                    viewOfLayout.findViewById<TextView>(R.id.descriptionFood).text = response.body()!!.description
                    val img = viewOfLayout.findViewById<ImageView>(R.id.contactImage)

                    Picasso.get().load( response.body()!!.imagem).into(img);

                    var quantidadeText = viewOfLayout.findViewById<TextView>(R.id.quantidade)

                    quantidadeText.text = "Quantidade: $quantidade"


                }
                
            }
            override fun onFailure(call: Call<Food>, t: Throwable) {

                val builder = context?.let {
                    AlertDialog.Builder(it)
                }

                builder?.setTitle("Erro")
                builder?.setMessage("Falha na Conexão com o Servidor")

                val dialog: AlertDialog = builder!!.create()
                dialog.show()

                Log.d("main", "onFailure: ${t.message}")

            }
        })

    }

    private fun RequestCreator.into(img: Unit) {
        return img
    }


}