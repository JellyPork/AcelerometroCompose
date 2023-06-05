package com.argent.acelerometrocompose

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SensorWaitScreen(onBack: () -> Unit,onBegin: () -> Unit){
    val context = LocalContext.current
    BackHandler {
        disconnectBroker(context)
        vals.sesion.value="default"
        vals.item.value="default"
        vals.mensajeBroker.value="default"
        vals.usuario.value="usuario"
        onBack()
    }
    var topico= "${vals.brokerTopic.value}/channel${vals.canalBroker.value}"
    val urlBroker="tcp://${vals.brokerServer.value}:${vals.brokerPort.value}"

    //SUSCRIBIRSE AL CANAL DE COMUNICACION CUANDO EL BROKER SE CONECTE
    if(vals.brokerConected.value)
        suscribeBroker(context = context, top = topico, qos = 0)


    //SINTAXIS: TEXTO DE CONFIGURACION DEL CONTROLADOR
    // BEGIN,EJERCICIO,ITEM,USUARIO
    if(vals.mensajeBroker.value.contains("BEGIN")&& vals.begin.value==false)
        if(vals.mensajeBroker.value.contains(",")){
            val configs = vals.mensajeBroker.value.split(",")
            if(configs.size>1){
                vals.sesion.value=configs[1]
                vals.item.value=configs[2]
                vals.usuario.value=configs[3]
                Toast.makeText(context,"BEGIN",Toast.LENGTH_SHORT).show()
                vals.begin.value=true
                onBegin()
            }
        }


    Column(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize()
        .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "MODO SENSOR",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(R.drawable.gyroscope),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Espera mientras el encargado configura la sesion.",)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = urlBroker)
        Text(text = topico)
        Text(text = vals.mensajeBroker.value)
    }

}