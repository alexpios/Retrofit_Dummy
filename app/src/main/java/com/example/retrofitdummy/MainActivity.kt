package com.example.retrofitdummy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitdummy.adapter.ProductAdapter
import com.example.retrofitdummy.databinding.ActivityMainBinding
import com.example.retrofitdummy.retrof.AuthRequest
import com.example.retrofitdummy.retrof.MainApi
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        adapter = ProductAdapter()
        setContentView(view)
        binding.recView.layoutManager = LinearLayoutManager(this)
        binding.recView.adapter = adapter
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY


        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val mainApi = retrofit.create(MainApi::class.java)


        CoroutineScope(Dispatchers.IO).launch {
            val list = mainApi.getAllProducts()
            runOnUiThread {
                adapter.submitList(list.products)
            }
        }





        binding.button.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {

                val product = mainApi.getProductById(3)
                runOnUiThread {
                    binding.textView.text = product.title

                }

            }
        }

        binding.signButton.setOnClickListener {
        CoroutineScope(Dispatchers.IO).launch {

            val user = mainApi.auth(AuthRequest(binding.userName.text.toString(),
                binding.editTextTextPassword.text.toString()))


            runOnUiThread{
                binding.apply {

                    Picasso.get()
                        .load(user.image)
//                        .resize(50, 50)
//                        .centerCrop()
                        .into(imageView)
                     firstName.text = user.firstName
                    lastName.text = user.lastName

                }

            }
        }



        }





    }
}