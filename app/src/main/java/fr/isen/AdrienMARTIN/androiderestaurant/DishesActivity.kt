package fr.isen.AdrienMARTIN.androiderestaurant

import android.content.Intent
import android.os.Bundle
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
import fr.isen.AdrienMARTIN.androiderestaurant.ui.theme.AndroidERestaurantTheme

class DishesActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            AndroidERestaurantTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    val nom = intent.getStringExtra("NOM")
//                    val nomV2= nom ?: "Entrees"
//                    EntreesScreen(nomV2)
//                }
//            }
            val nom = intent.getStringExtra("NOM") ?: "error"
            val dishList = mutableListOf<Dish>()
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),

                        title = {
                            Text(

                                text = "$nom",
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


}
data class Dish(val name: String)

@Composable
fun listDish(dish: Dish, startActivity: (String) -> Unit){
    Column (

        modifier = Modifier.padding(16.dp).fillMaxWidth(),
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
