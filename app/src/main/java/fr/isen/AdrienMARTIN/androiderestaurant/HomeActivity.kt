package fr.isen.AdrienMARTIN.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import fr.isen.AdrienMARTIN.androiderestaurant.ui.theme.AndroidERestaurantTheme


// code logique: celui qui est du "vrai code"

class HomeActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
//            AndroidERestaurantTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
////                            .background(color = Color(0xFFFFA500))
//                            .padding(16.dp),
////                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//
//                    )
//                    {
//                        TopBar()
//                        bienvenu()
//                        menuBouton (
//                            showToast = {text -> showToast(text)},
//                            startActivity = {nom -> startActivity(nom)}
//                        )
//
//
//
//
//                    }
//
//
//                }
//            }


            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            Text(
                                text = "Raccoon café",
                                fontSize = 20.sp,
                                fontStyle = FontStyle.Italic,
                                modifier = Modifier
                            )

                        }
                    )
                },
            ) {

                ScrollContent(
                    showToast = { text -> showToast(text) },
                    startActivity = { nom -> startActivity(nom) },
                    innerPadding = it

                )


            }


        }
    }

    fun showToast(text: String) {
        Toast.makeText(
            this,
            text,
            Toast.LENGTH_SHORT
        ).show()

    }

    fun startActivity(nom: String) {

        val intent = Intent(this, DishesActivity::class.java).apply {
            putExtra("NOM", nom)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "L'activité a été détruite")
    }
}

@Composable
fun ScrollContent(
    innerPadding: PaddingValues,
    showToast: (String) -> Unit,
    startActivity: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
//                            .background(color = Color(0xFFFFA500))
            .padding(16.dp),
//                        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    )
    {
        TopBar()
        bienvenu()
        menuBouton(
            showToast = { text -> showToast(text) },
            startActivity = { nom -> startActivity(nom) }
        )

    }
}


@Composable
fun TopBar() {

    Spacer(modifier = Modifier.height(10.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFFFA500))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "DroidRestaurant",
            fontSize = 20.sp,
            modifier = Modifier

        )
    }
}

@Composable
fun bienvenu() {
    // un text centrer sur 3 ligne avec une image a droite
    Row(
        modifier = Modifier.fillMaxWidth(),
    )
    {
        Column(
            modifier = Modifier

                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .padding(end = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End

        ) {
            Text(
                text = "Bienvenue",
                fontSize = 20.sp,
                modifier = Modifier,
//            fontColor = Color(0xFFFFA500),
                color = Color(0xFFFFA500),
            );
            Text(
                text = "Chez",
                fontSize = 20.sp,
                color = Color(0xFFFFA500)

            );
            Text(
                text = "Raccoon Café",
                fontStyle = FontStyle.Italic,
                color = Color(0xFFA52A2A),
            );
        }
        Image(
            painter = painterResource(id = R.drawable.raccoon), // Replace R.drawable.your_image with the actual image resource
            contentDescription = "Image",
            modifier = Modifier.size(100.dp)
        )
    }

}


@Composable
fun menuBouton(showToast: (String) -> Unit, startActivity: (String) -> Unit) {
    // 3 bouton pour les 3 categories


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//
        Text(

            text = "entrées",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    showToast("entrées")
                    // on lance l'activité des entrées
                    startActivity("Entrées")


                }

        )
        Trait()
        Text(
            text = "Plats",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    showToast("Plats")
                    startActivity("Plats")
                }
        )
        Trait()
        Text(
            text = "Desserts",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    showToast("Desserts")
                    startActivity("Desserts")
                }
        )


    }
}


@Composable
fun Trait() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp), // Ajustez la hauteur du trait selon vos préférences
        color = Color.Black // Couleur du trait
    )
}













