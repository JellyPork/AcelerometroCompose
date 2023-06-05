package com.argent.acelerometrocompose

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

private var iniciado: Boolean = false
lateinit var broker: MqttAndroidClient

@Composable
fun ConfigurarMqttScreen(onBack: () -> Unit){
    val context = LocalContext.current
    var ser = remember {mutableStateOf(vals.brokerServer.value)}
    var puerto = remember {mutableStateOf(vals.brokerPort.value)}
    var top = remember {mutableStateOf(vals.brokerTopic.value)}
    var cli = remember {mutableStateOf(vals.brokerClient.value)}
    var pas = remember {mutableStateOf(vals.brokerPass.value)}
    val placeholder = ""
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
            BasicTextField(
                value = ser.value,
                onValueChange = { newText ->
                    ser.value = newText
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 64.dp) // margin left and right
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = Color(0xFFAAE9E6),
                                shape = RoundedCornerShape(size = 16.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp), // inner padding
                    ) {
                        if (ser.value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.LightGray
                            )
                        }
                        innerTextField()
                    }
                }
            )
            BasicTextField(
                value = puerto.value,
                onValueChange = { newText ->
                    puerto.value = newText
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 64.dp) // margin left and right
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = Color(0xFFAAE9E6),
                                shape = RoundedCornerShape(size = 16.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp), // inner padding
                    ) {
                        if (puerto.value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.LightGray
                            )
                        }
                        innerTextField()
                    }
                }
            )
        BasicTextField(
            value = top.value,
            onValueChange = { newText ->
                top.value = newText
            },
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 64.dp) // margin left and right
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color(0xFFAAE9E6),
                            shape = RoundedCornerShape(size = 16.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp), // inner padding
                ) {
                    if (puerto.value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.LightGray
                        )
                    }
                    innerTextField()
                }
            }
        )
        BasicTextField(
            value = cli.value,
            onValueChange = { newText ->
                cli.value = newText
            },
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 64.dp) // margin left and right
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color(0xFFAAE9E6),
                            shape = RoundedCornerShape(size = 16.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp), // inner padding
                ) {
                    if (puerto.value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.LightGray
                        )
                    }
                    innerTextField()
                }
            }
        )
        BasicTextField(
            value = pas.value,
            onValueChange = { newText ->
                pas.value = newText
            },
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 64.dp) // margin left and right
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color(0xFFAAE9E6),
                            shape = RoundedCornerShape(size = 16.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp), // inner padding
                ) {
                    if (puerto.value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.LightGray
                        )
                    }
                    innerTextField()
                }
            }
        )

            Button(
                onClick = {
                    vals.brokerServer.value=ser.value
                    vals.brokerPort.value=puerto.value
                    vals.brokerTopic.value=top.value
                    vals.brokerClient.value=cli.value
                    vals.brokerPass.value=pas.value
                    onBack()
//                    var url ="tcp://${ser.value}:${puerto.value}"
//                    connectBroker(context, url)
                },
            ) {
                Text(text = "Guardar")
            }
        }
}

fun connectBroker(applicationContext : Context, url:String): Boolean {
    broker = MqttAndroidClient ( applicationContext,url,"CLIENTE" )
    var connect = false
    try {
        val token = broker.connect()
        token.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken)                        {
                Log.i("Connection", "success ")
                Toast.makeText(applicationContext,"BRK: Conectado", Toast.LENGTH_SHORT).show()
                connect = true
                vals.brokerConected.value=true
                //connectionStatus = true
                // Give your callback on connection established here
            }
            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                //connectionStatus = false

                Toast.makeText(applicationContext,"BRK: Error", Toast.LENGTH_LONG).show()
                Log.i("Connection", "failure")
                // Give your callback on connection failure here
                exception.printStackTrace()
            }
        }
    } catch (e: MqttException) {
        Toast.makeText(applicationContext,e.toString(), Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
    return connect
}

 fun publishBroker(topic: String, msg: String, qos: Int, retained: Boolean) {
    try {
        val token = broker.publish(topic,msg.toByteArray(),qos,retained)
        token.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                //Log.i("brkok", msg)
            }
            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                //Toast.makeText(applicationContext,"Publish NO",Toast.LENGTH_SHORT).show()
                exception.printStackTrace()
            }
        }
    } catch (e: MqttException) {
        //Toast.makeText(LocalContext.current,e.toString(),Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
}
fun suscribeBroker(context: Context,top: String,qos: Int=0){
    if(!vals.brokerSuscriber.value) {
        val token = broker.subscribe(top, qos)
        token.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                vals.brokerSuscriber.value=true
                Toast.makeText(context, "[$top] Suscripción Exitosa", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Toast.makeText(context, "Suscripción error", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        broker.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                // Pérdida de conexión con el broker
                Toast.makeText(context, "BRKR: LOST", Toast.LENGTH_SHORT).show()
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                val receivedMessage = message?.toString()
                if (receivedMessage != null) {
                    //Toast.makeText(context, receivedMessage, Toast.LENGTH_SHORT).show()
                    vals.mensajeBroker.value = receivedMessage
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                // Entrega completa del mensaje (opcional)
            }
        })
    }
}

//FUNCION PARA DESCONECTAR EL BROKER, NO SE SI FUNCIONA
 fun disconnectBroker(context: Context) {
    try {
        broker.disconnect().actionCallback= object :IMqttActionListener{
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                vals.brokerConected.value=false
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Toast.makeText(context, "BRKR: Error Desconexion",Toast.LENGTH_LONG).show()
            }

        }
        Toast.makeText(context,"BRK: Desconectado".toString(),Toast.LENGTH_SHORT).show()
    } catch (e: MqttException) {
        //Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
}

fun publicar(applicationContext: Context, top: String, xh: Float, yh: Float, zh: Float){
    if(!iniciado) {
        iniciado = true
        Toast.makeText(applicationContext,"HANDLER INICIADO", Toast.LENGTH_SHORT).show()
        publishBroker(top, "${String.format("%2.2f", xh)},${String.format("%2.2f", yh)},${String.format("%2.2f", zh)}",0, false)
    }else{
        Toast.makeText(applicationContext,"HANDLER DETENENIDO", Toast.LENGTH_SHORT).show()
        iniciado = false
        //detener handler
    }
}