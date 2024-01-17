package com.argent.acelerometrocompose

import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.argent.acelerometrocompose.data.StoreData
import com.argent.acelerometrocompose.ktor.PostsService
import com.argent.acelerometrocompose.ktor.dto.FileUploadRequest
import com.argent.acelerometrocompose.ui.theme.BlackCustom
import com.argent.acelerometrocompose.ui.theme.WhiteCustom2
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.util.Base64


@Composable
fun FilesScreen(onBack: () -> Unit, storeData: StoreData) {
    BackHandler {
        onBack()
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val directory = File(vals.datasetDir)
    var files by remember { mutableStateOf(directory.listFiles()?.map { FileWrapper(it) } ?: emptyList()) }
    val fileLaunch = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    // Maintain a list of selected items
    var selectedFiles by remember { mutableStateOf(emptyList<FileWrapper>()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Top
        ) {
            Button(
                onClick = {
                      scope.launch {
                          val selectedFilesToUpload = files.filter { it.isSelected }

                          // Iterate through selected files and perform Ktor upload
                          selectedFilesToUpload.forEach { fileWrapper ->
                              Log.d("File Wrapper Content", fileWrapper.toString())

                              // Replace the following line with your actual Ktor upload logic
                              val postsService = PostsService.create()
                              var encodedFileContent = ""
                              var fileContent = fileWrapper.file.readBytes()
                              encodedFileContent = String(Base64.getEncoder().encode(fileContent))
                              Log.d("Encoded File Content", encodedFileContent)

                              val data = postsService.uploadFile(
                                  FileUploadRequest(
                                      id = storeData.getUserId.first(),
                                      file = encodedFileContent
                                  )
                              )
                              encodedFileContent = ""
                              fileContent = ByteArray(0)

                              Log.d("Uploading File", data.toString())
                          }
                      }
                },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WhiteCustom2,
                    contentColor = BlackCustom,
                ),
                border = BorderStroke(4.dp, BlackCustom)
            ) {
                Text("Subir Archivos ")
            }
        }

        Text(
            text = "DATASETS",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        LazyColumn(contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)) {
            items(files) { fileWrapper ->
                FileItem(fileWrapper = fileWrapper) { updatedFile ->
                    // Handle file click and update the list of files
                    files = files.map { if (it.file == updatedFile.file) updatedFile else it }
                }
            }
        }

    }
}

@Composable
fun FileItem(fileWrapper: FileWrapper, onFileClick: (FileWrapper) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                // Automatically select the checkbox when the card is clicked
                onFileClick(fileWrapper.copy(isSelected = !fileWrapper.isSelected))
            },
        colors = CardDefaults.cardColors(
            containerColor = if (fileWrapper.isSelected) Color.White else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // File logo
            Icon(
                painter = painterResource(id = R.drawable.file),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            // File name
            Text(
                text = fileWrapper.file.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )

            // Checkbox for selection
            Checkbox(
                checked = fileWrapper.isSelected,
                onCheckedChange = { isSelected ->
                    // Update the isSelected property of the file
                    onFileClick(fileWrapper.copy(isSelected = isSelected))
                },
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

data class FileWrapper(val file: File, val isSelected: Boolean = false)
