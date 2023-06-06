package com.argent.acelerometrocompose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ControlScreen(onBack: ()-> Unit){
    val context = LocalContext.current
    val topico= "${vals.brokerTopic.value}/channel${vals.canalBroker.value}"
    BackHandler() {
        if(vals.brokerConected.value) {
            disconnectBroker(context)
        }
        onBack()
    }
    var datos by remember {
        mutableStateOf("")
    }
    var acc by remember {
        mutableStateOf("")
    }
    var gyr by remember {
        mutableStateOf("")
    }
    var ang by remember {
        mutableStateOf("")
    }
    var mgt by remember {
        mutableStateOf("")
    }
    var init by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize()
        .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "MODO CONTROL", fontSize = 30.sp,fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Usuario: ${vals.usuario.value}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth()) {
            Image(
                bitmap = vals.currentBitmap,
                contentDescription = null, modifier = Modifier.size(100.dp,100.dp),
                contentScale = ContentScale.FillBounds
            )
            Column {
                Text(text = "Prueba: ${vals.sesion.value}",fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(text = "Ejercicio: ${vals.item.value}",fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        if(vals.brokerConected.value && !init) {
            publishBroker(
                topico,
                "BEGIN,${vals.sesion.value},${vals.item.value},${vals.usuario.value},${vals.showScore.value}",
                0,
                false
            )
            suscribeBroker(context,topico,0, onRecibir = {datos=it})
            vals.brokerSuscriber.value=false
            suscribeBroker(context,"$topico/ANG",0, onRecibir = {ang=it})
            vals.brokerSuscriber.value=false
            suscribeBroker(context,"$topico/ACC",0, onRecibir = {acc=it})
            vals.brokerSuscriber.value=false
            suscribeBroker(context,"$topico/GYR",0, onRecibir = {gyr=it})
            vals.brokerSuscriber.value=false
            suscribeBroker(context,"$topico/MAG",0, onRecibir = {mgt=it})
            vals.brokerSuscriber.value=false
            init=true
        }

        Button(onClick = {
            publishBroker(topico,"START",0,false)

        }) {
            Text(text = "START")
        }
        Button(onClick = {
            publishBroker(topico,"STOP",0,false)

        }) {
            Text(text = "STOP")
        }
        Text(text = datos)
        Text(text = "YPR: $ang")
        Text(text = "ACC: $acc")
        Text(text = "GYR: $gyr")
        Text(text = "MAG: $mgt")
    }
}