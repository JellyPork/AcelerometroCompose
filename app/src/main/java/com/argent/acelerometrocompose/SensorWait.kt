package com.argent.acelerometrocompose

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File

@Composable
fun SensorWaitScreen(onBack: () -> Unit,onBegin: () -> Unit){
    val context = LocalContext.current
    val topico= "${vals.brokerTopic.value}/channel${vals.canalBroker.value}"

    BackHandler {
        disconnectBroker(context)
        //unsuscribeBroker(topico)
        vals.sesion.value="default"
        vals.item.value="default"
        vals.mensajeBroker.value="default"
        vals.usuario.value="usuario"
        onBack()
    }

    var init by remember {
        mutableStateOf(false)
    }

    val urlBroker="tcp://${vals.brokerServer.value}:${vals.brokerPort.value}"

    //SUSCRIBIRSE AL CANAL DE COMUNICACION CUANDO EL BROKER SE CONECTE
    if(vals.brokerConected.value)
        suscribeBroker(context = context, top = topico, qos = 0, onRecibir = {vals.mensajeBroker.value=it})


    //SINTAXIS: TEXTO DE CONFIGURACION DEL CONTROLADOR
    // BEGIN,EJERCICIO,ITEM,USUARIO,SCOREBOOL
    if(vals.mensajeBroker.value.contains("BEGIN")&& vals.begin.value==false)
        if(vals.mensajeBroker.value.contains(",")){
            val configs = vals.mensajeBroker.value.split(",")
            if(configs.size>1){
                vals.sesion.value=configs[1]
                vals.item.value=configs[2]
                vals.usuario.value=configs[3]
                vals.showScore.value=configs[4].toBoolean()
                if(!init) {
                    init = true
                    vals.json.forEach { item ->
                        if (item.name.equals(configs[1].trim())) {
                            item.items?.forEach { nitem ->
                                if(nitem.no==configs[2].toInt()){
                                    val imagen = storageRef.child(nitem.image)
                                    val localFile = File.createTempFile("temp", ".jpg")
                                    imagen.getFile(localFile).addOnSuccessListener {
                                        val bmp= BitmapFactory.decodeFile(localFile.absolutePath)
                                        vals.currentBitmap=bmp.asImageBitmap()
                                        vals.begin.value=true
                                        onBegin()
                                    }
                                }
                            }
                        }
                    }
                }
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