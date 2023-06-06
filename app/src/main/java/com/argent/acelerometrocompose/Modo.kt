package com.argent.acelerometrocompose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ModoScreen(onHome: () -> Unit, onSolo: () -> Unit, onDuo: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.ModoDeApp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            vals.modo.value=false
            onSolo()
        } ) {
            Icon(painterResource(id = R.drawable.solo),contentDescription = null)
            Text(text = "Solo",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = {
            vals.modo.value=true
            onDuo()
        } ) {
            Icon(painterResource(id = R.drawable.duo),contentDescription = null)
            Text(text = "Duo",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}