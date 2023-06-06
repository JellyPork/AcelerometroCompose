package com.argent.acelerometrocompose

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun FilesScreen(onBack: () -> Unit){
    BackHandler {
        onBack()
    }
    val context = LocalContext.current
    val directory = File(vals.datasetDir)
    val files = directory.listFiles()?.toList() ?: emptyList()
    val fileLaunch = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "DATASETS", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        LazyColumn(contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)) {
            items(files){ fileName ->

                Button(onClick = {
                    val uri = FileProvider.getUriForFile(context, "com.argent.acelerometrocompose.fileprovider", fileName)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri,"text/*")
                    intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.flags=Intent.FLAG_GRANT_READ_URI_PERMISSION
                    fileLaunch.launch(Intent.createChooser(intent,"Abrir Archivo") )
                }) {
                    androidx.compose.material.Icon(
                        painterResource(id = R.drawable.file),
                        contentDescription = null
                    )
                    Text(text = fileName.name, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

            }
        }

    }
}