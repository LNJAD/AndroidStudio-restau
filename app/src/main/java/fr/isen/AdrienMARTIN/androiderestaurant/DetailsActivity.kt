package fr.isen.AdrienMARTIN.androiderestaurant

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import coil.compose.rememberImagePainter
import fr.isen.AdrienMARTIN.androiderestaurant.model.Ingredients
import fr.isen.AdrienMARTIN.androiderestaurant.model.Item
import fr.isen.AdrienMARTIN.androiderestaurant.model.Items
import fr.isen.AdrienMARTIN.androiderestaurant.ui.theme.AndroidERestaurantTheme

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
                    scaffold(item, quantity, total)

                }
            }
        }
    }
}


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun scaffold(
    dish: Items,
    quantity: MutableState<Int>,
//    onQuantityChange: () -> Unit,
    total: Float?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),

                title = {
                    Text(
                        text = dish.nameFr ?: "error",
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,

                        modifier = Modifier
                    )

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
            priceBouton(total)

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
        modifier = Modifier.fillMaxSize(0.8f),
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
fun priceBouton(total: Float?) {

    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .background(color = Color(0xFFFFA500)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$total" + " " + "€",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(16.dp)

        )

    }

}

