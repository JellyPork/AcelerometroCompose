package com.argent.acelerometrocompose

data class Prueba (
    val name: String? = null,
    val nItems: Int? = null,
    val items: List<Items>? = null
//
)

data class Items(
    val image: String? = null,
    val itemName: String? = null,
    val no: Int? = null,
    val score: Int? = null,
    val timeLimit: Int? = null
)