package com.example.andro.Network

import com.example.andro.Model.Food
import com.example.andro.Model.Loja
import retrofit2.Call
import retrofit2.http.*

interface AndroApi{

    @GET("tarefa")
    fun getFood():Call<ArrayList<Food>>

    @GET("loja")
    fun getLoja():Call<ArrayList<Loja>>

    @POST("lojaSearch")
    fun getLojaSearch(@Body loja: Loja):Call<ArrayList<Loja>>

    @GET("tarefa/{id}")
    fun getOneFood(@Path(value="id")id:String):Call<Food>

}