package com.argent.acelerometrocompose.ktor.dto

import android.graphics.BitmapFactory
import android.util.Log
import com.argent.acelerometrocompose.storageRef
import com.argent.acelerometrocompose.vals
import java.io.File

data class Prueba (
    val name: String? = null,
    val nItems: Int? = null,
    val items: List<Items>? = null
)

data class Items(
    val image: String = "probando.jpg",
    val itemName: String? = null,
    val no: Int? = null,
    val score: Int? = null,
    val timeLimit: Int? = null
){
    fun createFile(){
        val imagen = storageRef.child(image)
        val localFile = File.createTempFile("temp", ".jpg")
        imagen.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            vals.listBitMap.add(bitmap)
            // Local temp file has been created
            Log.d("Archivo", "Archivo creado")
        }.addOnFailureListener {
            // Handle any errors
        }
    }
}