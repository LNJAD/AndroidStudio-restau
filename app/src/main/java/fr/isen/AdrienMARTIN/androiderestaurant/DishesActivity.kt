package fr.isen.AdrienMARTIN.androiderestaurant

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.Nullable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.Coil
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import fr.isen.AdrienMARTIN.androiderestaurant.model.DisheClass
import fr.isen.AdrienMARTIN.androiderestaurant.model.Items
import fr.isen.AdrienMARTIN.androiderestaurant.ui.theme.AndroidERestaurantTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.sql.DataSource


class DishesActivity : ComponentActivity() {
//val dataList = ArrayList<Items>()
//var mutalbleDataList by remember { mutableStateOf(mutableStateListOf<Items>()) }
//    val mutalbleDataList = mutableStateListOf<Items>()
var mutableDataList by mutableStateOf(emptyList<Items>())

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
//
        super.onCreate(savedInstanceState)
        setContent {

            val nomTypePlat = intent.getStringExtra("NOM") ?: "error";
            // nouvelle list de plats



            val apiUrl = "http://test.api.catering.bluecodegames.com/menu"



            val queue: RequestQueue = Volley.newRequestQueue(applicationContext)

            val itemList = ArrayList<Items>()


            // Fetch dishes from server
            val url = "http://test.api.catering.bluecodegames.com/menu"
            val requestBody = JSONObject().apply {
                put("id_shop", "1")
            }.toString()

            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener<String>
                { response ->
                    try {
//                        handleResponse(response,nomTypePlat)
                        //TODO mettre cette merde en fonction
                        val menuResponse = Gson().fromJson(response, DisheClass::class.java)
                        val categoryChoisi = menuResponse.data.find { it.nameFr== nomTypePlat}

                        val items = categoryChoisi?.items


                        val itemsList = items?.map { Items(it.id, it.nameFr, it.idCategory, it.categNameFr, it.images, it.ingredients, it.prices) }
                        this@DishesActivity.mutableDataList = itemsList ?: emptyList()
//
                    Log.d("GSON", "test outside: $mutableDataList")

                    }catch (e: Exception){
                        Log.e("DISHES", "Error: ${e.toString()}")

                    }
//

                },
                Response.ErrorListener { error ->
                    Log.e("DISHES", "Error: ${error.toString()}")
                }
            ) {
                override fun getBody(): ByteArray = requestBody.toByteArray()
                override fun getBodyContentType(): String = "application/json; charset=utf-8"
            }

            queue.add(stringRequest)


               AndroidERestaurantTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        //CategoryScreen(dataCategory, navigateToDetails = { dish ->
                        //                                navigateToDetailsScreen(dish)
                        //                            })
                        scaffold(this@DishesActivity.mutableDataList,nomTypePlat, startActivity = {dish -> startActivity(dish)})


                    }
                }







        }
    }


    fun startActivity(dish :Items){

        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra("DISH", dish)
        }
        startActivity(intent)
    }


data class Dish(val name: String)




@OptIn(ExperimentalCoilApi::class)
@Composable
fun listDish(dish: Items, startActivity: (Items) -> Unit){
    Column (

        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
    ){


//        AsyncImage(
//
//            model = dish.images[0],
//            contentDescription = null,
//        )
        Box(
            modifier = Modifier
                .clickable {
                    startActivity(dish)
                }

        ){
            Column {


                val painter = rememberImagePainter(
                    data = dish.images.firstOrNull(),
                    builder = {
                        crossfade(true)
                        fallback(R.drawable.foodplaceholder)
                        dish.images.drop(1).forEach {
                            if (it.isNotEmpty()) {
                                data(it)
                                return@forEach
                            }
                        }
                        error(R.drawable.foodplaceholder)
                    }
                )
                Image(
                    painter = painter,
                    contentDescription = "Dish Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = dish.nameFr ?: "error",
                    fontSize = 20.sp,
                    color = Color(0xFFFFA500),

                    modifier = Modifier
                        .padding(16.dp)

                )
                Text(
                    text = dish.prices.first().price + " " + "€",
                    fontSize = 20.sp,
                    color = Color(0xFFFFA500),
                    modifier = Modifier
                        .padding(16.dp)
//                .clickable {
                )
            }
        }


        Spacer(modifier = Modifier.size(10.dp))
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun scaffold (dishes: List<Items>,nomTypePlat:String, startActivity: (Items) -> Unit){
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),

                title = {
                    Text(
                        text = "$nomTypePlat",
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,

                        modifier = Modifier
                    )

                }
            )
        },
    ) {

            innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
        ) {
//                    val nom = intent.getStringExtra("NOM")
//                    val nomV2= nom ?: "Entrees"

            LazyColumn {
//                items(count = dishes.size) {
                items(dishes) { dish ->
                    listDish(dish, startActivity= {dish -> startActivity(dish)})


//                    listDish(Dish(dish.nameFr ?: "error"), startActivity)
                }
            }



        }


    }
}
}