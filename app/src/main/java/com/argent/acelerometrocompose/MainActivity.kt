package com.argent.acelerometrocompose

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import org.json.JSONObject

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
    var ini  by remember {
        mutableStateOf(false)
    }
    val firebase = remember {
        FireBase()
    }
    if(!ini){
        ini = true;
        //firebase.enablePersistence()
        vals.json.clear()
        firebase.enablePersistence()
        vals.json = firebase.getCollenction()
    }


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
                onBack = { navController.popBackStack() },
                onSensores = { navController.navigate("acel") },
                onControles = { navController.navigate("control") }
            )
        }
        composable("mqtt"){
            ConfigurarMqttScreen (
                onBack = { navController.popBackStack() }
            )
        }
        composable("acel"){
            AcelScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable("files"){
            FilesScreen (
                onBack = { navController.popBackStack() }
            )
        }
        composable("duo") {
            DuoScreen(
                onBack = { navController.popBackStack() },
                onControlMode = { navController.navigate("solo") },
                onSensorMode = { navController.navigate("sensorWait") }
            )
        }
        composable("sensorWait") {
            SensorWaitScreen(
                onBack = { navController.popBackStack() },
                onBegin = { navController.navigate("acel") }
            )
        }
        composable("control") {
            ControlScreen(
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
            painter = painterResource(R.drawable.gyroscope),
            contentDescription = null,
            modifier=Modifier.height(200.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
        Button(onClick = {
            navController.navigate("modo")
        }) {
            Icon(painterResource(id = R.drawable.playcircle),contentDescription = null)
            Text(text = "Iniciar",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = { navController.navigate("files")}) {
            Icon(painterResource(id = R.drawable.folder),contentDescription = null)
            Text(text = "Archivos",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = { navController.navigate("mqtt")}) {
            Icon(painterResource(id = R.drawable.config),contentDescription = null)
            Text(text = "Ajustes",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold)
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