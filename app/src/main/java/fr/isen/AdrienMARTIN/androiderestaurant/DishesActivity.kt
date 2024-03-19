package fr.isen.AdrienMARTIN.androiderestaurant

import android.app.VoiceInteractor
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.privacysandbox.tools.core.model.Method
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

class DishesActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val nomTypePlat = intent.getStringExtra("NOM") ?: "error";
            val itemJsonList = ArrayList<Items>()

            val apiUrl = "http://test.api.catering.bluecodegames.com/menu"
            val jsonRequest = JSONObject()
            jsonRequest.put("id_shop", "1")


            val queue: RequestQueue = Volley.newRequestQueue(applicationContext)




            // Fetch dishes from server
            val url = "http://test.api.catering.bluecodegames.com/menu"
            val requestBody = JSONObject().apply {
                put("id_shop", "1")
            }.toString()

            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener<String> { response ->
                    handleResponse(response)
                },
                Response.ErrorListener { error ->
                    Log.e("DISHES", "Error: ${error.toString()}")
                }
            ) {
                override fun getBody(): ByteArray = requestBody.toByteArray()
                override fun getBodyContentType(): String = "application/json; charset=utf-8"
            }
            queue.add(stringRequest)





//            val jsonObjectRequest = JsonObjectRequest(
//                Request.Method.POST, apiUrl, jsonRequest,
//                { response ->
//                    // Traitement de la réponse JSON
//                    // Ici, vous pouvez extraire les données et les utiliser comme nécessaire
//                      val data = response.getJSONArray("data")
//                    Log.d("jsonRequest", "DATAAA $data")
//
//                    // on recupere le nomfr de chaque data et on le met dans un array
//                    for (i in 0 until data.length()) {
//                        val nomFr = data.getJSONObject(i).getString("name_fr")
//                        if (nomFr == nomTypePlat) {
//                            Log.d("jsonRequest", " $nomFr == $nomTypePlat")
//                            val items = data.getJSONObject(i).getJSONArray("items")
//
//
//
//
//                        }else {
////                            Log.d("jsonRequest", " ERROR TF ?!$nomFr != $nomTypePlat")
//                        }
//
//                        Log.d("jsonRequest", "nomFr $nomFr")
//                    }
////                    val nomFr = data.getJSONObject(i).getString("name_fr")
//
//                },
//                { error ->
//                    Log.d("jsonRequest", "error json response")
//                })
//            queue.add(jsonObjectRequest)
//            Log.d("DATA JSON", "data //: $queue ")





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

                            val dishesArray = resources.getStringArray(R.array.dishes)
                            val dishesList = dishesArray.map { Dish(it) }

                            dishesList.forEach {
                                listDish(it){
                                    startActivity(it)
                                }
//                        Text(
//                            text = it.name,
//                            modifier = Modifier.padding(16.dp).clickable {
//                                startActivity(it.name)
//                            }
//
//                            )


                            }
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

    fun handleResponse (response: String ){
        val menuResponse = Gson().fromJson(response, DisheClass::class.java)
        val category = menuResponse.data.find { it.nameFr== "Entrées" }
        Log.d("GSON", "handleResponse: $category ")
//        val category = menuResponse.data.find { it.name_fr == buttonText }
    }

}
data class Dish(val name: String)

@Composable
fun listDish(dish: Dish, startActivity: (String) -> Unit){
    Column (

        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(
            text = dish.name,
            fontSize = 20.sp,
            color = Color(0xFFFFA500),

            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    startActivity(dish.name)
                }
        )
    }

}
