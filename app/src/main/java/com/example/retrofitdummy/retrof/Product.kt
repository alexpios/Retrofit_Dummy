package com.example.retrofitdummy.retrof

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Float,
    val rating: Float,
    val stock: Int,
    val tags: List<String>,
    val images: List<String>

)
