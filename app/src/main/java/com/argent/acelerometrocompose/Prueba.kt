package com.argent.acelerometrocompose

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

data class Prueba (
    val name: String? = null,
    val nItems: Int? = null,
    val items: List<Items>? = null
)

data class Items(
    val image: String = "default.jpg",
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