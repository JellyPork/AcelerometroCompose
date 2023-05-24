package com.argent.acelerometrocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.argent.acelerometrocompose.ui.theme.AcelerometroComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AcelerometroComposeTheme(){
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationView()
                }
            }
        }

    }
}

@Composable
fun NavigationView() {
    val navController = rememberNavController()
    var sesion by remember{ mutableStateOf("") }
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("modo") {
            ModoScreen(
                onHome = { navController.popBackStack() },
                onSolo = { navController.navigate("solo") },
                onDuo = { navController.navigate("duo") }
            )
        }
        composable("solo"){
            SoloScreen(
                onBack = { navController.popBackStack() },
                onSoloSessions = { navController.navigate("soloSessions") }
            )
        }
        composable("soloSessions"){
            SoloSessionScreen(
                onBack = { navController.popBackStack() }
            )
        }

    }
}

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize()
        .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.Titulo_App),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
            )
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(R.drawable.logo_inicio),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(50.dp))
        Button(onClick = {
                navController.navigate("modo")
//            val intent = Intent(context, ModoActivity::class.java)
//            context.startActivity(intent)
        }) {
            Text(text = "Iniciar")
        }
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Ver archivos")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AcelerometroComposeTheme() {
        //HomeScreen()
    }
}