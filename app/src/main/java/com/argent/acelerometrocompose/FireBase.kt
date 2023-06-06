package com.argent.acelerometrocompose

import android.util.JsonReader
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.persistentCacheSettings
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import org.json.JSONObject
import java.io.InputStreamReader

class FireBase {
    private val db: FirebaseFirestore = Firebase.firestore

    fun enablePersistence(){
        val settings = firestoreSettings {
            setLocalCacheSettings(persistentCacheSettings {})
        }
        db.firestoreSettings = settings
    }

    fun getCollenction(): ArrayList<Prueba> {
        val docRef = db.collection("Pruebas")
        val json = arrayListOf<Prueba>()
        //val source = Source.CACHE

        docRef.get()
            .addOnSuccessListener { pruebas ->
                for(prueba in pruebas){
                    json.add(prueba.toObject<Prueba>())
                    Log.d("PRUEBA", "Prueba: ${prueba.data}")
                }
                Log.d("JSON", json.toString())
            }
            .addOnFailureListener{ exception ->
                Log.d("PRUEBA", "fallo por ", exception)
            }
        //val jsonData: JSONObject = json.
        return json
    }
}