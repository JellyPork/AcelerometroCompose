package com.argent.acelerometrocompose

import android.graphics.Paint.Align
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.argent.acelerometrocompose.ui.theme.Shapes

@Composable
fun SoloSessionScreen(onBack: () -> Unit, onSensores: () -> Unit){
    val context= LocalContext.current
    val numPruebas = listOf<String>("1","2","3","4","5","6","7","8","9","10")
    val items = remember {
        ArrayList<String>()
    }
    var init by remember {
        mutableStateOf(false)
    }
    if(!init){
        init = true
        vals.json.forEach { item ->
            if(item.name.toString() == vals.sesion.value){
                Log.d("Nombre", item.name.toString())
                item.items?.forEach { nitem ->
                    items.add(nitem.no.toString())
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
                            vals.item.value=mSelectedText
                            mExpanded = false
                            //
                        })
                    }
                }
            }

        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if(vals.item.value!="default") {
                onSensores()
                connectBroker(context,"tcp://${vals.brokerServer.value}:${vals.brokerPort.value}")
            }
            else Toast.makeText(context,"Seleciona un elemento.", Toast.LENGTH_SHORT).show()
        }) {
            Text(
                text = "Confirmar",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}