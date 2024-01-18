package com.argent.acelerometrocompose.ktor.dto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.argent.acelerometrocompose.vals
import kotlinx.serialization.Serializable


@Serializable
data class ImageData(
    val type: String,
    val data: List<Int> // Change the type to List<Int>
)

@Serializable
data class ImageUrl(
    val data: ImageData,
    val type: String
)

@Serializable
data class Item(
    val num: Int,
    val descripcion: String,
    val registro: String? = null,
    val tiempo: Int? = null,
    val escala: Int? = null,
    val imageUrl: String? = null,
    val _id: String
) {
//    fun createFile() {
//        imageUrl?.let {
//            try {
//                // Use imageUrl.imageUrl.data directly as it's now a List<Int>
//                val bitmap = decodeByteArrayImage(it.data.data)
//                Log.d("Decoded image", bitmap.toString())
//
//                if (bitmap != null) {
//                    Log.d("Decoded image", bitmap.toString())
//                    // Your other processing logic here
//                    vals.listBitMap.add(bitmap)
//                } else {
//                    Log.e("Decoding error", "Bitmap is null")
//                }
//            } catch (e: Exception) {
//                Log.e("Decoding error", "Error decoding image: ${e.message}")
//            }
//        }
//    }


    private fun decodeByteArrayImage(data: List<Int>): Bitmap? {
        try {
            // Convert the list of integers to a ByteArray
            val byteArray = data.map { it.toByte() }.toByteArray()

            // Decode the Base64 image
            val decodedBytes = Base64.decode(byteArray, Base64.DEFAULT)

            // Check if the decoding was successful
            if (decodedBytes == null) {
                Log.e("Decoding error", "Error decoding image: decodedBytes is null")
                return null
            }

            // Convert the decoded bytes to a Bitmap
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            Log.e("Decoding error", "Error decoding image: ${e.message}")
            return null
        }
    }

}