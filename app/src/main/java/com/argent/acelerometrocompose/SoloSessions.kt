package com.argent.acelerometrocompose

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize

@Composable
fun SoloSessionScreen(onBack: () -> Unit, onSensores: () -> Unit, onControles: () -> Unit){
    val context= LocalContext.current
    val items = remember {
        ArrayList<String>()
    }
    val times = remember{
        ArrayList<Long>()
    }
    val images = remember {
        ArrayList<Bitmap>()
    }
    var init by remember {
        mutableStateOf(false)
    }
    if(!init){
        init = true
        vals.listBitMap.clear()
        vals.json.forEach { item ->
            if(item.name.toString() == vals.sesion.value){
                Log.d("Nombre", item.name.toString())
                item.items?.forEach { nitem ->
                    items.add(nitem.no.toString())
                    times.add(nitem.timeLimit.toString().toLong())
                    Log.i("Ruta", nitem.image)
                    //images.add()
                    nitem.createFile()
                }
            }
        }
    }
    var mSelectedIndex by remember { mutableStateOf(0) }
    var mExpanded by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
    var mSelectedText by remember { mutableStateOf("1") }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        if(vals.listBitMap.size > mSelectedText.toInt() - 1){
            Image(
                modifier = Modifier.size(200.dp, 200.dp),
                bitmap = vals.listBitMap[mSelectedIndex].asImageBitmap(),
                contentDescription = null
            )
        }

        Text(text = vals.sesion.value)
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Prueba ")
            Button(
                onClick = { mExpanded = true },
                modifier = Modifier
                    .wrapContentSize()
                    .onGloballyPositioned { mTextFieldSize = it.size.toSize() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.typography.bodyMedium.color
                )
            ) {
                val icon = Icons.Filled.KeyboardArrowDown
                Text(text = mSelectedText)
                Icon(icon, "")
                DropdownMenu(
                    expanded = mExpanded,
                    onDismissRequest = { mExpanded = false },
                    modifier = Modifier
                        .height(400.dp)
                        .width(with(density) { mTextFieldSize.width.toDp() })
                ) {
                    items.forEach { label ->
                        DropdownMenuItem(text = { Text(text = label) }, onClick = {
                            mSelectedText = label
                            mSelectedIndex = mSelectedText.toInt() -1
                            vals.item.value=mSelectedText
                            vals.indexItem.value=mSelectedIndex
                            vals.timePrueba.value = times[mSelectedIndex]
                            val score = vals.json[vals.indexPrueba.value].items?.get(mSelectedIndex)?.score
                            when(score){
                                0 -> vals.showScore.value=false
                                else -> vals.showScore.value=true
                            }
                            mExpanded = false
                        })
                    }
                }
            }

        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            val isConnected = isInternetAvailable(context)
            if(vals.item.value!="default") {
                if(isConnected) {
                    if(vals.listBitMap.size != 0){
                        vals.currentBitmap = vals.listBitMap[vals.indexItem.value].asImageBitmap()
                    }

                    connectBroker(
                        context,
                        "tcp://${vals.brokerServer.value}:${vals.brokerPort.value}"
                    )
                }
                if(vals.modo.value) {
                    onControles()
                }
                else
                    onSensores()
            }
            else Toast.makeText(context,"Seleciona un elemento.", Toast.LENGTH_SHORT).show()
        }) {
            androidx.compose.material.Icon(
                painterResource(id = R.drawable.okey),
                contentDescription = null
            )
            Text(
                text = "Confirmar",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }
    return result
}