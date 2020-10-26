package com.example.andro.Model

import  com.google.gson.annotations.SerializedName

data class Food(
    @SerializedName("_id")
    val _id:String,
    @SerializedName("nome")
    val nome:String,
    @SerializedName("loja")
    val loja:String,
    @SerializedName("preco")
    val preco:Int,
    @SerializedName("imagem")
    val imagem:String,
    @SerializedName("description")
    val description:String,
    @SerializedName("dataCriacao")
    val dataCriacao:String)