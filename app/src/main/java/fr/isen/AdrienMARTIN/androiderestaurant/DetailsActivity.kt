package fr.isen.AdrienMARTIN.androiderestaurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import fr.isen.AdrienMARTIN.androiderestaurant.model.Ingredients
import fr.isen.AdrienMARTIN.androiderestaurant.model.Items
import fr.isen.AdrienMARTIN.androiderestaurant.model.PanierItem
import fr.isen.AdrienMARTIN.androiderestaurant.ui.theme.AndroidERestaurantTheme
import java.io.File

class DetailsActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val item: Items = intent.getSerializableExtra("DISH") as Items

        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    var quantity = remember { mutableIntStateOf(1) }
                    val price = item.prices.first().price?.toFloat()
                    val total = price?.times(quantity.intValue)

//
                    scaffold(item, quantity, total, context = this@DetailsActivity,  startActivity = { item -> startActivity(item) })


                }
            }
        }
    }

}
//fun startActivity2(flag : Int){
//
//    // version ultra scotch mais j'ai d'autre projet plus urgent mdrrrr et ca fonctionne just fine
//    val activity = when (flag) {
//        1 -> DetailsActivity::class.java
//        2 -> HomeActivity::class.java
//        3 -> PanierActivity::class.java
//        else -> DetailsActivity::class.java
//    }
//    val intent = Intent(this, activity)
//    startActivity(intent)
//}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun scaffold(
    dish: Items,
    quantity: MutableState<Int>,
    total: Float?,
    context: Context,
    startActivity: (Intent) -> Unit,

) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),

                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Raccoon café",
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Italic,
                        )
                        Box {


                            Image(
                                painter = painterResource(id = R.drawable.cart),
                                contentDescription = "raccoon",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(50.dp)
                                    .clickable(
                                        onClick = {
                                            startActivity(Intent(context, PanierActivity::class.java))
                                        }
                                    )

                            )
                            val cartCount = PreferenceManager.getCartCount(context)
                            PreferenceManager.setCartCount(context, 0)
                            if (cartCount > 0) {
                                Box(modifier =Modifier
                                    .background(color = Color(0xFFff4700), shape = CircleShape)
                                    ,
                                    contentAlignment = Alignment.TopStart,


                                )
                                {
                                Text(
                                    text = cartCount.toString(),
                                    fontSize = 20.sp,
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier
                                        .padding(8.dp),
                                    color = Color.White
                                )
                            }}
                        }

                    }

                }
            )
        },
    ) {

            innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            carroussel(dish)
            listIngredients(ingredients = dish.ingredients)

            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                selector(
                    quantity.value,
                    onQuantityChange = { newQuantity ->
                        if (newQuantity < 0) {
                            quantity.value = 0
                        } else {
                            quantity.value = newQuantity
                        }
                        Log.d("selector", "quant:$quantity ")
                    }
                )

                priceBouton(total, dish, quantity.value, context, startActivity = { dish -> startActivity(dish) })
            }


        }

        Text(
            text = dish.nameFr ?: "error",
            fontSize = 20.sp,
            color = Color(0xFFFFA500),

            modifier = Modifier
                .padding(16.dp)

        )


    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun carroussel(dish: Items) {

    Box(modifier = Modifier.fillMaxWidth()) {
        val pagerState = rememberPagerState(pageCount = { dish.images.size })


        val painter = rememberImagePainter(
            data = dish.images.firstOrNull(),
//
            builder = {
                crossfade(true)
                fallback(R.drawable.foodplaceholder)


                val currentPage = pagerState.currentPage

                for (index in currentPage until dish.images.size) {
                    error(R.drawable.foodplaceholder)
                    val image = dish.images[index]
                    if (image.isNotEmpty()) {
                        data(image)
                        break
                    }
                }


            }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
//                    .weight(1f)
        ) { page ->
            Image(
                painter = painter, // Remplacer par la ressource de votre image
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun listIngredients(ingredients: List<Ingredients>) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        Arrangement.Start,
    ) {
        ingredients.forEach { ingName ->
            Text(
                text = ingName.nameFr ?: "error",
                modifier = Modifier
                    .padding(8.dp)
                    .background(color = Color(0xFFD3D3D3))

            )
        }
    }
}

@Composable
fun selector(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = { onQuantityChange(quantity - 1) }) {
            Text(text = "-")
        }

        Text(text = "  $quantity  ")

        Button(onClick = { onQuantityChange(quantity + 1) }) {

            Text(text = "+")
        }
    }
}


@Composable
fun priceBouton(total: Float?, item: Items, quantity: Int, context: Context, startActivity: (Intent) -> Unit){
    val snackbarVisibleState: MutableState<Boolean> = remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .background(color = Color(0xFFFFA500)),
        contentAlignment = Alignment.Center,


        ) {
        Text(
            text = "$total" + " " + "€",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(16.dp)
                .clickable(
                    onClick = {
                        Log.d("FILE", "clicked")
                        snackbarVisibleState.value = true

                        val newCartCount = PreferenceManager.getCartCount(context) + quantity
                        PreferenceManager.setCartCount(context, newCartCount)
                        addPanier(item, quantity.toFloat(), total, context)


//                        val panier = loadPanier(context)
//                        Log.d(
//                            "FILE",
//                            "panier updated: ${panier.size} ${panier[0].items.nameFr} ${panier[0].count} ${panier[0].totalPrice}"
//                        )


                    }
                )
        )

    }



    if (snackbarVisibleState.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .background(color = Color(0xFF0000)),

                action = {
                    Column {
                        Text(
                            text = "Panier",
                            modifier = Modifier.clickable {
                                snackbarVisibleState.value = false
                                startActivity(Intent(context, PanierActivity::class.java))
                            }
                        )

                        Text(
                            text = "continu shopping",
                            modifier = Modifier.clickable {
                                snackbarVisibleState.value = false
                                startActivity(Intent(context, HomeActivity::class.java))
                            }
                        )

                    }

//                    TextButton(onClick = { snackbarVisibleState.value = false }) {
//                        Text("Panier")
////                        startActivity(Intent(context, PanierActivity::class.java))
//
//                    }
                },
                content = {
                    Log.d("snack", "priceBouton: coucou ")
                    Text("added to the cart for ")
                },
            )

        }


    }
}
object PreferenceManager {
    private const val PREF_NAME = "resto_pref"
    private const val KEY_CART_COUNT = "CART_COUNT"

    fun getCartCount(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_CART_COUNT, 0) // 0 est la valeur par défaut si la clé n'existe pas
    }

    fun setCartCount(context: Context, count: Int) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(KEY_CART_COUNT, count).apply()
    }
}

fun addPanier(item: Items, count: Float?, total: Float?, context: Context) {

    val panier = loadPanier(context)

    val itemToAdd = PanierItem(item, count?.toInt() ?: 0, total ?: 0f)
    val existingItem = panier.find { it.items.id == item.id }
    if (existingItem != null) {
        existingItem.count += count?.toInt() ?: 0
        existingItem.totalPrice += total ?: 0f
    } else {
        panier.add(itemToAdd)
    }


    Log.d("FILE", "addPanier new panier:$panier ")

    savePanier(panier, context)


}

fun savePanier(panier: ArrayList<PanierItem>, context: Context) {
    Log.d("FILE", "savePanier panier: $panier")

    val gson = Gson()
    val json = gson.toJson(panier)
    Log.d("FILE", "savePanier json: $json")
    val filePath = "data.json"

    val file = File(context.filesDir, filePath)
    file.writeText(json)

}

fun loadPanier(context: Context): ArrayList<PanierItem> {

    val filePath = "data.json"
    val gson = Gson()
    val file = File(context.filesDir, filePath)
    if (!file.exists() || file.length() == 0.toLong()) {
        println("Le fichier $filePath n'existe pas.")
        return ArrayList<PanierItem>()
    }
    val json = file.readText()
    Log.d("FILE", "loadPanier json: $json")

    val panier = gson.fromJson(json, Array<PanierItem>::class.java)
    Log.d("FILE", "loadPanier panier: $panier")


    return panier.toCollection(ArrayList())


}


