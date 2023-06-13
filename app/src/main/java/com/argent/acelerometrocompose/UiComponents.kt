package com.argent.acelerometrocompose

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.lifecycle.ViewModel
import com.argent.acelerometrocompose.ui.theme.AcelerometroComposeTheme


@Composable
fun <T> LargeDropdownMenu(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    notSetLabel: String? = null,
    items: List<T>,
    selectedIndex: Int = -1,
    onItemSelected: (index: Int, item: T) -> Unit,
    selectedItemToString: (T) -> String = { it.toString() },
    drawItem: @Composable (T, Boolean, Boolean, () -> Unit) -> Unit = { item, selected, itemEnabled, onClick ->
        LargeDropdownMenuItem(
            text = item.toString(),
            selected = selected,
            enabled = itemEnabled,
            onClick = onClick,
        )
    },
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier
        .height(IntrinsicSize.Min)
        .width(IntrinsicSize.Min)) {
        OutlinedTextField(
            label = { Text(label) },
            value = items.getOrNull(selectedIndex)?.let { selectedItemToString(it) } ?: "",
            enabled = enabled,
            trailingIcon = {
                val icon = when(expanded) {
                    true -> Icons.Filled.KeyboardArrowUp
                    false -> Icons.Filled.KeyboardArrowDown
                }
                Icon(icon, "")
            },
            onValueChange = { },
            readOnly = true,
        )

        // Transparent clickable surface on top of OutlinedTextField
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable(enabled = enabled) { expanded = true },
            color = Color.Transparent,
        ) {}
    }

    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false },
        ) {
            AcelerometroComposeTheme() {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                ) {
                    val listState = rememberLazyListState()
                    if (selectedIndex > -1) {
                        LaunchedEffect("ScrollToSelected") {
                            listState.scrollToItem(index = selectedIndex)
                        }
                    }

                    LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
                        if (notSetLabel != null) {
                            item {
                                LargeDropdownMenuItem(
                                    text = notSetLabel,
                                    selected = false,
                                    enabled = false,
                                    onClick = { },
                                )
                            }
                        }
                        itemsIndexed(items) { index, item ->
                            val selectedItem = index == selectedIndex
                            drawItem(
                                item,
                                selectedItem,
                                true
                            ) {
                                onItemSelected(index, item)
                                expanded = false
                            }

                            if (index < items.lastIndex) {
                                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LargeDropdownMenuItem(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface
        selected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface
    }

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(modifier = Modifier
            .clickable(enabled) { onClick() }
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@Composable
fun ScorePopUp(onSelect: (Int) -> Unit){
        Popup(alignment= Alignment.BottomCenter, onDismissRequest = {/*control=false*/}, offset = IntOffset(0,150)) {
            Column(
                modifier = Modifier
                    .background(Color.LightGray)
                    .height(400.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(
                    text = "CALIFICA ESTE EJERCICIO:",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign =  TextAlign.Center
                )

                Spacer(modifier = Modifier.height(80.dp))

                Row(horizontalArrangement = Arrangement.Center){
                    Image(painter = painterResource(id = R.drawable.star0),contentDescription = "score 0", modifier = Modifier
                        .clickable { onSelect(0);/*control=false*/ }
                        .height(110.dp)
                        .padding(horizontal = 5.dp))
                    Image(painter = painterResource(id = R.drawable.star1),contentDescription = "score 1", modifier = Modifier
                        .clickable { onSelect(1);/*control=false*/ }
                        .height(110.dp)
                        .padding(horizontal = 5.dp))
                    Image(painter = painterResource(id = R.drawable.star2),contentDescription = "score 2", modifier = Modifier
                        .clickable { onSelect(2);/*control=false*/ }
                        .height(110.dp)
                        .padding(horizontal = 5.dp))
                }
            }
        }
//    }
}

class variables: ViewModel(){
    var sesion =  mutableStateOf("default")
    var item = mutableStateOf("default")
    var usuario = mutableStateOf("Usuario")

    var modo = mutableStateOf(false)    //false:solo   true:duo
    var begin = mutableStateOf(false)
    var started = mutableStateOf(false)
    var showScore = mutableStateOf(false)

    var indexPrueba = mutableStateOf(0)
    var indexItem = mutableStateOf(0)
    var timePrueba = mutableStateOf<Long>(0)

    var brokerServer = mutableStateOf("test.mosquitto.org")
    var brokerPort = mutableStateOf("1883")
    var brokerTopic = mutableStateOf("biogait")
    var brokerClient= mutableStateOf("SMUclient")
    var brokerPass= mutableStateOf("")
    var brokerConected = mutableStateOf(false)
    var mensajeBroker = mutableStateOf("default")
    var canalBroker = mutableStateOf("0")
    var brokerSuscriber= mutableStateOf(false)

    val datasetDir="/storage/emulated/0/Download/Datasets"

    var json = ArrayList<Prueba>()
    var listBitMap = mutableStateListOf<Bitmap>()
    lateinit var currentBitmap: ImageBitmap
}

val vals = variables()