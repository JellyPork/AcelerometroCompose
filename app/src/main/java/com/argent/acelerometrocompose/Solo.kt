package com.argent.acelerometrocompose

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SoloScreen(onBack: () -> Unit, onSoloSessions: () -> Unit) {
    val context = LocalContext.current
    val den = LocalDensity.current
    var mExpanded by remember {
        mutableStateOf(false)
    }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }
    var mSelectedIndex by remember { mutableStateOf(0) }
    var mSelectedText by remember {
        mutableStateOf("")
    }
    val density by remember {
        mutableStateOf(den)
    }
    val pruebas = remember {
        ArrayList<String>()
    }
    var init by remember {
        mutableStateOf(false)
    }
    if(!init){
        init = true
        vals.json.forEach { item ->
            pruebas.add(item.name.toString())
            Log.d("Agregar", item.name.toString())
        }
    }

    //val pruebas = vals.json //stringArrayResource(R.array.Pruebas).toList()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.Selec_Pruebas),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        //SELECTOR DE NOMBRE DE USUARIO
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            value = vals.usuario.value.trim(),
            onValueChange = { vals.usuario.value= it },
            label = { Text("Canal") },
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 20.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(20.dp))
        LargeDropdownMenu(
            label = pruebas[mSelectedIndex],
            items = pruebas,
            onItemSelected = { index, _ ->
                mSelectedIndex = index
                mSelectedText = pruebas[mSelectedIndex]
                vals.sesion.value = mSelectedText
            }
        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(onClick = {
            if(vals.sesion.value!="")
                onSoloSessions()
            else Toast.makeText(context,"Seleciona un elemento.", Toast.LENGTH_SHORT).show()
        })
        {
            Text(text = "Seleccionar",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold)
        }

    }
}


