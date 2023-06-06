package com.argent.acelerometrocompose

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.argent.acelerometrocompose.ui.theme.GreenCustom
import com.argent.acelerometrocompose.ui.theme.RedCustom
import com.opencsv.CSVWriter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Composable
fun AcelScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val sensorVM: SensorViewModel = viewModel()
    val estado = sensorVM.estado.collectAsState().value
    var boolScore by remember {
        mutableStateOf(false)
    }

    sensorVM.setupSensor(context)
    BackHandler {
        vals.indexItem.value=0;
        vals.begin.value=false
        vals.mensajeBroker.value="STOP"
        if(!vals.modo.value) disconnectBroker(context) //Desconectar el broker si esta en modo olo
        sensorVM.stopSensors()
        sensorVM.stopHandler()
        onBack()
    }
    val bcolor: androidx.compose.ui.graphics.Color
    val bText:String

    when(estado.active){
        true -> {
            bcolor= RedCustom
            bText="Detener"
        }
        false -> {
            bcolor= GreenCustom
            bText="Iniciar"
        }
    }

    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "Ejercicio: ${vals.sesion.value}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold)
        Text(text = "Item: ${vals.item.value}",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            bitmap = vals.currentBitmap,
            contentDescription = null, modifier = Modifier.size(200.dp,200.dp),
            contentScale = ContentScale.FillBounds
        )
        if(boolScore){
            ScorePopUp(onSelect = {sensorVM.setScore(it);sensorVM.generarDataset(context);boolScore=false})
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Acc:\t\t${String.format("%2.2f", estado.lecturaACC[0])},\t\t${String.format("%2.2f", estado.lecturaACC[1])},\t\t${String.format("%2.2f", estado.lecturaACC[2])}")
        Text(text = "Gyr:\t\t${String.format("%2.2f", estado.lecturaGYR[0])},\t\t${String.format("%2.2f", estado.lecturaGYR[1])},\t\t${String.format("%2.2f", estado.lecturaGYR[2])}")
        Text(text = "Mag:\t\t${String.format("%2.2f", estado.lecturaMGT[0])},\t\t${String.format("%2.2f", estado.lecturaMGT[1])},\t\t${String.format("%2.2f", estado.lecturaMGT[2])}")
        Text(text = "Ort:\t\t${String.format("%2.2f", estado.lecturaORT[1])},\t\t${String.format("%2.2f", estado.lecturaORT[2])},\t\t${String.format("%2.2f", estado.lecturaORT[0])}")
        Text(text = "Ang:\t\t${String.format("%2.2f", estado.angulosOrientacion[1])},\t\t${String.format("%2.2f",estado.angulosOrientacion[2])},\t\t${String.format("%2.2f", estado.angulosOrientacion[0])}")
        Spacer(modifier = Modifier.height(20.dp))


        if(vals.modo.value){    //Si esta en modo duo
            Text(text = "MODO DUO")
            if(vals.mensajeBroker.value.contains("START") && vals.started.value==false) //INCIAR SENSADO AL RECIBIR MENSAJE START
            {
                vals.started.value=true
                sensorVM.starSensors()
                sensorVM.startHandler("${vals.brokerTopic.value}/channel${vals.canalBroker.value}")
            }else if (vals.mensajeBroker.value.contains("STOP")&& vals.started.value==true) //PARAR SENSADO AL RECIBIR MENSAJE START
            {
                vals.started.value=false
                sensorVM.stopSensors()
                sensorVM.stopHandler()
                if(!vals.showScore.value)
                    sensorVM.generarDataset(context)
                else
                    boolScore=true
            }

        }else{  //Si esta en modo solo
            Button(onClick = {
                if(estado.active){
                    sensorVM.stopSensors()
                    sensorVM.stopHandler()
                    if(!vals.showScore.value)
                        sensorVM.generarDataset(context)
                    else
                        boolScore=true
                }
                else {
                    sensorVM.starSensors()
                    sensorVM.startHandler(vals.brokerTopic.value)
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = bcolor), modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(10.dp))
            {
                Icon(painterResource(id = R.drawable.playcircle),contentDescription = null)
                Text(text = bText,fontSize =30.sp, fontWeight = FontWeight.Bold)
            }

        }
    }
}

@Suppress("DEPRECATION")
class SensorViewModel(): ViewModel(),SensorEventListener{
    private val _estado = MutableStateFlow(SensorValues())
    val estado: StateFlow<SensorValues> = _estado.asStateFlow()
    private  lateinit var  sensorManager: SensorManager
    var handler: Handler? = null
    val angArr: ArrayList<String> =  ArrayList()
    val accArr: ArrayList<String> =  ArrayList()
    val gyrArr: ArrayList<String> =  ArrayList()
    val magArr: ArrayList<String> =  ArrayList()
    val ortArr: ArrayList<String> =  ArrayList()
    val minArr: ArrayList<String> =  ArrayList()
    var calendar: Calendar = Calendar.getInstance()


    fun setupSensor(context: Context) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    fun starSensors(){
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensorManager.registerListener(
                this,
                magneticField,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)?.also { orientation ->
            sensorManager.registerListener(
                this,
                orientation,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.also { gyro ->
            sensorManager.registerListener(
                this,
                gyro,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
        _estado.update { x -> x.copy(active = true)  }
    }

    fun stopSensors(){
        sensorManager.unregisterListener(this)
        _estado.update { x -> x.copy(active = false)  }
    }
    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                _estado.update { x -> x.copy(lecturaACC = event.values) } }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                _estado.update { x -> x.copy(lecturaMGT = event.values) } }
            Sensor.TYPE_ORIENTATION -> {
                _estado.update { x -> x.copy(lecturaORT = event.values) }  }
            Sensor.TYPE_GYROSCOPE->{
                _estado.update { x -> x.copy(lecturaGYR = event.values) } }
        }
        val auxRotacion = FloatArray(9)
        val auxOrientacion = FloatArray(3)
        SensorManager.getRotationMatrix(auxRotacion,null, estado.value.lecturaACC, estado.value.lecturaMGT)
        SensorManager.getOrientation(auxRotacion, auxOrientacion)
        auxOrientacion[1] = Math.toDegrees(auxOrientacion[1].toDouble()).toFloat()
        auxOrientacion[2]= Math.toDegrees(auxOrientacion[2].toDouble()).toFloat()
        auxOrientacion[0]= Math.toDegrees(auxOrientacion[0].toDouble()).toFloat()
        _estado.update { x -> x.copy(matrizRotacion = auxRotacion, angulosOrientacion = auxOrientacion) }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //NADA
    }

    fun setScore(s:Int){
        _estado.update { x -> x.copy(score = s) }
    }

    fun startHandler(base:String){
        //LIMPIAR ARRAYS
        angArr.clear()
        accArr.clear()
        gyrArr.clear()
        ortArr.clear()
        minArr.clear()
        magArr.clear()
        handler = Handler()
        viewModelScope.launch(Dispatchers.IO) {
            handler!!.postDelayed(object : Runnable {
                override fun run() {
                    calendar = Calendar.getInstance()
                    //STRINGS DE DATOS
                    val angstr="${String.format("%3.4f", _estado.value.angulosOrientacion[1])},${String.format("%3.4f",_estado.value.angulosOrientacion[2])},${String.format("%3.4f", _estado.value.angulosOrientacion[0])}"
                    val accstr="${String.format("%3.4f",_estado.value.lecturaACC[0])},${String.format("%3.4f",_estado.value.lecturaACC[1])},${String.format("%3.4f", _estado.value.lecturaACC[2])}"
                    val gyrstr="${String.format("%3.4f",_estado.value.lecturaGYR[0])},${ String.format("%3.4f",_estado.value.lecturaGYR[1])},${String.format("%3.4f", _estado.value.lecturaGYR[2])}"
                    val magstr="${String.format("%3.4f", _estado.value.lecturaMGT[0])},${String.format("%3.4f", _estado.value.lecturaMGT[1])},${String.format("%3.4f", _estado.value.lecturaMGT[2])}"
                    val ortstr="${String.format("%3.4f", _estado.value.lecturaORT[1])},${String.format("%3.4f", _estado.value.lecturaORT[2])},${String.format("%3.4f", _estado.value.lecturaORT[0])}"
                    val curTM=String.format("%02d:%02d:%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND))
                    //ACTUALIZAR ARREGLOS
                    angArr.add(angstr)
                    accArr.add(accstr)
                    gyrArr.add(gyrstr)
                    magArr.add(magstr)
                    ortArr.add(ortstr)
                    minArr.add(curTM)
                    //MANDAR A BROKER
                    publishBroker("$base/ANG", angstr, 0,false)
                    publishBroker("$base/ACC", accstr,0,false)
                    publishBroker("$base/GYR",gyrstr,0, false)
                    publishBroker("$base/MAG",magstr , 0,false)
                    publishBroker("$base/ORT",ortstr, 0, false)
                    //DELAY HANDELR
                    handler!!.postDelayed(this, 100) //se ejecutara cada 100 Msegundos
                }
            }, 100)
        }
    }
    fun stopHandler(){
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun generarDataset(context: Context){
        var fileWriter :FileWriter
        var csvWriter: CSVWriter
        //CREAR CARPETA DOWNLOADS/DATASETS
        val directory = File(vals.datasetDir)
        if (!directory.exists()) {
            directory.mkdir()
        }
        viewModelScope.launch(Dispatchers.IO){
            val fechaHora = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val archivo = File(vals.datasetDir,"${vals.brokerTopic.value}_$fechaHora.csv")

            try {
                //ESCRIBIR DATASET
                fileWriter = FileWriter(archivo)
                csvWriter = CSVWriter(fileWriter,',',CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.NO_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END)
                csvWriter.writeNext(arrayOf("timestamp","accX","accY","accZ","gyrX","gyrY","gyrZ","mgtX","mgtY","mgtZ","pitch","roll","yaw","test","item","score","usuario"))
                for (i in 0 until accArr.size){
                    csvWriter.writeNext(arrayOf(minArr[i],accArr[i],gyrArr[i],magArr[i],angArr[i],vals.sesion.value,vals.item.value,_estado.value.score.toString(),vals.usuario.value))
                }
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Dataset generado", Toast.LENGTH_SHORT).show()
                }
                csvWriter.close()
            }catch (e: IOException){
                e.printStackTrace()
                Log.i("csve", e.toString())
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Error Generando Dataset $e", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun arrayStringCSV(arr:FloatArray): String = arr.joinToString(separator = ",")

}

data class SensorValues(
    val lecturaACC: FloatArray = FloatArray(3),
    val lecturaGYR: FloatArray = FloatArray(3),
    val lecturaMGT: FloatArray = FloatArray(3),
    val lecturaORT: FloatArray = FloatArray(3),
    val matrizRotacion: FloatArray = FloatArray(9),
    val angulosOrientacion: FloatArray = FloatArray(3),
    val active: Boolean=false,
    val score: Int=0
)
