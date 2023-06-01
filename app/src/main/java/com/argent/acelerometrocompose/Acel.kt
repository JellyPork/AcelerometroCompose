package com.argent.acelerometrocompose

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.argent.acelerometrocompose.ui.theme.GreenCustom
import com.argent.acelerometrocompose.ui.theme.RedCustom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import android.os.Handler





@Composable
fun AcelScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val sensorVM: SensorViewModel = viewModel()
    val estado = sensorVM.estado.collectAsState().value

    sensorVM.setupSensor(context)
    BackHandler {
        disconnectBroker(context)
        sensorVM.stopSensors()
        sensorVM.stopHandel()
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
            painter = painterResource(R.drawable.logo_inicio),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Acc:\t\t${String.format("%2.2f", estado.lecturaACC[0])},\t\t${String.format("%2.2f", estado.lecturaACC[1])},\t\t${String.format("%2.2f", estado.lecturaACC[2])}")
        Text(text = "Gyr:\t\t${String.format("%2.2f", estado.lecturaGYR[0])},\t\t${String.format("%2.2f", estado.lecturaGYR[1])},\t\t${String.format("%2.2f", estado.lecturaGYR[2])}")
        Text(text = "Mag:\t\t${String.format("%2.2f", estado.lecturaMGT[0])},\t\t${String.format("%2.2f", estado.lecturaMGT[1])},\t\t${String.format("%2.2f", estado.lecturaMGT[2])}")
        Text(text = "Ort:\t\t${String.format("%2.2f", estado.lecturaORT[1])},\t\t${String.format("%2.2f", estado.lecturaORT[2])},\t\t${String.format("%2.2f", estado.lecturaORT[0])}")
        Text(text = "Ang:\t\t${String.format("%2.2f", estado.angulosOrientacion[1])},\t\t${String.format("%2.2f",estado.angulosOrientacion[2])},\t\t${String.format("%2.2f", estado.angulosOrientacion[0])}")
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if(estado.active){
                sensorVM.stopSensors()
                sensorVM.stopHandel()
            }
            else {
                sensorVM.starSensors()
                sensorVM.startHadler()

            }
        }, colors = ButtonDefaults.buttonColors(containerColor = bcolor), modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp))
        {
            Text(text = bText,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold)
        }
    }
}

class SensorViewModel(): ViewModel(),SensorEventListener{
    private val _estado = MutableStateFlow(SensorValues());
    val estado: StateFlow<SensorValues> = _estado.asStateFlow()
    private  lateinit var  sensorManager: SensorManager
    var handler: Handler? = null


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

    fun startHadler(){
        handler = Handler()
        handler!!.postDelayed(object : Runnable {
            override fun run() {
                //mandar angulos
                publishBroker(vals.brokerTopic.value, "${String.format("%2.2f", _estado.value.angulosOrientacion[1])},${String.format("%2.2f", _estado.value.angulosOrientacion[2])},${String.format("%2.2f",_estado.value.angulosOrientacion[0])}",0, false) //llamamos nuestro metodo
                //mandar acc
                publishBroker("${vals.brokerTopic.value}/ACC", "${String.format("%2.2f", _estado.value.lecturaACC[0])},${String.format("%2.2f", _estado.value.lecturaACC[1])},${String.format("%2.2f",_estado.value.lecturaACC[2])}", 0, false) //llamamos nuestro metodo
                //mandar gyr
                publishBroker("${vals.brokerTopic.value}/GYR", "${String.format("%2.2f", _estado.value.lecturaGYR[0])},${String.format("%2.2f", _estado.value.lecturaGYR[1])},${String.format("%2.2f",_estado.value.lecturaGYR[2])}", 0, false) //llamamos nuestro metodo
                //mandar mag
                publishBroker("${vals.brokerTopic.value}/MAG", "${String.format("%2.2f", _estado.value.lecturaMGT[0])},${String.format("%2.2f",_estado.value.lecturaMGT[1])},${String.format("%2.2f", _estado.value.lecturaMGT[2])}", 0, false) //llamamos nuestro metodo
                //mandar ort
                publishBroker("${vals.brokerTopic.value}/ORT", "${String.format("%2.2f", _estado.value.lecturaORT[1])},${String.format("%2.2f", _estado.value.lecturaORT[2])},${String.format("%2.2f", _estado.value.lecturaORT[0])}", 0, false) //llamamos nuestro metodo
                handler!!.postDelayed(this, 100) //se ejecutara cada 100 segundos
            }
        }, 100)
    }
    fun stopHandel(){
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }
}

data class SensorValues(
    val lecturaACC: FloatArray = FloatArray(3),
    val lecturaGYR: FloatArray = FloatArray(3),
    val lecturaMGT: FloatArray = FloatArray(3),
    val lecturaORT: FloatArray = FloatArray(3),
    val matrizRotacion: FloatArray = FloatArray(9),
    val angulosOrientacion: FloatArray = FloatArray(3),
    val active: Boolean=false
)



//@Composable
//fun AcelScreen(onBack: () -> Unit) {
//    var gravity = rememberSaveable {
//        FloatArray(3)
//    }
//    var geomag = rememberSaveable {
//        FloatArray(3)
//    }
//    val yaw = rememberSaveable {
//        mutableStateOf(0.0)
//    }
//    val pitch = rememberSaveable {
//        mutableStateOf(0.0)
//    }
//    val roll = rememberSaveable {
//        mutableStateOf(0.0)
//    }
//    var register by remember {
//        mutableStateOf(false)
//    }
//    val context = LocalContext.current
//    val sm: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
//    var sensor: Sensor? by rememberSaveable {
//        mutableStateOf(null)
//    }
//    var sensor2: Sensor? by rememberSaveable {
//        mutableStateOf(null)
//    }
//    if(sensor == null && sensor2 == null){
//        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//        sensor2 = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
//        val accSensorEventListener = object : SensorEventListener {
//            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
//                return
//            }
//
//            // on below line we are creating a sensor on sensor changed
//            override fun onSensorChanged(event: SensorEvent) {
//                if (event.sensor?.type == Sensor.TYPE_ACCELEROMETER)
//                    gravity = event.values!!
//
//                if (event.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD)
//                    geomag = event.values!!
//
//                if (gravity != null && geomag != null) {
//                    val r = FloatArray(9)
//                    val i = FloatArray(9)
//
//                    if (SensorManager.getRotationMatrix(r, i, gravity, geomag)) {
//                        val orientation = FloatArray(3)
//                        SensorManager.getOrientation(r, orientation)
//                        yaw.value = Math.toDegrees(orientation[0].toDouble())
//                        pitch.value = Math.toDegrees(orientation[1].toDouble())
//                        roll.value = Math.toDegrees(orientation[2].toDouble())
//                    }
//                }
//            }
//        }
//        sm.registerListener(accSensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
//        sm.registerListener(accSensorEventListener, sensor2, SensorManager.SENSOR_DELAY_NORMAL)
//
//    }
//
//    Vista(yaw.value, pitch.value, roll.value)
//}

//@Composable
//fun Vista(yaw: Double, pitch: Double, roll: Double) {
//    Column(modifier = Modifier
//        .fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center) {
//        val yawString = String.format("%.2f", yaw)
//        val pitchString = String.format("%.2f", pitch)
//        val rollString = String.format("%.2f", roll)
//        Text(text = yawString)
//        Text(text = pitchString)
//        Text(text = rollString)
//    }
//}

