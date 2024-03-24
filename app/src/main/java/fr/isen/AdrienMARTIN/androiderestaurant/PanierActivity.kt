package fr.isen.AdrienMARTIN.androiderestaurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberImagePainter
import fr.isen.AdrienMARTIN.androiderestaurant.model.Items
import fr.isen.AdrienMARTIN.androiderestaurant.model.PanierItem
import fr.isen.AdrienMARTIN.androiderestaurant.ui.theme.AndroidERestaurantTheme

class PanierActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val panier = loadPanier(this)
            Log.d("Panier", "onCreate: $panier ")

            scaffoldPanier(panier = panier,this)

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun scaffoldPanier(panier: List<PanierItem>, context: Context) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),

                title = {
                    Text(
                        text = "Panier",
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,

                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    val intent = Intent(context, HomeActivity::class.java)
                                    startActivity(context, intent, null)
                                }
                            )
                    )

                }
            )
        },
    ) {

            innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
//                    val nom = intent.getStringExtra("NOM")
//                    val nomV2= nom ?: "Entrees"

            LazyColumn {
//                items(count = dishes.size) {
                items(panier) { item ->
                    listDish(item, context )


//                    listDish(Dish(dish.nameFr ?: "error"), startActivity)
                }
            }

            Button(onClick = {
                val intent = Intent(context, OrderActivity::class.java)
                startActivity(context, intent, null)
            }) {
                Text(text = "Total : ${panier.sumByDouble { it.totalPrice.toDouble()}} €")
                Text(text = "Commander")

            }


        }


    }
}

@Composable
fun listDish(item: PanierItem,context: Context) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = Color(0xFFD8D8D8))

        ) {
            val painter = rememberImagePainter(
                data = item.items.images.firstOrNull(),
                builder = {
                    crossfade(true)
                    fallback(R.drawable.foodplaceholder)
                    item.items.images.drop(1).forEach {
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

            Text(text = item.items.nameFr ?: "error")
            Text(text = "Quantité: ${item.count}")
            Button(onClick = { deletePanierItem(item,context) }) {
                Text(text = "Supprimer")
            }

        }
    }
}

fun deletePanierItem( item: PanierItem,context: Context) {
    val panier = loadPanier(context)
    val newPanier = panier.filter { it != item }
    val intent = Intent(context, PanierActivity::class.java)
    // on oublie pas de update le reference

    startActivity(context, intent, null)

    savePanier(newPanier as ArrayList<PanierItem>,context)
}
