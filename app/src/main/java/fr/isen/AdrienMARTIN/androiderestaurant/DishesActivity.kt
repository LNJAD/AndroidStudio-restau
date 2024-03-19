package fr.isen.AdrienMARTIN.androiderestaurant

import android.app.VoiceInteractor
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.privacysandbox.tools.core.model.Method
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.isen.AdrienMARTIN.androiderestaurant.model.Data
import fr.isen.AdrienMARTIN.androiderestaurant.model.DisheClass
import fr.isen.AdrienMARTIN.androiderestaurant.model.Ingredients
import fr.isen.AdrienMARTIN.androiderestaurant.model.Items
import fr.isen.AdrienMARTIN.androiderestaurant.model.Prices
import fr.isen.AdrienMARTIN.androiderestaurant.ui.theme.AndroidERestaurantTheme
import org.json.JSONObject
import kotlin.math.log

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
                        val menuResponse = Gson().fromJson(response, DisheClass::class.java)
                        val categoryChoisi = menuResponse.data.find { it.nameFr== nomTypePlat}

                        val items = categoryChoisi?.items
                        val itemsList = items?.map { Items(it.id, it.nameFr, it.nameEn, it.idCategory, it.categNameFr, it.categNameEn, it.images, it.ingredients, it.prices) }
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
                        scaffold(this@DishesActivity.mutableDataList,nomTypePlat)


                    }
                }







        }
    }


    fun startActivity(dish :String){

        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra("DISH", dish)
        }
        startActivity(intent)
    }

//    fun handleResponse (response: String , nomTypePlat: String){
//        val menuResponse = Gson().fromJson(response, DisheClass::class.java)
//        val category = menuResponse.data.find { it.nameFr== nomTypePlat}
//
//            val nameList = category?.items?.map { it.nameFr }
//            val imageList = category?.items?.map { it.images }
//
//
//        val nameFromCategory = category?.items?.map { it.nameFr to it.images }
//
////        Log.d("GSON", "handleResponse: all of 1 cat $category ")
////        Log.d("GSON", "handleResponse name + images: $nameFromCategory ")
////        Log.d("GSON", "handleResponse: $imageList ")
//
//        // on met ca dans une dataclass  Items
//        val items = category?.items
//        val itemsList = items?.map { Items(it.id, it.nameFr, it.nameEn, it.idCategory, it.categNameFr, it.categNameEn, it.images, it.ingredients, it.prices) }
//
//       // complete la dataclass DisheClass
//
//        val disheClass = DisheClass(menuResponse.data.map { Data(it.nameFr,  itemsList as List<Items>) })
//
//
//
//        Log.d("GSON", "test_dataclass:  $disheClass")
//
//        dataList.add(Data(nomTypePlat, itemsList as List<Items>))
//
//
//
//    }

//    private fun fetchData( category : String, items:MutableList<Items>){
//        val jsonRequest = JSONObject()
//        jsonRequest.put("id_shop", "1")
//        val jsonObjectRequest = JsonObjectRequest(
//            Request.Method.POST, "http://test.api.catering.bluecodegames.com/menu", jsonRequest,
//            { response ->
//                    val result = Gson.fromJson(response.toString(), DisheClass::class.java)
//
//            },
//            { error ->
//                Log.d(TAG, "fetchData: $error")
//            }
//        )
//    }

}
data class Dish(val name: String)




@OptIn(ExperimentalCoilApi::class)
@Composable
fun listDish(dish: Items){
    Column (

        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
    ){

//        Image(
////            painter = dish.images[0].let { painterResource(id = it.toInt()) },
//            painter= rememberImagePainter(data = dish.images[0]),
//            contentDescription = "Image",
//
//            modifier = Modifier.size(100.dp)
//        )
        AsyncImage(

//            model = "https://i.pinimg.com/originals/1b/23/07/1b230783cb0d380a4586f386c4cd7e29.jpg",
            model = dish.images[0],
            contentDescription = null,
        )

        Text(
            text = dish.nameFr ?: "error",
            fontSize = 20.sp,
            color = Color(0xFFFFA500),

            modifier = Modifier
                .padding(16.dp)
                .clickable {
//                    startActivity(dish.name)
                }
        )

        Spacer(modifier = Modifier.size(10.dp))
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun scaffold (dishes: List<Items>,nomTypePlat:String){
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
                    listDish(dish)


//                    listDish(Dish(dish.nameFr ?: "error"), startActivity)
                }
            }



        }


    }
}